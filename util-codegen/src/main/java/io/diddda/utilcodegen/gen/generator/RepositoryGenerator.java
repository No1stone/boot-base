package io.diddda.utilcodegen.gen.generator;


import io.diddda.utilcodegen.gen.metadata.TableMetadata;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;

public class RepositoryGenerator {

    public void generate(TableMetadata table, String outputDir) {
        String entityName = toPascalCase(table.tableName());
        String pkType = getPkType(table); // ex: String, Long, Integer
        String context = extractContext(table.tableName());
        String packageName = "io.diddda.api." + context + ".repository";
        String filePath = Paths.get(outputDir, context, "repository", entityName + "Repository.java").toString();

        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(file)) {

                // 패키지 및 import
                writer.write("package " + packageName + ";\n\n");
                writer.write("import com.querydsl.jpa.impl.JPAQueryFactory;\n");
                writer.write("import io.diddda.api." + context + ".entity." + entityName + ";\n");
                writer.write("import lombok.RequiredArgsConstructor;\n");
                writer.write("import org.springframework.data.jpa.repository.JpaSpecificationExecutor;\n");
                writer.write("import org.springframework.data.jpa.repository.JpaRepository;\n");
                writer.write("import org.springframework.stereotype.Repository;\n");
                writer.write("import java.util.List;\n\n");

                // JavaDoc
                writer.write("/**\n");
                writer.write(" * " + entityName + " Repository\n");
                writer.write(" */\n");

                // Repository 인터페이스
                writer.write("public interface " + entityName + "Repository extends JpaRepository<" + entityName + ", " + pkType + ">, " + entityName + "CustomRepository , JpaSpecificationExecutor<"+entityName+">{\n");
                writer.write("    List<" + entityName + "> findByIdIn(List<" + pkType + "> ids);\n");
                writer.write("}\n\n");

                // CustomRepository 인터페이스
                writer.write("@Repository\n");
                writer.write("interface " + entityName + "CustomRepository {\n");
                writer.write("}\n\n");

                // CustomRepository 구현체
                writer.write("@RequiredArgsConstructor\n");
                writer.write("class " + entityName + "CustomRepositoryImpl implements " + entityName + "CustomRepository {\n");
                writer.write("    private final JPAQueryFactory queryFactory;\n");
                writer.write("}\n");

            }

            System.out.println("✔ Repository 생성 완료: " + file.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("Repository 생성 실패", e);
        }
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

    private String extractContext(String tableName) {
        int underscoreIndex = tableName.indexOf("_");
        return (underscoreIndex != -1) ? tableName.substring(0, underscoreIndex) : "common";
    }

    private String getPkType(TableMetadata table) {
        return table.columns().stream()
                .filter(col -> col.primaryKey())
                .findFirst()
                .map(col -> {
                    String type = col.dataType().toLowerCase();
                    return switch (type) {
                        case "int", "smallint", "mediumint" -> "Integer";
                        case "bigint" -> "Long";
                        default -> "String";
                    };
                })
                .orElse("String");
    }
}