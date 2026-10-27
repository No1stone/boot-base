package com.origemite.eventsaga.saga.facade;

import com.origemite.eventsaga.saga.dto.SagaReq;
import com.origemite.eventsaga.saga.dto.SagaRes;
import com.origemite.eventsaga.saga.outbox.EventOutboxPort;
import com.origemite.eventsaga.saga.outbox.EventOutboxRecord;
import com.origemite.lib.message.saga.SagaMessage;
import com.origemite.lib.message.saga.SagaMessageType;
import com.origemite.lib.message.saga.SagaPublishPolicy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tools.jackson.databind.json.JsonMapper;

import java.util.UUID;

@Service
public class SagaPublishFacade {

    private final EventOutboxPort eventOutboxPort;
    private final JsonMapper jsonMapper;

    public SagaPublishFacade(EventOutboxPort eventOutboxPort, JsonMapper sagaJsonMapper) {
        this.eventOutboxPort = eventOutboxPort;
        this.jsonMapper = sagaJsonMapper;
    }

    @Transactional
    public SagaRes.Accepted accept(SagaReq.Ingress request) {
        SagaMessageType type = parseType(request.getType());
        SagaPublishPolicy.requirePublishable(type, request.getStatus());

        String eventId = StringUtils.hasText(request.getEventId())
                ? request.getEventId()
                : UUID.randomUUID().toString();
        String sagaId = StringUtils.hasText(request.getSagaId()) ? request.getSagaId() : eventId;

        String status = request.getStatus();
        if (type == SagaMessageType.COMPLETE && !StringUtils.hasText(status)) {
            status = "SUCCEEDED";
        }

        SagaMessage message = SagaMessage.builder(type, request.getDomain())
                .eventId(eventId)
                .sagaId(sagaId)
                .step(request.getStep())
                .status(status)
                .payload(request.getPayload())
                .build();

        eventOutboxPort.save(EventOutboxRecord.pending(message, jsonMapper.writeValueAsString(message)));

        return new SagaRes.Accepted(eventId, sagaId, true);
    }

    private static SagaMessageType parseType(String type) {
        if (!StringUtils.hasText(type)) {
            throw new IllegalArgumentException("type must not be blank");
        }
        try {
            return SagaMessageType.valueOf(type.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("unknown type=" + type, ex);
        }
    }
}
