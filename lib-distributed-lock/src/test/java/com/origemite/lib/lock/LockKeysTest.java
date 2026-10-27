package com.origemite.lib.lock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LockKeysTest {

    @Test
    @DisplayName("영역·리소스로 lock:a:b 키를 만든다")
    void ofAreaResource() {
        assertThat(LockKeys.of("member", "save")).isEqualTo("lock:member:save");
    }

    @Test
    @DisplayName("id를 붙이면 lock:a:b:id 키를 만든다")
    void ofWithId() {
        assertThat(LockKeys.of("member", "save", 42L)).isEqualTo("lock:member:save:42");
    }

    @Test
    @DisplayName("세그먼트에 콜론이 있으면 거절한다")
    void rejectsColonInSegment() {
        assertThatThrownBy(() -> LockKeys.of("mem:ber", "save"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
