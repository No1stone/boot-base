package com.origemite.lib.model.a4.api.dto.response;

import com.origemite.lib.model.enums.common.EnStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * API 응답 DTO
 */
public class ApiRes {

    @Schema(description = "API 아이디 응답")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Id {
        @Schema(description = "API 아이디")
        private Integer id;
    }

    @Schema(description = "API 아이디 응답")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Name extends Id{
        @Schema(description = "API 이름")
        private String name;
    }



    @Schema(description = "API 단일 응답")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Single extends Name {
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
    }

    @Schema(description = "API 상세 응답")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Item extends Single {
        // 하위 연관 관계 DTO는 직접 작성할 수 있음.
    }
}
