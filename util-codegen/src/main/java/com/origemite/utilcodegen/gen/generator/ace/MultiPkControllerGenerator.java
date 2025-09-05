package com.origemite.utilcodegen.gen.generator.ace;

import com.origemite.utilcodegen.gen.metadata.ColumnMetadata;
import com.origemite.utilcodegen.gen.metadata.TableMetadata;
import com.origemite.utilcodegen.gen.metadata.TypeResolver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class MultiPkControllerGenerator {

    public void generate(TableMetadata table, String outputDir) {
        String entity = table.className(); // 예: Codeshop
        String schema = table.schemaName(); // provisioning
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String pkClass = entity + "Id";

        List<ColumnMetadata> pkColumns = table.columns().stream().filter(ColumnMetadata::primaryKey).toList();
        boolean isCompositePk = pkColumns.size() >= 2;
        String pkType = isCompositePk ? pkClass : TypeResolver.resolveJavaType(pkColumns.get(0));

        String packageName = "io.diddda.api." + schema + ".controller";
        String entityVar = Character.toLowerCase(entity.charAt(0)) + entity.substring(1);

        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(packageName).append(";\n\n")
                .append("import io.diddda.api.").append(schema).append(".service.").append(entity).append("Service;\n")
                .append("import io.diddda.lib.common.util.CommonResponseUtils;\n")
                .append("import io.diddda.lib.common.web.CommonResponse;\n")
                .append("import io.diddda.lib.model.").append(schema).append(".dto.request.").append(entity).append("Req;\n")
                .append("import io.diddda.lib.model.").append(schema).append(".dto.response.").append(entity).append("Res;\n")
                .append("import io.swagger.v3.oas.annotations.Operation;\n")
                .append("import io.swagger.v3.oas.annotations.Parameter;\n")
                .append("import io.swagger.v3.oas.annotations.tags.Tag;\n")
                .append("import jakarta.validation.Valid;\n")
                .append("import lombok.RequiredArgsConstructor;\n")
                .append("import lombok.extern.slf4j.Slf4j;\n")
                .append("import org.springframework.data.domain.Page;\n")
                .append("import org.springframework.data.domain.Pageable;\n")
                .append("import org.springframework.web.bind.annotation.*;\n")
                .append("import java.util.*;\n\n");

        sb.append("@Tag(name = \"").append(schema).append("/").append(entity.toLowerCase()).append("\", description = \"").append(entity).append(" API\")\n")
                .append("@Slf4j\n")
                .append("@RestController\n")
                .append("@RequiredArgsConstructor\n")
                .append("@RequestMapping(\"/").append(uuid).append("\")\n")
                .append("public class ").append(entity).append("Controller {\n\n")
                .append("    private final ").append(entity).append("Service ").append(entityVar).append("Service;\n\n")

                .append("    @Operation(summary = \"").append(entity).append(" 목록 조회\")\n")
                .append("    @GetMapping\n")
                .append("    public CommonResponse<Page<").append(entity).append("Res.Item>> search(@ModelAttribute @Valid ")
                .append(entity).append("Req.Filter filter, Pageable pageable) {\n")
                .append("        return CommonResponseUtils.responseSuccess(").append(entityVar).append("Service.search(filter, pageable));\n    }\n\n")

                .append("    @Operation(summary = \"").append(entity).append(" 생성\")\n")
                .append("    @PostMapping\n")
                .append("    public CommonResponse<").append(entity).append("Res.Id> save(@RequestBody @Valid ")
                .append(entity).append("Req.Create create) {\n")
                .append("        return CommonResponseUtils.responseSuccess(").append(entityVar).append("Service.save(create));\n    }\n\n")

                .append("    @Operation(summary = \"").append(entity).append(" 단건 조회\")\n")
                .append("    @GetMapping(\"/{id}\")\n")
                .append("    public CommonResponse<").append(entity).append("Res.Item> findById(@Parameter(description = \"ID\") @PathVariable ")
                .append(pkType).append(" id) {\n")
                .append("        return CommonResponseUtils.responseSuccess(").append(entityVar).append("Service.findById(id));\n    }\n\n")

                .append("    @Operation(summary = \"").append(entity).append(" 수정\")\n")
                .append("    @PutMapping(\"/{id}\")\n")
                .append("    public CommonResponse<").append(entity).append("Res.Id> saveById(@Parameter(description = \"ID\") @PathVariable ")
                .append(pkType).append(" id, @RequestBody ").append(entity).append("Req.Update update) {\n")
                .append("        return CommonResponseUtils.responseSuccess(").append(entityVar).append("Service.saveById(id, update));\n    }\n\n")

                .append("    @Operation(summary = \"").append(entity).append(" 삭제\")\n")
                .append("    @DeleteMapping(\"/{id}\")\n")
                .append("    public CommonResponse<?> deleteById(@Parameter(description = \"ID\") @PathVariable ")
                .append(pkType).append(" id) {\n")
                .append("        ").append(entityVar).append("Service.deleteById(id);\n")
                .append("        return CommonResponseUtils.responseSuccess();\n    }\n\n")

                .append("    @Operation(summary = \"").append(entity).append(" 전체 조회\")\n")
                .append("    @GetMapping(\"/find-all\")\n")
                .append("    public CommonResponse<List<").append(entity).append("Res.Item>> findAll(@ModelAttribute ")
                .append(entity).append("Req.Filter filter) {\n")
                .append("        return CommonResponseUtils.responseSuccess(").append(entityVar).append("Service.findAll(filter));\n    }\n\n");

        if (!isCompositePk) {
            sb.append("    @Operation(summary = \"").append(entity).append(" 아이디 선택 조회\")\n")
                    .append("    @GetMapping(\"/find-by-ids\")\n")
                    .append("    public CommonResponse<List<").append(entity).append("Res.Item>> findByIds(@RequestParam @Valid List<")
                    .append(pkType).append("> ids) {\n")
                    .append("        return CommonResponseUtils.responseSuccess(").append(entityVar).append("Service.findByIds(ids));\n    }\n\n")
                    .append("    @Operation(summary = \"").append(entity).append(" 선택 삭제\")\n")
                    .append("    @DeleteMapping(\"/delete-by-ids\")\n")
                    .append("    public CommonResponse<?> deleteByIds(@RequestParam @Valid List<").append(pkType).append("> ids) {\n")
                    .append("        ").append(entityVar).append("Service.deleteByIds(ids);\n")
                    .append("        return CommonResponseUtils.responseSuccess();\n    }\n\n");
        } else {
            sb.append("    @Operation(summary = \"").append(entity).append(" 아이디 선택 조회\")\n")
                    .append("    @PostMapping(\"/find-by-ids\")\n")
                    .append("    public CommonResponse<List<").append(entity).append("Res.Item>> findByIds(@RequestBody @Valid List<")
                    .append(pkType).append("> ids) {\n")
                    .append("        return CommonResponseUtils.responseSuccess(").append(entityVar).append("Service.findByIds(ids));\n    }\n\n")
                    .append("    @Operation(summary = \"").append(entity).append(" 선택 삭제\")\n")
                    .append("    @DeleteMapping(\"/delete-by-ids\")\n")
                    .append("    public CommonResponse<?> deleteByIds(@RequestBody @Valid List<").append(pkType).append("> ids) {\n")
                    .append("        ").append(entityVar).append("Service.deleteByIds(ids);\n")
                    .append("        return CommonResponseUtils.responseSuccess();\n    }\n\n");
        }

        sb.append("    @Operation(summary = \"").append(entity).append(" 이름 페이지 조회\")\n")
                .append("    @GetMapping(\"/search-name\")\n")
                .append("    public CommonResponse<Page<").append(entity).append("Res.Name>> searchName(@ModelAttribute ")
                .append(entity).append("Req.Filter filter, Pageable pageable) {\n")
                .append("        return CommonResponseUtils.responseSuccess(").append(entityVar).append("Service.searchName(filter, pageable));\n    }\n\n")

                .append("    @Operation(summary = \"").append(entity).append(" 이름 전체 조회\")\n")
                .append("    @GetMapping(\"/find-all-name\")\n")
                .append("    public CommonResponse<List<").append(entity).append("Res.Name>> findAllName(@ModelAttribute ")
                .append(entity).append("Req.Filter filter) {\n")
                .append("        return CommonResponseUtils.responseSuccess(").append(entityVar).append("Service.findAllName(filter));\n    }\n");

        sb.append("}\n");

        try {
            File dir = new File(outputDir + "/controller");
            dir.mkdirs();
            File file = new File(dir, entity + "Controller.java");
            try (FileWriter fw = new FileWriter(file)) {
                fw.write(sb.toString());
            }
            System.out.println("✅ Controller 생성 완료: " + file.getPath());
        } catch (IOException e) {
            throw new RuntimeException("컨트롤러 파일 생성 실패", e);
        }
    }
}
