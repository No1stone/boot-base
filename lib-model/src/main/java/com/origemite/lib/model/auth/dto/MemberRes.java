package com.origemite.lib.model.auth.dto;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.FieldDefaults;
import java.util.*;
import java.time.*;

public class MemberRes {

    @Schema(description = "MemberRes.Id DTO")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Id {
        @Schema(description = "회원 아이디")
        private String id;
    }

    @Schema(description = "MemberRes.Name DTO")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Name extends Id {
        // 변수 없음
    }

    @Schema(description = "MemberRes.Single DTO")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Single extends Name {
        @Schema(description = "로그인 아이디 (Portal: 전화번호, 코드샵: 이메일)")
        private String loginId;
        @Schema(description = "로그인 패스워드 (nullable 가능, varchar(255) → varchar(512) 변경)")
        private String loginPassword;
        @Schema(description = "로그인 패스워드 솔트 키")
        private String loginPasswordSaltKey;
        @Schema(description = "상태 (I: 초대, A: 활성, D: 비활성)")
        private String status;
        @Schema(description = "수정일시 (yyyy-MM-dd HH:mm:ss)")
        private LocalDateTime updatedAt;
        @Schema(description = "수정자")
        private String updatedBy;
        @Schema(description = "낙관적 락 버전")
        private Integer version;
        @Schema(description = "생성일시 (yyyy-MM-dd HH:mm:ss)")
        private LocalDateTime createdAt;
        @Schema(description = "생성자")
        private String createdBy;
    }

    @Schema(description = "MemberRes.Item DTO")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Item extends Single {
        // 하위 연관 관계 DTO는 직접 작성할 수 있음.
    }
}
