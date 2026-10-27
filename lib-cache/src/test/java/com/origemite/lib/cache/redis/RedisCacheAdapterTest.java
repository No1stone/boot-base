package com.origemite.lib.cache.redis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedisCacheAdapterTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @InjectMocks
    private RedisCacheAdapter redisCacheAdapter;

    @Test
    @DisplayName("get/set은 ValueOperations에 위임한다")
    void getAndSet() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("k")).thenReturn("v");

        assertThat(redisCacheAdapter.get("k")).isEqualTo("v");
        redisCacheAdapter.set("k", "v");
        verify(valueOperations).set("k", "v");
    }

    @Test
    @DisplayName("exists는 hasKey 결과를 반환한다")
    void exists() {
        when(redisTemplate.hasKey("access:t")).thenReturn(true);
        assertThat(redisCacheAdapter.exists("access:t")).isTrue();
    }

    @Test
    @DisplayName("TTL set과 hashPut을 위임한다")
    void setWithTtlAndHashPut() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        redisCacheAdapter.set("k", "v", Duration.ofSeconds(10));
        redisCacheAdapter.hashPut("h", "f", "v");

        verify(valueOperations).set("k", "v", 10_000L, TimeUnit.MILLISECONDS);
        verify(hashOperations).put("h", "f", "v");
    }
}
