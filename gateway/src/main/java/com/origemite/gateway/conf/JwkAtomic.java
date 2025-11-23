package com.origemite.gateway.conf;

import com.origemite.gateway.conf.dto.JwkMeta;
import com.origemite.lib.webflux.common.TransformUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwkAtomic {

    private static final String LATEST_VERSION_KEY = "auth-sig:latest-version";
    private static final String PREVIOUS_VERSION_KEY = "auth-sig:previous-version";
    private static final String META_KEY = "auth-sig:meta";
    private static final String JWK_KEY_PREFIX   = "auth-sig:key:";
    private static final long CACHE_TTL_MS = 30_000L;
    private volatile long loadedAt = 0L;

    private final AtomicReference<JwkMeta> stateRef = new AtomicReference<>();
    private final StringRedisTemplate redisTemplate;

    private boolean isReloadNeeded(int tokenVer) {
        JwkMeta state = stateRef.get();
        long now = System.currentTimeMillis();

        // 1) 아직 캐시 없음
        if (state == null) return true;

        // 2) TTL 만료
        if (now - loadedAt > CACHE_TTL_MS) return true;

        // 3) 토큰 버전이 latest보다 크면: 우리가 뒤쳐짐
        if (tokenVer > state.getLatestVersion()) return true;

        return false;
    }

    private synchronized JwkMeta reloadFromRedis() {

        // double-check
        JwkMeta prev = stateRef.get();
        if (prev != null && (System.currentTimeMillis() - loadedAt) < CACHE_TTL_MS) {
            return prev;
        }

        // 1) latest & previous version 가져오기
        HashOperations<String,String,String> ops = redisTemplate.opsForHash();
        Map<String, String> map = ops.entries(META_KEY);
        int latest = Integer.parseInt(map.get(LATEST_VERSION_KEY));
        int previous = Integer.parseInt(map.get(PREVIOUS_VERSION_KEY));
        // 2) publicKey 가져오기
        String latestPub  = redisTemplate.opsForValue().get(JWK_KEY_PREFIX + latest);
        String previousPub = redisTemplate.opsForValue().get(JWK_KEY_PREFIX + previous);

        // 3) JwkMeta 스냅샷 생성
        JwkMeta newMeta = JwkMeta.builder()
                .setLatestVersion(latest)
                .setPreviousVersion(previous)
                .setLatestPublicKey(latestPub)
                .setPreviousPublicKey(previousPub)
                ;
//        log.debug("getLatestPublicKey - {}", TransformUtils.toString(newMeta.getLatestPublicKey()));
        // 4) atomic 교체
        stateRef.set(newMeta);
        loadedAt = System.currentTimeMillis();
        return newMeta;
    }

    public RSAPublicKey resolveKey(String kid) {
        int tokenVer = Integer.parseInt(kid);

        JwkMeta state = stateRef.get();

        if (isReloadNeeded(tokenVer)) {
            state = reloadFromRedis();
        }

        // 용인되는 범위 판단
        if (tokenVer == state.getLatestVersion()) {
            return state.getRsaLatestPublicKey();
        }

        if (tokenVer == state.getPreviousVersion()) {
            return state.getRsaPriviousPublicKey();
        }

        if (tokenVer > state.getLatestVersion()) {
            // reload 이후에도 latest보다 크면 위조 or 잘못된 토큰
            throw new RuntimeException("Invalid or future kid version.");
        }

        if (tokenVer < state.getPreviousVersion()) {
            throw new RuntimeException("Expired kid version.");
        }

        throw new RuntimeException("Unknown kid.");
    }

    private int extractVersion(String kid) {
        if (kid == null) {
            throw new IllegalArgumentException("kid is null");
        }

        int idx = kid.lastIndexOf(':');
        if (idx == -1 || idx == kid.length() - 1) {
            throw new IllegalArgumentException("Invalid kid format: " + kid);
        }

        String verStr = kid.substring(idx + 1);
        try {
            return Integer.parseInt(verStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid version number in kid: " + kid, e);
        }
    }


}
