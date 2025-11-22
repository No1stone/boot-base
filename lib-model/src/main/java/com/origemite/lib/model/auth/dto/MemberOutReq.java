package com.origemite.lib.model.auth.dto;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.FieldDefaults;
import java.util.*;
import java.time.*;

import java.time.LocalDateTime;;

public class MemberOutReq {

    @Schema(description = "MemberOutReq.Ids DTO")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ids {
        @Schema(description = "아이디")
        private List<Integer> ids;
    }

    @Schema(description = "MemberOutReq.Filter DTO")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Filter {
        @Schema(description = "아이디")
        private Integer id;
        @Schema(description = "회원 아이디")
        private String memberId;
        @Schema(description = "탈퇴 일시")
        private LocalDateTime outAt;
        @Schema(description = "탈퇴 이유")
        private String outReason;
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

    @Schema(description = "MemberOutReq.Create DTO")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        @Schema(description = "아이디")
        private Integer id;
        @Schema(description = "회원 아이디")
        private String memberId;
        @Schema(description = "탈퇴 일시")
        private LocalDateTime outAt;
        @Schema(description = "탈퇴 이유")
        private String outReason;
    }

    @Schema(description = "MemberOutReq.Update DTO")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        @Schema(description = "아이디")
        private Integer id;
        @Schema(description = "회원 아이디")
        private String memberId;
        @Schema(description = "탈퇴 일시")
        private LocalDateTime outAt;
        @Schema(description = "탈퇴 이유")
        private String outReason;
    }

}
