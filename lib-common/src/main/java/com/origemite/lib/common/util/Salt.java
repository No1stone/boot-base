package com.origemite.lib.common.util;

import java.security.SecureRandom;
import java.util.Base64;

public class Salt {

    public static String generate() {
        return generate(32);
    }
    public static String generate(int size) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[size];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
