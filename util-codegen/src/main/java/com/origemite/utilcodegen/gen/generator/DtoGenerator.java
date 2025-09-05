package com.origemite.utilcodegen.gen.generator;


import com.origemite.utilcodegen.gen.metadata.ColumnMetadata;
import com.origemite.utilcodegen.gen.metadata.TableMetadata;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DtoGenerator {
    private static final List<String> AUDIT_FIELDS = List.of("created_at", "created_by", "updated_at", "updated_by", "version");

    public void generate(TableMetadata table, String path) {
        String entityName = toPascalCase(table.tableName());
        String context = extractContext(table.tableName());
        String pkType = getPkType(table);

        String reqPath = Paths.get(path, context, "dto", entityName + "Req.java").toString();
        String resPath = Paths.get(path, context, "dto", entityName + "Res.java").toString();

        generateReq(table, entityName, pkType, context, reqPath);
        generateRes(table, entityName, pkType, context, resPath);
    }

    private void generateReq(TableMetadata table, String entityName, String pkType, String context, String filePath) {
        try (FileWriter writer = prepareFile(filePath)) {
            writer.write("package io.diddda.api." + context + ".dto;\n\n");
            writer.write("import lombok.*;\nimport io.swagger.v3.oas.annotations.media.Schema;\n");
            writer.write("import lombok.experimental.FieldDefaults;\nimport java.util.*;\nimport java.time.*;\n\n");
            writer.write("import java.time.LocalDateTime;;\n\n");

            writer.write("public class " + entityName + "Req {\n\n");

            // Ids
            writer.write("    @Schema(description = \"" + entityName + "Req.Ids DTO\")\n");
            writer.write("    @Data\n    @Builder\n    @FieldDefaults(level = AccessLevel.PRIVATE)\n");
            writer.write("    public static class Ids {\n");
            writer.write("        @Schema(description = \"" + getPkColumn(table).comment() + "\")\n");
            writer.write("        private List<" + pkType + "> ids;\n    }\n\n");

            // Filter/Create/Update
            for (String type : List.of("Filter", "Create", "Update")) {
                writer.write("    @Schema(description = \"" + entityName + "Req." + type + " DTO\")\n");
                writer.write("    @Data\n    @Builder\n    @FieldDefaults(level = AccessLevel.PRIVATE)\n");
                writer.write("    public static class " + type + " {\n");
                for (ColumnMetadata col : table.columns()) {
                    boolean isAudit = AUDIT_FIELDS.contains(col.name().toLowerCase());
                    if ((type.equals("Filter")) || (!type.equals("Filter") && !isAudit)) {
                        writer.write("        @Schema(description = \"" + col.comment() + "\")\n");
                        writer.write("        private " + mapToJavaType(col.dataType()) + " " + toCamelCase(col.name()) + ";\n");
                    }
                }
                writer.write("    }\n\n");
            }

            writer.write("}\n");
            System.out.println("✔ Req DTO 생성 완료: " + filePath);
        } catch (Exception e) {
            throw new RuntimeException("Req DTO 생성 실패", e);
        }
    }
    private void generateRes(TableMetadata table, String entityName, String pkType, String context, String filePath) {
        try (FileWriter writer = prepareFile(filePath)) {
            writer.write("package io.diddda.api." + context + ".dto;\n\n");
            writer.write("import lombok.*;\nimport io.swagger.v3.oas.annotations.media.Schema;\n");
            writer.write("import lombok.experimental.FieldDefaults;\nimport java.util.*;\nimport java.time.*;\n\n");


            writer.write("public class " + entityName + "Res {\n\n");

            // Id
            ColumnMetadata pkCol = getPkColumn(table);
            writer.write("    @Schema(description = \"" + entityName + "Res.Id DTO\")\n");
            writer.write("    @Data\n    @FieldDefaults(level = AccessLevel.PRIVATE)\n");
            writer.write("    public static class Id {\n");
            writer.write("        @Schema(description = \"" + pkCol.comment() + "\")\n");
            writer.write("        private " + pkType + " " + toCamelCase(pkCol.name()) + ";\n    }\n\n");

            // Name
            List<ColumnMetadata> nameCols = table.columns().stream()
                    .filter(col -> col.name().toLowerCase(Locale.ROOT).contains("name"))
                    .collect(Collectors.toList());

            writer.write("    @Schema(description = \"" + entityName + "Res.Name DTO\")\n");
            writer.write("    @Data\n    @EqualsAndHashCode(callSuper = true)\n    @FieldDefaults(level = AccessLevel.PRIVATE)\n");
            writer.write("    public static class Name extends Id {\n");
            for (ColumnMetadata col : nameCols) {
                writer.write("        @Schema(description = \"" + col.comment() + "\")\n");
                writer.write("        private " + mapToJavaType(col.dataType()) + " " + toCamelCase(col.name()) + ";\n");
            }
            if (nameCols.isEmpty()) {
                writer.write("        // 변수 없음\n");
            }
            writer.write("    }\n\n");

            // Single
            writer.write("    @Schema(description = \"" + entityName + "Res.Single DTO\")\n");
            writer.write("    @Data\n    @EqualsAndHashCode(callSuper = true)\n    @FieldDefaults(level = AccessLevel.PRIVATE)\n");
            writer.write("    public static class Single extends Name {\n");
            for (ColumnMetadata col : table.columns()) {
                String name = col.name().toLowerCase(Locale.ROOT);
                if (!name.equals(pkCol.name()) && !name.contains("name")) {
                    writer.write("        @Schema(description = \"" + col.comment() + "\")\n");
                    writer.write("        private " + mapToJavaType(col.dataType()) + " " + toCamelCase(col.name()) + ";\n");
                }
            }
            writer.write("    }\n\n");

            // Item
            writer.write("    @Schema(description = \"" + entityName + "Res.Item DTO\")\n");
            writer.write("    @Data\n    @EqualsAndHashCode(callSuper = true)\n    @FieldDefaults(level = AccessLevel.PRIVATE)\n");
            writer.write("    public static class Item extends Single {\n        // 하위 연관 관계 DTO는 직접 작성할 수 있음.\n    }\n");

            writer.write("}\n");
            System.out.println("✔ Res DTO 생성 완료: " + filePath);
        } catch (Exception e) {
            throw new RuntimeException("Res DTO 생성 실패", e);
        }
    }

    // 공통 유틸
    private String extractContext(String tableName) {
        int idx = tableName.indexOf("_");
        return (idx != -1) ? tableName.substring(0, idx) : "common";
    }

    private String toPascalCase(String text) {
        int idx = text.indexOf("_");
        String trimmed = (idx != -1) ? text.substring(idx + 1) : text;
        String[] parts = trimmed.split("_");
        StringBuilder sb = new StringBuilder();
        for (String p : parts)
            sb.append(p.substring(0, 1).toUpperCase()).append(p.substring(1));
        return sb.toString();
    }

    private String toCamelCase(String text) {
        String[] parts = text.split("_");
        StringBuilder sb = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++)
            sb.append(parts[i].substring(0, 1).toUpperCase()).append(parts[i].substring(1));
        return sb.toString();
    }

    private String mapToJavaType(String type) {
        return switch (type.toLowerCase()) {
            case "varchar", "char", "text" -> "String";
            case "int", "smallint", "mediumint" -> "Integer";
            case "bigint" -> "Long";
            case "decimal", "double", "float" -> "Double";
            case "datetime", "timestamp" -> "LocalDateTime";
            case "json" -> "Map<String, String>";
            default -> "String";
        };
    }

    private String getPkType(TableMetadata table) {
        return mapToJavaType(getPkColumn(table).dataType());
    }

    private ColumnMetadata getPkColumn(TableMetadata table) {
        return table.columns().stream()
                .filter(ColumnMetadata::primaryKey)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("PK 컬럼이 없습니다."));
    }

    private FileWriter prepareFile(String path) throws Exception {
        File file = new File(path);
        file.getParentFile().mkdirs();
        return new FileWriter(file);
    }
}
