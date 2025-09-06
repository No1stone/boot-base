package com.origemite.lib.model.auth.cipherkey.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


public class CipherKeyRes {

    @Schema(description = "cipher_key id")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Id {
        @Schema(description = "아이디")
        String id;
    }

    @Schema(description = "cipher_key")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Single extends Id {

        @Schema(description = "모듈 분류 (Module Prefix) e.g., Common (co), Partner (pa)")
        String modulePrefix;

        @Schema(description = "테이블 명")
        String tableName;

        @Schema(description = "암호화 키 값")
        String value;

        @Schema(description = "등록일시", example = "")
        LocalDateTime createdAt;

        @Schema(description = "등록자 아이디", example = "")
        String createdBy;

        @Schema(description = "수정일시", example = "")
        LocalDateTime updatedAt;

        @Schema(description = "수정자 아이디", example = "")
        String updatedBy;
    }
}
