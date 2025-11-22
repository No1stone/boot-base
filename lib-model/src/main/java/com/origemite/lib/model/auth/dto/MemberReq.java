package com.origemite.lib.model.auth.dto;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.FieldDefaults;
import java.util.*;
import java.time.*;

import java.time.LocalDateTime;;

public class MemberReq {

    @Schema(description = "MemberReq.Ids DTO")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ids {
        @Schema(description = "회원 아이디")
        private List<String> ids;
    }

    @Schema(description = "MemberReq.Filter DTO")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Filter {
        @Schema(description = "회원 아이디")
        private String id;
        @Schema(description = "로그인 아이디 (Portal: 전화번호, 코드샵: 이메일)")
        private String loginId;
        @Schema(description = "로그인 패스워드 (nullable 가능, varchar(255) → varchar(512) 변경)")
        private String loginPassword;
        @Schema(description = "로그인 패스워드 솔트 키")
        private String loginPasswordSaltKey;
        @Schema(description = "상태 (I: 초대, A: 활성, D: 비활성)")
        private String status;
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

    @Schema(description = "MemberReq.Create DTO")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        @Schema(description = "회원 아이디")
        private String id;
        @Schema(description = "로그인 아이디 (Portal: 전화번호, 코드샵: 이메일)")
        private String loginId;
        @Schema(description = "로그인 패스워드 (nullable 가능, varchar(255) → varchar(512) 변경)")
        private String loginPassword;
        @Schema(description = "로그인 패스워드 솔트 키")
        private String loginPasswordSaltKey;
        @Schema(description = "상태 (I: 초대, A: 활성, D: 비활성)")
        private String status;
    }

    @Schema(description = "MemberReq.Update DTO")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        @Schema(description = "회원 아이디")
        private String id;
        @Schema(description = "로그인 아이디 (Portal: 전화번호, 코드샵: 이메일)")
        private String loginId;
        @Schema(description = "로그인 패스워드 (nullable 가능, varchar(255) → varchar(512) 변경)")
        private String loginPassword;
        @Schema(description = "로그인 패스워드 솔트 키")
        private String loginPasswordSaltKey;
        @Schema(description = "상태 (I: 초대, A: 활성, D: 비활성)")
        private String status;
    }

}
