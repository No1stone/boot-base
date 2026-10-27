package com.origemite.eventsaga.saga.controller;

import com.origemite.eventsaga.saga.dto.SagaReq;
import com.origemite.eventsaga.saga.dto.SagaRes;
import com.origemite.eventsaga.saga.facade.SagaPublishFacade;
import com.origemite.lib.message.saga.grpc.SagaIngressGrpc;
import com.origemite.lib.message.saga.grpc.SagaRequest;
import com.origemite.lib.message.saga.grpc.SagaResponse;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.util.StringUtils;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;

@GrpcService
public class SagaGrpcService extends SagaIngressGrpc.SagaIngressImplBase {

    private final SagaPublishFacade facade;
    private final JsonMapper jsonMapper;

    public SagaGrpcService(SagaPublishFacade facade, JsonMapper sagaJsonMapper) {
        this.facade = facade;
        this.jsonMapper = sagaJsonMapper;
    }

    @Override
    public void accept(SagaRequest request, StreamObserver<SagaResponse> responseObserver) {
        try {
            SagaReq.Ingress body = new SagaReq.Ingress();
            body.setType(request.getType());
            body.setEventId(StringUtils.hasText(request.getEventId()) ? request.getEventId() : null);
            body.setSagaId(StringUtils.hasText(request.getSagaId()) ? request.getSagaId() : null);
            body.setDomain(request.getDomain());
            body.setStep(StringUtils.hasText(request.getStep()) ? request.getStep() : null);
            body.setStatus(StringUtils.hasText(request.getStatus()) ? request.getStatus() : null);
            if (StringUtils.hasText(request.getPayloadJson())) {
                JavaType mapType = jsonMapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
                body.setPayload(jsonMapper.readValue(request.getPayloadJson(), mapType));
            }
            SagaRes.Accepted result = facade.accept(body);
            responseObserver.onNext(SagaResponse.newBuilder()
                    .setEventId(result.getEventId())
                    .setSagaId(result.getSagaId())
                    .setAccepted(result.isAccepted())
                    .setMessage(result.getMessage())
                    .build());
            responseObserver.onCompleted();
        } catch (Exception ex) {
            responseObserver.onError(ex);
        }
    }
}
