package com.origemite.utilcodegen.gen.generator;

import com.origemite.utilcodegen.gen.metadata.ColumnMetadata;
import com.origemite.utilcodegen.gen.metadata.TableMetadata;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EntityGenerator {

    private static final Map<String, String> typeMapping = Map.ofEntries(
            Map.entry("varchar", "String"),
            Map.entry("char", "String"),
            Map.entry("text", "String"),
            Map.entry("int", "Integer"),
            Map.entry("bigint", "Long"),
            Map.entry("tinyint", "Boolean"),
            Map.entry("smallint", "Integer"),
            Map.entry("decimal", "BigDecimal"),
            Map.entry("double", "Double"),
            Map.entry("float", "Double"),
            Map.entry("datetime", "LocalDateTime"),
            Map.entry("timestamp", "LocalDateTime"),
            Map.entry("json", "Map<String, String>")
    );

    public void generate(TableMetadata table, String outputDirIgnored) {
        String className = toPascalCase(table.tableName());
        String context = extractContext(table.tableName());
        String baseDir =outputDirIgnored;
        String filePath = Paths.get(baseDir, context, "entity", className + ".java").toString();

        boolean hasCreatedAt = hasField(table.columns(), "created_at");
        boolean hasUpdatedAt = hasField(table.columns(), "updated_at");
        boolean hasAuditFields = hasCreatedAt || hasUpdatedAt;

        List<String> auditFields = List.of("created_at", "created_by", "updated_at", "updated_by", "version");

        String superClass = null;
        if (hasCreatedAt && hasUpdatedAt) superClass = "UpdatedEntity";
        else if (hasCreatedAt) superClass = "CreatedEntity";

        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(file)) {

                writer.write("package io.diddda.api." + context + ".entity;\n\n");
                writer.write("import io.diddda.lib.common.util.BeanUtils;\n");
                if (superClass != null)
                    writer.write("import io.diddda.lib.common.base." + superClass + ";\n");
                writer.write("import jakarta.persistence.*;\n");
                writer.write("import lombok.*;\n");
                writer.write("import lombok.experimental.FieldNameConstants;\n");
                writer.write("import org.hibernate.annotations.DynamicInsert;\n");
                writer.write("import io.swagger.v3.oas.annotations.media.Schema;\n");
                writer.write("import java.time.*;\n");
                writer.write("import java.util.*;\n");
                writer.write("import java.math.*;\n\n");

                writer.write("/**\n * " + table.tableName() + " Entity\n */\n");
                writer.write("@Getter\n@Setter\n@Builder\n@NoArgsConstructor(access = AccessLevel.PROTECTED)\n@AllArgsConstructor\n");
                writer.write("@Entity\n@Table(name = \"" + table.tableName() + "\")\n@DynamicInsert\n@FieldNameConstants\n");
                writer.write("public class " + className);
                if (superClass != null) writer.write(" extends " + superClass);
                writer.write(" {\n\n");

                for (ColumnMetadata col : table.columns()) {
                    if (hasAuditFields && auditFields.contains(col.name().toLowerCase())) continue;
                    String fieldName = toCamelCase(col.name());
                    String javaType = mapToJavaType(col.dataType());

                    writer.write("    /** " + col.comment() + " */\n");
                    writer.write("    @Column(name = \"" + col.name() + "\"");
                    if (col.charLength() != null) writer.write(", length = " + col.charLength());
//                    if (col.numericPrecision() != null && col.numericScale() != null)
//                        writer.write(", precision = " + col.numericPrecision() + ", scale = " + col.numericScale());
                    if (!col.nullable()) writer.write(", nullable = false");
                    if (col.primaryKey()) writer.write(", unique = true");
                    writer.write(")\n");

                    if (col.primaryKey()) {
                        writer.write("    @Id\n");
                        if (isNumericType(col.dataType()) && col.autoIncrement()) {
                            writer.write("    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
                        }
                    }
                    if (col.dataType().equalsIgnoreCase("json")) {
                        writer.write("    @Convert(converter = JsonToMapConverter.class)\n");
                    }
                    writer.write("    @Schema(description = \"" + col.comment() + "\")\n");
                    writer.write("    private " + javaType + " " + fieldName + ";\n\n");
                }

                writer.write("    public void delete() {\n        // 삭제 처리 로직 (상황에 맞게 구현)\n    }\n\n");

                writer.write("    public void update(" + className + " entity) {\n");
                writer.write("        BeanUtils.map(entity, this);\n    }\n");

                writer.write("}\n");
            }

            System.out.println("✔ Entity 생성 완료: " + file.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("Entity 생성 실패", e);
        }
    }

    private boolean isNumericType(String dbType) {
        return Set.of("int", "bigint", "smallint", "mediumint", "tinyint").contains(dbType.toLowerCase());
    }

    private String mapToJavaType(String dbType) {
        return typeMapping.getOrDefault(dbType.toLowerCase(), "String");
    }

    private String toCamelCase(String text) {
        String[] parts = text.split("_");
        StringBuilder sb = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++)
            sb.append(parts[i].substring(0, 1).toUpperCase()).append(parts[i].substring(1));
        return sb.toString();
    }

    private String toPascalCase(String tableName) {
        int underscoreIndex = tableName.indexOf("_");
        String trimmed = (underscoreIndex != -1) ? tableName.substring(underscoreIndex + 1) : tableName;
        String[] parts = trimmed.split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts)
            sb.append(part.substring(0, 1).toUpperCase()).append(part.substring(1));
        return sb.toString();
    }

    private boolean hasField(List<ColumnMetadata> columns, String fieldName) {
        return columns.stream().anyMatch(col -> col.name().equalsIgnoreCase(fieldName));
    }

    private String extractContext(String tableName) {
        int underscoreIndex = tableName.indexOf("_");
        return (underscoreIndex != -1) ? tableName.substring(0, underscoreIndex) : "common";
    }
}