package com.origemite.lib.message.saga.example.async;

import com.origemite.lib.message.saga.SagaMessage;
import com.origemite.lib.message.saga.SagaMessageType;
import com.origemite.lib.message.saga.SagaPublishPort;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

/**
 * 사용처 패턴 예: ApplicationEvent → @Async → REST/gRPC로 event-saga.
 * <p>
 * 도메인은 {@code ApplicationEventPublisher.publishEvent(new OrderStartedEvent(...))} 만 호출.
 * 이 브리지만 Saga/브로커를 알고, 앱에는 Kafka·Rabbit 의존이 필요 없다 (발행 경로).
 * </p>
 *
 * <pre>
 * // Facade
 * events.publishEvent(new OrderStartedEvent(orderId, "order", payload));
 *
 * // 설정
 * origemite.message.saga.publish.transport: rest
 * origemite.message.saga.publish.base-url: http://event-saga:8095
 * origemite.message.saga.consume.enabled: false
 * // Kafka exclude 가능 (발행만 쓸 때)
 * </pre>
 */
public class OrderStartedSagaBridge {

    private final SagaPublishPort sagaPublishPort;

    public OrderStartedSagaBridge(SagaPublishPort sagaPublishPort) {
        this.sagaPublishPort = sagaPublishPort;
    }

    @Async
    @EventListener
    public void onOrderStarted(OrderStartedEvent event) {
        sagaPublishPort.publish(
                SagaMessage.builder(SagaMessageType.COMPLETE, event.domain())
                        .sagaId(event.orderId())
                        .status("STARTED")
                        .payload(event.payload())
                        .build()
        );
    }
}
