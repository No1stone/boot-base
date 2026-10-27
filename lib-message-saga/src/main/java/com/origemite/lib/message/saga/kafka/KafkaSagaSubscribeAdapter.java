package com.origemite.lib.message.saga.kafka;

import com.origemite.lib.message.saga.SagaEventDispatcher;
import com.origemite.lib.message.saga.SagaMessage;
import com.origemite.lib.message.saga.SagaSubscribePort;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.util.Assert;
import tools.jackson.databind.json.JsonMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class KafkaSagaSubscribeAdapter implements SagaSubscribePort {

    private final Map<String, Object> consumerConfigs;
    private final SagaEventDispatcher dispatcher;
    private final JsonMapper jsonMapper;
    private final SagaKafkaConsumerProperties properties;
    private final Map<String, ConcurrentMessageListenerContainer<String, String>> containers = new ConcurrentHashMap<>();

    public KafkaSagaSubscribeAdapter(
            Map<String, Object> consumerConfigs,
            SagaEventDispatcher dispatcher,
            JsonMapper jsonMapper,
            SagaKafkaConsumerProperties properties
    ) {
        this.consumerConfigs = consumerConfigs;
        this.dispatcher = dispatcher;
        this.jsonMapper = jsonMapper;
        this.properties = properties;
    }

    @Override
    public void subscribe(String topic, String consumerGroup) {
        Assert.hasText(topic, "topic must not be blank");
        Assert.hasText(consumerGroup, "consumerGroup must not be blank");

        String key = topic + "::" + consumerGroup;
        containers.computeIfAbsent(key, ignored -> {
            Map<String, Object> props = new HashMap<>(consumerConfigs);
            props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
            props.putIfAbsent(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            props.putIfAbsent(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            props.putIfAbsent(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
            props.putIfAbsent(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

            DefaultKafkaConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(props);
            ContainerProperties containerProperties = new ContainerProperties(topic);
            containerProperties.setMessageListener((MessageListener<String, String>) record -> {
                SagaMessage message = jsonMapper.readValue(record.value(), SagaMessage.class);
                dispatcher.dispatch(message);
            });

            ConcurrentMessageListenerContainer<String, String> container =
                    new ConcurrentMessageListenerContainer<>(cf, containerProperties);
            container.setConcurrency(Math.max(1, properties.getConcurrency()));
            container.setBeanName("saga-consume-" + topic + "-" + consumerGroup);
            container.start();
            log.info("Subscribed saga topic={} group={}", topic, consumerGroup);
            return container;
        });
    }

    public void stopAll() {
        containers.values().forEach(ConcurrentMessageListenerContainer::stop);
        containers.clear();
    }
}
