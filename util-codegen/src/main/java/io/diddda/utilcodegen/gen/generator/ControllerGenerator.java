package io.diddda.utilcodegen.gen.generator;


import io.diddda.utilcodegen.gen.metadata.ColumnMetadata;
import io.diddda.utilcodegen.gen.metadata.TableMetadata;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.UUID;

public class ControllerGenerator {

    public void generate(TableMetadata table, String path) {
        String entityName = toPascalCase(table.tableName());
        String context = extractContext(table.tableName());
        String pkType = getPkType(table);
        String requestMapping = "/" + UUID.randomUUID(); // 사용자 문서에서 직접 수정

        String filePath = Paths.get(path, context, "controller", entityName + "Controller.java").toString();

        try (FileWriter writer = prepareFile(filePath)) {
            writer.write("package io.diddda.api." + context + ".controller;\n\n");
            writer.write("import io.diddda.api." + context + ".dto." + entityName + "Req;\n");
            writer.write("import io.diddda.api." + context + ".dto." + entityName + "Res;\n");
            writer.write("import io.diddda.api." + context + ".service." + entityName + "Service;\n");
            writer.write("import io.diddda.lib.common.util.CommonResponseUtils;\n");
            writer.write("import io.diddda.lib.common.web.CommonResponse;\n");
            writer.write("import io.swagger.v3.oas.annotations.Operation;\n");
            writer.write("import io.swagger.v3.oas.annotations.Parameter;\n");
            writer.write("import io.swagger.v3.oas.annotations.tags.Tag;\n");
            writer.write("import jakarta.validation.Valid;\n");
            writer.write("import lombok.RequiredArgsConstructor;\n");
            writer.write("import lombok.extern.slf4j.Slf4j;\n");
            writer.write("import org.springframework.data.domain.*;\n");
            writer.write("import org.springframework.web.bind.annotation.*;\n");
            writer.write("import java.util.*;\n\n");

            writer.write("@Tag(name = \"" + requestMapping.substring(1) + "\", description = \"" + entityName + "\")\n");
            writer.write("@Slf4j\n@RestController\n@RequiredArgsConstructor\n@RequestMapping(\"" + requestMapping + "\")\n");
            writer.write("public class " + entityName + "Controller {\n\n");
            writer.write("    private final " + entityName + "Service " + toCamelCase(entityName) + "Service;\n\n");

            writer.write("    @Operation(summary = \"목록 조회\")\n");
            writer.write("    @GetMapping\n");
            writer.write("    public CommonResponse<Page<" + entityName + "Res.Item>> search(@ModelAttribute @Valid " + entityName + "Req.Filter filter, Pageable pageable) {\n");
            writer.write("        return CommonResponseUtils.responseSuccess(" + toCamelCase(entityName) + "Service.search(filter, pageable));\n    }\n\n");

            writer.write("    @Operation(summary = \"생성\")\n");
            writer.write("    @PostMapping\n");
            writer.write("    public CommonResponse<" + entityName + "Res.Id> save(@RequestBody @Valid " + entityName + "Req.Create create) {\n");
            writer.write("        return CommonResponseUtils.responseSuccess(" + toCamelCase(entityName) + "Service.save(create));\n    }\n\n");

            writer.write("    @Operation(summary = \"단건 조회\")\n");
            writer.write("    @GetMapping(\"/{id}\")\n");
            writer.write("    public CommonResponse<" + entityName + "Res.Item> findById(@Parameter(description = \"ID\") @PathVariable(name = \"id\") " + pkType + " id) {\n");
            writer.write("        return CommonResponseUtils.responseSuccess(" + toCamelCase(entityName) + "Service.findById(id));\n    }\n\n");

            writer.write("    @Operation(summary = \"수정\")\n");
            writer.write("    @PutMapping(\"/{id}\")\n");
            writer.write("    public CommonResponse<" + entityName + "Res.Id> saveById(@Parameter(description = \"ID\") @PathVariable(name = \"id\") " + pkType + " id, @RequestBody " + entityName + "Req.Update update) {\n");
            writer.write("        return CommonResponseUtils.responseSuccess(" + toCamelCase(entityName) + "Service.saveById(id, update));\n    }\n\n");

            writer.write("    @Operation(summary = \"삭제\")\n");
            writer.write("    @DeleteMapping(\"/{id}\")\n");
            writer.write("    public CommonResponse<?> deleteById(@Parameter(description = \"ID\") @PathVariable(name = \"id\") " + pkType + " id) {\n");
            writer.write("        " + toCamelCase(entityName) + "Service.deleteById(id);\n        return CommonResponseUtils.responseSuccess();\n    }\n\n");

            writer.write("    @Operation(summary = \"전체 조회\")\n");
            writer.write("    @GetMapping(\"/find-all\")\n");
            writer.write("    public CommonResponse<List<" + entityName + "Res.Item>> findAll(@ModelAttribute " + entityName + "Req.Filter filter) {\n");
            writer.write("        return CommonResponseUtils.responseSuccess(" + toCamelCase(entityName) + "Service.findAll(filter));\n    }\n\n");

            writer.write("    @Operation(summary = \"아이디 선택 조회\")\n");
            writer.write("    @GetMapping(\"/find-by-ids\")\n");
            writer.write("    public CommonResponse<List<" + entityName + "Res.Item>> findByIds(@RequestParam @Valid List<" + pkType + "> ids) {\n");
            writer.write("        return CommonResponseUtils.responseSuccess(" + toCamelCase(entityName) + "Service.findByIds(ids));\n    }\n\n");

            writer.write("    @Operation(summary = \"이름 검색\")\n");
            writer.write("    @GetMapping(\"/search-name\")\n");
            writer.write("    public CommonResponse<Page<" + entityName + "Res.Name>> searchName(@ModelAttribute @Valid " + entityName + "Req.Filter filter, Pageable pageable) {\n");
            writer.write("        return CommonResponseUtils.responseSuccess(" + toCamelCase(entityName) + "Service.searchName(filter, pageable));\n    }\n\n");

            writer.write("    @Operation(summary = \"이름 전체 조회\")\n");
            writer.write("    @GetMapping(\"/find-all-name\")\n");
            writer.write("    public CommonResponse<List<" + entityName + "Res.Name>> findAllName(@ModelAttribute @Valid " + entityName + "Req.Filter filter) {\n");
            writer.write("        return CommonResponseUtils.responseSuccess(" + toCamelCase(entityName) + "Service.findAllName(filter));\n    }\n\n");

            writer.write("    @Operation(summary = \"아이디 선택 삭제\")\n");
            writer.write("    @DeleteMapping(\"/delete-by-ids\")\n");
            writer.write("    public CommonResponse<?> deleteByIds(@RequestParam @Valid List<" + pkType + "> ids) {\n");
            writer.write("        " + toCamelCase(entityName) + "Service.deleteByIds(ids);\n        return CommonResponseUtils.responseSuccess();\n    }\n}");

            System.out.println("✔ Controller 생성 완료: " + filePath);
        } catch (Exception e) {
            throw new RuntimeException("Controller 생성 실패", e);
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
