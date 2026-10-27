package com.origemite.lib.message.saga;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SagaEventDispatcher {

    private static final Logger log = LoggerFactory.getLogger(SagaEventDispatcher.class);

    private final List<SagaEventHandler> handlers;

    public SagaEventDispatcher(List<SagaEventHandler> handlers) {
        this.handlers = List.copyOf(handlers);
    }

    public void dispatch(SagaMessage message) {
        List<SagaEventHandler> matched = new ArrayList<>();
        for (SagaEventHandler handler : handlers) {
            if (handler.type() != message.type()) {
                continue;
            }
            String d = handler.domain();
            if ("*".equals(d) || d.equalsIgnoreCase(message.domain())) {
                matched.add(handler);
            }
        }
        if (matched.isEmpty()) {
            log.warn("No SagaEventHandler type={} domain={} eventId={}",
                    message.type(), message.domain(), message.eventId());
            return;
        }
        for (SagaEventHandler handler : matched) {
            handler.onEvent(message);
        }
    }
}
