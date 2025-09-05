package com.origemite.utilcodegen.gen.generator.ace;

import com.origemite.utilcodegen.gen.metadata.ColumnMetadata;
import com.origemite.utilcodegen.gen.metadata.TableMetadata;
import com.origemite.utilcodegen.gen.metadata.TypeResolver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MultiPkRepositoryGenerator {

    public void generate(TableMetadata table, String outputDir) {
        String className = table.className();                  // ex) Codeshop
        String idClassName = className + "Id";                 // ex) CodeshopId
        String packageName = "io.diddda.api." + table.schemaName() + ".entity";

        List<ColumnMetadata> pkColumns = table.columns().stream()
                .filter(ColumnMetadata::primaryKey)
                .toList();

        String pkType = pkColumns.size() == 1
                ? TypeResolver.resolveJavaType(pkColumns.get(0))
                : idClassName;

        StringBuilder sb = new StringBuilder();

        // package & import
        sb.append("package ").append(packageName).append(";\n\n");
        sb.append("import com.querydsl.jpa.impl.JPAQueryFactory;\n");
        sb.append("import org.springframework.data.jpa.repository.JpaRepository;\n");
        sb.append("import org.springframework.data.jpa.repository.JpaSpecificationExecutor;\n");
        sb.append("import org.springframework.stereotype.Repository;\n");
        sb.append("import lombok.RequiredArgsConstructor;\n\n");
        sb.append("import java.util.List;\n\n");

        // JavaDoc
        sb.append("/**\n * ").append(table.tableName()).append(" Repository\n */\n");

        // Main Repository interface
        sb.append("public interface ").append(className).append("Repository extends JpaRepository<")
                .append(className).append(", ").append(pkType).append(">, ")
                .append(className).append("CustomRepository, ")
                .append("JpaSpecificationExecutor<").append(className).append("> {\n");
        sb.append("    List<").append(className).append("> findByIdIn(List<").append(pkType).append("> ids);\n");
        sb.append("}\n\n");

        // Custom Repository interface
        sb.append("@Repository\n");
        sb.append("interface ").append(className).append("CustomRepository {\n");
        sb.append("}\n\n");

        // Impl
        sb.append("@RequiredArgsConstructor\n");
        sb.append("class ").append(className).append("CustomRepositoryImpl implements ")
                .append(className).append("CustomRepository {\n");
        sb.append("    private final JPAQueryFactory queryFactory;\n");
        sb.append("}\n");

        // 파일 출력
        try {
            String dirPath = outputDir + "/repository";
            File dir = new File(dirPath);
            dir.mkdirs();
            File file = new File(dir, className + "Repository.java");
            try (FileWriter fw = new FileWriter(file)) {
                fw.write(sb.toString());
            }
            System.out.println("✅ Repository 생성 완료: " + file.getPath());
        } catch (IOException e) {
            throw new RuntimeException("Repository 파일 생성 실패", e);
        }
    }
}
