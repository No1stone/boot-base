package com.origemite.apiauth.support.encryption;

import com.origemite.lib.common.encryption.EncryptionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.vault.core.VaultTemplate;

@Component
@RequiredArgsConstructor
public class VaultEncryptionAdapter implements EncryptionPort {

    private final VaultTemplate vaultTemplate;

    @Override
    public String encrypt(String keyName, String plaintext) {
        return vaultTemplate.opsForTransit().encrypt(keyName, plaintext);
    }

    @Override
    public String decrypt(String keyName, String ciphertext) {
        return vaultTemplate.opsForTransit().decrypt(keyName, ciphertext);
    }
}
