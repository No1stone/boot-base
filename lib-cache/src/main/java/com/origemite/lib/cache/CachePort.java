package com.origemite.lib.cache;

import java.time.Duration;

/**
 * 캐시 능력 계약.
 * Redis 구현: {@link com.origemite.lib.cache.redis.RedisCacheAdapter}.
 * 분산 락: {@link LockPort} / {@link com.origemite.lib.cache.redis.RedissonLockAdapter}.
 */
public interface CachePort {

    String get(String key);

    boolean exists(String key);

    void set(String key, String value);

    void set(String key, String value, Duration ttl);

    void hashPut(String key, String field, String value);

    void delete(String key);

    /** prefix로 시작하는 키를 모두 삭제한다. {@code @CacheEvict(allEntries=true)} 용. */
    void deleteByPrefix(String prefix);
}
