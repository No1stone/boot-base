package com.origemite.lib.message.routing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MessageHandlerDispatcherTest {

    @Test
    @DisplayName("messageType에 맞는 핸들러로 위임한다")
    void dispatchesByType() {
        AtomicBoolean ran = new AtomicBoolean(false);
        MessageHandler handler = new MessageHandler() {
            @Override
            public String messageType() {
                return "BarrierOpenRequested";
            }

            @Override
            public void onMessage(RoutingMessage message) {
                ran.set(true);
            }
        };
        MessageHandlerDispatcher dispatcher = new MessageHandlerDispatcher(List.of(handler));

        dispatcher.dispatch(RoutingMessage.builder("BarrierOpenRequested").data("x").build());

        assertThat(ran).isTrue();
    }

    @Test
    @DisplayName("만료된 메시지는 핸들러를 호출하지 않는다")
    void skipsExpired() {
        AtomicBoolean ran = new AtomicBoolean(false);
        MessageHandler handler = new MessageHandler() {
            @Override
            public String messageType() {
                return "BarrierOpenRequested";
            }

            @Override
            public void onMessage(RoutingMessage message) {
                ran.set(true);
            }
        };
        MessageHandlerDispatcher dispatcher = new MessageHandlerDispatcher(List.of(handler));

        dispatcher.dispatch(RoutingMessage.builder("BarrierOpenRequested")
                .expiresAt(Instant.now().minusSeconds(10))
                .data("x")
                .build());

        assertThat(ran).isFalse();
    }

    @Test
    @DisplayName("핸들러가 없으면 예외")
    void missingHandler() {
        MessageHandlerDispatcher dispatcher = new MessageHandlerDispatcher(List.of());
        assertThatThrownBy(() -> dispatcher.dispatch(RoutingMessage.builder("Unknown").build()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("동일 type 핸들러 중복이면 생성 실패")
    void duplicateType() {
        MessageHandler ha = new MessageHandler() {
            public String messageType() { return "T"; }
            public void onMessage(RoutingMessage m) { }
        };
        MessageHandler hb = new MessageHandler() {
            public String messageType() { return "T"; }
            public void onMessage(RoutingMessage m) { }
        };
        assertThatThrownBy(() -> new MessageHandlerDispatcher(List.of(ha, hb)))
                .isInstanceOf(IllegalStateException.class);
    }
}
