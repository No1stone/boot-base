package com.origemite.lib.model.auth.cipherkey.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
public class CipherKeyReq {

    @Data
    @Builder
    @AllArgsConstructor
    public static class KeyNamespace {

        String modulePrefix;

        String tableName;
    }
}
