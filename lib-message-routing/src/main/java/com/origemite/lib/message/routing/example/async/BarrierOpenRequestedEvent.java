package com.origemite.lib.message.routing.example.async;

/**
 * 도메인 로컬 이벤트. Rabbit 타입을 몰라도 된다.
 */
public record BarrierOpenRequestedEvent(
        String siteId,
        String barrierId,
        String reason
) {
}
