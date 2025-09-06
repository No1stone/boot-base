package com.origemite.lib.model.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

;

public class AppMemberLoginHistoryReq {

    @Schema(description = "AppMemberLoginHistoryReq.Ids DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Ids {
        @Schema(description = "회원 로그인 이력 아이디")
        private List<Integer> ids;
    }

    @Schema(description = "AppMemberLoginHistoryReq.Filter DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Filter {
        @Schema(description = "회원 로그인 이력 아이디")
        private Integer id;
        @Schema(description = "회원 아이디")
        private String memberId;
        @Schema(description = "로그인 일시")
        private LocalDateTime loginAt;
        @Schema(description = "로그인 이력 사용자 에이전트")
        private String loginHistoryUserAgent;
        @Schema(description = "로그인 이력 사용자 IP 주소")
        private String loginHistoryIp;
        @Schema(description = "로그인 성공 여부 (Y: 성공, N: 실패)")
        private String loginSuccessYn;
        @Schema(description = "등록일시")
        private LocalDateTime createdAt;
        @Schema(description = "등록자 아이디")
        private String createdBy;
    }

    @Schema(description = "AppMemberLoginHistoryReq.Create DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Create {
        @Schema(description = "회원 로그인 이력 아이디")
        private Integer id;
        @Schema(description = "회원 아이디")
        private String memberId;
        @Schema(description = "로그인 일시")
        private LocalDateTime loginAt;
        @Schema(description = "로그인 이력 사용자 에이전트")
        private String loginHistoryUserAgent;
        @Schema(description = "로그인 이력 사용자 IP 주소")
        private String loginHistoryIp;
        @Schema(description = "로그인 성공 여부 (Y: 성공, N: 실패)")
        private String loginSuccessYn;
    }

    @Schema(description = "AppMemberLoginHistoryReq.Update DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Update {
        @Schema(description = "회원 로그인 이력 아이디")
        private Integer id;
        @Schema(description = "회원 아이디")
        private String memberId;
        @Schema(description = "로그인 일시")
        private LocalDateTime loginAt;
        @Schema(description = "로그인 이력 사용자 에이전트")
        private String loginHistoryUserAgent;
        @Schema(description = "로그인 이력 사용자 IP 주소")
        private String loginHistoryIp;
        @Schema(description = "로그인 성공 여부 (Y: 성공, N: 실패)")
        private String loginSuccessYn;
    }

}
