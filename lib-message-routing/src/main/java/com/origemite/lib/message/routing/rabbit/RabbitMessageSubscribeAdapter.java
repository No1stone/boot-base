package com.origemite.lib.message.routing.rabbit;

import com.origemite.lib.message.routing.MessageHandlerDispatcher;
import com.origemite.lib.message.routing.MessageSubscribePort;
import com.origemite.lib.message.routing.RoutingMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.util.Assert;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 구독·바인딩. Boot {@link SimpleRabbitListenerContainerFactory}가 있으면 그 설정(prefetch 등)을 따른다.
 * 연결 복구는 ConnectionFactory automatic recovery + 컨테이너 재시작에 맡긴다.
 */
@Slf4j
public class RabbitMessageSubscribeAdapter implements MessageSubscribePort {

    private final ConnectionFactory connectionFactory;
    private final RabbitAdmin rabbitAdmin;
    private final TopicExchange topicExchange;
    private final MessageHandlerDispatcher dispatcher;
    private final JsonMapper jsonMapper;
    private final MessageRoutingProperties properties;
    private final ObjectProvider<SimpleRabbitListenerContainerFactory> containerFactory;

    private final Map<String, SimpleMessageListenerContainer> containers = new ConcurrentHashMap<>();

    public RabbitMessageSubscribeAdapter(
            ConnectionFactory connectionFactory,
            RabbitAdmin rabbitAdmin,
            TopicExchange topicExchange,
            MessageHandlerDispatcher dispatcher,
            JsonMapper jsonMapper,
            MessageRoutingProperties properties,
            ObjectProvider<SimpleRabbitListenerContainerFactory> containerFactory
    ) {
        this.connectionFactory = connectionFactory;
        this.rabbitAdmin = rabbitAdmin;
        this.topicExchange = topicExchange;
        this.dispatcher = dispatcher;
        this.jsonMapper = jsonMapper;
        this.properties = properties;
        this.containerFactory = containerFactory;
    }

    @Override
    public void bind(String queue, String exchange, String routingKey) {
        Assert.hasText(queue, "queue must not be blank");
        Assert.hasText(exchange, "exchange must not be blank");
        Assert.hasText(routingKey, "routingKey must not be blank");

        containers.computeIfAbsent(queue, q -> {
            Queue durableQueue = new Queue(q, true, false, false);
            rabbitAdmin.declareQueue(durableQueue);
            TopicExchange declaredExchange = exchange.equals(topicExchange.getName())
                    ? topicExchange
                    : new TopicExchange(exchange, true, false);
            rabbitAdmin.declareExchange(declaredExchange);
            rabbitAdmin.declareBinding(BindingBuilder.bind(durableQueue).to(declaredExchange).with(routingKey));

            SimpleMessageListenerContainer container = createContainer();
            container.setQueueNames(q);
            container.setMissingQueuesFatal(properties.isMissingQueuesFatal());
            container.setAcknowledgeMode(AcknowledgeMode.AUTO);
            // 멱등 키는 호스트가 처리. 실패 시 재전달 허용.
            container.setDefaultRequeueRejected(true);
            container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
                RoutingMessage routingMessage = jsonMapper.readValue(message.getBody(), RoutingMessage.class);
                dispatcher.dispatch(routingMessage);
            });
            container.afterPropertiesSet();
            container.start();
            log.info("Subscribed queue={} exchange={} routingKey={}", q, exchange, routingKey);
            return container;
        });
    }

    private SimpleMessageListenerContainer createContainer() {
        SimpleRabbitListenerContainerFactory factory = containerFactory.getIfAvailable();
        if (factory != null) {
            SimpleMessageListenerContainer container = factory.createListenerContainer();
            container.setConnectionFactory(connectionFactory);
            return container;
        }
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setRecoveryInterval(properties.getNetworkRecoveryIntervalMs());
        return container;
    }

    public void stopAll() {
        containers.values().forEach(SimpleMessageListenerContainer::stop);
        containers.clear();
    }
}
