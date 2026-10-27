package com.origemite.lib.message.saga;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SagaTopicsTest {

    @Test
    @DisplayName("type.domain 토픽")
    void topic() {
        assertThat(SagaTopics.of(SagaMessageType.CREATE, "order")).isEqualTo("create.order");
        assertThat(SagaTopics.of(SagaMessageType.COMPLETE, "order")).isEqualTo("complete.order");
    }
}
