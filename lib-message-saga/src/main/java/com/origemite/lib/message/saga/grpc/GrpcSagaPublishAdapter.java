package com.origemite.lib.message.saga.grpc;

import com.origemite.lib.message.saga.SagaMessage;
import com.origemite.lib.message.saga.SagaPublishPolicy;
import com.origemite.lib.message.saga.SagaPublishPort;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * event-saga {@code SagaIngress.Accept} gRPC 클라이언트.
 * 호출마다 deadline 적용 (빈 생성 시 withDeadlineAfter 금지 — 절대시각이 고정됨).
 */
public class GrpcSagaPublishAdapter implements SagaPublishPort {

    private final SagaIngressGrpc.SagaIngressBlockingStub stub;
    private final JsonMapper jsonMapper;
    private final Duration deadline;

    public GrpcSagaPublishAdapter(
            SagaIngressGrpc.SagaIngressBlockingStub stub,
            JsonMapper jsonMapper,
            Duration deadline
    ) {
        this.stub = stub;
        this.jsonMapper = jsonMapper;
        this.deadline = deadline == null ? Duration.ofSeconds(3) : deadline;
    }

    @Override
    public void publish(SagaMessage message) {
        SagaPublishPolicy.requirePublishable(message.type(), message.status());
        String payloadJson = message.payload() == null ? "" : jsonMapper.writeValueAsString(message.payload());
        SagaRequest request = SagaRequest.newBuilder()
                .setType(message.type().name())
                .setEventId(nullToEmpty(message.eventId()))
                .setSagaId(nullToEmpty(message.sagaId()))
                .setDomain(nullToEmpty(message.domain()))
                .setStep(nullToEmpty(message.step()))
                .setStatus(nullToEmpty(message.status()))
                .setPayloadJson(payloadJson)
                .build();
        SagaResponse response = stub
                .withDeadlineAfter(deadline.toMillis(), TimeUnit.MILLISECONDS)
                .accept(request);
        if (!response.getAccepted()) {
            throw new IllegalStateException("event-saga rejected publish: " + response.getMessage());
        }
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
