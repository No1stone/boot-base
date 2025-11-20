package com.origemite.lib.model.auth.dto;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.FieldDefaults;
import java.util.*;
import java.time.*;

import java.time.LocalDateTime;;

public class MemberLoginHistoryReq {

    @Schema(description = "MemberLoginHistoryReq.Ids DTO")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Ids {
        @Schema(description = "회원 로그인 이력 아이디")
        private List<Integer> ids;
    }

    @Schema(description = "MemberLoginHistoryReq.Filter DTO")
    @Data
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

    @Schema(description = "MemberLoginHistoryReq.Create DTO")
    @Data
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

    @Schema(description = "MemberLoginHistoryReq.Update DTO")
    @Data
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
