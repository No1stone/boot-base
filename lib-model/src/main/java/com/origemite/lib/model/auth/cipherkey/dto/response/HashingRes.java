package com.origemite.lib.model.auth.cipherkey.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

public class HashingRes {

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Password {

        String hashedPassword;
        String saltKey;
    }
}
