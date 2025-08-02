package io.origemite.lib.common.util;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.services.kms.model.EncryptResult;
import com.amazonaws.services.kms.model.EncryptionAlgorithmSpec;
import org.springframework.cache.annotation.Cacheable;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 암호화 유틸 클래스
 *
 * @author korea
 */
public class EncUtils {

    private static final String IV = "a26b95e254308c4d7e692ec73d1dd527";
    private static final String DEFAULT_KEY = "82af9ebe8024d4182cdf6e9016443ed6";

//    private static final AWSKMS kmsClient;
//
//    private EncUtils() {
//        throw new AssertionError();
//    }
//
//    static {
//        kmsClient = AWSKMSClientBuilder.standard()
//                .withRegion(Regions.AP_NORTHEAST_2)
//                .build();
//    }
//
//    /**
//     * KMS 암호화
//     * @param str
//     * @param secretKey
//     * @return
//     */
//    public static String encKMS(String str, String secretKey) {
//
//        EncryptRequest request = new EncryptRequest();
//        request.withKeyId(secretKey);
//        request.withPlaintext(ByteBuffer.wrap(str.getBytes(StandardCharsets.UTF_8)));
//        request.withEncryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256);
//
//        EncryptResult result = kmsClient.encrypt(request);
//        ByteBuffer ciphertextBlob = result.getCiphertextBlob();
//
//        return Base64.getEncoder().encodeToString((ciphertextBlob.array()));
//    }
//
//    /**
//     * KMS 복호화
//     * @param str
//     * @param secretKey
//     * @return
//     */
//    public static String decKMS(String str, String secretKey) {
//
//        if (!StringUtils.matches(str, "^[A-Za-z0-9+/]+={2}$")) {
//            return str;
//        }
//
//        DecryptRequest request = new DecryptRequest();
//        request.withCiphertextBlob(ByteBuffer.wrap(Base64.getDecoder().decode(str)));
//        request.withKeyId(secretKey);
//        request.withEncryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256);
//        ByteBuffer plainText = kmsClient.decrypt(request).getPlaintext();
//
//        return new String(plainText.array());
//    }

    /**
     * AES 암호화
     * assertEquals("UMh15bCLarqZdQx8oSX4iA==", EncUtils.encAES("test", SECRET_KEY));
     *
     * @param str
     * @return
     */
    public static String encAES(String str) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return encAES(str, DEFAULT_KEY);
    }

    /**
     * AES 암호화
     * assertEquals("UMh15bCLarqZdQx8oSX4iA==", EncUtils.encAES("test", SECRET_KEY));
     *
     * @param str
     * @return
     */
    public static String encAES(String str, String secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(toBytes(secretKey), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(toBytes(IV));

        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        //return toHex(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
        return Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * AES 복호화
     * assertEquals("test", EncUtils.decAES("UMh15bCLarqZdQx8oSX4iA==", SECRET_KEY));
     *
     * @param str
     * @return
     */
    public static String decAES(String str)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return decAES(str, DEFAULT_KEY);
    }

    /**
     * AES 복호화
     * assertEquals("test", EncUtils.decAES("UMh15bCLarqZdQx8oSX4iA==", SECRET_KEY));
     *
     * @param str
     * @return
     */
    public static String decAES(String str, String secretKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(toBytes(secretKey), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(toBytes(IV));

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        //return new String(cipher.doFinal(toBytes(str)), "UTF-8");
        return new String(cipher.doFinal(Base64.getDecoder().decode(str)), "UTF-8");
    }

    /**
     * SHA-256 암호화
     */
    public static String encSHA256(String str) {
        return encSHA256(str, "");
    }


    /**
     * SHA-256 암호화
     * assertNotNull(EncUtils.encSHA256("TEST", "salt"));
     */
    public static String encSHA256(String str, String salt) {
        return encSHA(str, salt, "SHA-256");
    }

    /**
     * SHA-512 암호화
     * assertNotNull(EncUtils.encSHA512("TEST", "salt"));
     */
    public static String encSHA512(String str) {
        return encSHA512(str, "");
    }

    /**
     * SHA-512 암호화
     * assertNotNull(EncUtils.encSHA512("TEST", "salt"));
     */
    public static String encSHA512(String str, String salt) {
        return encSHA(str, salt, "SHA-512");
    }

    /**
     * SHA 암호화
     */
    private static String encSHA(String str, String salt, String algorithm) {
        if (str == null) return null;

        String result = "";

        byte[] a = str.getBytes();
        byte[] b = salt.getBytes();
        byte[] bytes = new byte[a.length + b.length];
        System.arraycopy(a, 0, bytes, 0, a.length);
        System.arraycopy(b, 0, bytes, a.length, b.length);

        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(bytes);

            byte[] byteData = md.digest();

//            StringBuffer sb = new StringBuffer();
//
//            for (byte bd : byteData) {
//                String hex = Integer.toHexString(0xff & bd);
//                if (hex.length() == 1) {
//                    sb.append('0');
//                }
//                sb.append(hex);
//            }
//
//            result = sb.toString(); // 최종 결과값을 result에 반환

            result = Base64.getEncoder().encodeToString(byteData);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * hex to byte[] : 16진수 문자열을 바이트 배열로 변환한다.
     *
     * @param hex hex string
     * @return
     */
    private static byte[] toBytes(String hex) {

        if (hex == null || hex.length() == 0) {
            return null;
        }

        byte[] ba = new byte[hex.length() / 2];

        for (int i = 0; i < ba.length; i++) {
            ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }

        return ba;
    }

    /**
     * byte[] to hex : unsigned byte(바이트) 배열을 16진수 문자열로 바꾼다.
     *
     * @param batyes byte[]
     * @return
     */
    private static String toHex(byte[] batyes) {

        if (batyes == null || batyes.length == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer(batyes.length * 2);
        String hexNumber;

        for (int x = 0; x < batyes.length; x++) {
            hexNumber = "0" + Integer.toHexString(0xff & batyes[x]);
            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }

        return sb.toString();
    }


}