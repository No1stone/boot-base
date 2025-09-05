package com.origemite.lib.model.a4.member.dto.response;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

public class MemberTaskHistoryRes {

    @Schema(description = "회원 작업 내역 관리 테이블 - Id DTO")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Id {
        @Schema(description = "작업 내역 아이디")
        private Integer id;
    }

    @Schema(description = "회원 작업 내역 관리 테이블 - Name DTO")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Name extends Id {
        // 변수 없이도 클래스 유지
    }

    @Schema(description = "회원 작업 내역 관리 테이블 - Single DTO (모든 필드 포함)")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Single extends Name {
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

    @Schema(description = "회원 작업 내역 관리 테이블 - Item DTO")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Item extends Single {
        // 하위 연관 관계 DTO는 직접 작성할 수 있음.
    }
}
