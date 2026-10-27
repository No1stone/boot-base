package com.origemite.eventsaga.saga.persistence;

import com.origemite.eventsaga.saga.outbox.EventOutboxPort;
import com.origemite.eventsaga.saga.outbox.EventOutboxRecord;
import com.origemite.eventsaga.saga.outbox.EventOutboxStatus;
import com.origemite.eventsaga.saga.persistence.entity.SagaOutboxEntity;
import com.origemite.eventsaga.saga.persistence.repository.SagaOutboxRepository;
import com.origemite.lib.message.saga.SagaMessageType;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
public class SagaOutboxAdapter implements EventOutboxPort {

    private final SagaOutboxRepository repository;

    public SagaOutboxAdapter(SagaOutboxRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public EventOutboxRecord save(EventOutboxRecord record) {
        SagaOutboxEntity entity = new SagaOutboxEntity();
        entity.setEventId(record.eventId());
        entity.setCorrelationId(record.correlationId());
        entity.setType(record.type().name());
        entity.setDomain(record.domain());
        entity.setTopic(record.topic());
        entity.setPayloadJson(record.payloadJson());
        entity.setDeliveryStatus(record.deliveryStatus().name());
        entity.setCreatedAt(record.createdAt());
        entity.setSentAt(record.sentAt());
        return toRecord(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventOutboxRecord> findPending(int limit) {
        return repository.findByDeliveryStatusOrderByIdAsc("PENDING", PageRequest.of(0, limit)).stream()
                .map(this::toRecord)
                .toList();
    }

    @Override
    @Transactional
    public void markSent(Long id) {
        repository.findById(id).ifPresent(entity -> {
            entity.setDeliveryStatus("SENT");
            entity.setSentAt(Instant.now());
        });
    }

    @Override
    @Transactional
    public void markFailed(Long id) {
        repository.findById(id).ifPresent(entity -> entity.setDeliveryStatus("FAILED"));
    }

    private EventOutboxRecord toRecord(SagaOutboxEntity entity) {
        return new EventOutboxRecord(
                entity.getId(),
                entity.getEventId(),
                entity.getCorrelationId(),
                SagaMessageType.valueOf(entity.getType()),
                entity.getDomain(),
                entity.getTopic(),
                entity.getPayloadJson(),
                EventOutboxStatus.valueOf(entity.getDeliveryStatus()),
                entity.getCreatedAt(),
                entity.getSentAt()
        );
    }
}
