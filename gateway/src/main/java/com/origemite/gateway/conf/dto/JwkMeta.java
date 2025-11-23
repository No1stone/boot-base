package com.origemite.gateway.conf.dto;

import com.origemite.lib.webflux.exception.BizErrorException;
import com.origemite.lib.webflux.web.ResponseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Getter
public class JwkMeta {

    private int latestVersion;
    private int previousVersion;
    private String latestPublicKey;
    private String previousPublicKey;
    private RSAPublicKey rsaLatestPublicKey;
    private RSAPublicKey rsaPriviousPublicKey;

    public static JwkMeta builder() {
        return new JwkMeta();
    }

    public JwkMeta setLatestVersion(int latestVersion) {
        this.latestVersion = latestVersion;
        return this;
    }

    public JwkMeta setPreviousVersion(int previousVersion) {
        this.previousVersion = previousVersion;
        return this;
    }

    public JwkMeta setLatestPublicKey(String latestPublicKey) {
        this.latestPublicKey = latestPublicKey;
        this.rsaLatestPublicKey = rsa(latestPublicKey);
        return this;
    }

    public JwkMeta setPreviousPublicKey(String previousPublicKey) {
        this.previousPublicKey = previousPublicKey;
        this.rsaPriviousPublicKey = rsa(previousPublicKey);
        return this;
    }

    private RSAPublicKey rsa(String publicKey) {
        RSAPublicKey rsaPublicKey = null;
        try {
            byte[] decoded = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
           return rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new BizErrorException(ResponseType.AD_INVALID_ACCESS_TOKEN);
        }
    }
}
