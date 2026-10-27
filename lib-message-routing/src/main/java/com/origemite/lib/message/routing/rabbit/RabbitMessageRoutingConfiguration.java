package com.origemite.lib.message.routing.rabbit;

import com.origemite.lib.message.routing.MessageHandler;
import com.origemite.lib.message.routing.MessageHandlerDispatcher;
import com.origemite.lib.message.routing.MessagePublishPort;
import com.origemite.lib.message.routing.MessageSubscribePort;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.amqp.autoconfigure.ConnectionFactoryCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

/**
 * RabbitMQ 기반 라우팅.
 * 연결 유지: automatic recovery + heartbeat + topology recovery.
 * 브로커 주소는 {@code spring.rabbitmq.*}.
 */
@Configuration
@ConditionalOnClass(RabbitTemplate.class)
@EnableConfigurationProperties(MessageRoutingProperties.class)
public class RabbitMessageRoutingConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "messageRoutingJsonMapper")
    public JsonMapper messageRoutingJsonMapper() {
        return JsonMapper.builder().findAndAddModules(getClass().getClassLoader()).build();
    }

    /**
     * Boot가 만드는 Rabbit {@link com.rabbitmq.client.ConnectionFactory}에 복구·heartbeat를 고정한다.
     */
    @Bean
    ConnectionFactoryCustomizer messageRoutingConnectionFactoryCustomizer(MessageRoutingProperties properties) {
        return factory -> {
            factory.setAutomaticRecoveryEnabled(true);
            factory.setTopologyRecoveryEnabled(true);
            factory.setNetworkRecoveryInterval(properties.getNetworkRecoveryIntervalMs());
            if (properties.getRequestedHeartbeatSeconds() > 0) {
                factory.setRequestedHeartbeat(properties.getRequestedHeartbeatSeconds());
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    @ConditionalOnMissingBean(name = "messageRoutingTopicExchange")
    public TopicExchange messageRoutingTopicExchange(MessageRoutingProperties properties) {
        return new TopicExchange(properties.getExchange(), true, false);
    }

    @Bean
    ApplicationRunner messageRoutingDeclareExchange(RabbitAdmin rabbitAdmin, TopicExchange messageRoutingTopicExchange) {
        return args -> rabbitAdmin.declareExchange(messageRoutingTopicExchange);
    }

    @Bean
    @ConditionalOnMissingBean
    public MessageHandlerDispatcher messageHandlerDispatcher(ObjectProvider<MessageHandler> handlers) {
        List<MessageHandler> list = handlers.orderedStream().toList();
        return new MessageHandlerDispatcher(list);
    }

    @Bean
    @ConditionalOnMissingBean(MessagePublishPort.class)
    public MessagePublishPort messagePublishPort(
            RabbitTemplate rabbitTemplate,
            MessageRoutingProperties properties,
            JsonMapper messageRoutingJsonMapper
    ) {
        return new RabbitMessagePublishAdapter(rabbitTemplate, properties, messageRoutingJsonMapper);
    }

    @Bean
    @ConditionalOnMissingBean(MessageSubscribePort.class)
    public MessageSubscribePort messageSubscribePort(
            ConnectionFactory connectionFactory,
            RabbitAdmin rabbitAdmin,
            TopicExchange messageRoutingTopicExchange,
            MessageHandlerDispatcher messageHandlerDispatcher,
            JsonMapper messageRoutingJsonMapper,
            MessageRoutingProperties properties,
            ObjectProvider<SimpleRabbitListenerContainerFactory> containerFactory
    ) {
        return new RabbitMessageSubscribeAdapter(
                connectionFactory,
                rabbitAdmin,
                messageRoutingTopicExchange,
                messageHandlerDispatcher,
                messageRoutingJsonMapper,
                properties,
                containerFactory
        );
    }
}
