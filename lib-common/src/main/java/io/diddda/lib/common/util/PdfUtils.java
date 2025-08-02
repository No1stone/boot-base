package io.diddda.lib.common.util;

import io.diddda.lib.common.client.AbstractRestClient;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;


@Slf4j
public class PdfUtils extends AbstractRestClient {

    private static String fontPath = "https://static.drp.mintit.ai/fonts/SUIT-Regular.woff2";
    private static Integer yDeviceInfo = 612;
    private static Integer yErasureResult1 = 472;
    private static Integer yErasureResult2 = 368;
    private static Integer yReportDetail = 226;
    private final static String NA = "N/A";

    public static byte[] convert(String html) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ConverterProperties converterProperties = new ConverterProperties();
            converterProperties.setCharset("EUC-KR");
            HtmlConverter.convertToPdf(html, outputStream, converterProperties);

            return outputStream.toByteArray();
        }
    }

    public static byte[] convertHttpURL(String urls) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            URL url = new URL(urls);
            InputStream inputStream = url.openStream();
            HtmlConverter.convertToPdf(inputStream, outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.info(" - - {}", e.getStackTrace());
        }
        return null;
    }

    public static byte[] safePdf(Map<String, String> data, Boolean isDeletion, InputStream file) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

//            String readerPath = "api/src/main/resources/templates/safeDeletionPdfReader.pdf";
//            Resource resource = new ClassPathResource("templates/safeDeletionPdfReader.pdf");
//            Resource resource = resourceLoader.getResource("classpath:templates/safeDeletionPdfReader.pdf");
            //InputStream resourceAsStream = PdfUtils.class.getClassLoader().getResourceAsStream("templates/safeDeletionPdfReader.pdf");


            PdfDocument pdfDoc = new PdfDocument(new PdfReader(file), new PdfWriter(outputStream));
            pdfDoc.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDoc);

            // 글씨 덮어쓰기
            for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
                PdfPage page = pdfDoc.getPage(i);
                PdfCanvas canvas = new PdfCanvas(page);
                // 덮어쓸 텍스트
                //좌표에 글씨쓰기
                //관리번호
                safeManagementNum(page, data.getOrDefault("deviceDeletionId", NA));

                safeX1(page, data.getOrDefault("imei", NA), yDeviceInfo);
                safeX1(page, data.getOrDefault("manufactureName", NA), yDeviceInfo - 21);
                safeX1(page, data.getOrDefault("modelName", NA), yDeviceInfo - 42);
                safeX1(page, data.getOrDefault("osTypeCode", NA), yDeviceInfo - 64);

                safeX1(page, data.getOrDefault("storageCapacity", NA), yErasureResult1);
                safeX1(page, data.getOrDefault("storageCapacity", NA), yErasureResult1 - 21);
                safeX1(page, data.getOrDefault("deletionMethodCode", NA), yErasureResult1 - 42);

                if (isDeletion) {
                    safeX1(page, "삭제완료", yErasureResult1 - 64);
                    //외장메모리는 전무NA
                    safeX1(page, NA, yErasureResult2 - 64);
                } else {
                    safeX1(page, "삭제실패", yErasureResult1 - 64);
                    //외장메모리는 전무NA
                    safeX1(page, NA, yErasureResult2 - 64);
                }


                //외장메모리는 전무NA
                safeX1(page, NA, yErasureResult2);
                safeX1(page, NA, yErasureResult2 - 21);
                safeX1(page, NA, yErasureResult2 - 42);

                safeX1(page, data.getOrDefault("createdAt", NA), yReportDetail);
                safeX1(page, data.getOrDefault("deletionAppVersion", NA), yReportDetail - 21);

                safeX2(page, data.getOrDefault("storageCapacity", NA), yDeviceInfo);
                safeX2(page, data.getOrDefault("외장메모리", NA), yDeviceInfo - 22);
                safeX2(page, data.getOrDefault("rootingYn", NA), yDeviceInfo - 44);

            }

            // 문서 닫기
            document.close();
            return outputStream.toByteArray();
        }


    }

    public static PdfFont pdfFont() {
        try {
            PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H);
            return font;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static PdfCanvas safeManagementNum(PdfPage page, String text) {
        PdfCanvas canvas = new PdfCanvas(page);
        // 덮어쓸 텍스트
        // 위치 및 스타일 설정
        canvas.beginText();
        canvas.setFontAndSize(pdfFont(), 10);
        canvas.moveText(230, 692);
        canvas.showText(text);
        canvas.endText();
        return canvas;
    }

    public static PdfCanvas safeX1(PdfPage page, String text, Integer y) {
        PdfCanvas canvas = new PdfCanvas(page);
        // 덮어쓸 텍스트
        // 위치 및 스타일 설정
        canvas.beginText();
        canvas.setFontAndSize(pdfFont(), 10);
        canvas.moveText(180, y);
        canvas.showText(text);
        canvas.endText();
        return canvas;
    }


    public static PdfCanvas safeX2(PdfPage page, String text, Integer y) {
        PdfCanvas canvas = new PdfCanvas(page);
        // 덮어쓸 텍스트
        // 위치 및 스타일 설정
        canvas.beginText();
        canvas.setFontAndSize(pdfFont(), 10);
        canvas.moveText(384, y);
        canvas.showText(text);
        canvas.endText();
        return canvas;
    }

}