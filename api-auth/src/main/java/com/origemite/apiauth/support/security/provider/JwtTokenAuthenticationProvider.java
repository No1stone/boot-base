package com.origemite.apiauth.support.security.provider;

import com.origemite.apiauth.config.prop.JwtTokenProp;
import io.origemite.lib.common.exception.BizErrorException;
import io.origemite.lib.common.secruity.CustomUser;
import io.origemite.lib.common.util.DateUtils;
import io.origemite.lib.common.util.StringUtils;
import io.origemite.lib.common.util.UuidUtils;
import io.origemite.lib.common.web.ResponseType;
import io.origemite.lib.model.auth.AuthenticationDto;
import io.origemite.lib.model.auth.JwtToken;
import io.origemite.lib.model.auth.RefreshToken;
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

import static io.origemite.lib.common.util.ModelMapperUtil.map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenProp jwtTokenProp;

    private static final String JWT_TYPE_KEY = "T";
    private static final String TOKEN_TYPE_KEY = "P"; //"service" or "auth"
    private static final String ID_KEY = "I";
    private static final String ROLE_KEY = "R";
    private static final String SESSION_ID_KEY = "S";

    public JwtToken createOtpToken(String id, List<AuthenticationDto.ServiceRole> roles, Long accessTokenMilliseconds, Long refreshTokenMilliseconds) {

        if (accessTokenMilliseconds == null) accessTokenMilliseconds = jwtTokenProp.getAccessTokenExpiration();
        if (refreshTokenMilliseconds == null) refreshTokenMilliseconds = jwtTokenProp.getRefreshTokenExpiration();

        String accessToken = createAccessToken(id, roles, accessTokenMilliseconds, "AUTH");
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
                .roles(roles)
                .type("AUTH")
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

    public String createAccessToken(String id, List<AuthenticationDto.ServiceRole> roles, Long milliseconds, String tokenType) {

        Date issuedAt = new Date();

        return Jwts.builder()
                .claim(TOKEN_TYPE_KEY, tokenType)
                .claim(JWT_TYPE_KEY, jwtTokenProp.getTokenType())
                .claim(ID_KEY, id)
                .claim(SESSION_ID_KEY, UuidUtils.uuidToBase64(UUID.randomUUID()))
                .claim(ROLE_KEY, roles)
                .setIssuedAt(issuedAt)
                .setExpiration(new Date(issuedAt.getTime() + milliseconds))
                .signWith(Keys.hmacShaKeyFor(jwtTokenProp.getSecretKey().getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication createAuthentication(String token) {
        Claims claims = parse(token);

        String memberId = MapUtils.getString(claims, ID_KEY, null);
        String tokenType = claims.get(TOKEN_TYPE_KEY).toString();

        if ("AUTH".equals(tokenType)) {
            // OTP 토큰일 경우에는 roleClaims가 없을 수 있음
            if (memberId == null) {
                throw new IllegalArgumentException("Invalid JWT claims: Missing memberId");
            }

            // OTP 토큰은 권한(role) 정보가 없으므로, 바로 인증 처리
            CustomUser principal = CustomUser.builder()
                    .id(memberId)
                    .token(token)
                    .authorities(Collections.emptyList()) // OTP 토큰은 권한 없이 인증만 수행
                    .roles(Collections.emptyList()) // OTP 토큰은 role 정보가 없음
                    .build();

            return new UsernamePasswordAuthenticationToken(principal, token, Collections.emptyList());
        }

        // "P"가 OTP가 아닌 경우, roleClaims 추출 및 검증
        List<Map<String, Object>> roleClaims = (List<Map<String, Object>>) claims.get(ROLE_KEY);

        if (roleClaims == null || roleClaims.isEmpty()) {
            throw new IllegalArgumentException("Invalid JWT claims: No roles found");
        }

        // roleClaims를 CustomUser.RoleInfo 객체로 변환
        List<CustomUser.RoleInfo> roleInfoList = roleClaims.stream()
                .map(roleClaim -> map(roleClaim, CustomUser.RoleInfo.class))
                .toList();

        if (memberId == null) {
            throw new IllegalArgumentException("Invalid JWT claims: Missing memberId");
        }

        // authorities를 생성하는 부분
        List<SimpleGrantedAuthority> authorities = roleClaims.stream()
                .map(roleMap -> {
                    String service = (String) roleMap.get("service");
                    Integer role = (Integer) roleMap.get("role");

                    if (service == null || role == null) {
                        throw new IllegalArgumentException("Invalid JWT claims: Missing service or role");
                    }

                    // service와 role을 이용하여 authority를 생성
                    return new SimpleGrantedAuthority(String.format("SERVICE:%s:ROLE:%d", service, role));
                })
                .toList();

        CustomUser principal = CustomUser.builder()
                .id(memberId)
                .token(token)
                .authorities(authorities)
                .roles(roleInfoList)
                .build();

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }


    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtTokenProp.getHeaderString());
        if (bearerToken != null && bearerToken.startsWith(StringUtils.concat(jwtTokenProp.getTokenType(), " "))) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        if (token == null) throw new BizErrorException(ResponseType.AD_INVALID_ACCESS_TOKEN); // TODO. 에러처리 시 refactoring
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
