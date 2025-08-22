package com.origemite.gateway.filter;

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
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        try {
            var claim = jwtTokenAuthenticationProvider.parse(authHeader.substring(7).trim());
            if (claim.get("T") == null || claim.getExpiration().before(new Date())) {
                return false;
            }
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT expired: {}", e.getMessage());
            throw new AuthenticationException();
        } catch (JwtException e) {
            log.warn("JWT parsing error: {}", e.getMessage());
            throw new AuthenticationException();
        }
    }

    public static class Config {
        // 필터 구성에 사용될 설정을 정의할 수 있음
    }
}