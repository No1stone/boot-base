package com.origemite.utilcodegen.gen.generator;

import com.origemite.utilcodegen.gen.metadata.ColumnMetadata;
import com.origemite.utilcodegen.gen.metadata.TableMetadata;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;

public class FacadeGenerator {
    public void generate(TableMetadata table, String rootPath) {
        // 테이블: auth_member -> entityName: Member, context: auth
        String entityName = toPascalCase(table.tableName());
        String context = extractContext(table.tableName());   // lib.model.<context>.* 에 사용
        String pkType = getPkType(table);                     // String / Integer / Long

        // member / facade / MemberFacade.java
        String entityVar = toLowerCamel(entityName);          // Member -> member
//        String filePath = Paths.get(
//                rootPath,
//                entityVar,
//                "facade",
//                entityName + "Facade.java"
//        ).toString();
        String filePath = Paths.get(rootPath, context, "facade", entityName + "Facade.java").toString();


        try (FileWriter writer = prepareFile(filePath)) {

            // package
            writer.write("package com.origemite.apiauth." + entityVar + ".facade;\n\n");

            // imports (샘플 기준: lib.model.<context>.*)
//            writer.write("import com.origemite.lib.model." + context + ".dto." + entityName + "Req;\n");
//            writer.write("import com.origemite.lib.model." + context + ".dto." + entityName + "Res;\n");
//            writer.write("import com.origemite.lib.model." + context + ".service." + entityName + "Service;\n");
            writer.write("import lombok.RequiredArgsConstructor;\n");
            writer.write("import lombok.extern.slf4j.Slf4j;\n");
            writer.write("import org.springframework.data.domain.Page;\n");
            writer.write("import org.springframework.data.domain.Pageable;\n");
            writer.write("import org.springframework.stereotype.Component;\n");
            writer.write("import org.springframework.transaction.annotation.Transactional;\n");
            writer.write("import java.util.List;\n\n");

            // class 선언부
            writer.write("@Slf4j\n");
            writer.write("@Component\n");
            writer.write("@RequiredArgsConstructor\n");
            writer.write("@Transactional(readOnly = true)\n");
            writer.write("public class " + entityName + "Facade {\n\n");

            // 필드
            writer.write("    private final " + entityName + "Service " + entityVar + "Service;\n\n");

            // search
            writer.write("    public Page<" + entityName + "Res.Item> search(" + entityName + "Req.Filter filter, Pageable pageable) {\n");
            writer.write("        return " + entityVar + "Service.search(filter, pageable);\n");
            writer.write("    }\n\n");

            // save
            writer.write("    @Transactional\n");
            writer.write("    public " + entityName + "Res.Id save(" + entityName + "Req.Create create) {\n");
            writer.write("        return " + entityVar + "Service.save(create);\n");
            writer.write("    }\n\n");

            // findById
            writer.write("    public " + entityName + "Res.Item findById(" + pkType + " id) {\n");
            writer.write("        return " + entityVar + "Service.findById(id);\n");
            writer.write("    }\n\n");

            // saveById
            writer.write("    @Transactional\n");
            writer.write("    public " + entityName + "Res.Id saveById(" + pkType + " id, " + entityName + "Req.Update update) {\n");
            writer.write("        return " + entityVar + "Service.saveById(id, update);\n");
            writer.write("    }\n\n");

            // deleteById
            writer.write("    @Transactional\n");
            writer.write("    public void deleteById(" + pkType + " id) {\n");
            writer.write("        " + entityVar + "Service.deleteById(id);\n");
            writer.write("    }\n\n");

            // findAll
            writer.write("    public List<" + entityName + "Res.Item> findAll(" + entityName + "Req.Filter filter) {\n");
            writer.write("        return " + entityVar + "Service.findAll(filter);\n");
            writer.write("    }\n\n");

            // findByIds
            writer.write("    public List<" + entityName + "Res.Item> findByIds(List<" + pkType + "> ids) {\n");
            writer.write("        return " + entityVar + "Service.findByIds(ids);\n");
            writer.write("    }\n\n");

            // searchName
            writer.write("    public Page<" + entityName + "Res.Name> searchName(" + entityName + "Req.Filter filter, Pageable pageable) {\n");
            writer.write("        return " + entityVar + "Service.searchName(filter, pageable);\n");
            writer.write("    }\n\n");

            // findAllName
            writer.write("    public List<" + entityName + "Res.Name> findAllName(" + entityName + "Req.Filter filter) {\n");
            writer.write("        return " + entityVar + "Service.findAllName(filter);\n");
            writer.write("    }\n\n");

            // deleteByIds
            writer.write("    @Transactional\n");
            writer.write("    public void deleteByIds(List<" + pkType + "> ids) {\n");
            writer.write("        " + entityVar + "Service.deleteByIds(ids);\n");
            writer.write("    }\n");

            writer.write("}\n");

            System.out.println("✔ Facade 생성 완료: " + filePath);
        } catch (Exception e) {
            throw new RuntimeException("Facade 생성 실패", e);
        }
    }

    // ===== 공통 유틸 =====

    private String extractContext(String tableName) {
        int idx = tableName.indexOf("_");
        return (idx != -1) ? tableName.substring(0, idx) : "common";
    }

    /**
     * 테이블명 기준 PascalCase
     * - auth_member -> Member
     * - me_member_password_change_history -> MemberPasswordChangeHistory
     */
    private String toPascalCase(String text) {
        int idx = text.indexOf("_");
        String trimmed = (idx != -1) ? text.substring(idx + 1) : text;
        String[] parts = trimmed.split("_");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (p.isEmpty()) continue;
            sb.append(p.substring(0, 1).toUpperCase()).append(p.substring(1));
        }
        return sb.toString();
    }

    /**
     * 클래스명 -> lowerCamel
     * - MemberPasswordChangeHistory -> memberPasswordChangeHistory
     */
    private String toLowerCamel(String name) {
        if (name == null || name.isEmpty()) return name;
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    private String getPkType(TableMetadata table) {
        return table.columns().stream()
                .filter(ColumnMetadata::primaryKey)
                .findFirst()
                .map(col -> switch (col.dataType().toLowerCase()) {
                    case "int", "smallint", "mediumint" -> "Integer";
                    case "bigint" -> "Long";
                    default -> "String";
                })
                .orElse("String");
    }

    private FileWriter prepareFile(String path) throws Exception {
        File file = new File(path);
        file.getParentFile().mkdirs();
        return new FileWriter(file);
    }
}