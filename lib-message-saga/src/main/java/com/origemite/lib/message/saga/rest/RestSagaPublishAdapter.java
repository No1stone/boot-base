package com.origemite.lib.message.saga.rest;

import com.origemite.lib.message.saga.SagaMessage;
import com.origemite.lib.message.saga.SagaPublishPolicy;
import com.origemite.lib.message.saga.SagaPublishPort;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * event-saga {@code POST /sagas}. Kafka 의존 없음 (HTTP만).
 */
public class RestSagaPublishAdapter implements SagaPublishPort {

    private final RestClient restClient;

    public RestSagaPublishAdapter(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public void publish(SagaMessage message) {
        SagaPublishPolicy.requirePublishable(message.type(), message.status());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", message.type().name());
        body.put("eventId", message.eventId());
        body.put("sagaId", message.sagaId());
        body.put("domain", message.domain());
        body.put("step", message.step());
        body.put("status", message.status());
        body.put("payload", message.payload());

        restClient.post()
                .uri("/sagas")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }
}
