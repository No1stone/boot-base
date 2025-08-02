package io.origemite.utilcodegen.gen.generator;

import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.parser.OpenAPIV3Parser;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class WebClientDtoGenerator {

    public static void generateDtoFromSwagger(String swaggerUrl, String outputDir) {
        OpenAPI openAPI = new OpenAPIV3Parser().read(swaggerUrl);
        if (openAPI == null || openAPI.getPaths() == null) {
            log.error("❌ OpenAPI 문서 파싱 실패: {}", swaggerUrl);
            return;
        }

        Components components = openAPI.getComponents();
        openAPI.getPaths().forEach((path, pathItem) -> {
            pathItem.readOperationsMap().forEach((httpMethod, operation) -> {
                if (operation.getTags() != null && !operation.getTags().isEmpty()) {
                    String tag = operation.getTags().get(0);
                    generateCreateDto(tag, operation, components, outputDir);
                }
            });
        });
    }

    public static void generateCreateDto(String tag, Operation operation, Components components, String outputDir) {
        if (operation.getRequestBody() == null || operation.getRequestBody().getContent() == null) return;

        RequestBody requestBody = operation.getRequestBody();
        MediaType mediaType = requestBody.getContent().get("application/json");
        if (mediaType == null || mediaType.getSchema() == null) return;

        Schema<?> schema = mediaType.getSchema();

        // code, message, data 감싸기 제거
        if (schema.getProperties() != null && schema.getProperties().containsKey("data")) {
            Object innerData = schema.getProperties().get("data");
            if (innerData instanceof Schema) {
                schema = (Schema<?>) innerData;
            }
        }

        String ref = schema.get$ref();
        if (ref == null) return;

        String refName = ref.substring(ref.lastIndexOf("/") + 1);
        if (!components.getSchemas().containsKey(refName)) {
            // 뒤에서 내부클래스 명만 추출
            if (refName.contains("$")) {
                refName = refName.substring(refName.lastIndexOf(".") + 1);
            }
        }

// 비정상적인 prefix 제거
        if (refName.contains("List")) {
            refName = refName.substring(refName.lastIndexOf(".") + 1); // 가장 끝 클래스 이름 추출
        }

// 정규표현식 필터링: 허용 문자만 남김
//        refName = refName.replaceAll("[^a-zA-Z0-9$]", "");
        Schema<?> targetSchema = components.getSchemas().get(refName);
        if (targetSchema == null) {
            log.warn("⚠️ '{}' 스키마를 찾을 수 없습니다. (components.schemas에 없음)", refName);
            return;
        }

        String className = extractSimpleClassName(refName);

        StringBuilder sb = new StringBuilder();
        sb.append("package io.diddda.utilcodegen.created.dto.request;\n\n")
                .append("import io.swagger.v3.oas.annotations.media.Schema;\n")
                .append("import lombok.*;\n")
                .append("import java.util.*;\n\n")
                .append("@Data\n")
                .append("@Builder\n")
                .append("@AllArgsConstructor\n")
                .append("@NoArgsConstructor\n")
                .append("public class ").append(className).append(" {\n\n")

                .append("    @Data\n")
                .append("    @Builder\n")
                .append("    @AllArgsConstructor\n")
                .append("    @NoArgsConstructor\n")
                .append("    public static class Filter {\n");

        targetSchema.getProperties().forEach((name, propSchema) -> {
            String type = toJavaType(propSchema);
            sb.append("        @Schema(description = \"\")\n")
                    .append("        private ").append(type).append(" ").append(name).append(";\n");
        });

        sb.append("    }\n\n")

                .append("    @Data\n")
                .append("    @Builder\n")
                .append("    @AllArgsConstructor\n")
                .append("    @NoArgsConstructor\n")
                .append("    public static class Create {\n");

        targetSchema.getProperties().forEach((name, propSchema) -> {
            String type = toJavaType(propSchema);
            sb.append("        @Schema(description = \"\")\n")
                    .append("        private ").append(type).append(" ").append(name).append(";\n");
        });

        sb.append("    }\n\n")

                .append("    @Data\n")
                .append("    @Builder\n")
                .append("    @AllArgsConstructor\n")
                .append("    @NoArgsConstructor\n")
                .append("    public static class Update {\n");

        targetSchema.getProperties().forEach((name, propSchema) -> {
            String type = toJavaType(propSchema);
            sb.append("        @Schema(description = \"\")\n")
                    .append("        private ").append(type).append(" ").append(name).append(";\n");
        });

        sb.append("    }\n\n")

                .append("    @Data\n")
                .append("    @Builder\n")
                .append("    @AllArgsConstructor\n")
                .append("    @NoArgsConstructor\n")
                .append("    public static class Ids {\n")
                .append("        private List<Long> ids;\n")
                .append("    }\n");

        sb.append("}\n");

        try {
            File file = new File(outputDir + "/" + className + ".java");
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            writer.write(sb.toString());
            writer.close();
        } catch (Exception e) {
            log.error("❌ DTO 파일 생성 실패: {}", className, e);
        }
    }

    private static String toJavaType(Schema<?> schema) {
        if (schema.getType() == null && schema.get$ref() != null) return "Object";
        return switch (schema.getType()) {
            case "string" -> "String";
            case "integer" -> "Integer";
            case "number" -> "Double";
            case "boolean" -> "Boolean";
            case "array" -> {
                Schema<?> items = ((io.swagger.v3.oas.models.media.ArraySchema) schema).getItems();
                yield "List<" + toJavaType(items) + ">";
            }
            default -> "Object";
        };
    }

    private static String toPascalCase(String input) {
        return Arrays.stream(input.split("[-_\\s]"))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                .collect(Collectors.joining());
    }

    private static String extractSimpleClassName(String refName) {
        if (refName.contains(".")) {
            String fullClass = refName.substring(refName.lastIndexOf(".") + 1);
            if (fullClass.contains("$")) {
                String outer = fullClass.substring(0, fullClass.indexOf("$"));
//                return outer + "Req";
                return outer ;
            } else {
                return fullClass;
            }
        }
        return toPascalCase(refName.replaceAll("Req|Request|Dto", "")) ;
//        return toPascalCase(refName.replaceAll("Req|Request|Dto", "")) + "Req";
    }
}
