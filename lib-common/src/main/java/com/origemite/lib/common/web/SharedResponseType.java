package com.origemite.lib.common.web;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SharedResponseType implements ResponseTypeInterface {

    // Common
    RSA_ENCRYPTION_ERROR(HttpStatus.OK, "SSCOPI1001", "RSA 암호화 실패"),
    AES_ENCRYPTION_ERROR(HttpStatus.OK, "SSCOPI1002", "AES 암호화 실패"),
    AES_DECRYPTION_ERROR(HttpStatus.OK, "SSCOPI1003", "AES 복호화 실패"),
    HASH_ERROR(HttpStatus.OK, "SSCOPI1004", "해쉬 암호화 실패"),
    SHARED_COMMON_API_UNKNOWN(HttpStatus.OK, "SSCOPI9999", "정의되어있지 않음"),

    // Partner

    // Legacy
    SECRET_MANAGER_ERROR(HttpStatus.OK, "SSLEPI1001", "Secret Manager 키 조회 실패"),
    RSA_DECRYPTION_ERROR(HttpStatus.OK, "SSLEPI1002", "RSA 복호화 실패"),

    SHARED_LEGACY_API_UNKNOWN(HttpStatus.OK, "SSLEPI9999", "정의되어있지 않음");

    private final HttpStatus httpStatus;
    private final String code;
    private final String desc;


    SharedResponseType(HttpStatus httpStatus, String code, String desc) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.desc = desc;
    }

}
