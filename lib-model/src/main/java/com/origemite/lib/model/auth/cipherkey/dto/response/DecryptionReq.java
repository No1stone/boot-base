package com.origemite.lib.model.auth.cipherkey.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

public class DecryptionReq {

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RsaDecrypt {

        String encryptedText;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class AesDecrypt {

        String cipherKeyId;
        List<String> encryptedTexts;
    }
}
