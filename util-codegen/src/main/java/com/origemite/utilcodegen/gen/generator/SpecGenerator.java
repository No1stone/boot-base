package com.origemite.utilcodegen.gen.generator;


import com.origemite.utilcodegen.gen.metadata.ColumnMetadata;
import com.origemite.utilcodegen.gen.metadata.TableMetadata;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;

public class SpecGenerator {

    public void generate(TableMetadata table, String outputDirIgnored) {
        String className = toPascalCase(table.tableName());
        String context = extractContext(table.tableName());
        String filePath = Paths.get(outputDirIgnored, context, "spec", className + "Specs.java").toString();

        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("package io.diddda.api." + context + ".spec;\n\n");
                writer.write("import io.diddda.lib.common.base.UpdatedEntity;\n");
                writer.write("import io.diddda.lib.common.base.CreatedEntity;\n");
                writer.write("import io.diddda.lib.common.base.PredicateBuilder;\n");
                writer.write("import org.springframework.data.jpa.domain.Specification;\n\n");

                writer.write("public class " + className + "Specs {\n\n");
                writer.write("    public static Specification<" + className + "> of(" + className + "Req.Filter filter) {\n");
                writer.write("        return (root, query, builder) -> {\n\n");
                writer.write("            PredicateBuilder predicateBuilder = new PredicateBuilder(builder, root);\n\n");

                for (ColumnMetadata col : table.columns()) {
                    String fieldName = toCamelCase(col.name());
                    String entityField = className + ".Fields." + fieldName;
                    if (col.name().equalsIgnoreCase("created_at")) {
                        writer.write("            // predicateBuilder.goe(CreatedEntity.Fields.createdAt, filter.getStartAt());\n");
                        writer.write("            // predicateBuilder.loe(CreatedEntity.Fields.createdAt, filter.getEndAt());\n");
                    } else if (col.name().equalsIgnoreCase("created_by")) {
                        writer.write("            // predicateBuilder.equal(CreatedEntity.Fields.createdBy, filter.getCreatedBy());\n");
                    } else if (col.name().equalsIgnoreCase("updated_at")) {

                    } else if (col.name().equalsIgnoreCase("updated_by")) {
                        writer.write("            // predicateBuilder.equal(UpdatedEntity.Fields.updatedBy, filter.getUpdatedBy());\n");
                    } else if (col.name().equalsIgnoreCase("version")) {

                    } else {
                        writer.write("            predicateBuilder.equal(" + entityField + ", filter.get" + capitalize(fieldName) + "());\n");
                    }
                }

                writer.write("\n            return predicateBuilder.build();\n");
                writer.write("        };\n");
                writer.write("    }\n");
                writer.write("}\n");
            }

            System.out.println("✔ Spec 생성 완료: " + file.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("Spec 생성 실패", e);
        }
    }

    private String extractContext(String tableName) {
        int underscoreIndex = tableName.indexOf("_");
        return (underscoreIndex != -1) ? tableName.substring(0, underscoreIndex) : "common";
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

    private String toCamelCase(String text) {
        String[] parts = text.split("_");
        StringBuilder sb = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++)
            sb.append(parts[i].substring(0, 1).toUpperCase()).append(parts[i].substring(1));
        return sb.toString();
    }

    private String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
