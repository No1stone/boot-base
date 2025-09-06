package com.origemite.apiauth.support.security.provider;

import com.origemite.apiauth.config.prop.JwtTokenProp;
import com.origemite.lib.common.exception.BizErrorException;
import com.origemite.lib.common.secruity.CustomUser;
import com.origemite.lib.common.util.DateUtils;
import com.origemite.lib.common.util.StringUtils;
import com.origemite.lib.common.util.UuidUtils;
import com.origemite.lib.common.web.ResponseType;
import com.origemite.lib.model.auth.AuthenticationDto;
import com.origemite.lib.model.auth.JwtToken;
import com.origemite.lib.model.auth.RefreshToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.origemite.lib.common.util.ModelMapperUtil.map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenProp jwtTokenProp;

    private static final String JWT_TYPE_KEY = "T";
    //    private static final String TOKEN_TYPE_KEY = "P"; //"service" or "auth"
    private static final String ID_KEY = "I";
    //    private static final String ROLE_KEY = "R";
    private static final String SESSION_ID_KEY = "S";

    public JwtToken createToken(String id, Long accessTokenMilliseconds, Long refreshTokenMilliseconds) {

        if (accessTokenMilliseconds == null) accessTokenMilliseconds = jwtTokenProp.getAccessTokenExpiration();
        if (refreshTokenMilliseconds == null) refreshTokenMilliseconds = jwtTokenProp.getRefreshTokenExpiration();

        String accessToken = createAccessToken(id, accessTokenMilliseconds);
        RefreshToken refreshToken = createRefreshToken(refreshTokenMilliseconds);
        Claims claims = parse(accessToken);

        return JwtToken.builder()
                .jwtTokenType(MapUtils.getString(claims, JWT_TYPE_KEY))
                .id(MapUtils.getString(claims, ID_KEY))
                .loginSessionId(MapUtils.getString(claims, SESSION_ID_KEY))
                .accessToken(accessToken)
                .accessTokenExpiration(DateUtils.of(claims.getExpiration()))
                .refreshToken(refreshToken.getRefreshToken())
                .refreshTokenExpiration(DateUtils.of(refreshToken.getRefreshTokenExpiration()))
                .accessTokenExpiresIn(accessTokenMilliseconds)
                .refreshTokenExpiresIn(refreshTokenMilliseconds)
                .build();
    }

    public RefreshToken createRefreshToken(long milliseconds) {

        Date issuedAt = new Date();

        String refreshToken = UuidUtils.uuidToBase64(UUID.randomUUID());
        Date refreshTokenExpiration = new Date(issuedAt.getTime() + milliseconds);

        return RefreshToken.builder()
                .refreshToken(refreshToken)
                .refreshTokenExpiration(refreshTokenExpiration)
                .build();
    }

    public String createAccessToken(String id, Long milliseconds) {
        Date issuedAt = new Date();
        return Jwts.builder()
                .claim(JWT_TYPE_KEY, jwtTokenProp.getTokenType())
                .claim(ID_KEY, id)
                .claim(SESSION_ID_KEY, UuidUtils.uuidToBase64(UUID.randomUUID()))
                .setIssuedAt(issuedAt)
                .setExpiration(new Date(issuedAt.getTime() + milliseconds))
                .signWith(Keys.hmacShaKeyFor(jwtTokenProp.getSecretKey().getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication createAuthentication(String token) {
        Claims claims = parse(token);
        String memberId = MapUtils.getString(claims, ID_KEY, null);
        CustomUser principal = CustomUser.builder()
                .id(memberId)
                .token(token)
                .build();

        return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtTokenProp.getHeaderString());
        if (bearerToken != null && bearerToken.startsWith(StringUtils.concat(jwtTokenProp.getTokenType(), " "))) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        if (token == null)
            throw new BizErrorException(ResponseType.AD_INVALID_ACCESS_TOKEN); // TODO. 에러처리 시 refactoring
        parse(token);
        return true;
    }

    private Claims parse(String token) {
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

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }

}
