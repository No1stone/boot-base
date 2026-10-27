package com.origemite.lib.cache.spring;

import com.origemite.lib.cache.CachePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CachePortCacheTest {

    @Mock
    private CachePort cachePort;

    private CachePortCache cache;

    @BeforeEach
    void setUp() {
        cache = new CachePortCache("member", cachePort, Duration.ofMinutes(30), new JsonCacheValueCodec());
    }

    @Test
    @DisplayName("put 후 get은 JSON으로 CachePort를 왕복한다")
    void putAndGetRoundTrip() {
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

        Sample value = new Sample("m1");
        cache.put("m1", value);

        verify(cachePort).set(keyCaptor.capture(), valueCaptor.capture(), eq(Duration.ofMinutes(30)));
        assertThat(keyCaptor.getValue()).isEqualTo("member::m1");
        assertThat(valueCaptor.getValue()).contains("m1");
        assertThat(valueCaptor.getValue().trim()).startsWith("{");

        when(cachePort.get("member::m1")).thenReturn(valueCaptor.getValue());

        Sample loaded = cache.get("m1", Sample.class);
        assertThat(loaded).isNotNull();
        assertThat(loaded.id()).isEqualTo("m1");
    }

    @Test
    @DisplayName("evict는 CachePort.delete를 호출한다")
    void evict() {
        cache.evict("m1");
        verify(cachePort).delete("member::m1");
    }

    @Test
    @DisplayName("clear는 prefix 삭제를 호출한다")
    void clear() {
        cache.clear();
        verify(cachePort).deleteByPrefix("member::");
    }

    @Test
    @DisplayName("미스면 null이다")
    void miss() {
        when(cachePort.get(anyString())).thenReturn(null);
        assertThat(cache.get("missing")).isNull();
    }

    private record Sample(String id) {
    }
}
