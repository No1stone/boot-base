package com.origemite.apiauth.support.security.provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.origemite.apiauth.config.prop.JwtTokenProp;
import com.origemite.apiauth.support.token.JwtSigningKeySync;
import com.origemite.lib.common.cache.CachePort;
import com.origemite.lib.common.exception.BizErrorException;
import com.origemite.lib.common.secruity.CustomUser;
import com.origemite.lib.common.token.TokenPort;
import com.origemite.lib.common.token.TokenSignature;
import com.origemite.lib.common.util.TransformUtils;
import com.origemite.lib.common.util.UuidUtils;
import com.origemite.lib.common.web.ResponseType;
import com.origemite.lib.model.auth.RefreshToken;
import com.origemite.lib.model.enums.auth.EnVaultType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class VaultJwtTokenProvider {

    private final TokenPort tokenPort;
    private final CachePort cachePort;
    private final JwtSigningKeySync jwtSigningKeySync;
    private final JwtTokenProp jwtTokenProp;

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

        String redisVersion = jwtSigningKeySync.getLatestVersion();
        Claims claims = createPayload(id, issuedAt, milliseconds);
        String headerBase64 = createBase64(createTokenHeader(redisVersion));
        String payloadBase64 = createBase64(claims);
        String signValue = headerBase64 + "." + payloadBase64;
        TokenSignature signature = tokenPort.sign(EnVaultType.AUTH_JWT.getValue(), signValue);
        String accessToken;
        if (!redisVersion.equals(String.valueOf(signature.getLatestVersion()))) {
            jwtSigningKeySync.syncAuthJwtKeys();

            headerBase64 = createBase64(createTokenHeader(redisVersion));
            payloadBase64 = createBase64(claims);
            signValue = headerBase64 + "." + payloadBase64;
            signature = tokenPort.sign(EnVaultType.AUTH_JWT.getValue(), signValue);
            accessToken = headerBase64 + "." + payloadBase64 + "." + signature.getSignature();
        } else {
            accessToken = signValue + "." + signature.getSignature();
        }

        cachePort.set("access:" + accessToken, "1", Duration.ofSeconds(6_000_000));
        return accessToken;
    }

    public Map<String, String> createTokenHeader(String version) {
        Map<String, String> header = new HashMap<>();
        header.put("kid", version);
        header.put("alg", "RS256");
        header.put("typ", "JWT");
        return header;
    }

    public Claims createPayload(String id, Date issuedAt, Long milliseconds) {
        Claims claims = Jwts.claims();
        claims.put("sub", id);
        claims.setExpiration(new Date(issuedAt.getTime() + milliseconds));
        return claims;
    }

    public String createBase64(Map<String, String> data) {
        String str = TransformUtils.toString(data);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    public String createBase64(Claims data) {
        String str = TransformUtils.toString(data);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    public boolean validateToken(String token) {
        if (token == null) {
            throw new BizErrorException(ResponseType.AD_INVALID_ACCESS_TOKEN);
        }
        parse(token);
        return true;
    }

    public Claims parse(String token) {
        String split = token.split("\\.")[0];
        byte[] headerDecodedBytes = Base64.getUrlDecoder().decode(split);
        String json = new String(headerDecodedBytes, StandardCharsets.UTF_8);
        Map<String, String> header = TransformUtils.parse(json, new TypeReference<>() {
        });
        String publicKey = jwtSigningKeySync.getPublicKeyForVersion(header.get("kid"));
        RSAPublicKey rsaPublicKey;

        try {
            byte[] decoded = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new BizErrorException(ResponseType.AD_INVALID_ACCESS_TOKEN);
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(rsaPublicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException | IllegalArgumentException | MalformedJwtException | UnsupportedJwtException |
                 ExpiredJwtException e) {
            throw new BizErrorException(ResponseType.AD_INVALID_ACCESS_TOKEN);
        }
    }

    public Authentication createAuthentication(String token) {
        Claims claims = parse(token);
        String memberId = MapUtils.getString(claims, "sub", null);
        CustomUser principal = CustomUser.builder()
                .id(memberId)
                .token(token)
                .build();
        return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
    }
}
