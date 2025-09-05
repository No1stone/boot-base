package com.origemite.utilcodegen.gen.generator;

import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
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
public class FilterBasedResponseDtoGenerator {

    public static void generateDtoFromSwagger(String swaggerUrl, String outputDir) {
        OpenAPI openAPI = new OpenAPIV3Parser().read(swaggerUrl);
        if (openAPI == null || openAPI.getPaths() == null) {
            log.error("❌ OpenAPI 문서 파싱 실패: {}", swaggerUrl);
            return;
        }

        Components components = openAPI.getComponents();
        openAPI.getPaths().forEach((path, pathItem) -> {
            pathItem.readOperationsMap().forEach((httpMethod, operation) -> {
                if (operation.getRequestBody() == null) return;

                RequestBody requestBody = operation.getRequestBody();
                MediaType mediaType = requestBody.getContent().get("application/json");
                if (mediaType == null || mediaType.getSchema() == null) return;

                Schema<?> schema = mediaType.getSchema();
                if (schema.get$ref() == null) return;

                String refName = schema.get$ref().substring(schema.get$ref().lastIndexOf("/") + 1);
                Schema<?> dtoSchema = components.getSchemas().get(refName);
                if (dtoSchema == null) return;

                String className = extractSimpleClassName(refName).replaceAll("Request|Req|Dto", "") + "Res";
                generateResponseDto(className, dtoSchema, outputDir);
            });
        });
    }

    private static void generateResponseDto(String className, Schema<?> schema, String outputDir) {
        StringBuilder sb = new StringBuilder();
        sb.append("package io.diddda.utilcodegen.created.dto.response;\n\n")
                .append("import io.swagger.v3.oas.annotations.media.Schema;\n")
                .append("import lombok.*;\n")
                .append("import java.util.*;\n\n")
                .append("@Data\n")
                .append("@Builder\n")
                .append("@NoArgsConstructor\n")
                .append("@AllArgsConstructor\n")
                .append("public class ").append(className).append(" {\n\n")

                .append("    @Data\n")
                .append("    @Builder\n")
                .append("    @NoArgsConstructor\n")
                .append("    @AllArgsConstructor\n")
                .append("    public static class Id {\n")
                .append("        @Schema(description = \"ID\")\n")
                .append("        private Long id;\n")
                .append("    }\n\n")

                .append("    @EqualsAndHashCode(callSuper = true)\n")
                .append("    @Data\n")
                .append("    @Builder\n")
                .append("    @NoArgsConstructor\n")
                .append("    @AllArgsConstructor\n")
                .append("    public static class Name extends Id {\n")
                .append("    }\n\n")

                .append("    @EqualsAndHashCode(callSuper = true)\n")
                .append("    @Data\n")
                .append("    @Builder\n")
                .append("    @NoArgsConstructor\n")
                .append("    @AllArgsConstructor\n")
                .append("    public static class Single extends Name {\n");

        schema.getProperties().forEach((name, propSchema) -> {
            if (name.equals("id") || name.equals("name")) return;
            String type = toJavaType(propSchema);
            sb.append("        @Schema(description = \"\")\n")
                    .append("        private ").append(type).append(" ").append(name).append(";\n");
        });

        sb.append("    }\n\n")
                .append("    @EqualsAndHashCode(callSuper = true)\n")
                .append("    @Data\n")
                .append("    @Builder\n")
                .append("    @NoArgsConstructor\n")
                .append("    @AllArgsConstructor\n")
                .append("    public static class Item extends Single {\n")
                .append("    }\n");

        sb.append("}\n");

        try {
            File file = new File(outputDir + "/" + className + ".java");
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            writer.write(sb.toString());
            writer.close();
        } catch (Exception e) {
            log.error("❌ Response DTO 파일 생성 실패: {}", className, e);
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

    private static String extractSimpleClassName(String refName) {
        if (refName.contains(".")) {
            String fullClass = refName.substring(refName.lastIndexOf(".") + 1);
            if (fullClass.contains("$")) {
                return fullClass.substring(0, fullClass.indexOf("$"));
            } else {
                return fullClass;
            }
        }
        return toPascalCase(refName.replaceAll("Req|Request|Dto", ""));
    }

    private static String toPascalCase(String input) {
        return Arrays.stream(input.split("[-_\\s]"))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                .collect(Collectors.joining());
    }
}
