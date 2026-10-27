package com.origemite.lib.common.cache;

import java.time.Duration;

/**
 * 캐시 능력 계약.
 * Redis / Caffeine 등 Provider는 Adapter가 고른다.
 */
public interface CachePort {

    String get(String key);

    boolean exists(String key);

    void set(String key, String value);

    void set(String key, String value, Duration ttl);

    void hashPut(String key, String field, String value);
}
