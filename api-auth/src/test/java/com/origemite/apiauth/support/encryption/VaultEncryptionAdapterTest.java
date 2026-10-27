package com.origemite.apiauth.support.encryption;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.core.VaultTransitOperations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VaultEncryptionAdapterTest {

    @Mock
    private VaultTemplate vaultTemplate;

    @Mock
    private VaultTransitOperations transitOperations;

    @InjectMocks
    private VaultEncryptionAdapter vaultEncryptionAdapter;

    @Test
    @DisplayName("encrypt는 transit encrypt를 호출한다")
    void encryptDelegates() {
        when(vaultTemplate.opsForTransit()).thenReturn(transitOperations);
        when(transitOperations.encrypt("member-aes", "plain")).thenReturn("vault:v1:cipher");

        String result = vaultEncryptionAdapter.encrypt("member-aes", "plain");

        assertThat(result).isEqualTo("vault:v1:cipher");
        verify(transitOperations).encrypt("member-aes", "plain");
    }

    @Test
    @DisplayName("decrypt는 transit decrypt를 호출한다")
    void decryptDelegates() {
        when(vaultTemplate.opsForTransit()).thenReturn(transitOperations);
        when(transitOperations.decrypt("member-aes", "vault:v1:cipher")).thenReturn("plain");

        String result = vaultEncryptionAdapter.decrypt("member-aes", "vault:v1:cipher");

        assertThat(result).isEqualTo("plain");
        verify(transitOperations).decrypt("member-aes", "vault:v1:cipher");
    }
}
