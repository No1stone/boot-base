package com.origemite.lib.cache.redis;

import com.origemite.lib.cache.LockPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedissonLockAdapter implements LockPort {

    private final RedissonClient redissonClient;

    @Override
    public boolean tryWithLock(String key, Duration wait, Duration lease, Runnable action) {
        return tryWithLock(key, wait, lease, () -> {
            action.run();
            return Boolean.TRUE;
        }).isPresent();
    }

    @Override
    public <T> Optional<T> tryWithLock(String key, Duration wait, Duration lease, Supplier<T> action) {
        RLock lock = redissonClient.getLock(key);
        boolean acquired = false;
        try {
            acquired = lock.tryLock(wait.toMillis(), lease.toMillis(), TimeUnit.MILLISECONDS);
            if (!acquired) {
                return Optional.empty();
            }
            return Optional.ofNullable(action.get());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Interrupted while waiting for lock key={}", key);
            return Optional.empty();
        } finally {
            if (acquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
