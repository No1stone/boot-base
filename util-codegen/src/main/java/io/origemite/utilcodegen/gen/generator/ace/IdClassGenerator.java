package io.origemite.utilcodegen.gen.generator.ace;

import io.origemite.utilcodegen.gen.metadata.CamelCase;
import io.origemite.utilcodegen.gen.metadata.ColumnMetadata;
import io.origemite.utilcodegen.gen.metadata.TableMetadata;
import io.origemite.utilcodegen.gen.metadata.TypeResolver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class IdClassGenerator {

    public void generate(TableMetadata table, String outputDir) {
        String className = table.className() + "Id";
        String packageName = "io.diddda.api." + table.schemaName() + ".entity";
        String fileName = className + ".java";

        List<ColumnMetadata> pkColumns = table.columns().stream()
                .filter(ColumnMetadata::primaryKey)
                .toList();

        if (pkColumns.size() < 2) return; // 단일키면 생성 안 함

        StringBuilder sb = new StringBuilder();

        sb.append("package ").append(packageName).append(";\n\n");
        sb.append("import lombok.*;\n");
        sb.append("import java.io.Serializable;\n");
        sb.append("import java.time.*;\n");
        sb.append("import java.math.*;\n\n");

        sb.append("@Data\n@NoArgsConstructor\n@AllArgsConstructor\n");
        sb.append("public class ").append(className).append(" implements Serializable {\n\n");

        for (ColumnMetadata col : pkColumns) {
            String type = TypeResolver.resolveJavaType(col);
            String field = CamelCase.of(col.name());
            sb.append("    private ").append(type).append(" ").append(field).append(";\n");
        }

        sb.append("}\n");

        try {
            String dirPath = outputDir + "/id";
            File dir = new File(dirPath);
            dir.mkdirs();
            File file = new File(dir, fileName);
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(sb.toString());
            }
            System.out.println("✅ IdClass 생성 완료: " + file.getPath());
        } catch (IOException e) {
            throw new RuntimeException("IdClass 파일 생성 실패", e);
        }
    }
}