package com.origemite.gateway.filter;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.origemite.lib.webflux.exception.BizErrorException;
import io.origemite.lib.webflux.web.ResponseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenAuthenticationProvider {

    private final JwtTokenProp jwtTokenProp;


    public boolean validateToken(String token) {
        if (token == null) throw new BizErrorException(ResponseType.AD_INVALID_ACCESS_TOKEN);
        parse(token);
        return true;
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


}
