package com.origemite.lib.common.encryption;

/**
 * 암·복호화 능력 계약.
 * 제품명(Vault 등)은 Port에 두지 않는다.
 */
public interface EncryptionPort {

    String encrypt(String keyName, String plaintext);

    String decrypt(String keyName, String ciphertext);
}
