package com.origemite.utilcodegen.gen.generator;

import com.origemite.utilcodegen.gen.metadata.ColumnMetadata;
import com.origemite.utilcodegen.gen.metadata.TableMetadata;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ControllerTestGenerator {

    private static final List<String> AUDIT_FIELDS =
            List.of("created_at", "created_by", "updated_at", "updated_by", "version");

    public void generate(TableMetadata table, String testRootPath) {
        // 엔티티명: me_member_password_change_history -> MemberPasswordChangeHistory
        String entityName = toPascalCase(table.tableName());
        // context: me_member_password_change_history -> me
        String context = extractContext(table.tableName());
        String pkType = getPkType(table); // Long / Integer / String

        String filePath = Paths.get(
                testRootPath,
                "com", "origemite", "api", context, "controller",
                entityName + "ControllerTest.java"
        ).toString();

        try (FileWriter writer = prepareFile(filePath)) {
            // package
            writer.write("package com.origemite.api." + context + ".controller;\n\n");

            // imports
            writer.write("import com.origemite.api." + context + ".dto." + entityName + "Req;\n");
            writer.write("import com.origemite.api." + context + ".dto." + entityName + "Res;\n");
            writer.write("import com.origemite.api." + context + ".service." + entityName + "Service;\n");
            writer.write("import org.junit.jupiter.api.DisplayName;\n");
            writer.write("import org.junit.jupiter.api.Test;\n");
            writer.write("import org.junit.jupiter.api.extension.ExtendWith;\n");
            writer.write("import org.mockito.InjectMocks;\n");
            writer.write("import org.mockito.Mock;\n");
            writer.write("import org.mockito.junit.jupiter.MockitoExtension;\n");
            writer.write("import org.springframework.data.domain.Page;\n");
            writer.write("import org.springframework.data.domain.PageImpl;\n");
            writer.write("import org.springframework.data.domain.PageRequest;\n");
            writer.write("import org.springframework.data.domain.Pageable;\n\n");

            writer.write("import java.time.LocalDateTime;\n");
            writer.write("import java.util.List;\n");
            writer.write("import java.util.Map;\n\n");

            writer.write("import static org.mockito.ArgumentMatchers.*;\n");
            writer.write("import static org.mockito.BDDMockito.given;\n");
            writer.write("import static org.mockito.Mockito.verify;\n");
            writer.write("import static org.junit.jupiter.api.Assertions.*;\n\n");

            // 클래스 선언
            writer.write("@ExtendWith(MockitoExtension.class)\n");
            writer.write("class " + entityName + "ControllerTest {\n\n");

            String controllerVar = toLowerCamel(entityName) + "Controller";
            String serviceVar = toLowerCamel(entityName) + "Service";

            // @InjectMocks / @Mock
            writer.write("    @InjectMocks\n");
            writer.write("    " + entityName + "Controller " + controllerVar + ";\n\n");

            writer.write("    @Mock\n");
            writer.write("    " + entityName + "Service " + serviceVar + ";\n\n");

            // ========== search ==========
            writer.write("    @Test\n");
            writer.write("    @DisplayName(\"" + entityName + " 목록 조회 성공\")\n");
            writer.write("    void search_success() {\n");
            writer.write("        // given\n");
            writer.write("        " + entityName + "Req.Filter filter = sampleFilter();\n");
            writer.write("        Pageable pageable = PageRequest.of(0, 10);\n");
            writer.write("        Page<" + entityName + "Res.Item> page = new PageImpl<>(List.of(sampleItem()));\n");
            writer.write("        given(" + serviceVar + ".search(any(), any())).willReturn(page);\n\n");
            writer.write("        // when\n");
            writer.write("        var result = " + controllerVar + ".search(filter, pageable);\n\n");
            writer.write("        // then\n");
            writer.write("        assertNotNull(result);\n");
            writer.write("        verify(" + serviceVar + ").search(any(), any());\n");
            writer.write("    }\n\n");

            // ========== save ==========
            writer.write("    @Test\n");
            writer.write("    @DisplayName(\"" + entityName + " 생성 성공\")\n");
            writer.write("    void save_success() {\n");
            writer.write("        // given\n");
            writer.write("        " + entityName + "Req.Create create = sampleCreate();\n");
            writer.write("        " + entityName + "Res.Id idRes = sampleIdRes();\n");
            writer.write("        given(" + serviceVar + ".save(any())).willReturn(idRes);\n\n");
            writer.write("        // when\n");
            writer.write("        var result = " + controllerVar + ".save(create);\n\n");
            writer.write("        // then\n");
            writer.write("        assertNotNull(result);\n");
            writer.write("        verify(" + serviceVar + ").save(any());\n");
            writer.write("    }\n\n");

            // ========== findById ==========
            writer.write("    @Test\n");
            writer.write("    @DisplayName(\"" + entityName + " 단건 조회 성공\")\n");
            writer.write("    void findById_success() {\n");
            writer.write("        // given\n");
            writer.write("        " + entityName + "Res.Item item = sampleItem();\n");
            writer.write("        given(" + serviceVar + ".findById(any())).willReturn(item);\n");
            writer.write("        " + pkType + " id = sampleId();\n\n");
            writer.write("        // when\n");
            writer.write("        var result = " + controllerVar + ".findById(id);\n\n");
            writer.write("        // then\n");
            writer.write("        assertNotNull(result);\n");
            writer.write("        verify(" + serviceVar + ").findById(any());\n");
            writer.write("    }\n\n");

            // ========== saveById ==========
            writer.write("    @Test\n");
            writer.write("    @DisplayName(\"" + entityName + " 수정 성공\")\n");
            writer.write("    void saveById_success() {\n");
            writer.write("        // given\n");
            writer.write("        " + entityName + "Req.Update update = sampleUpdate();\n");
            writer.write("        " + entityName + "Res.Id idRes = sampleIdRes();\n");
            writer.write("        " + pkType + " id = sampleId();\n");
            writer.write("        given(" + serviceVar + ".saveById(any(), any())).willReturn(idRes);\n\n");
            writer.write("        // when\n");
            writer.write("        var result = " + controllerVar + ".saveById(id, update);\n\n");
            writer.write("        // then\n");
            writer.write("        assertNotNull(result);\n");
            writer.write("        verify(" + serviceVar + ").saveById(any(), any());\n");
            writer.write("    }\n\n");

            // ========== deleteById ==========
            writer.write("    @Test\n");
            writer.write("    @DisplayName(\"" + entityName + " 삭제 성공\")\n");
            writer.write("    void deleteById_success() {\n");
            writer.write("        // given\n");
            writer.write("        " + pkType + " id = sampleId();\n\n");
            writer.write("        // when\n");
            writer.write("        " + controllerVar + ".deleteById(id);\n\n");
            writer.write("        // then\n");
            writer.write("        verify(" + serviceVar + ").deleteById(any());\n");
            writer.write("    }\n\n");

            // ========== findAll ==========
            writer.write("    @Test\n");
            writer.write("    @DisplayName(\"" + entityName + " 전체 조회 성공\")\n");
            writer.write("    void findAll_success() {\n");
            writer.write("        // given\n");
            writer.write("        " + entityName + "Req.Filter filter = sampleFilter();\n");
            writer.write("        given(" + serviceVar + ".findAll(any()))\n");
            writer.write("                .willReturn(List.of(sampleItem()));\n\n");
            writer.write("        // when\n");
            writer.write("        var result = " + controllerVar + ".findAll(filter);\n\n");
            writer.write("        // then\n");
            writer.write("        assertNotNull(result);\n");
            writer.write("        verify(" + serviceVar + ").findAll(any());\n");
            writer.write("    }\n\n");

            // ========== findByIds ==========
            writer.write("    @Test\n");
            writer.write("    @DisplayName(\"" + entityName + " ID 리스트 조회 성공\")\n");
            writer.write("    void findByIds_success() {\n");
            writer.write("        // given\n");
            writer.write("        List<" + pkType + "> ids = List.of(sampleId());\n");
            writer.write("        given(" + serviceVar + ".findByIds(anyList()))\n");
            writer.write("                .willReturn(List.of(sampleItem()));\n\n");
            writer.write("        // when\n");
            writer.write("        var result = " + controllerVar + ".findByIds(ids);\n\n");
            writer.write("        // then\n");
            writer.write("        assertNotNull(result);\n");
            writer.write("        verify(" + serviceVar + ").findByIds(anyList());\n");
            writer.write("    }\n\n");

            // ======== 헬퍼 메서드들 ========

            // sampleId() — PK 타입에 맞게 정확히 반환
            if ("Long".equals(pkType)) {
                writer.write("    private Long sampleId() {\n");
                writer.write("        return 1L;\n");
                writer.write("    }\n\n");
            } else if ("Integer".equals(pkType)) {
                writer.write("    private Integer sampleId() {\n");
                writer.write("        return 1;\n");
                writer.write("    }\n\n");
            } else { // String
                writer.write("    private String sampleId() {\n");
                writer.write("        return \"sample-id\";\n");
                writer.write("    }\n\n");
            }

            // sampleIdRes()
            String pkFieldName = toCamelCase(getPkColumn(table).name());
            writer.write("    private " + entityName + "Res.Id sampleIdRes() {\n");
            writer.write("        " + entityName + "Res.Id id = new " + entityName + "Res.Id();\n");
            writer.write("        id.set" + toPascalCase(pkFieldName) + "(sampleId());\n");
            writer.write("        return id;\n");
            writer.write("    }\n\n");

            // sampleFilter()
            writer.write("    private " + entityName + "Req.Filter sampleFilter() {\n");
            writer.write("        " + entityName + "Req.Filter.FilterBuilder builder = " + entityName + "Req.Filter.builder();\n");
            for (ColumnMetadata col : table.columns()) {
                String fieldName = toCamelCase(col.name());
                String sampleExpr = sampleValueExpr(col);
                writer.write("        builder." + fieldName + "(" + sampleExpr + ");\n");
            }
            writer.write("        return builder.build();\n");
            writer.write("    }\n\n");

            // sampleCreate()
            writer.write("    private " + entityName + "Req.Create sampleCreate() {\n");
            writer.write("        " + entityName + "Req.Create.CreateBuilder builder = " + entityName + "Req.Create.builder();\n");
            for (ColumnMetadata col : table.columns()) {
                String colNameLower = col.name().toLowerCase();
                if (AUDIT_FIELDS.contains(colNameLower)) continue;
                String fieldName = toCamelCase(col.name());
                String sampleExpr = sampleValueExpr(col);
                writer.write("        builder." + fieldName + "(" + sampleExpr + ");\n");
            }
            writer.write("        return builder.build();\n");
            writer.write("    }\n\n");

            // sampleUpdate()
            writer.write("    private " + entityName + "Req.Update sampleUpdate() {\n");
            writer.write("        " + entityName + "Req.Update.UpdateBuilder builder = " + entityName + "Req.Update.builder();\n");
            for (ColumnMetadata col : table.columns()) {
                String colNameLower = col.name().toLowerCase();
                if (AUDIT_FIELDS.contains(colNameLower)) continue;
                String fieldName = toCamelCase(col.name());
                String sampleExpr = sampleValueExpr(col);
                writer.write("        builder." + fieldName + "(" + sampleExpr + ");\n");
            }
            writer.write("        return builder.build();\n");
            writer.write("    }\n\n");

            // sampleItem()
            writer.write("    private " + entityName + "Res.Item sampleItem() {\n");
            writer.write("        " + entityName + "Res.Item item = new " + entityName + "Res.Item();\n");
            for (ColumnMetadata col : table.columns()) {
                String fieldName = toCamelCase(col.name());
                String methodName = "set" + toPascalCase(fieldName);
                String sampleExpr = sampleValueExpr(col);
                writer.write("        item." + methodName + "(" + sampleExpr + ");\n");
            }
            writer.write("        return item;\n");
            writer.write("    }\n\n");

            writer.write("}\n");

            System.out.println("✔ Controller Unit Test 생성 완료: " + filePath);
        } catch (Exception e) {
            throw new RuntimeException("Controller Test 생성 실패", e);
        }
    }

    // ===== 공통 유틸 =====

    private String extractContext(String tableName) {
        int idx = tableName.indexOf("_");
        return (idx != -1) ? tableName.substring(0, idx) : "common";
    }

    /**
     * 테이블명 기준 PascalCase
     * - me_member_password_change_history -> MemberPasswordChangeHistory
     * - me_member -> Member
     */
    private String toPascalCase(String text) {
        int idx = text.indexOf("_");
        String trimmed = (idx != -1) ? text.substring(idx + 1) : text; // 첫 prefix(me_)는 context로 쓰고 버림
        String[] parts = trimmed.split("_");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (p.isEmpty()) continue;
            sb.append(p.substring(0, 1).toUpperCase()).append(p.substring(1));
        }
        return sb.toString();
    }

    /**
     * snake_case -> camelCase
     * - member_password_change_history -> memberPasswordChangeHistory
     * - me_member_id -> meMemberId (컬럼명 그대로)
     */
    private String toCamelCase(String text) {
        String[] parts = text.split("_");
        if (parts.length == 0) return text;
        StringBuilder sb = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            if (parts[i].isEmpty()) continue;
            sb.append(parts[i].substring(0, 1).toUpperCase()).append(parts[i].substring(1));
        }
        return sb.toString();
    }

    /**
     * 클래스명 -> lowerCamel 필드명
     * - MemberPasswordChangeHistory -> memberPasswordChangeHistory
     */
    private String toLowerCamel(String name) {
        if (name == null || name.isEmpty()) return name;
        return name.substring(0, 1).toLowerCase() + name.substring(1);
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

    /**
     * 테스트용 샘플 값 생성 (Java 코드 표현식)
     */
    private String sampleValueExpr(ColumnMetadata col) {
        String javaType = mapToJavaType(col.dataType());
        String colName = col.name().toLowerCase();

        // PK 컬럼이면 sampleId() 재사용
        if (col.primaryKey()) {
            return "sampleId()";
        }

        return switch (javaType) {
            case "String" -> "\"" + colName + "-sample\"";
            case "Integer" -> "1";
            case "Long" -> "1L";
            case "Double" -> "1.0";
            case "LocalDateTime" -> "LocalDateTime.now()";
            case "Map<String, String>" -> "Map.of(\"key\", \"value\")";
            default -> "null";
        };
    }

    private FileWriter prepareFile(String path) throws java.io.IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();
        return new FileWriter(file);
    }
}
