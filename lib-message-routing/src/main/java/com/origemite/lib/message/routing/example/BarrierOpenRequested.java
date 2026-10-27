package com.origemite.lib.message.routing.example;

/**
 * 주차 관제 샘플 페이로드. 호스트 도메인 DTO로 교체한다.
 */
public record BarrierOpenRequested(
        String siteId,
        String barrierId,
        String reason
) {
    public static final String MESSAGE_TYPE = "BarrierOpenRequested";
}
