package com.origemite.lib.message.routing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * messageType → {@link MessageHandler} 위임.
 * 만료({@link RoutingMessage#isExpired()})면 핸들러를 호출하지 않는다.
 * 멱등 처리는 호스트(핸들러) 책임 — 봉투의 {@code idempotencyKey}를 쓴다.
 */
public class MessageHandlerDispatcher {

    private static final Logger log = LoggerFactory.getLogger(MessageHandlerDispatcher.class);

    private final Map<String, MessageHandler> handlersByType;

    public MessageHandlerDispatcher(List<MessageHandler> handlers) {
        this.handlersByType = handlers.stream()
                .collect(Collectors.toMap(MessageHandler::messageType, Function.identity(), (a, b) -> {
                    throw new IllegalStateException(
                            "Duplicate MessageHandler for type=" + a.messageType());
                }));
    }

    public void dispatch(RoutingMessage message) {
        if (message.isExpired()) {
            log.info("Skip expired routing message type={} id={} expiresAt={}",
                    message.messageType(), message.messageId(), message.expiresAt());
            return;
        }
        MessageHandler handler = handlersByType.get(message.messageType());
        if (handler == null) {
            throw new IllegalStateException("No MessageHandler for type=" + message.messageType());
        }
        handler.onMessage(message);
    }
}
