package com.origemite.lib.message.saga.example.async;

/**
 * 도메인/애플리케이션이 발행하는 로컬 이벤트.
 * Kafka·Rabbit·gRPC 타입을 몰라도 된다.
 */
public record OrderStartedEvent(
        String orderId,
        String domain,
        Object payload
) {
}
