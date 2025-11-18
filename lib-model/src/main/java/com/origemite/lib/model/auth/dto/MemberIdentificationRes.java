package com.origemite.lib.model.auth.dto;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.FieldDefaults;
import java.util.*;
import java.time.*;

public class MemberIdentificationRes {

    @Schema(description = "MemberIdentificationRes.Id DTO")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Id {
        @Schema(description = "패스 본인 확인 아이디")
        private Integer id;
    }

    @Schema(description = "MemberIdentificationRes.Name DTO")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Name extends Id {
        @Schema(description = "(Encrypted) 이름")
        private String name;
        @Schema(description = "(Hashed) 이름")
        private String nameSha;
    }

    @Schema(description = "MemberIdentificationRes.Single DTO")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Single extends Name {
        @Schema(description = "(Encrypted) 휴대폰번호")
        private String mobilePhoneNumber;
        @Schema(description = "(Encrypted) 이메일")
        private String email;
        @Schema(description = "(Encrypted) CI")
        private String ci;
        @Schema(description = "생년월일")
        private String birthday;
        @Schema(description = "성별 (M: 남성, W: 여성, U: 알 수 없음)")
        private String gender;
        @Schema(description = "외국인 여부 (Y: 외국인, N: 내국인)")
        private String foreignYn;
        @Schema(description = "(Hashed) 휴대폰번호")
        private String mobilePhoneNumberSha;
        @Schema(description = "(Hashed) 이메일")
        private String emailSha;
        @Schema(description = "(Hashed) CI")
        private String ciSha;
        @Schema(description = "암호화 키 아이디 (co_cipher_key)")
        private String cipherKeyId;
        @Schema(description = "이동 통신사 코드 (CC. 공통)")
        private String mobileCarrierCode;
        @Schema(description = "회원 아이디")
        private String memberId;
        @Schema(description = "상태 (R: 요청, C: 완료, A: 취소, E: 만료)")
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

    @Schema(description = "MemberIdentificationRes.Item DTO")
    @Data
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Item extends Single {
        // 하위 연관 관계 DTO는 직접 작성할 수 있음.
    }

}
