package com.origemite.lib.common.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenSigningKeys {

    private String name;
    private String type;
    private int latestVersion;
    private Map<String, KeyVersion> keys;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyVersion {
        private String name;
        private String publicKey;
        private Instant creationTime;
    }
}
