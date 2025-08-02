package io.origemite.lib.model.a4.member.dto.request;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

public class MemberTaskHistoryReq {

    @Schema(description = "회원 작업 내역 관리 테이블 - Ids DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Ids {
        @Schema(description = "작업 내역 아이디")
        private List<Integer> ids;
    }

    @Schema(description = "회원 작업 내역 관리 테이블 - Filter DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Filter {
        @Schema(description = "작업 내역 아이디")
        private Integer id;

        @Schema(description = "회원 아이디")
        private String memberId;

        @Schema(description = "Referer 페이지")
        private String menuName;

        @Schema(description = "Source URL")
        private String menuUrl;

        @Schema(description = "Client IP")
        private String clientIp;

        @Schema(description = "User Agent")
        private String userAgent;

        @Schema(description = "HTTP Method")
        private String method;

        @Schema(description = "Path parameter 포함")
        private String url;

        @Schema(description = "Query parameter 또는 payload")
        private String params;

        @Schema(description = "조회 숫자")
        private Integer count;

        @Schema(description = "다운로드 사유")
        private String downloadReason;

        @Schema(description = "파일 이름")
        private String fileName;

        @Schema(description = "파일 사이즈 (Byte)")
        private String fileSize;

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

    @Schema(description = "회원 작업 내역 관리 테이블 - Create DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Create {
        @Schema(description = "회원 아이디")
        private String memberId;

        @Schema(description = "Referer 페이지")
        private String menuName;

        @Schema(description = "Source URL")
        private String menuUrl;

        @Schema(description = "Client IP")
        private String clientIp;

        @Schema(description = "User Agent")
        private String userAgent;

        @Schema(description = "HTTP Method")
        private String method;

        @Schema(description = "Path parameter 포함")
        private String url;

        @Schema(description = "Query parameter 또는 payload")
        private String params;

        @Schema(description = "조회 숫자")
        private Integer count;

        @Schema(description = "다운로드 사유")
        private String downloadReason;

        @Schema(description = "파일 이름")
        private String fileName;

        @Schema(description = "파일 사이즈 (Byte)")
        private String fileSize;
    }

    @Schema(description = "회원 작업 내역 관리 테이블 - Update DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Update {
        @Schema(description = "작업 내역 아이디")
        private Integer id;

        @Schema(description = "Query parameter 또는 payload")
        private String params;

        @Schema(description = "다운로드 사유")
        private String downloadReason;

        @Schema(description = "파일 이름")
        private String fileName;

        @Schema(description = "파일 사이즈 (Byte)")
        private String fileSize;

        @Schema(description = "수정일시")
        private LocalDateTime updatedAt;

        @Schema(description = "수정자 아이디")
        private String updatedBy;
    }
}
