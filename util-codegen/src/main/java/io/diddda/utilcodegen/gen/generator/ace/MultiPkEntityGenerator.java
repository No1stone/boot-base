package io.diddda.utilcodegen.gen.generator.ace;

import io.diddda.utilcodegen.gen.metadata.ColumnMetadata;
import io.diddda.utilcodegen.gen.metadata.TableMetadata;
import io.diddda.utilcodegen.gen.metadata.TypeResolver;
import io.diddda.utilcodegen.gen.metadata.CamelCase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MultiPkEntityGenerator {

    public void generate(TableMetadata table, String outputDir) {
        String className = table.className();
        String idClassName = className + "Id";
        String packagePath = "io.diddda.api." + table.schemaName() + ".entity";
        List<ColumnMetadata> columns = table.columns();

        long pkCount = columns.stream().filter(ColumnMetadata::primaryKey).count();
        boolean isCompositePk = pkCount >= 2;

        StringBuilder sb = new StringBuilder();

        // 패키지 및 import
        sb.append("package ").append(packagePath).append(";\n\n");
        sb.append("import jakarta.persistence.*;\n");
        sb.append("import lombok.*;\n");
        sb.append("import import io.diddda.lib.common.util.BeanUtils;\n");
        sb.append("import lombok.experimental.FieldNameConstants;\n");
        sb.append("import org.hibernate.annotations.DynamicInsert;\n");
        sb.append("import io.swagger.v3.oas.annotations.media.Schema;\n");
        sb.append("import java.time.*;\n");
        sb.append("import java.math.*;\n\n");

        // 클래스 Javadoc
        sb.append("/**\n * ").append(table.tableName()).append(" 엔티티\n */\n");

        // 클래스 어노테이션
        sb.append("@Getter\n@Setter\n@Builder\n");
        sb.append("@NoArgsConstructor(access = AccessLevel.PROTECTED)\n@AllArgsConstructor\n");
        sb.append("@Entity\n@Table(name = \"").append(table.tableName()).append("\"");
        if (!table.schemaName().isBlank()) {
            sb.append(", schema = \"").append(table.schemaName()).append("\"");
        }
        sb.append(")\n@DynamicInsert\n@FieldNameConstants\n");

        // 클래스 선언
        if (isCompositePk) {
            sb.append("@IdClass(").append(idClassName).append(".class)\n");
        }
        sb.append("public class ").append(className).append(" {\n\n");

        // 필드들
        for (ColumnMetadata col : columns) {
            String javaType = TypeResolver.resolveJavaType(col);
            String name = CamelCase.of(col.name());
            String comment = col.comment() != null && !col.comment().isBlank()
                    ? col.comment()
                    : name;

            if (col.primaryKey()) {
                sb.append("    @Id\n");
                if (col.autoIncrement()) {
                    sb.append("    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
                }
            }

            sb.append("    @Column(name = \"").append(col.name()).append("\"");
            if (!col.nullable()) sb.append(", nullable = false");
            sb.append(")\n");
            sb.append("    @Schema(description = \"").append(comment).append("\")\n");
            sb.append("    private ").append(javaType).append(" ").append(name).append(";\n\n");
        }

        // delete() method
//        sb.append("    public void delete() {\n");
//        sb.append("        this.status = \"D\";\n");
//        sb.append("    }\n\n");

        // update() method
        sb.append("    public void update(").append(className).append(" source) {\n");
        sb.append("        BeanUtils.map(source, this);\n");
        sb.append("    }\n");

        sb.append("}\n");

        // 파일 쓰기
        try {
            String dirPath = outputDir + "/entity";
            File dir = new File(dirPath);
            dir.mkdirs();
            File file = new File(dir, className + ".java");
            try (FileWriter fw = new FileWriter(file)) {
                fw.write(sb.toString());
            }
            System.out.println("✅ Entity 생성 완료: " + file.getPath());
        } catch (IOException e) {
            throw new RuntimeException("Entity 파일 생성 실패", e);
        }
    }
}