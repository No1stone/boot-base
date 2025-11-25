package com.origemite.gateway.filter;

import com.origemite.gateway.conf.dto.AuthServerAllowPath;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import javax.security.sasl.AuthenticationException;
import java.util.Date;

@Component
@Slf4j
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider;

    public JwtAuthenticationFilter(JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider) {
        super(Config.class);
        this.jwtTokenAuthenticationProvider = jwtTokenAuthenticationProvider;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            try {
                if (!isJwtValid(exchange.getRequest())) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            } catch (AuthenticationException e) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        };
    }

    public boolean isJwtValid(ServerHttpRequest request) throws AuthenticationException {
        String path = request.getPath().toString();
        boolean isAllowPath = AuthServerAllowPath.ALLOWED_PATHS.stream()
                .anyMatch(allowedPath ->
                path.matches(allowedPath.replaceAll("\\*\\*", ".*"))
        );

        if (isAllowPath) {
            return isAllowPath;
        }
        else {
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            String token = authHeader.substring(7).trim();


            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return false;
            }
            boolean accessExist = jwtTokenAuthenticationProvider.verifyAccessToken(token);
            if(!accessExist){
                log.info("안타야함");
                var claim = jwtTokenAuthenticationProvider.rsaJwtParse(token);
            }
            return true;
        }
    }

    public static class Config {
        // 필터 구성에 사용될 설정을 정의할 수 있음
    }
}