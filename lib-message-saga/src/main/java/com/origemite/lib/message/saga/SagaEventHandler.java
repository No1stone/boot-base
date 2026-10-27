package com.origemite.lib.message.saga;

/**
 * type(+domain) 단위 처리기. 호스트가 빈으로 등록.
 */
public interface SagaEventHandler {

    SagaMessageType type();

    /** 특정 도메인만 처리. {@code *} 이면 모든 domain. */
    default String domain() {
        return "*";
    }

    void onEvent(SagaMessage message);
}
