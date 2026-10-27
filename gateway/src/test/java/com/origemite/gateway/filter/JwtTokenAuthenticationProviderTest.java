package com.origemite.gateway.filter;

import com.origemite.gateway.conf.JwkAtomic;
import com.origemite.lib.cache.CachePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenAuthenticationProviderTest {

    @Mock
    private JwtTokenProp jwtTokenProp;

    @Mock
    private JwkAtomic jwkAtomic;

    @Mock
    private CachePort cachePort;

    @InjectMocks
    private JwtTokenAuthenticationProvider provider;

    @Test
    @DisplayName("access 토큰 존재 여부는 CachePort.exists로 확인한다")
    void existsAccessTokenUsesCachePort() {
        when(cachePort.exists("access:token-1")).thenReturn(true);

        assertThat(provider.existsAccessToken("token-1")).isTrue();
        verify(cachePort).exists("access:token-1");
    }

    @Test
    @DisplayName("캐시에 없으면 verifyAccessToken도 현재 정책상 true다")
    void verifyWhenMissingStillTrue() {
        when(cachePort.exists("access:missing")).thenReturn(false);
        assertThat(provider.verifyAccessToken("missing")).isTrue();
    }
}
