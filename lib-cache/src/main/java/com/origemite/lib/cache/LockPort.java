package com.origemite.lib.cache;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 분산 락 능력 계약.
 * 제품(Redis/Redisson 등) 이름은 Port에 두지 않는다.
 */
public interface LockPort {

    /**
     * 락을 획득하면 action을 실행하고 해제한다.
     * 획득 실패 시 false를 반환하고 action은 실행하지 않는다.
     */
    boolean tryWithLock(String key, Duration wait, Duration lease, Runnable action);

    /**
     * 락을 획득하면 action 결과를 Optional로 반환한다.
     * 획득 실패 시 Optional.empty().
     */
    <T> Optional<T> tryWithLock(String key, Duration wait, Duration lease, Supplier<T> action);
}
