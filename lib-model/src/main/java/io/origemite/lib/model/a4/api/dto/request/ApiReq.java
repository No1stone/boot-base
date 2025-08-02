package io.origemite.lib.model.a4.api.dto.request;

import io.origemite.lib.model.enums.common.EnStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * API 요청 DTO
 */
public class ApiReq {

    @Schema(description = "API 아이디 목록 요청")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Ids {
        @Schema(description = "API 아이디 목록 (다중 조회/삭제용)", example = "[1, 2, 3, 4]")
        private List<Integer> ids;
    }

    @Schema(description = "API 요청 필터")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Filter {
        @Schema(description = "API 아이디")
        private Integer id;

        @Schema(description = "API 이름")
        private String name;

        @Schema(description = "API 설명")
        private String description;

        @Schema(description = "API 서비스 URL")
        private String url;

        @Schema(description = "API URI")
        private String uri;

        @Schema(description = "HTTP 메소드 (GET, POST, PUT, DELETE)")
        private String httpMethod;

        @Schema(description = "접근 가능 역할")
        private Integer roles;

        @Schema(description = "상태 (A: 활성, D: 비활성)")
        private EnStatus status;

        @Schema(description = "생성 일시 (필터용)")
        private LocalDateTime createdAt;

        @Schema(description = "수정 일시 (필터용)")
        private LocalDateTime updatedAt;

        @Schema(description = "생성자 아이디 (필터용)")
        private String createdBy;

        @Schema(description = "수정자 아이디 (필터용)")
        private String updatedBy;
    }

    @Schema(description = "API 생성 요청")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Create {
        @NotBlank
        @Schema(description = "API 이름")
        private String name;

        @Schema(description = "API 설명")
        private String description;

        @NotBlank
        @Schema(description = "API 서비스 URL (예: https://codeshop.diddda.io)")
        private String url;

        @NotBlank
        @Schema(description = "API URI")
        private String uri;

        @NotBlank
        @Schema(description = "HTTP 메소드 (GET, POST, PUT, DELETE)")
        private String httpMethod;

        @Schema(description = "접근 가능 역할")
        private Integer roles;

        @NotBlank
        @Schema(description = "상태 (A: 활성, D: 비활성)")
        private EnStatus status;
    }

    @Schema(description = "API 수정 요청")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Update {
        @NotBlank
        @Schema(description = "API 아이디 (수정 대상)")
        private Integer id;

        @Schema(description = "API 이름")
        private String name;

        @Schema(description = "API 설명")
        private String description;

        @Schema(description = "API 서비스 URL (예: https://codeshop.diddda.io)")
        private String url;

        @Schema(description = "API URI")
        private String uri;

        @Schema(description = "HTTP 메소드 (GET, POST, PUT, DELETE)")
        private String httpMethod;

        @Schema(description = "접근 가능 역할")
        private Integer roles;

        @Schema(description = "상태 (A: 활성, D: 비활성)")
        private EnStatus status;
    }
}
