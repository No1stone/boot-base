package io.origemite.utilcodegen.gen.generator.ace;

import io.origemite.utilcodegen.gen.metadata.ColumnMetadata;
import io.origemite.utilcodegen.gen.metadata.TableMetadata;
import io.origemite.utilcodegen.gen.metadata.TypeResolver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MultiPkServiceGenerator {

    public void generate(TableMetadata table, String outputDir) {
        String entity = table.className(); // 예: Codeshop
        String schema = table.schemaName(); // 예: provisioning
        String repoVar = Character.toLowerCase(entity.charAt(0)) + entity.substring(1) + "Repository";
        String repoName = entity + "Repository";
        String idClass = entity + "Id";

        List<ColumnMetadata> pkColumns = table.columns().stream().filter(ColumnMetadata::primaryKey).toList();
        boolean isCompositePk = pkColumns.size() >= 2;
        String pkType = isCompositePk ? idClass : TypeResolver.resolveJavaType(pkColumns.get(0));

        String packageName = "io.diddda.api." + schema + ".service";

        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(packageName).append(";\n\n")
                .append("import io.diddda.api.").append(schema).append(".entity.").append(entity).append(";\n")
                .append("import io.diddda.api.").append(schema).append(".repository.").append(repoName).append(";\n")
                .append("import io.diddda.api.").append(schema).append(".repository.spec.").append(entity).append("Specs;\n")
                .append("import io.diddda.lib.common.exception.BizErrorException;\n")
                .append("import io.diddda.lib.common.util.BeanUtils;\n")
                .append("import io.diddda.lib.common.web.ResponseType;\n")
                .append("import io.diddda.lib.common.util.ModelMapperUtil;\n")
                .append("import lombok.RequiredArgsConstructor;\n")
                .append("import lombok.extern.slf4j.Slf4j;\n")
                .append("import org.springframework.data.domain.*;\n")
                .append("import org.springframework.stereotype.Service;\n")
                .append("import org.springframework.transaction.annotation.Transactional;\n")
                .append("import java.util.*;\n\n")

                .append("import static io.diddda.lib.common.util.ModelMapperUtil.map;\n")
                .append("import static io.diddda.lib.common.util.ModelMapperUtil.mapAll;\n")
                .append("import static io.diddda.lib.common.util.ModelMapperUtil.*;\n");

        sb.append("@Slf4j\n")
                .append("@Service\n")
                .append("@RequiredArgsConstructor\n")
                .append("@Transactional(readOnly = true)\n")
                .append("public class ").append(entity).append("Service {\n\n")
                .append("    private final ").append(repoName).append(" ").append(repoVar).append(";\n\n")

                // search
                .append("    public Page<").append(entity).append("Res.Item> search(").append(entity).append("Req.Filter filter, Pageable pageable) {\n")
                .append("        ").append(entity).append(" entity = map(filter, ").append(entity).append(".class);\n")
                .append("        Page<").append(entity).append("> page = ").append(repoVar).append(".findAll(").append(entity).append("Specs.of(filter), pageable);\n")
                .append("        return mapAll(page, ").append(entity).append("Res.Item.class);\n")
                .append("    }\n\n")

                // save
                .append("    @Transactional\n")
                .append("    public ").append(entity).append("Res.Id save(").append(entity).append("Req.Create create) {\n")
                .append("        ").append(entity).append(" entity = map(create, ").append(entity).append(".class);\n")
                .append("        entity = ").append(repoVar).append(".save(entity);\n")
                .append("        return map(entity, ").append(entity).append("Res.Id.class);\n")
                .append("    }\n\n")

                // findById
                .append("    public ").append(entity).append("Res.Item findById(").append(pkType).append(" id) {\n")
                .append("        ").append(entity).append(" entity = ").append(repoVar).append(".findById(id)\n")
                .append("                .orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));\n")
                .append("        return map(entity, ").append(entity).append("Res.Item.class);\n")
                .append("    }\n\n")

                // saveById
                .append("    @Transactional\n")
                .append("    public ").append(entity).append("Res.Id saveById(").append(pkType).append(" id, ").append(entity).append("Req.Update update) {\n")
                .append("        ").append(entity).append(" entity = ").append(repoVar).append(".findById(id)\n")
                .append("                .orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));\n")
                .append("        BeanUtils.map(update, entity);\n")
                .append("        return map(entity, ").append(entity).append("Res.Id.class);\n")
                .append("    }\n\n")

                // deleteById
                .append("    @Transactional\n")
                .append("    public void deleteById(").append(pkType).append(" id) {\n")
                .append("        ").append(entity).append(" entity = ").append(repoVar).append(".findById(id)\n")
                .append("                .orElseThrow(() -> new BizErrorException(ResponseType.UNDEFINED));\n")
                .append("        entity.delete();\n")
                .append("    }\n\n")

                // findAll
                .append("    public List<").append(entity).append("Res.Item> findAll(").append(entity).append("Req.Filter filter) {\n")
                .append("        return search(filter, Pageable.unpaged()).getContent();\n")
                .append("    }\n\n")

                // findByIds
                .append("    public List<").append(entity).append("Res.Item> findByIds(List<").append(pkType).append("> ids) {\n")
                .append("        List<").append(entity).append("> list = ").append(repoVar).append(".findByIdIn(ids);\n")
                .append("        return mapAll(list, ").append(entity).append("Res.Item.class);\n")
                .append("    }\n\n")

                // searchName
                .append("    public Page<").append(entity).append("Res.Name> searchName(").append(entity).append("Req.Filter filter, Pageable pageable) {\n")
                .append("        Page<").append(entity).append("> page = ").append(repoVar).append(".findAll(").append(entity).append("Specs.of(filter), pageable);\n")
                .append("        return mapAll(page, ").append(entity).append("Res.Name.class);\n")
                .append("    }\n\n")

                // findAllName
                .append("    public List<").append(entity).append("Res.Name> findAllName(").append(entity).append("Req.Filter filter) {\n")
                .append("        Page<").append(entity).append("> page = ").append(repoVar).append(".findAll(").append(entity).append("Specs.of(filter), Pageable.unpaged());\n")
                .append("        return mapAll(page.stream().toList(), ").append(entity).append("Res.Name.class);\n")
                .append("    }\n\n")

                // deleteByIds
                .append("    @Transactional\n")
                .append("    public void deleteByIds(List<").append(pkType).append("> ids) {\n")
                .append("        ").append(repoVar).append(".findByIdIn(ids).forEach(").append(entity).append("::delete);\n")
                .append("    }\n");

        sb.append("}\n");

        try {
            File dir = new File(outputDir + "/service");
            dir.mkdirs();
            File file = new File(dir, entity + "Service.java");
            try (FileWriter fw = new FileWriter(file)) {
                fw.write(sb.toString());
            }
            System.out.println("✅ Service 클래스 생성 완료: " + file.getPath());
        } catch (IOException e) {
            throw new RuntimeException("서비스 클래스 파일 생성 실패", e);
        }
    }
}
