package com.origemite.lib.cache.redis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedissonLockAdapterTest {

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock lock;

    @InjectMocks
    private RedissonLockAdapter redissonLockAdapter;

    @Test
    @DisplayName("락 획득 성공 시 action을 실행하고 unlock한다")
    void runsActionWhenLockAcquired() throws Exception {
        when(redissonClient.getLock("lock:job")).thenReturn(lock);
        when(lock.tryLock(anyLong(), anyLong(), eq(TimeUnit.MILLISECONDS))).thenReturn(true);
        when(lock.isHeldByCurrentThread()).thenReturn(true);
        AtomicBoolean ran = new AtomicBoolean(false);

        boolean acquired = redissonLockAdapter.tryWithLock(
                "lock:job", Duration.ofSeconds(1), Duration.ofSeconds(30), () -> ran.set(true));

        assertThat(acquired).isTrue();
        assertThat(ran).isTrue();
        verify(lock).unlock();
    }

    @Test
    @DisplayName("락 획득 실패 시 action을 실행하지 않는다")
    void skipsActionWhenLockNotAcquired() throws Exception {
        when(redissonClient.getLock(anyString())).thenReturn(lock);
        when(lock.tryLock(anyLong(), anyLong(), eq(TimeUnit.MILLISECONDS))).thenReturn(false);
        AtomicBoolean ran = new AtomicBoolean(false);

        boolean acquired = redissonLockAdapter.tryWithLock(
                "lock:job", Duration.ofMillis(10), Duration.ofSeconds(1), () -> ran.set(true));

        assertThat(acquired).isFalse();
        assertThat(ran).isFalse();
        verify(lock, never()).unlock();
    }

    @Test
    @DisplayName("Supplier 성공 시 결과를 Optional로 반환한다")
    void returnsSupplierResult() throws Exception {
        when(redissonClient.getLock("lock:value")).thenReturn(lock);
        when(lock.tryLock(anyLong(), anyLong(), eq(TimeUnit.MILLISECONDS))).thenReturn(true);
        when(lock.isHeldByCurrentThread()).thenReturn(true);

        Optional<String> result = redissonLockAdapter.tryWithLock(
                "lock:value", Duration.ofSeconds(1), Duration.ofSeconds(5), () -> "ok");

        assertThat(result).contains("ok");
        verify(lock).unlock();
    }

    @Test
    @DisplayName("대기 중 인터럽트되면 empty를 반환한다")
    void interruptedReturnsEmpty() throws Exception {
        when(redissonClient.getLock(anyString())).thenReturn(lock);
        when(lock.tryLock(anyLong(), anyLong(), eq(TimeUnit.MILLISECONDS)))
                .thenThrow(new InterruptedException("interrupted"));

        Optional<String> result = redissonLockAdapter.tryWithLock(
                "lock:job", Duration.ofSeconds(1), Duration.ofSeconds(1), () -> "x");

        assertThat(result).isEmpty();
        assertThat(Thread.currentThread().isInterrupted()).isTrue();
        Thread.interrupted();
    }
}
