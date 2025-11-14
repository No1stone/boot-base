package com.origemite.lib.model.auth.dto;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.FieldDefaults;
import java.util.*;
import java.time.*;

import java.time.LocalDateTime;;

public class MemberPasswordChangeHistoryReq {

    @Schema(description = "MemberPasswordChangeHistoryReq.Ids DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Ids {
        @Schema(description = "회원 비밀번호 변경 이력 아이디")
        private List<Integer> ids;
    }

    @Schema(description = "MemberPasswordChangeHistoryReq.Filter DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Filter {
        @Schema(description = "회원 비밀번호 변경 이력 아이디")
        private Integer id;
        @Schema(description = "회원 아이디")
        private String memberId;
        @Schema(description = "비밀번호")
        private String loginPassword;
        @Schema(description = "비밀번호 salt_key")
        private String loginPasswordSaltKey;
        @Schema(description = "비밀번호 변경 일시")
        private LocalDateTime changeAt;
        @Schema(description = "암호화 키 아이디")
        private String cipherKeyId;
        @Schema(description = "수정일시 (yyyy-MM-dd HH:mm:ss)")
        private LocalDateTime updatedAt;
        @Schema(description = "수정자")
        private String updatedBy;
        @Schema(description = "낙관적 락 버전")
        private Integer version;
        @Schema(description = "생성일시 (yyyy-MM-dd HH:mm:ss)")
        private LocalDateTime createdAt;
        @Schema(description = "생성자")
        private String createdBy;
    }

    @Schema(description = "MemberPasswordChangeHistoryReq.Create DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Create {
        @Schema(description = "회원 비밀번호 변경 이력 아이디")
        private Integer id;
        @Schema(description = "회원 아이디")
        private String memberId;
        @Schema(description = "비밀번호")
        private String loginPassword;
        @Schema(description = "비밀번호 salt_key")
        private String loginPasswordSaltKey;
        @Schema(description = "비밀번호 변경 일시")
        private LocalDateTime changeAt;
        @Schema(description = "암호화 키 아이디")
        private String cipherKeyId;
    }

    @Schema(description = "MemberPasswordChangeHistoryReq.Update DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Update {
        @Schema(description = "회원 비밀번호 변경 이력 아이디")
        private Integer id;
        @Schema(description = "회원 아이디")
        private String memberId;
        @Schema(description = "비밀번호")
        private String loginPassword;
        @Schema(description = "비밀번호 salt_key")
        private String loginPasswordSaltKey;
        @Schema(description = "비밀번호 변경 일시")
        private LocalDateTime changeAt;
        @Schema(description = "암호화 키 아이디")
        private String cipherKeyId;
    }

}
