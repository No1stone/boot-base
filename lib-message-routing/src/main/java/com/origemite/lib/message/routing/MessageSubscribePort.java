package com.origemite.lib.message.routing;

/**
 * 큐 구독 계약. 바인딩·리스너 기동은 어댑터가 담당한다.
 */
public interface MessageSubscribePort {

    /**
     * queue를 exchange+routingKey에 묶고, 수신 시 {@link MessageHandler}로 위임한다.
     */
    void bind(String queue, String exchange, String routingKey);
}
