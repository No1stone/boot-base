package com.origemite.lib.common.util;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class FileUtils {

    public static File convertBase64ToFile(String base64Image, String fileName) throws IOException {

        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(decodedBytes);
        }
        return file;
    }

    public static MultipartFile convertBase64ToMultipartFile(String base64Image, String fileName) throws IOException {

        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
        return new MockMultipartFile(fileName, fileName, "image/jpeg", new ByteArrayInputStream(decodedBytes));
    }
}
