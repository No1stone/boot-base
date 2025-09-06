package com.origemite.lib.model.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

public class AppMemberSessionRes {

    @Schema(description = "AppMemberSessionRes.Id DTO")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Id {
        @Schema(description = "중복 로그인 허용 로그인 세션 아이디")
        private String id;
    }

    @Schema(description = "AppMemberSessionRes.Name DTO")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Name extends Id {
        // 변수 없음
    }

    @Schema(description = "AppMemberSessionRes.Single DTO")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Single extends Name {
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

    @Schema(description = "AppMemberSessionRes.Item DTO")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Item extends Single {
        // 하위 연관 관계 DTO는 직접 작성할 수 있음.
    }
}
