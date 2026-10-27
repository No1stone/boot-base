package com.origemite.apiauth.support.token;

import com.origemite.lib.common.token.TokenPort;
import com.origemite.lib.common.token.TokenSignature;
import com.origemite.lib.common.token.TokenSigningKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.core.VaultTransitOperations;
import org.springframework.vault.support.Plaintext;
import org.springframework.vault.support.Signature;
import org.springframework.vault.support.VaultSignRequest;
import org.springframework.vault.support.VaultTransitKey;

import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class VaultTokenAdapter implements TokenPort {

    private final VaultTemplate vaultTemplate;

    @Override
    public TokenSignature sign(String keyName, String signingInput) {
        VaultSignRequest request = VaultSignRequest.builder()
                .plaintext(Plaintext.of(signingInput))
                .signatureAlgorithm("pkcs1v15")
                .hashAlgorithm("sha2-256")
                .build();

        Signature signature = vaultTemplate.opsForTransit().sign(keyName, request);
        String[] parts = signature.getSignature().split(":");
        String version = parts[1];
        String signatureString = parts[2];
        int keyVersion = Integer.parseInt(version.substring(1));
        byte[] sigBytes = Base64.getDecoder().decode(signatureString);
        String signatureBase64Url = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(sigBytes);

        return new TokenSignature(keyVersion, signatureBase64Url);
    }

    @Override
    @SuppressWarnings("unchecked")
    public TokenSigningKeys loadSigningKeys(String keyName) {
        VaultTransitOperations transit = vaultTemplate.opsForTransit();
        VaultTransitKey vaultKey = transit.getKey(keyName);
        log.debug("transit.getKey = {}", vaultKey != null ? vaultKey.getKeys() : null);

        if (vaultKey == null) {
            throw new IllegalStateException("No key found for " + keyName);
        }

        TokenSigningKeys response = new TokenSigningKeys();
        response.setName(vaultKey.getName());
        response.setType(vaultKey.getType());
        response.setLatestVersion(vaultKey.getLatestVersion());
        Map<String, TokenSigningKeys.KeyVersion> keyVersions = new HashMap<>();
        response.setKeys(keyVersions);

        for (Map.Entry<String, Object> entry : vaultKey.getKeys().entrySet()) {
            String version = entry.getKey();
            Map<String, Object> keyInfo = (Map<String, Object>) entry.getValue();
            String name = (String) keyInfo.get("name");
            String publicKey = (String) keyInfo.get("public_key");
            String creationTime = (String) keyInfo.get("creation_time");

            TokenSigningKeys.KeyVersion keyVersion = new TokenSigningKeys.KeyVersion();
            keyVersion.setPublicKey(publicKey
                    .replace("\\n", "\n")
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", ""));
            keyVersion.setCreationTime(Instant.parse(creationTime));
            keyVersion.setName(name);
            keyVersions.put(version, keyVersion);
        }
        return response;
    }
}
