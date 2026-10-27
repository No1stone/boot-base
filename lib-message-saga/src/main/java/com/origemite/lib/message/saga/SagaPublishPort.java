package com.origemite.lib.message.saga;

/**
 * 자기 이벤트 발행. 구현은 gRPC → event-saga(Outbox). Kafka 직접 발행 없음.
 */
public interface SagaPublishPort {

    void publish(SagaMessage message);
}
