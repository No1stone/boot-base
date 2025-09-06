package com.origemite.lib.model.auth.cipherkey.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

public class HashingReq {

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Password {

        @Schema(description = "G/W에서 RSA Decrypt 된 암호 ")
        String plainText;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class MatchPassword {

        @Schema(description = "G/W에서 RSA Decrypt 된 암호 ")
        String password;
        String hashedPassword;
        String saltKey;
    }
}
