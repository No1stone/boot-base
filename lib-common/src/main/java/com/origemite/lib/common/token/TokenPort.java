package com.origemite.lib.common.token;

/**
 * 토큰 서명·키 재료 능력 계약.
 * Vault 등 제품명은 Port에 두지 않는다.
 */
public interface TokenPort {

    TokenSignature sign(String keyName, String signingInput);

    TokenSigningKeys loadSigningKeys(String keyName);
}
