package com.origemite.lib.message.routing.example;

import com.origemite.lib.message.routing.MessagePublishPort;
import com.origemite.lib.message.routing.RoutingKeys;
import com.origemite.lib.message.routing.RoutingMessage;

/**
 * Facade에서 발행하는 패턴 예.
 * {@code MessagePublishPort}만 주입한다 (Rabbit 타입 직접 사용 금지).
 */
public class BarrierCommandPublisherExample {

    private final MessagePublishPort messagePublishPort;

    public BarrierCommandPublisherExample(MessagePublishPort messagePublishPort) {
        this.messagePublishPort = messagePublishPort;
    }

    public void requestOpen(String siteId, String barrierId, String reason) {
        String routingKey = RoutingKeys.of("parking", "barrier", barrierId, "command");
        RoutingMessage message = RoutingMessage.builder(BarrierOpenRequested.MESSAGE_TYPE)
                .data(new BarrierOpenRequested(siteId, barrierId, reason))
                .build();
        messagePublishPort.publish(routingKey, message);
    }
}
