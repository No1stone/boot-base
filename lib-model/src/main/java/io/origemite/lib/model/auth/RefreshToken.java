package io.origemite.lib.model.auth;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class RefreshToken {
    String refreshToken;
    Date refreshTokenExpiration;
}
