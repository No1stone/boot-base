package com.origemite.eventsaga.saga.outbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.TimeUnit;

/**
 * PENDING Outbox → Kafka producer. event-saga 전용.
 */
public class EventOutboxRelay {

    private static final Logger log = LoggerFactory.getLogger(EventOutboxRelay.class);

    private final EventOutboxPort outboxPort;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final int batchSize;

    public EventOutboxRelay(EventOutboxPort outboxPort, KafkaTemplate<String, String> kafkaTemplate, int batchSize) {
        this.outboxPort = outboxPort;
        this.kafkaTemplate = kafkaTemplate;
        this.batchSize = batchSize;
    }

    public int relayOnce() {
        int sent = 0;
        for (EventOutboxRecord record : outboxPort.findPending(batchSize)) {
            try {
                kafkaTemplate.send(record.topic(), record.correlationId(), record.payloadJson())
                        .get(10, TimeUnit.SECONDS);
                outboxPort.markSent(record.id());
                sent++;
            } catch (Exception ex) {
                log.error("Outbox relay failed id={} eventId={}", record.id(), record.eventId(), ex);
                outboxPort.markFailed(record.id());
            }
        }
        return sent;
    }
}
