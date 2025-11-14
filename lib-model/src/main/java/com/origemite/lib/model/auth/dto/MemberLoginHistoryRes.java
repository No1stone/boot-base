package com.origemite.lib.model.auth.dto;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.FieldDefaults;
import java.util.*;
import java.time.*;

public class MemberLoginHistoryRes {

    @Schema(description = "MemberLoginHistoryRes.Id DTO")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Id {
        @Schema(description = "회원 로그인 이력 아이디")
        private Integer id;
    }

    @Schema(description = "MemberLoginHistoryRes.Name DTO")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Name extends Id {
        // 변수 없음
    }

    @Schema(description = "MemberLoginHistoryRes.Single DTO")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Single extends Name {
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

    @Schema(description = "MemberLoginHistoryRes.Item DTO")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Item extends Single {
        // 하위 연관 관계 DTO는 직접 작성할 수 있음.
    }
}
