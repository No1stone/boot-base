package com.origemite.lib.model.auth.dto;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.FieldDefaults;
import java.util.*;
import java.time.*;

import java.time.LocalDateTime;;

public class MemberConnectReq {

    @Schema(description = "MemberConnectReq.Ids DTO")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Ids {
        @Schema(description = "회원 연결 아이디")
        private List<Integer> ids;
    }

    @Schema(description = "MemberConnectReq.Filter DTO")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Filter {
        @Schema(description = "회원 연결 아이디")
        private Integer id;
        @Schema(description = "회원 아이디")
        private String memberId;
        @Schema(description = "연결 유형 (구글, 네이버, 카카오 등)")
        private String connectTypeCode;
        @Schema(description = "연결된 계정의 고유 식별자 (구글, 네이버, 카카오 등)")
        private String connectedId;
        @Schema(description = "(Encrypted) 이름")
        private String name;
        @Schema(description = "(Encrypted) 이메일")
        private String email;
        @Schema(description = "(Encrypted) 휴대폰번호")
        private String mobile;
        @Schema(description = "프로필 사진 URL")
        private String picture;
        @Schema(description = "성별 (M: 남성, F: 여성, U: 알 수 없음)")
        private String gender;
        @Schema(description = "연령대 (e.g., 20~29)")
        private String age;
        @Schema(description = "생일 (MMDD, e.g., 0923)")
        private String birthday;
        @Schema(description = "출생년도 (YYYY, e.g., 1973)")
        private String birthyear;
        @Schema(description = "언어 및 지역 코드 (e.g., ko_KR)")
        private String locale;
        @Schema(description = "암호화 키 아이디 (co_cipher_key)")
        private String cipherKeyId;
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

    @Schema(description = "MemberConnectReq.Create DTO")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Create {
        @Schema(description = "회원 연결 아이디")
        private Integer id;
        @Schema(description = "회원 아이디")
        private String memberId;
        @Schema(description = "연결 유형 (구글, 네이버, 카카오 등)")
        private String connectTypeCode;
        @Schema(description = "연결된 계정의 고유 식별자 (구글, 네이버, 카카오 등)")
        private String connectedId;
        @Schema(description = "(Encrypted) 이름")
        private String name;
        @Schema(description = "(Encrypted) 이메일")
        private String email;
        @Schema(description = "(Encrypted) 휴대폰번호")
        private String mobile;
        @Schema(description = "프로필 사진 URL")
        private String picture;
        @Schema(description = "성별 (M: 남성, F: 여성, U: 알 수 없음)")
        private String gender;
        @Schema(description = "연령대 (e.g., 20~29)")
        private String age;
        @Schema(description = "생일 (MMDD, e.g., 0923)")
        private String birthday;
        @Schema(description = "출생년도 (YYYY, e.g., 1973)")
        private String birthyear;
        @Schema(description = "언어 및 지역 코드 (e.g., ko_KR)")
        private String locale;
        @Schema(description = "암호화 키 아이디 (co_cipher_key)")
        private String cipherKeyId;
        @Schema(description = "상태 (I: 초대, A: 활성, D: 비활성)")
        private String status;
    }

    @Schema(description = "MemberConnectReq.Update DTO")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Update {
        @Schema(description = "회원 연결 아이디")
        private Integer id;
        @Schema(description = "회원 아이디")
        private String memberId;
        @Schema(description = "연결 유형 (구글, 네이버, 카카오 등)")
        private String connectTypeCode;
        @Schema(description = "연결된 계정의 고유 식별자 (구글, 네이버, 카카오 등)")
        private String connectedId;
        @Schema(description = "(Encrypted) 이름")
        private String name;
        @Schema(description = "(Encrypted) 이메일")
        private String email;
        @Schema(description = "(Encrypted) 휴대폰번호")
        private String mobile;
        @Schema(description = "프로필 사진 URL")
        private String picture;
        @Schema(description = "성별 (M: 남성, F: 여성, U: 알 수 없음)")
        private String gender;
        @Schema(description = "연령대 (e.g., 20~29)")
        private String age;
        @Schema(description = "생일 (MMDD, e.g., 0923)")
        private String birthday;
        @Schema(description = "출생년도 (YYYY, e.g., 1973)")
        private String birthyear;
        @Schema(description = "언어 및 지역 코드 (e.g., ko_KR)")
        private String locale;
        @Schema(description = "암호화 키 아이디 (co_cipher_key)")
        private String cipherKeyId;
        @Schema(description = "상태 (I: 초대, A: 활성, D: 비활성)")
        private String status;
    }

}
