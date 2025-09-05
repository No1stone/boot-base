package com.origemite.utilcodegen.gen.generator.ace;

import com.origemite.utilcodegen.gen.metadata.CamelCase;
import com.origemite.utilcodegen.gen.metadata.ColumnMetadata;
import com.origemite.utilcodegen.gen.metadata.TableMetadata;
import com.origemite.utilcodegen.gen.metadata.TypeResolver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MultiPkDtoGenerator {

    public void generate(TableMetadata table, String outputDir) {
        String className = table.className(); // 예: Codeshop
        String schema = table.schemaName();
        String packageName = "io.diddda.api." + schema + ".dto";
        String idClassName = className + "Id";

        List<ColumnMetadata> columns = table.columns();
        List<ColumnMetadata> pkColumns = columns.stream().filter(ColumnMetadata::primaryKey).toList();
        boolean isCompositePk = pkColumns.size() >= 2;

        generateRequest(className, idClassName, columns, isCompositePk, packageName, outputDir);
        generateResponse(className, idClassName, columns, isCompositePk, packageName, outputDir);
    }

    private void generateRequest(String className, String idClassName, List<ColumnMetadata> columns, boolean isCompositePk, String packageName, String outputDir) {
        StringBuilder sb = new StringBuilder();
        List<ColumnMetadata> pkColumns = columns.stream().filter(ColumnMetadata::primaryKey).toList();

        sb.append("package ").append(packageName).append(";\n\n")
                .append("import io.swagger.v3.oas.annotations.media.Schema;\n")
                .append("import lombok.*;\n")
                .append("import lombok.experimental.FieldDefaults;\n")
                .append("import java.util.*;\n")
                .append("import java.time.*;\n")
                .append("import java.math.*;\n\n")
                .append("public class ").append(className).append("Req {\n\n");

        // Filter
        sb.append("    @Schema(description = \"").append(className).append(" - Filter\")\n")
                .append("    @Data\n    @Builder\n    @FieldDefaults(level = AccessLevel.PRIVATE)\n")
                .append("    public static class Filter {\n");
        for (ColumnMetadata col : columns) {
            String javaType = TypeResolver.resolveJavaType(col);
            String fieldName = CamelCase.of(col.name());
            String comment = col.comment();
            sb.append("        @Schema(description = \"").append(comment).append("\")\n");
            sb.append("        ").append(javaType).append(" ").append(fieldName).append(";\n");
        }
        sb.append("    }\n\n");

        // Create + Update
        for (String type : List.of("Create", "Update")) {
            sb.append("    @Schema(description = \"").append(className).append(" - ").append(type).append("\")\n")
                    .append("    @Data\n    @Builder\n    @FieldDefaults(level = AccessLevel.PRIVATE)\n")
                    .append("    public static class ").append(type).append(" {\n");
            for (ColumnMetadata col : columns) {
                String name = col.name().toLowerCase();
                if (name.equals("created_at") || name.equals("created_by") ||
                        name.equals("updated_at") || name.equals("updated_by")) continue;

                String javaType = TypeResolver.resolveJavaType(col);
                String fieldName = CamelCase.of(col.name());
                String comment = col.comment();
                sb.append("        @Schema(description = \"").append(comment).append("\")\n");
                sb.append("        ").append(javaType).append(" ").append(fieldName).append(";\n");
            }
            sb.append("    }\n\n");
        }

        // Ids
        sb.append("    @Schema(description = \"").append(className).append(" - Ids\")\n")
                .append("    @Data\n    @Builder\n    @FieldDefaults(level = AccessLevel.PRIVATE)\n")
                .append("    public static class Ids {\n")
                .append("        List<").append(isCompositePk ? idClassName : TypeResolver.resolveJavaType(pkColumns.get(0))).append("> ids;\n")
                .append("    }\n");

        sb.append("}\n");

        writeFile(outputDir + "/dto", className + "Req.java", sb.toString());
    }

    private void generateResponse(String className, String idClassName, List<ColumnMetadata> columns, boolean isCompositePk, String packageName, String outputDir) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(packageName).append(";\n\n")
                .append("import io.swagger.v3.oas.annotations.media.Schema;\n")
                .append("import lombok.*;\n")
                .append("import lombok.experimental.FieldDefaults;\n")
                .append("import java.util.*;\n")
                .append("import java.time.*;\n")
                .append("import java.math.*;\n\n")
                .append("public class ").append(className).append("Res {\n\n");

        // Id
        sb.append("    @Schema(description = \"").append(className).append(" - Id\")\n")
                .append("    @Data\n    @Builder\n    @FieldDefaults(level = AccessLevel.PRIVATE)\n")
                .append("    public static class Id {\n");
        for (ColumnMetadata col : columns.stream().filter(ColumnMetadata::primaryKey).toList()) {
            String javaType = TypeResolver.resolveJavaType(col);
            String fieldName = CamelCase.of(col.name());
            String comment = col.comment();
            sb.append("        @Schema(description = \"").append(comment).append("\")\n");
            sb.append("        ").append(javaType).append(" ").append(fieldName).append(";\n");
        }
        sb.append("    }\n\n");

        // Name
        sb.append("    @Schema(description = \"").append(className).append(" - Name\")\n")
                .append("    @Data\n    @Builder\n    @EqualsAndHashCode(callSuper = true)\n    @FieldDefaults(level = AccessLevel.PRIVATE)\n")
                .append("    public static class Name extends Id {\n");
        columns.stream()
                .filter(c -> CamelCase.of(c.name()).equals("name"))
                .forEach(col -> {
                    String javaType = TypeResolver.resolveJavaType(col);
                    String comment = col.comment();
                    sb.append("        @Schema(description = \"").append(comment).append("\")\n");
                    sb.append("        ").append(javaType).append(" name;\n");
                });
        sb.append("    }\n\n");

        // Single
        sb.append("    @Schema(description = \"").append(className).append(" - Single\")\n")
                .append("    @Data\n    @Builder\n    @EqualsAndHashCode(callSuper = true)\n    @FieldDefaults(level = AccessLevel.PRIVATE)\n")
                .append("    public static class Single extends Name {\n");
        for (ColumnMetadata col : columns) {
            String field = CamelCase.of(col.name());
            if (field.equals("name")) continue;
            if (columns.stream().filter(ColumnMetadata::primaryKey)
                    .map(c -> CamelCase.of(c.name()))
                    .anyMatch(pk -> pk.equals(field))) continue;

            String javaType = TypeResolver.resolveJavaType(col);
            String comment = col.comment();
            sb.append("        @Schema(description = \"").append(comment).append("\")\n");
            sb.append("        ").append(javaType).append(" ").append(field).append(";\n");
        }
        sb.append("    }\n\n");

        // Item
        sb.append("    @Schema(description = \"").append(className).append(" - Item\")\n")
                .append("    @Data\n    @Builder\n    @EqualsAndHashCode(callSuper = true)\n    @FieldDefaults(level = AccessLevel.PRIVATE)\n")
                .append("    public static class Item extends Single {\n")
                .append("    }\n");

        sb.append("}\n");

        writeFile(outputDir + "/dto", className + "Res.java", sb.toString());
    }

    private void writeFile(String dirPath, String fileName, String content) {
        try {
            File dir = new File(dirPath);
            dir.mkdirs();
            File file = new File(dir, fileName);
            try (FileWriter fw = new FileWriter(file)) {
                fw.write(content);
            }
            System.out.println("✅ DTO 생성 완료: " + file.getPath());
        } catch (IOException e) {
            throw new RuntimeException("DTO 파일 생성 실패", e);
        }
    }
}
