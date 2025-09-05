package com.origemite.lib.model.oam.release.dto.request;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

public class ReleaseReq {

    @Schema(description = "릴리스 관리 테이블 - Ids DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Ids {
        @Schema(description = "릴리스 아이디")
        private List<Integer> ids;
    }

    @Schema(description = "릴리스 관리 테이블 - Filter DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Filter {
        @Schema(description = "릴리스 아이디")
        private Integer id;

        @Schema(description = "어플리케이션 코드 (CC. Application)")
        private String applicationCode;

        @Schema(description = "어플리케이션 버전 e.g., v1.0.0")
        private String applicationVersion;

        @Schema(description = "릴리스 노트 또는 confluence or Jira page link")
        private String note;

        @Schema(description = "앱 파일 아이디 (For app, 파일 ID / For web, bucket URL)")
        private String fileId;

        @Schema(description = "프로파일 파일 아이디 (for iOS)")
        private String profileFileId;

        @Schema(description = "강제 업데이트 여부 (Y: 강제, N: 선택)")
        private String forceUpdateYn;

        @Schema(description = "상태 (A: 활성, D: 비활성)")
        private String status;

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

    @Schema(description = "릴리스 관리 테이블 - Create DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Create {
        @Schema(description = "어플리케이션 코드 (CC. Application)")
        private String applicationCode;

        @Schema(description = "어플리케이션 버전 e.g., v1.0.0")
        private String applicationVersion;

        @Schema(description = "릴리스 노트 또는 confluence or Jira page link")
        private String note;

        @Schema(description = "앱 파일 아이디 (For app, 파일 ID / For web, bucket URL)")
        private String fileId;

        @Schema(description = "프로파일 파일 아이디 (for iOS)")
        private String profileFileId;

        @Schema(description = "강제 업데이트 여부 (Y: 강제, N: 선택)")
        private String forceUpdateYn;

        @Schema(description = "상태 (A: 활성, D: 비활성)")
        private String status;
    }

    @Schema(description = "릴리스 관리 테이블 - Update DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Update {
        @Schema(description = "릴리스 아이디")
        private Integer id;

        @Schema(description = "어플리케이션 버전 e.g., v1.0.0")
        private String applicationVersion;

        @Schema(description = "릴리스 노트 또는 confluence or Jira page link")
        private String note;

        @Schema(description = "앱 파일 아이디 (For app, 파일 ID / For web, bucket URL)")
        private String fileId;

        @Schema(description = "프로파일 파일 아이디 (for iOS)")
        private String profileFileId;

        @Schema(description = "강제 업데이트 여부 (Y: 강제, N: 선택)")
        private String forceUpdateYn;

        @Schema(description = "상태 (A: 활성, D: 비활성)")
        private String status;

        @Schema(description = "수정일시")
        private LocalDateTime updatedAt;

        @Schema(description = "수정자 아이디")
        private String updatedBy;
    }
}
