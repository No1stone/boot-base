package com.origemite.lib.message.saga.kafka;

import com.origemite.lib.message.saga.SagaEventDispatcher;
import com.origemite.lib.message.saga.SagaEventHandler;
import com.origemite.lib.message.saga.SagaSubscribePort;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import tools.jackson.databind.json.JsonMapper;

import java.util.HashMap;

/**
 * 컨슘 전용. Kafka producer/Outbox는 event-saga 서버에만 있다.
 */
@Configuration
@ConditionalOnClass(ConsumerFactory.class)
@ConditionalOnProperty(prefix = "origemite.message.saga.consume", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({SagaKafkaConsumerProperties.class, KafkaProperties.class})
public class KafkaSagaConsumerConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "sagaJsonMapper")
    public JsonMapper sagaJsonMapper() {
        return JsonMapper.builder().findAndAddModules(getClass().getClassLoader()).build();
    }

    @Bean
    @ConditionalOnMissingBean
    public SagaEventDispatcher sagaEventDispatcher(ObjectProvider<SagaEventHandler> handlers) {
        return new SagaEventDispatcher(handlers.orderedStream().toList());
    }

    @Bean
    @ConditionalOnMissingBean(SagaSubscribePort.class)
    public SagaSubscribePort sagaSubscribePort(
            KafkaProperties kafkaProperties,
            SagaEventDispatcher sagaEventDispatcher,
            JsonMapper sagaJsonMapper,
            SagaKafkaConsumerProperties consumeProperties
    ) {
        return new KafkaSagaSubscribeAdapter(
                new HashMap<>(kafkaProperties.buildConsumerProperties()),
                sagaEventDispatcher,
                sagaJsonMapper,
                consumeProperties
        );
    }
}
