package com.origemite.lib.message.routing;

/**
 * 메시지 발행 계약. 제품(RabbitMQ 등) 이름은 Port에 두지 않는다.
 */
public interface MessagePublishPort {

    /**
     * 기본 exchange로 발행한다.
     *
     * @param routingKey {@link RoutingKeys}로 만든 키
     * @param message    봉투
     */
    void publish(String routingKey, RoutingMessage message);

    /**
     * exchange를 명시해 발행한다.
     */
    void publish(String exchange, String routingKey, RoutingMessage message);
}
