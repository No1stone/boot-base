package com.origemite.apiauth.support.token;

import com.origemite.lib.cache.CachePort;
import com.origemite.lib.common.token.TokenPort;
import com.origemite.lib.common.token.TokenSigningKeys;
import com.origemite.lib.lock.LockPort;
import com.origemite.lib.lock.LockResult;
import com.origemite.lib.model.enums.auth.EnVaultType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtSigningKeySyncTest {

    @Mock
    private TokenPort tokenPort;

    @Mock
    private CachePort cachePort;

    @Mock
    private LockPort lockPort;

    @InjectMocks
    private JwtSigningKeySync jwtSigningKeySync;

    @Test
    @DisplayName("락 획득 시 서명 키를 캐시에 동기화한다")
    void syncAuthJwtKeys() {
        String keyName = EnVaultType.AUTH_JWT.getValue();
        TokenSigningKeys keys = new TokenSigningKeys();
        keys.setLatestVersion(2);
        keys.setKeys(Map.of(
                "1", new TokenSigningKeys.KeyVersion("k1", "pub1", Instant.now()),
                "2", new TokenSigningKeys.KeyVersion("k2", "pub2", Instant.now())
        ));
        when(tokenPort.loadSigningKeys(keyName)).thenReturn(keys);
        when(lockPort.tryWithLock(eq(JwtSigningKeySync.SYNC_LOCK_KEY), any(Duration.class), any(Runnable.class)))
                .thenAnswer(invocation -> {
                    invocation.getArgument(2, Runnable.class).run();
                    return LockResult.hit(null);
                });

        jwtSigningKeySync.syncAuthJwtKeys();

        verify(cachePort).set(JwtSigningKeySync.publicKeyCacheKey(keyName, "1"), "pub1");
        verify(cachePort).set(JwtSigningKeySync.publicKeyCacheKey(keyName, "2"), "pub2");
        verify(cachePort).set(JwtSigningKeySync.LATEST_VERSION_KEY, "2");
        verify(cachePort).set(JwtSigningKeySync.PREVIOUS_VERSION_KEY, "1");
    }

    @Test
    @DisplayName("락 미획득 시 캐시에 쓰지 않는다")
    void skipsSyncWhenLockMissed() {
        when(lockPort.tryWithLock(eq(JwtSigningKeySync.SYNC_LOCK_KEY), any(Duration.class), any(Runnable.class)))
                .thenReturn(LockResult.missed());

        jwtSigningKeySync.syncAuthJwtKeys();

        verify(tokenPort, never()).loadSigningKeys(any());
        verify(cachePort, never()).set(any(), any());
    }

    @Test
    @DisplayName("캐시에서 최신 버전·공개키를 읽는다")
    void readsFromCache() {
        when(cachePort.get(JwtSigningKeySync.LATEST_VERSION_KEY)).thenReturn("3");
        when(cachePort.get(JwtSigningKeySync.publicKeyCacheKey(EnVaultType.AUTH_JWT.getValue(), "3")))
                .thenReturn("pubkey");

        assertThat(jwtSigningKeySync.getLatestVersion()).isEqualTo("3");
        assertThat(jwtSigningKeySync.getPublicKeyForVersion("3")).isEqualTo("pubkey");
        verify(cachePort).get(eq(JwtSigningKeySync.LATEST_VERSION_KEY));
    }
}
