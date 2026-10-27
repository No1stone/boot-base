package com.origemite.apiauth.support.token;

import com.origemite.lib.common.token.TokenSignature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.core.VaultTransitOperations;
import org.springframework.vault.support.Signature;
import org.springframework.vault.support.VaultSignRequest;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VaultTokenAdapterTest {

    @Mock
    private VaultTemplate vaultTemplate;

    @Mock
    private VaultTransitOperations transitOperations;

    @InjectMocks
    private VaultTokenAdapter vaultTokenAdapter;

    @Test
    @DisplayName("sign은 Vault transit 서명을 URL-safe Base64로 변환한다")
    void signConvertsSignature() {
        when(vaultTemplate.opsForTransit()).thenReturn(transitOperations);
        byte[] raw = "sig-bytes".getBytes();
        String vaultSig = "vault:v2:" + Base64.getEncoder().encodeToString(raw);
        when(transitOperations.sign(eq("auth-jwt"), any(VaultSignRequest.class)))
                .thenReturn(Signature.of(vaultSig));

        TokenSignature result = vaultTokenAdapter.sign("auth-jwt", "header.payload");

        assertThat(result.getLatestVersion()).isEqualTo(2);
        assertThat(result.getSignature()).isEqualTo(
                Base64.getUrlEncoder().withoutPadding().encodeToString(raw)
        );
    }
}
