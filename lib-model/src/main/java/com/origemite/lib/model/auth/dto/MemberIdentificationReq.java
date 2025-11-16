package com.origemite.lib.model.auth.dto;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.FieldDefaults;
import java.util.*;
import java.time.*;

import java.time.LocalDateTime;;

public class MemberIdentificationReq {

    @Schema(description = "MemberIdentificationReq.Ids DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Ids {
        @Schema(description = "패스 본인 확인 아이디")
        private List<Integer> ids;
    }

    @Schema(description = "MemberIdentificationReq.Filter DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Filter {
        @Schema(description = "패스 본인 확인 아이디")
        private Integer id;
        @Schema(description = "(Encrypted) 이름")
        private String name;
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
        @Schema(description = "(Hashed) 이름")
        private String nameSha;
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

    @Schema(description = "MemberIdentificationReq.Create DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Create {
        @Schema(description = "패스 본인 확인 아이디")
        private Integer id;
        @Schema(description = "(Encrypted) 이름")
        private String name;
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
        @Schema(description = "(Hashed) 이름")
        private String nameSha;
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
    }

    @Schema(description = "MemberIdentificationReq.Update DTO")
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Update {
        @Schema(description = "패스 본인 확인 아이디")
        private Integer id;
        @Schema(description = "(Encrypted) 이름")
        private String name;
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
        @Schema(description = "(Hashed) 이름")
        private String nameSha;
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
    }

}
