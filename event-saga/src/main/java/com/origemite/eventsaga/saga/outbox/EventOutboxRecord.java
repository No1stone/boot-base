package com.origemite.eventsaga.saga.outbox;

import com.origemite.lib.message.saga.SagaMessage;
import com.origemite.lib.message.saga.SagaMessageType;
import com.origemite.lib.message.saga.SagaTopics;

import java.time.Instant;

public record EventOutboxRecord(
        Long id,
        String eventId,
        String correlationId,
        SagaMessageType type,
        String domain,
        String topic,
        String payloadJson,
        EventOutboxStatus deliveryStatus,
        Instant createdAt,
        Instant sentAt
) {

    public static EventOutboxRecord pending(SagaMessage message, String payloadJson) {
        return new EventOutboxRecord(
                null,
                message.eventId(),
                message.sagaId(),
                message.type(),
                message.domain(),
                SagaTopics.of(message),
                payloadJson,
                EventOutboxStatus.PENDING,
                Instant.now(),
                null
        );
    }
}
