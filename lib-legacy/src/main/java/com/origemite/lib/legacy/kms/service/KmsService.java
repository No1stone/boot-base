package com.origemite.lib.legacy.kms.service;


import com.origemite.lib.common.util.StringUtils;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.kms.AWSKMS;
//import com.amazonaws.services.kms.AWSKMSClientBuilder;
//import com.amazonaws.services.kms.model.*;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class KmsService {

    private final  KmsClient kmsClient = KmsClient.builder()
            .region(Region.AP_NORTHEAST_2)
            .build();

    public String encrypt(String str, String secretKey) {

        if( StringUtils.isEmpty(str) ) {
            return str;
        }

        if( str.length() > 190 ) {
            return str;
        }

//        if (StringUtils.matches(str, "^[A-Za-z0-9+/]+={2}$")) {
//            return str;
//        }

//        EncryptRequest request = new EncryptRequest();
//        request.withKeyId(secretKey);
//        request.withPlaintext(ByteBuffer.wrap(str.getBytes(StandardCharsets.UTF_8)));
//        request.withEncryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256);
        EncryptRequest encryptRequest = EncryptRequest.builder()
                .keyId(secretKey)
                .encryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256)
                .plaintext(SdkBytes.fromUtf8String(StandardCharsets.UTF_8.toString()))
                .build();


//        EncryptResult result = kmsClient.encrypt(request);
//        ByteBuffer ciphertextBlob = result.getCiphertextBlob();

        EncryptResponse response = kmsClient.encrypt(encryptRequest);

        return Base64.getEncoder().encodeToString(response.ciphertextBlob().asByteArray());


//        return Base64.getEncoder().encodeToString((ciphertextBlob.array()));
    }

    @Cacheable(value = "kmsCache", key = "#str")
    public String decrypt(String str, String secretKey) {

        if( StringUtils.isEmpty(str) ) {
            return str;
        }

        if (!StringUtils.matches(str, "^[A-Za-z0-9+/]+={2}$")) {
            return str;
        }

//        DecryptRequest request = new DecryptRequest();
//        request.withCiphertextBlob(ByteBuffer.wrap(Base64.getDecoder().decode(str)));
//        request.withKeyId(secretKey);
//        request.withEncryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256);
//        ByteBuffer plainText = null;
        SdkBytes plainText = null;
        DecryptRequest decryptRequest = DecryptRequest.builder()
                .keyId(secretKey)
                .encryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256)
                .ciphertextBlob(SdkBytes.fromByteArray(Base64.getDecoder().decode(str)))
                .build();
        try {
//            plainText = kmsClient.decrypt(request).getPlaintext();
            plainText = kmsClient.decrypt(decryptRequest).plaintext();
        } catch (InvalidCiphertextException | InvalidKeyUsageException e) {
            return str;
        }
//        return new String(plainText.array());
        return plainText.asUtf8String();
    }

    public boolean isEncrypted(String str, String secretKey) {
        try {
            if( StringUtils.isEmpty(str) ) {
                return false;
            }

            if( str.length() > 190 ) {
                return true;
            }

//            if (!StringUtils.matches(str, "^[A-Za-z0-9+/]+={2}$")) {
//                return false;
//            }

//            DecryptRequest request = new DecryptRequest();
//            request.withCiphertextBlob(ByteBuffer.wrap(Base64.getDecoder().decode(str)));
//            request.withKeyId(secretKey);
//            request.withEncryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256);
//            kmsClient.decrypt(request).getPlaintext();
            decrypt(str, secretKey);
            return true; // 복호화가 성공했으므로 암호화된 값임
        } catch (InvalidCiphertextException e) {
            return false; // 복호화에 실패했으므로 암호화된 값이 아님
        } catch (Exception e) {
            return false;
        }
    }

}
