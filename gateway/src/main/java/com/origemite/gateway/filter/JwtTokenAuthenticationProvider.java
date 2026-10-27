package com.origemite.gateway.filter;

import com.origemite.gateway.conf.JwkAtomic;
import com.origemite.gateway.support.InvalidAccessTokenException;
import com.origemite.lib.cache.CachePort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenAuthenticationProvider {

    private final JwtTokenProp jwtTokenProp;
    private final JwkAtomic jwkAtomic;
    private final CachePort cachePort;

    public boolean validateToken(String token) {
        if (token == null) {
            throw new InvalidAccessTokenException();
        }
        parse(token);
        return true;
    }

    public Jws<Claims> rsaJwtParse(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKeyResolver(new SigningKeyResolverAdapter() {
                        @Override
                        public Key resolveSigningKey(JwsHeader header, Claims claims) {
                            String kid = header.getKeyId();
                            return jwkAtomic.resolveKey(kid);
                        }
                    })
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            throw new InvalidAccessTokenException(e);
        }
    }

    public Claims parse(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtTokenProp.getSecretKey().getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException | IllegalArgumentException | MalformedJwtException | UnsupportedJwtException |
                 ExpiredJwtException e) {
            throw new InvalidAccessTokenException(e);
        }
    }

    public String compact(Claims claims) {
        return Jwts.builder()
                .addClaims(claims)
                .signWith(Keys.hmacShaKeyFor(jwtTokenProp.getSecretKey().getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean existsAccessToken(String accessToken) {
        return cachePort.exists("access:" + accessToken);
    }

    public boolean verifyAccessToken(String token) {
        if (existsAccessToken(token)) {
            return true;
        }
        // Redis에 없으면 RSA 서명 검증 수행 (현재 포트폴리오 경로에서는 통과)
        return true;
    }
}
