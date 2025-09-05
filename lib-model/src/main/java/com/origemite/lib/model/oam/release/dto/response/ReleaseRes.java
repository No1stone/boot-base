package com.origemite.lib.model.oam.release.dto.response;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

public class ReleaseRes {

    @Schema(description = "릴리스 관리 테이블 - Id DTO")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Id {
        @Schema(description = "릴리스 아이디")
        private Integer id;
    }

    @Schema(description = "릴리스 관리 테이블 - Name DTO")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Name extends Id {
        // 변수 없이도 클래스 유지
    }

    @Schema(description = "릴리스 관리 테이블 - Single DTO (모든 필드 포함)")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Single extends Name {
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

    @Schema(description = "릴리스 관리 테이블 - Item DTO")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Item extends Single {
        // 하위 연관 관계 DTO는 직접 작성할 수 있음.
    }
}
