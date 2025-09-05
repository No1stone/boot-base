package com.origemite.apiauth.config.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "security.jwt")
public class JwtTokenProp {
    private String secretKey;
    private String tokenType;
    private String headerString;
    private Long accessTokenExpiration;
    private Long refreshTokenExpiration;
}
