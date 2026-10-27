package com.origemite.lib.message.saga;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class SagaEventDispatcherTest {

    @Test
    @DisplayName("type·domain에 맞는 핸들러만 호출")
    void matches() {
        AtomicInteger hits = new AtomicInteger();
        SagaEventHandler orderCreate = new SagaEventHandler() {
            @Override
            public SagaMessageType type() {
                return SagaMessageType.CREATE;
            }

            @Override
            public String domain() {
                return "order";
            }

            @Override
            public void onEvent(SagaMessage message) {
                hits.incrementAndGet();
            }
        };
        SagaEventDispatcher dispatcher = new SagaEventDispatcher(List.of(orderCreate));

        dispatcher.dispatch(SagaMessage.builder(SagaMessageType.CREATE, "order").build());
        dispatcher.dispatch(SagaMessage.builder(SagaMessageType.CREATE, "payment").build());

        assertThat(hits.get()).isEqualTo(1);
    }
}
