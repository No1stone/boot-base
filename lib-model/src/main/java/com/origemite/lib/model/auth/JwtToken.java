package com.origemite.lib.model.auth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtToken {

    String option;
    String jwtTokenType;
    String type;
    String id;
    String loginSessionId;
    String accessToken;
    LocalDateTime accessTokenExpiration;
    Long accessTokenExpiresIn;
    String refreshToken;
    LocalDateTime refreshTokenExpiration;
    Long refreshTokenExpiresIn;
    List<AuthenticationDto.ServiceRole> roles;
    boolean isNew;
}
