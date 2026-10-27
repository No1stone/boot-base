package com.origemite.lib.message.routing.rabbit;

import com.origemite.lib.message.routing.MessagePublishPort;
import com.origemite.lib.message.routing.RoutingMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.util.Assert;
import tools.jackson.databind.json.JsonMapper;

@RequiredArgsConstructor
public class RabbitMessagePublishAdapter implements MessagePublishPort {

    public static final String HEADER_MESSAGE_TYPE = "messageType";
    public static final String HEADER_MESSAGE_ID = "messageId";
    public static final String HEADER_CORRELATION_ID = "correlationId";
    public static final String HEADER_IDEMPOTENCY_KEY = "idempotencyKey";
    public static final String HEADER_EXPIRES_AT = "expiresAt";

    private final RabbitTemplate rabbitTemplate;
    private final MessageRoutingProperties properties;
    private final JsonMapper jsonMapper;

    @Override
    public void publish(String routingKey, RoutingMessage message) {
        publish(properties.getExchange(), routingKey, message);
    }

    @Override
    public void publish(String exchange, String routingKey, RoutingMessage message) {
        Assert.hasText(exchange, "exchange must not be blank");
        Assert.hasText(routingKey, "routingKey must not be blank");
        Assert.notNull(message, "message must not be null");

        MessageProperties props = new MessageProperties();
        props.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        props.setMessageId(message.messageId());
        props.setHeader(HEADER_MESSAGE_TYPE, message.messageType());
        props.setHeader(HEADER_MESSAGE_ID, message.messageId());
        props.setHeader(HEADER_IDEMPOTENCY_KEY, message.idempotencyKey());
        if (message.correlationId() != null) {
            props.setCorrelationId(message.correlationId());
            props.setHeader(HEADER_CORRELATION_ID, message.correlationId());
        }
        if (message.expiresAt() != null) {
            props.setHeader(HEADER_EXPIRES_AT, message.expiresAt().toString());
            props.setExpiration(String.valueOf(
                    Math.max(1L, message.expiresAt().toEpochMilli() - System.currentTimeMillis())));
        }
        if (message.headers() != null) {
            message.headers().forEach(props::setHeader);
        }

        byte[] body = jsonMapper.writeValueAsBytes(message);
        rabbitTemplate.send(exchange, routingKey, new Message(body, props));
    }
}
