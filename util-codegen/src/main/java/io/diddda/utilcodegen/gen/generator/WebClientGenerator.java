package io.diddda.utilcodegen.gen.generator;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.parser.OpenAPIV3Parser;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WebClientGenerator {
    public void generate(String swaggerUrl, String basePath) {
        log.info("📥 Swagger 불러오는 중: {}", swaggerUrl);
        String baseUrl = swaggerUrl.replaceAll("/v3/api-docs.*$", "");
        OpenAPI openAPI = new OpenAPIV3Parser().read(swaggerUrl);

        if (openAPI == null) {
            throw new RuntimeException("Swagger 파싱 실패: " + swaggerUrl);
        }

        Paths paths = openAPI.getPaths();
        Map<String, StringBuilder> serviceMap = new HashMap<>();

        paths.forEach((path, item) -> {
            item.readOperationsMap().forEach((httpMethod, operation) -> {
                String tag = (operation.getTags() != null && !operation.getTags().isEmpty())
                        ? operation.getTags().get(0)
                        : "Common";

                StringBuilder sb = serviceMap.computeIfAbsent(tag, k -> new StringBuilder());

                // 메서드 빌더 호출
                String methodCode = WebClientMethodBuilder.buildMethod(baseUrl, path, httpMethod.name(), operation);
                sb.append("\n").append(methodCode);
            });
        });

        // 클래스별로 생성
        serviceMap.forEach((tag, code) -> {
            WebClientClassWriter.writeClass(basePath, tag, code.toString());
        });

        log.info("✔ WebClient 생성 완료");
    }
}