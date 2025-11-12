package com.origemite.apiauth.auth.service;

import com.origemite.lib.common.util.TransformUtils;
import com.origemite.lib.model.auth.VaultKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
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
    private final static String TRANSIT_KEY = "auth-sig";

    public Signature signJwtPayload(String payload) {
        VaultTransitOperations transit = vaultTemplate.opsForTransit();
        return transit.sign(TRANSIT_KEY, Plaintext.of(payload));
    }

    public VaultKey.Response getValutTransitKeys() {

        VaultTransitOperations transit = vaultTemplate.opsForTransit();
        VaultTransitKey vaultKey = transit.getKey("auth-sig");
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


}
