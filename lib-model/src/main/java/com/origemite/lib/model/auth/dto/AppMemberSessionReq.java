package com.origemite.lib.model.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

;

public class AppMemberSessionReq {

    @Schema(description = "AppMemberSessionReq.Ids DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Ids {
        @Schema(description = "중복 로그인 허용 로그인 세션 아이디")
        private List<String> ids;
    }

    @Schema(description = "AppMemberSessionReq.Filter DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Filter {
        @Schema(description = "중복 로그인 허용 로그인 세션 아이디")
        private String id;
        @Schema(description = "회원 아이디")
        private String memberId;
        @Schema(description = "Refresh token")
        private String refreshToken;
        @Schema(description = "Refresh token 만료 일시")
        private LocalDateTime expiration;
        @Schema(description = "등록일시")
        private LocalDateTime createdAt;
        @Schema(description = "등록자 아이디")
        private String createdBy;
        @Schema(description = "수정일시")
        private LocalDateTime updatedAt;
        @Schema(description = "수정자 아이디")
        private String updatedBy;
        @Schema(description = "버전")
        private Integer version;
    }

    @Schema(description = "AppMemberSessionReq.Create DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Create {
        @Schema(description = "중복 로그인 허용 로그인 세션 아이디")
        private String id;
        @Schema(description = "회원 아이디")
        private String memberId;
        @Schema(description = "Refresh token")
        private String refreshToken;
        @Schema(description = "Refresh token 만료 일시")
        private LocalDateTime expiration;
    }

    @Schema(description = "AppMemberSessionReq.Update DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Update {
        @Schema(description = "중복 로그인 허용 로그인 세션 아이디")
        private String id;
        @Schema(description = "회원 아이디")
        private String memberId;
        @Schema(description = "Refresh token")
        private String refreshToken;
        @Schema(description = "Refresh token 만료 일시")
        private LocalDateTime expiration;
    }

}
