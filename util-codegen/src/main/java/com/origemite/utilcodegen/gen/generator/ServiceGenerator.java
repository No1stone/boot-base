package com.origemite.utilcodegen.gen.generator;



import com.origemite.utilcodegen.gen.metadata.ColumnMetadata;
import com.origemite.utilcodegen.gen.metadata.TableMetadata;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;

public class ServiceGenerator {

    public void generate(TableMetadata table,String path) {
        String entityName = toPascalCase(table.tableName());
        String context = extractContext(table.tableName());
        String pkType = getPkType(table);

        String filePath = Paths.get(path, context, "service", entityName + "Service.java").toString();
        String repositoryName = toCamelCase(entityName) + "Repository";

        try (FileWriter writer = prepareFile(filePath)) {
            writer.write("package com.origemite.api." + context + ".service;\n\n");
            writer.write("import lombok.RequiredArgsConstructor;\n");
            writer.write("import lombok.extern.slf4j.Slf4j;\n");
            writer.write("import org.springframework.data.domain.*;\n");
            writer.write("import org.springframework.stereotype.Service;\n");
            writer.write("import org.springframework.transaction.annotation.Transactional;\n");
            writer.write("import com.origemite.api." + context + ".entity." + entityName + ";\n");
            writer.write("import com.origemite.api." + context + ".repository." + entityName + "Repository;\n");
            writer.write("import com.origemite.api." + context + ".dto." + entityName + "Req;\n");
            writer.write("import com.origemite.api." + context + ".dto." + entityName + "Res;\n");
            writer.write("import com.origemite.lib.common.exception.BizErrorException;\n");
            writer.write("import com.origemite.lib.common.util.BeanUtils;\n");
            writer.write("import com.origemite.lib.common.web.ResponseType;\n");
            writer.write("import java.util.*;\n\n");
            writer.write("import static com.origemite.lib.common.util.ModelMapperUtil.map;\n");
            writer.write("import static com.origemite.lib.common.util.ModelMapperUtil.mapAll;\n\n");

            writer.write("@Service\n@Slf4j\n@RequiredArgsConstructor\n@Transactional(readOnly = true)\n");
            writer.write("public class " + entityName + "Service {\n\n");
            writer.write("    private final " + entityName + "Repository " + repositoryName + ";\n\n");

            writer.write("    public Page<" + entityName + "Res.Item> search(" + entityName + "Req.Filter filter, Pageable pageable) {\n");
            writer.write("        " + entityName + " entity = map(filter, " + entityName + ".class);\n");
            writer.write("        Page<" + entityName + "> page = " + repositoryName + ".findAll(Example.of(entity), pageable);\n");
            writer.write("        return mapAll(page, " + entityName + "Res.Item.class);\n    }\n\n");

            writer.write("    @Transactional\n");
            writer.write("    public " + entityName + "Res.Id save(" + entityName + "Req.Create create) {\n");
            writer.write("        " + entityName + " entity = map(create, " + entityName + ".class);\n");
            writer.write("        entity = " + repositoryName + ".save(entity);\n");
            writer.write("        return map(entity, " + entityName + "Res.Id.class);\n    }\n\n");

            writer.write("    public " + entityName + "Res.Item findById(" + pkType + " id) {\n");
            writer.write("        " + entityName + " entity = " + repositoryName + ".findById(id).orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));\n");
            writer.write("        return map(entity, " + entityName + "Res.Item.class);\n    }\n\n");

            writer.write("    @Transactional\n");
            writer.write("    public " + entityName + "Res.Id saveById(" + pkType + " id, " + entityName + "Req.Update update) {\n");
            writer.write("        " + entityName + " entity = " + repositoryName + ".findById(id).orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));\n");
            writer.write("        BeanUtils.map(update, entity);\n");
            writer.write("        return map(entity, " + entityName + "Res.Id.class);\n    }\n\n");

            writer.write("    @Transactional\n");
            writer.write("    public void deleteById(" + pkType + " id) {\n");
            writer.write("        " + entityName + " entity = " + repositoryName + ".findById(id).orElseThrow(() -> new BizErrorException(ResponseType.UNDEFINED));\n");
            writer.write("        entity.delete();\n    }\n\n");

            writer.write("    public List<" + entityName + "Res.Item> findAll(" + entityName + "Req.Filter filter) {\n");
            writer.write("        return search(filter, Pageable.unpaged()).getContent();\n    }\n\n");

            writer.write("    public List<" + entityName + "Res.Item> findByIds(List<" + pkType + "> ids) {\n");
            writer.write("        List<" + entityName + "> list = " + repositoryName + ".findByIdIn(ids);\n");
            writer.write("        return mapAll(list, " + entityName + "Res.Item.class);\n    }\n\n");

            writer.write("    public Page<" + entityName + "Res.Name> searchName(" + entityName + "Req.Filter filter, Pageable pageable) {\n");
            writer.write("        " + entityName + " entity = map(filter, " + entityName + ".class);\n");
            writer.write("        Page<" + entityName + "> page = " + repositoryName + ".findAll(Example.of(entity), pageable);\n");
            writer.write("        return mapAll(page, " + entityName + "Res.Name.class);\n    }\n\n");

            writer.write("    public List<" + entityName + "Res.Name> findAllName(" + entityName + "Req.Filter filter) {\n");
            writer.write("        " + entityName + " entity = map(filter, " + entityName + ".class);\n");
            writer.write("        Page<" + entityName + "> page = " + repositoryName + ".findAll(Example.of(entity), Pageable.unpaged());\n");
            writer.write("        return mapAll(page.stream().toList(), " + entityName + "Res.Name.class);\n    }\n\n");

            writer.write("    @Transactional\n");
            writer.write("    public void deleteByIds(List<" + pkType + "> ids) {\n");
            writer.write("        " + repositoryName + ".findByIdIn(ids).forEach(" + entityName + "::delete);\n    }\n}");

            System.out.println("✔ Service 생성 완료: " + filePath);
        } catch (Exception e) {
            throw new RuntimeException("Service 생성 실패", e);
        }
    }

    private String extractContext(String tableName) {
        int idx = tableName.indexOf("_");
        return (idx != -1) ? tableName.substring(0, idx) : "common";
    }

    private String toPascalCase(String text) {
        int idx = text.indexOf("_");
        String trimmed = (idx != -1) ? text.substring(idx + 1) : text;
        String[] parts = trimmed.split("_");
        StringBuilder sb = new StringBuilder();
        for (String p : parts)
            sb.append(p.substring(0, 1).toUpperCase()).append(p.substring(1));
        return sb.toString();
    }

    private String toCamelCase(String text) {
        String pascal = toPascalCase(text);
        return pascal.substring(0, 1).toLowerCase() + pascal.substring(1);
    }

    private String getPkType(TableMetadata table) {
        return table.columns().stream()
                .filter(ColumnMetadata::primaryKey)
                .findFirst()
                .map(col -> switch (col.dataType().toLowerCase()) {
                    case "int", "smallint", "mediumint" -> "Integer";
                    case "bigint" -> "Long";
                    default -> "String";
                }).orElse("String");
    }

    private FileWriter prepareFile(String path) throws Exception {
        File file = new File(path);
        file.getParentFile().mkdirs();
        return new FileWriter(file);
    }
}
