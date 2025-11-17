package com.origemite.lib.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Map;

public class VaultKey {

    @Schema(description = "Vault key")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Response {

        String name;
        String type;
        int latestVersion;
        Map<String, KeyVersion> keys;

    }

    @Schema(description = "KeyVersion")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class KeyVersion {
        String name;
        String publicKey;
        Instant creationTime;
    }

    @Schema(description = "KeyVersion")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Signature {
        int latestVersion;
        String signature;
    }
}
