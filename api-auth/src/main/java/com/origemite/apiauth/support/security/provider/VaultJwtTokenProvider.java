package com.origemite.apiauth.support.security.provider;


import com.fasterxml.jackson.core.type.TypeReference;
import com.origemite.apiauth.auth.service.VaultService;
import com.origemite.apiauth.config.prop.JwtTokenProp;
import com.origemite.lib.common.exception.BizErrorException;
import com.origemite.lib.common.util.TransformUtils;
import com.origemite.lib.common.util.UuidUtils;
import com.origemite.lib.common.web.ResponseType;
import com.origemite.lib.model.auth.RefreshToken;
import com.origemite.lib.model.auth.VaultKey;
import com.origemite.lib.model.enums.auth.EnVaultType;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class VaultJwtTokenProvider {

    private final VaultService vaultService;
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

        String redisVersion = vaultService.getVaultLatestVersion();
        Claims claims = creataPayload(id, issuedAt, milliseconds);
        String headerBase64 = createBase64(createTokenHeader(redisVersion));
        String payloadBase64 = createBase64(claims);
        String signValue = headerBase64 + "." + payloadBase64;
        VaultKey.Signature signature = vaultService.signature(EnVaultType.AUTH_JWT, signValue);

        // 레디스 저장버전과 볼트버전이 일치하지 않는경우
        if (!redisVersion.equals(String.valueOf(signature.getLatestVersion()))) {
            // 볼트 갱신후 ...
            vaultService.initVaultForRedis();

            // 포트폴리오는 단순 재발행
            headerBase64 = createBase64(createTokenHeader(redisVersion));
            payloadBase64 = createBase64(claims);
            signValue = headerBase64 + "." + payloadBase64;
            signature = vaultService.signature(EnVaultType.AUTH_JWT, signValue);
            return headerBase64 + "." + payloadBase64 + "." + signature.getSignature();
        }
        return signValue + "." + signature.getSignature();
    }

    //Clamis 사용해도됨.
    public Map<String, String> createTokenHeader(String version) {
        Map<String, String> header = new HashMap<>();
        header.put("kid", version);
        header.put("alg", "RS256");
        header.put("typ", "JWT");
        return header;
    }

    public Claims creataPayload(String id, Date issuedAt, Long milliseconds) {
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

    public Claims parse(String token) {
        String split = token.split("\\.")[0];
//        String claimss = token.split("\\.")[1];
//        String signature = token.split("\\.")[2];
        byte[] headerDecodedBytes = Base64.getUrlDecoder().decode(split);
        String json = new String(headerDecodedBytes, StandardCharsets.UTF_8);
        Map<String, String> header = TransformUtils.parse(json, new TypeReference<Map<String, String>>() {
        });
        String publicKey = vaultService.getVaultKeyForVersion(header.get("kid"));
        RSAPublicKey rsaPublicKey = null;

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

}
