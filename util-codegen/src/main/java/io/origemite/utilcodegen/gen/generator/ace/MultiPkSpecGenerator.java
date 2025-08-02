package io.origemite.utilcodegen.gen.generator.ace;

import io.origemite.utilcodegen.gen.metadata.CamelCase;
import io.origemite.utilcodegen.gen.metadata.ColumnMetadata;
import io.origemite.utilcodegen.gen.metadata.TableMetadata;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MultiPkSpecGenerator {

    public void generate(TableMetadata table, String outputDir) {
        String entity = table.className();  // 예: Codeshop
        String schema = table.schemaName(); // 예: provisioning
        String packageName = "io.diddda.api." + schema + ".repository.spec";
        String dtoFilterClass = "io.diddda.lib.model." + schema + ".dto.request." + entity + "Req";

        List<ColumnMetadata> columns = table.columns();

        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(packageName).append(";\n\n")
                .append("import io.diddda.api.").append(schema).append(".entity.").append(entity).append(";\n")
                .append("import ").append(dtoFilterClass).append(";\n")
                .append("import io.diddda.lib.common.util.StringUtils;\n")
                .append("import jakarta.persistence.criteria.Predicate;\n")
                .append("import org.springframework.data.jpa.domain.Specification;\n\n")
                .append("import java.util.*;\n\n");

        sb.append("public class ").append(entity).append("Specs {\n\n")
                .append("    public static Specification<").append(entity).append("> of(")
                .append(entity).append("Req.Filter filter) {\n")
                .append("        return (root, query, cb) -> {\n")
                .append("            List<Predicate> predicates = new ArrayList<>();\n\n");

        for (ColumnMetadata column : columns) {
            String fieldName = CamelCase.of(column.name());
            String javaType = column.dataType().toLowerCase();
            String getter = "filter.get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1) + "()";

            if (javaType.contains("char") || javaType.contains("text") || javaType.equals("varchar")) {
                sb.append("            if (StringUtils.isNotEmpty(").append(getter).append(")) {\n")
                        .append("                predicates.add(cb.equal(root.get(\"").append(fieldName).append("\"), ")
                        .append(getter).append("));\n")
                        .append("            }\n");
            } else {
                sb.append("            if (").append(getter).append(" != null) {\n")
                        .append("                predicates.add(cb.equal(root.get(\"").append(fieldName).append("\"), ")
                        .append(getter).append("));\n")
                        .append("            }\n");
            }
        }

        sb.append("\n            return cb.and(predicates.toArray(new Predicate[0]));\n")
                .append("        };\n")
                .append("    }\n")
                .append("}\n");

        try {
            File dir = new File(outputDir + "/repository/spec");
            dir.mkdirs();
            File file = new File(dir, entity + "Specs.java");
            try (FileWriter fw = new FileWriter(file)) {
                fw.write(sb.toString());
            }
            System.out.println("✅ Spec 클래스 생성 완료: " + file.getPath());
        } catch (IOException e) {
            throw new RuntimeException("Spec 파일 생성 실패", e);
        }
    }
}
