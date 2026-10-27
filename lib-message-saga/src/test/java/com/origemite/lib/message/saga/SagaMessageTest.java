package com.origemite.lib.message.saga;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SagaMessageTest {

    @Test
    @DisplayName("CREATE는 sagaId 없으면 eventId로 채운다")
    void createFillsSagaId() {
        SagaMessage message = SagaMessage.builder(SagaMessageType.CREATE, "order")
                .eventId("e-1")
                .payload(MapLike.status("NEW"))
                .build();
        assertThat(message.sagaId()).isEqualTo("e-1");
    }

    @Test
    @DisplayName("STATUS는 sagaId 필수")
    void statusRequiresSagaId() {
        assertThatThrownBy(() -> SagaMessage.builder(SagaMessageType.STATUS, "order")
                .eventId("e-2")
                .sagaId("")
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static final class MapLike {
        static java.util.Map<String, String> status(String s) {
            return java.util.Map.of("status", s);
        }
    }
}
