package com.origemite.lib.lock.redis;

import com.origemite.lib.lock.LockKeys;
import com.origemite.lib.lock.LockResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
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
    @DisplayName("watchdog: 획득 성공 시 실행 후 unlock하고 acquired=true")
    void watchdogRunsAndUnlocks() throws Exception {
        String key = LockKeys.of("job", "run");
        when(redissonClient.getLock(key)).thenReturn(lock);
        when(lock.tryLock(anyLong(), eq(TimeUnit.MILLISECONDS))).thenReturn(true);
        when(lock.isHeldByCurrentThread()).thenReturn(true);
        AtomicBoolean ran = new AtomicBoolean(false);

        LockResult<Void> result = redissonLockAdapter.tryWithLock(
                key, Duration.ofSeconds(1), () -> ran.set(true));

        assertThat(result.acquired()).isTrue();
        assertThat(ran).isTrue();
        verify(lock).tryLock(anyLong(), eq(TimeUnit.MILLISECONDS));
        verify(lock).unlock();
    }

    @Test
    @DisplayName("고정 lease: tryLock(wait, lease)를 쓰고 성공 시 실행한다")
    void fixedLeaseUsesLeaseOverload() throws Exception {
        String key = LockKeys.of("job", "lease");
        when(redissonClient.getLock(key)).thenReturn(lock);
        when(lock.tryLock(anyLong(), anyLong(), eq(TimeUnit.MILLISECONDS))).thenReturn(true);
        when(lock.isHeldByCurrentThread()).thenReturn(true);

        LockResult<String> result = redissonLockAdapter.tryWithLock(
                key, Duration.ofSeconds(1), Duration.ofSeconds(30), () -> "ok");

        assertThat(result.acquired()).isTrue();
        assertThat(result.value()).isEqualTo("ok");
        verify(lock).tryLock(anyLong(), anyLong(), eq(TimeUnit.MILLISECONDS));
        verify(lock).unlock();
    }

    @Test
    @DisplayName("획득 실패 시 콜백을 실행하지 않고 missed")
    void skipsActionWhenNotAcquired() throws Exception {
        String key = LockKeys.of("job", "run");
        when(redissonClient.getLock(key)).thenReturn(lock);
        when(lock.tryLock(anyLong(), eq(TimeUnit.MILLISECONDS))).thenReturn(false);
        AtomicBoolean ran = new AtomicBoolean(false);

        LockResult<Void> result = redissonLockAdapter.tryWithLock(
                key, Duration.ofMillis(10), () -> ran.set(true));

        assertThat(result.acquired()).isFalse();
        assertThat(ran).isFalse();
        verify(lock, never()).unlock();
    }

    @Test
    @DisplayName("콜백이 null을 반환해도 acquired=true, value=null")
    void nullValueStillAcquired() throws Exception {
        String key = LockKeys.of("job", "null");
        when(redissonClient.getLock(key)).thenReturn(lock);
        when(lock.tryLock(anyLong(), eq(TimeUnit.MILLISECONDS))).thenReturn(true);
        when(lock.isHeldByCurrentThread()).thenReturn(true);

        LockResult<String> result = redissonLockAdapter.tryWithLock(
                key, Duration.ofSeconds(1), () -> null);

        assertThat(result.acquired()).isTrue();
        assertThat(result.value()).isNull();
        verify(lock).unlock();
    }

    @Test
    @DisplayName("작업 예외 시에도 unlock하고 예외를 전파한다")
    void unlocksWhenActionThrows() throws Exception {
        String key = LockKeys.of("job", "boom");
        when(redissonClient.getLock(key)).thenReturn(lock);
        when(lock.tryLock(anyLong(), eq(TimeUnit.MILLISECONDS))).thenReturn(true);
        when(lock.isHeldByCurrentThread()).thenReturn(true);

        assertThatThrownBy(() -> redissonLockAdapter.tryWithLock(
                key, Duration.ofSeconds(1), () -> {
                    throw new IllegalStateException("boom");
                }))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("boom");
        verify(lock).unlock();
    }

    @Test
    @DisplayName("대기 중 인터럽트되면 missed")
    void interruptedReturnsMissed() throws Exception {
        String key = LockKeys.of("job", "run");
        when(redissonClient.getLock(key)).thenReturn(lock);
        when(lock.tryLock(anyLong(), eq(TimeUnit.MILLISECONDS)))
                .thenThrow(new InterruptedException("interrupted"));

        LockResult<String> result = redissonLockAdapter.tryWithLock(
                key, Duration.ofSeconds(1), () -> "x");

        assertThat(result.acquired()).isFalse();
        assertThat(Thread.currentThread().isInterrupted()).isTrue();
        Thread.interrupted();
    }

    @Test
    @DisplayName("한 스레드가 락을 쥐면 다른 시도는 미실행(동시성 단위)")
    void secondCallerSkippedWhileHeld() throws Exception {
        String key = LockKeys.of("job", "concurrent");
        AtomicBoolean held = new AtomicBoolean(false);
        AtomicInteger runs = new AtomicInteger();
        CountDownLatch firstInside = new CountDownLatch(1);
        CountDownLatch releaseFirst = new CountDownLatch(1);

        RLock realStyle = org.mockito.Mockito.mock(RLock.class);
        when(redissonClient.getLock(key)).thenReturn(realStyle);
        when(realStyle.tryLock(anyLong(), eq(TimeUnit.MILLISECONDS))).thenAnswer(inv -> {
            if (held.compareAndSet(false, true)) {
                return true;
            }
            return false;
        });
        when(realStyle.isHeldByCurrentThread()).thenAnswer(inv -> held.get());
        org.mockito.Mockito.doAnswer(inv -> {
            held.set(false);
            return null;
        }).when(realStyle).unlock();

        Thread holder = new Thread(() -> {
            redissonLockAdapter.tryWithLock(key, Duration.ofSeconds(1), () -> {
                runs.incrementAndGet();
                firstInside.countDown();
                try {
                    releaseFirst.await(2, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        });
        holder.start();
        assertThat(firstInside.await(2, TimeUnit.SECONDS)).isTrue();

        LockResult<Void> second = redissonLockAdapter.tryWithLock(
                key, Duration.ofMillis(50), (Runnable) () -> runs.incrementAndGet());

        assertThat(second.acquired()).isFalse();
        assertThat(runs.get()).isEqualTo(1);

        releaseFirst.countDown();
        holder.join(2000);
        assertThat(holder.isAlive()).isFalse();
    }
}
