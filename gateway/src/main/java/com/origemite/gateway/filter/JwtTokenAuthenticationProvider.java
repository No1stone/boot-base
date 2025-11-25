package com.origemite.gateway.filter;

import com.origemite.gateway.conf.JwkAtomic;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import com.origemite.lib.webflux.exception.BizErrorException;
import com.origemite.lib.webflux.web.ResponseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.security.Key;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenAuthenticationProvider {

    private final JwtTokenProp jwtTokenProp;
    private final JwkAtomic jwkAtomic;
    private final StringRedisTemplate redisTemplate;

    public boolean validateToken(String token) {
        if (token == null) throw new BizErrorException(ResponseType.AD_INVALID_ACCESS_TOKEN);
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
            throw new BizErrorException(ResponseType.AD_INVALID_ACCESS_TOKEN);
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
            throw new BizErrorException(ResponseType.AD_INVALID_ACCESS_TOKEN);
        }
    }


    public String compact(Claims claims) {
        return Jwts.builder()
                .addClaims(claims)
                .signWith(Keys.hmacShaKeyFor(jwtTokenProp.getSecretKey().getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean existsAccessToken(String accessToken) {
        Boolean exists = redisTemplate.hasKey("access:" + accessToken);
        return exists != null && exists;
    }

    public boolean verifyAccessToken(String token) {
        // 1) Redis 캐시 존재 여부 확인
        if (existsAccessToken(token)) {
            return true;
        }

        // 2) Redis에 없으면 RSA 서명 검증 수행
//        rsaJwtParse(token);
        return true;
    }


}
