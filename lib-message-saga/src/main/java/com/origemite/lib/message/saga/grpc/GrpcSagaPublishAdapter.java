package com.origemite.lib.message.saga.grpc;

import com.origemite.lib.message.saga.SagaMessage;
import com.origemite.lib.message.saga.SagaPublishPolicy;
import com.origemite.lib.message.saga.SagaPublishPort;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.json.JsonMapper;

/**
 * event-saga {@code SagaIngress.Accept} gRPC 클라이언트.
 */
@RequiredArgsConstructor
public class GrpcSagaPublishAdapter implements SagaPublishPort {

    private final SagaIngressGrpc.SagaIngressBlockingStub stub;
    private final JsonMapper jsonMapper;

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
        SagaResponse response = stub.accept(request);
        if (!response.getAccepted()) {
            throw new IllegalStateException("event-saga rejected publish: " + response.getMessage());
        }
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
