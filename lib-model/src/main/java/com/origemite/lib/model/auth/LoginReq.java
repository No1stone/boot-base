package com.origemite.lib.model.auth;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

public class LoginReq {

    @Schema(description = "로그인")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class LoginForIdPw {

        @Schema(description = "로그인 아이디", example = "admin01")
        String loginId;

        @Schema(description = "로그인 패스워드", example = "admin01")
        String loginPassword;

    }

    @Schema(description = "로그인")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class LoginForOtp extends LoginForIdPw {
        @Schema(description = "인증번호", example = "")
        String authenticationNumber;
    }

    @Schema(description = "엑세스 토큰 재발행")
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RefreshToken {

        @NotBlank
        @Schema(description = "리프레시 토큰", example = "2XlbSHlfSxypLAqqKu6wgA")
        String refreshToken;

        @Hidden
        @Null
        @Schema(description = "억세스 토큰 만료시간", example = "")
        Long accessTokenExpiration;

        @Hidden
        @Null
        @Schema(description = "리프레시 토큰 만료시간", example = "")
        Long refreshTokenExpiration;
    }
}
