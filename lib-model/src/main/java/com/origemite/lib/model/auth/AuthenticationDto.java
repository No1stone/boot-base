package com.origemite.lib.model.auth;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

public class AuthenticationDto {

    @Data
    public static class AuthenticationResponseDto {

        AuthenticationSecurityAlertDto securityAlert;

        AccessTokenDto accessTokenDetail;

        public void setSecurityAlert(LoginHistoryDto recentHistory, String userAgent, String ip) {
            if(recentHistory != null) {
                var securityAlert = new AuthenticationSecurityAlertDto();
                var isUserAgentDifferent = recentHistory.getRecentLoginUserAgent() != null &&
                        !recentHistory.getRecentLoginUserAgent().equals(userAgent);
                var isIpDifferent = recentHistory.getRecentLoginIp() != null && !recentHistory.getRecentLoginIp().equals(ip);
                var showAlert = isUserAgentDifferent || isIpDifferent;
                securityAlert.setShowAlert(showAlert);
                if (showAlert) {
                    securityAlert.setMessage("최종 접속 계정(IP, 매체)정보와 상이합니다.");
                }
                this.securityAlert = securityAlert;
            } else {
                var securityAlert = new AuthenticationSecurityAlertDto();
                securityAlert.setMessage("첫 로그인 입니다.");
            }
        }
    }

    @Data
    @Builder
    public static class LoginHistoryDto {

        String recentLoginIp;

        String recentLoginUserAgent;

        LocalDateTime recentLoginAt;

        LocalDateTime recentPasswordChangeTime;
    }

    @Data
    public static class AuthenticationSecurityAlertDto {

        boolean showAlert;

        String message;
    }

    @Data
    @NoArgsConstructor
    public static class AccessTokenDto {

        @JsonAlias("token_type")
        final String tokenType = "bearer";

        @JsonAlias("access_token")
        String accessToken;

        @JsonAlias("expires_in")
        long expiresIn;

        @JsonAlias("refresh_token")
        String refreshToken;

        long refreshTokenExpiresIn;

        @JsonAlias("data")
        AccessTokenDto.UserIdDto data = new AccessTokenDto.UserIdDto();

        @Data
        @NoArgsConstructor
        public static class UserIdDto {

            String userId;
        }

        public AccessTokenDto(String accessToken, long expiresIn) {
            this.accessToken = accessToken;
            this.expiresIn = expiresIn;
        }
    }

    @Data
    @NoArgsConstructor
    public static class SaveForSubmitOtp {

        @NotNull
        String otpCode;
        String userAgent;
        String loginIp;
    }

    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Schema(description = "서비스별 역할 정보")
    public static class ServiceRole {
        @NotNull
        @Schema(description = "서비스 이름")
        String service;

        @Schema(description = "bitmask 형식의 역할 정보")
        Integer role;
    }
}
