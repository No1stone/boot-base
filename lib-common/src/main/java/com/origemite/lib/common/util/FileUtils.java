package com.origemite.lib.common.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
        return new BytesMultipartFile(fileName, fileName, "image/jpeg", decodedBytes);
    }

    /**
     * main 코드가 spring-test({@code MockMultipartFile})에 의존하지 않도록 둔다.
     */
    private static final class BytesMultipartFile implements MultipartFile {

        private final String name;
        private final String originalFilename;
        private final String contentType;
        private final byte[] content;

        private BytesMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
            this.name = name;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.content = content;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() {
            return content;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(File dest) throws IOException {
            try (FileOutputStream fos = new FileOutputStream(dest)) {
                fos.write(content);
            }
        }
    }
}
