package com.origemite.lib.message.routing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RoutingKeysTest {

    @Test
    @DisplayName("세그먼트를 점으로 이어 routing key를 만든다")
    void joinsSegments() {
        assertThat(RoutingKeys.of("parking", "barrier", "b1", "command"))
                .isEqualTo("parking.barrier.b1.command");
    }

    @Test
    @DisplayName("세그먼트에 점·콜론이 있으면 거절한다")
    void rejectsIllegalSegment() {
        assertThatThrownBy(() -> RoutingKeys.of("parking", "bar.rier"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
