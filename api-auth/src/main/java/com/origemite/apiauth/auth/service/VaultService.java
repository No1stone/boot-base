package com.origemite.apiauth.auth.service;

import com.origemite.lib.common.util.TransformUtils;
import com.origemite.lib.model.auth.VaultKey;
import com.origemite.lib.model.enums.auth.EnVaultType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.core.VaultTransitOperations;
import org.springframework.vault.support.Plaintext;
import org.springframework.vault.support.Signature;
import org.springframework.vault.support.VaultResponse;
import org.springframework.vault.support.VaultTransitKey;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaultService {

    private final VaultTemplate vaultTemplate;
    private final static String TRANSIT_PATH = "transit/keys/";
    //    private final static String TRANSIT_KEY = "auth-sig";
    private final StringRedisTemplate redisTemplate;
    private static final String LATEST_VERSION_KEY = "auth-sig:latest-version";

    //삭제할것.
    public VaultKey.Response getValutTransitKeys(EnVaultType vaultType) {
        return valutTransitKeys(vaultType);
    }

    private VaultKey.Response valutTransitKeys(EnVaultType vaultType) {

        VaultTransitOperations transit = vaultTemplate.opsForTransit();
        VaultTransitKey vaultKey = transit.getKey(vaultType.getValue());
        log.debug("transit.getKey = {}", vaultKey.getKeys());

        if (vaultKey == null) {
            throw new IllegalStateException("No key found for " + vaultKey);
        }

        VaultKey.Response response = new VaultKey.Response();
        response.setName(vaultKey.getName());
        response.setType(vaultKey.getType());
        response.setLatestVersion(vaultKey.getLatestVersion());
        Map<String, VaultKey.KeyVersion> keyVersions = new HashMap<>();
        response.setKeys(keyVersions);
        for (Map.Entry<String, Object> entry : vaultKey.getKeys().entrySet()) {
            String version = entry.getKey();
            Map<String, Object> keyInfo = (Map<String, Object>) entry.getValue();
            String name = (String) keyInfo.get("name");
            String publicKey = (String) keyInfo.get("public_key");
            String creationTime = (String) keyInfo.get("creation_time");
            VaultKey.KeyVersion keyVersion = new VaultKey.KeyVersion();
            keyVersion.setPublicKey(publicKey
                    .replace("\\n", "\n")
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", ""));
            keyVersion.setCreationTime(Instant.parse(creationTime));
            keyVersion.setName(name);
            Map<String, VaultKey.KeyVersion> keyMap = response.getKeys();
            keyMap.put(version, keyVersion);
        }
        return response;
    }


    public void initVaultForRedis() {
        VaultKey.Response authJwks = valutTransitKeys(EnVaultType.AUTH_JWT);
        for (Map.Entry<String, VaultKey.KeyVersion> entry : authJwks.getKeys().entrySet()) {
            redisTemplate.opsForValue().set(EnVaultType.AUTH_JWT.getValue()
                            + ":key:"
                            + entry.getKey()
                    , entry.getValue().getPublicKey());
        }
        redisTemplate.opsForValue().set(LATEST_VERSION_KEY, String.valueOf(authJwks.getLatestVersion()));
    }

    public String getVaultKeyForVersion(String version) {
        return redisTemplate.opsForValue().get(
                EnVaultType.AUTH_JWT.getValue()
                        + ":key:"
                        + version
        );
    }

    public String getVaultKeyForLastVersion() {
        return redisTemplate.opsForValue().get(
                EnVaultType.AUTH_JWT.getValue()
                        + ":key:"
                        + redisTemplate.opsForValue().get(LATEST_VERSION_KEY)
        );
    }
}
