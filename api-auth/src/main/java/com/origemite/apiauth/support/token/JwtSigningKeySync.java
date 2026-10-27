package com.origemite.apiauth.support.token;

import com.origemite.lib.common.cache.CachePort;
import com.origemite.lib.common.token.TokenPort;
import com.origemite.lib.common.token.TokenSigningKeys;
import com.origemite.lib.model.enums.auth.EnVaultType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 서명 키 재료(TokenPort)를 캐시(CachePort)에 동기화하는 api 유스케이스.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtSigningKeySync {

    public static final String LATEST_VERSION_KEY = "auth-sig:latest-version";
    public static final String PREVIOUS_VERSION_KEY = "auth-sig:previous-version";
    public static final String LATEST_PUBLIC_KEY = "auth-sig:latest-public-key";
    public static final String PREVIOUS_PUBLIC_KEY = "auth-sig:previous-public-key";
    public static final String META_KEY = "auth-sig:meta";

    private final TokenPort tokenPort;
    private final CachePort cachePort;

    public void syncAuthJwtKeys() {
        String keyName = EnVaultType.AUTH_JWT.getValue();
        TokenSigningKeys authJwks = tokenPort.loadSigningKeys(keyName);

        // lua script 동시성 제어 로직 추가필요.
        for (Map.Entry<String, TokenSigningKeys.KeyVersion> entry : authJwks.getKeys().entrySet()) {
            cachePort.set(publicKeyCacheKey(keyName, entry.getKey()), entry.getValue().getPublicKey());
        }

        List<Integer> keyList = authJwks.getKeys().keySet().stream()
                .map(Integer::valueOf)
                .sorted(Comparator.reverseOrder())
                .toList();

        int previousVersion = keyList.size() > 1 ? keyList.get(1) : authJwks.getLatestVersion();

        cachePort.set(LATEST_VERSION_KEY, String.valueOf(authJwks.getLatestVersion()));
        cachePort.set(PREVIOUS_VERSION_KEY, String.valueOf(previousVersion));

        cachePort.hashPut(META_KEY, LATEST_VERSION_KEY, String.valueOf(authJwks.getLatestVersion()));
        cachePort.hashPut(
                META_KEY,
                LATEST_PUBLIC_KEY,
                authJwks.getKeys().get(String.valueOf(authJwks.getLatestVersion())).getPublicKey()
        );
        cachePort.hashPut(META_KEY, PREVIOUS_VERSION_KEY, String.valueOf(previousVersion));
        cachePort.hashPut(
                META_KEY,
                PREVIOUS_PUBLIC_KEY,
                authJwks.getKeys().get(String.valueOf(previousVersion)).getPublicKey()
        );
    }

    public String getLatestVersion() {
        return cachePort.get(LATEST_VERSION_KEY);
    }

    public String getPublicKeyForVersion(String version) {
        return cachePort.get(publicKeyCacheKey(EnVaultType.AUTH_JWT.getValue(), version));
    }

    public static String publicKeyCacheKey(String keyName, String version) {
        return keyName + ":key:" + version;
    }
}
