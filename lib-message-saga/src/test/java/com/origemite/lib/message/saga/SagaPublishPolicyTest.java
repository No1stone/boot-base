package com.origemite.lib.message.saga;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SagaPublishPolicyTest {

    @Test
    @DisplayName("성공 COMPLETE · 보상 FAIL+COMPENSATE만 발행")
    void publishable() {
        assertThat(SagaPublishPolicy.shouldPublish(SagaMessageType.COMPLETE, "SUCCEEDED")).isTrue();
        assertThat(SagaPublishPolicy.shouldPublish(SagaMessageType.FAIL, "COMPENSATE_PAYMENT")).isTrue();
        assertThat(SagaPublishPolicy.shouldPublish(SagaMessageType.CREATE, null)).isFalse();
        assertThat(SagaPublishPolicy.shouldPublish(SagaMessageType.STATUS, "OK")).isFalse();
        assertThat(SagaPublishPolicy.shouldPublish(SagaMessageType.FAIL, "PAYMENT_ERROR")).isFalse();
    }

    @Test
    @DisplayName("비발행 type은 requirePublishable 예외")
    void requirePublishable() {
        assertThatThrownBy(() -> SagaPublishPolicy.requirePublishable(SagaMessageType.CREATE, null))
                .isInstanceOf(IllegalArgumentException.class);
        SagaPublishPolicy.requirePublishable(SagaMessageType.COMPLETE, null);
    }
}
