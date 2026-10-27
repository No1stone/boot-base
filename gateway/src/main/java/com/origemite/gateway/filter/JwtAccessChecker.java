package com.origemite.gateway.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

/**
 * 게이트웨이 JWT 접근 검사.
 * 구 {@code JwtService} 역할. 데이터 접근 Service가 아니다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAccessChecker {

    private final JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider;

    public boolean isJwtValid(ServerHttpRequest req, String token) {
        try {
            var claim = jwtTokenAuthenticationProvider.parse(token);
            if (claim.get("T") == null || claim.getExpiration().before(new Date())) {
                return false;
            }
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT expired: {}", e.getMessage());
        } catch (JwtException e) {
            log.warn("JWT parsing error: {}", e.getMessage());
        }
        return false;
    }

    public boolean isAllow(String path, Set<String> allowList) {
        return allowList.stream().anyMatch(allowedPath ->
                path.matches(allowedPath.replaceAll("\\*\\*", ".*"))
        );
    }
}
