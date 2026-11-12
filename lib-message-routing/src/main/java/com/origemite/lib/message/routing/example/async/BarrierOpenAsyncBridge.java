package com.origemite.lib.message.routing.example.async;

import com.origemite.lib.message.routing.MessagePublishPort;
import com.origemite.lib.message.routing.RoutingKeys;
import com.origemite.lib.message.routing.RoutingMessage;
import com.origemite.lib.message.routing.example.BarrierOpenRequested;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

/**
 * 사용처 패턴: ApplicationEvent → @Async → {@link MessagePublishPort}.
 * <p>
 * 도메인은 로컬 이벤트만 발행. Rabbit 의존은 인프라(어댑터) 모듈에만 둔다.
 * (라우팅은 브로커가 Rabbit이라 발행 어댑터 쪽에 AMQP는 필요. 도메인·Facade에는 불필요.)
 * </p>
 */
public class BarrierOpenAsyncBridge {

    private final MessagePublishPort messagePublishPort;

    public BarrierOpenAsyncBridge(MessagePublishPort messagePublishPort) {
        this.messagePublishPort = messagePublishPort;
    }

    @Async
    @EventListener
    public void onBarrierOpenRequested(BarrierOpenRequestedEvent event) {
        String routingKey = RoutingKeys.of("parking", "barrier", event.barrierId(), "command");
        messagePublishPort.publish(
                routingKey,
                RoutingMessage.builder(BarrierOpenRequested.MESSAGE_TYPE)
                        .data(new BarrierOpenRequested(event.siteId(), event.barrierId(), event.reason()))
                        .build()
        );
    }
}
