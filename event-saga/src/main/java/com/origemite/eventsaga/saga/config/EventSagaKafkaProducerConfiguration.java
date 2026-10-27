package com.origemite.eventsaga.saga.config;

import com.origemite.eventsaga.saga.outbox.EventOutboxPort;
import com.origemite.eventsaga.saga.outbox.EventOutboxRelay;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import tools.jackson.databind.json.JsonMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * event-saga 전용 Kafka producer (Outbox 릴레이). 컨슘은 lib 쪽.
 */
@Configuration
@EnableConfigurationProperties({EventSagaOutboxProperties.class, KafkaProperties.class})
public class EventSagaKafkaProducerConfiguration {

    @Bean
    public JsonMapper sagaJsonMapper() {
        return JsonMapper.builder().findAndAddModules(getClass().getClassLoader()).build();
    }

    @Bean
    public KafkaTemplate<String, String> eventOutboxKafkaTemplate(KafkaProperties kafkaProperties) {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());
        props.putIfAbsent(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.putIfAbsent(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.putIfAbsent(ProducerConfig.ACKS_CONFIG, "all");
        props.putIfAbsent(ProducerConfig.RETRIES_CONFIG, 3);
        ProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<>(props);
        return new KafkaTemplate<>(pf);
    }

    @Bean
    public EventOutboxRelay eventOutboxRelay(
            EventOutboxPort eventOutboxPort,
            KafkaTemplate<String, String> eventOutboxKafkaTemplate,
            EventSagaOutboxProperties properties
    ) {
        return new EventOutboxRelay(eventOutboxPort, eventOutboxKafkaTemplate, properties.getBatchSize());
    }
}
