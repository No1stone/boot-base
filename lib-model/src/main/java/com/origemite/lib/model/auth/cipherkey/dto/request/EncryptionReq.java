package com.origemite.lib.model.auth.cipherkey.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

public class EncryptionReq {

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RsaEncrypt {

        String plainText;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class AesEncrypt {

        String cipherKeyId;
        List<String> plainTexts;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ShaEncrypt {

        List<String> plainTexts;
    }
}
