package com.origemite.lib.message.routing;

/**
 * 구독 측 처리. 호스트가 빈으로 등록하면 라이브러리가 messageType으로 라우팅한다.
 */
public interface MessageHandler {

    /** 이 핸들러가 받을 {@link RoutingMessage#messageType()}. */
    String messageType();

    void onMessage(RoutingMessage message);
}
