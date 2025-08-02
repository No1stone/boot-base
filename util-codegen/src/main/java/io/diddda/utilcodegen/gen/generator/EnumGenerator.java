package io.diddda.utilcodegen.gen.generator;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;

public class EnumGenerator {

    public void generate(String enumClassName, String outputDirIgnored) {
        String filePath = Paths.get(outputDirIgnored, "enums", enumClassName + ".java").toString();

        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("package io.diddda.lib.enums;\n\n");
                writer.write("import io.diddda.lib.common.enums.CodeValue;\n");
                writer.write("import io.diddda.lib.common.exception.BizErrorException;\n");
                writer.write("import io.diddda.lib.common.processor.annotation.BizEnum;\n");
                writer.write("import io.diddda.lib.common.web.ResponseType;\n");
                writer.write("import lombok.AccessLevel;\n");
                writer.write("import lombok.RequiredArgsConstructor;\n");
                writer.write("import lombok.experimental.FieldDefaults;\n\n");
                writer.write("import java.util.Arrays;\n");
                writer.write("import java.util.stream.Collectors;\n\n");

                writer.write("@RequiredArgsConstructor\n");
                writer.write("@FieldDefaults(level = AccessLevel.PRIVATE)\n");
                writer.write("@BizEnum\n");
                writer.write("public enum " + enumClassName + " implements CodeValue {\n\n");

                writer.write("    // TODO: 항목 채우기\n");
                writer.write("    SampleA(\"\", \"\"),\n");
                writer.write("    SampleB(\"\", \"\"),\n");
                writer.write("    SampleC(\"\", \"\"),\n");
                writer.write("    ;\n\n");

                writer.write("    final String code;\n");
                writer.write("    final String value;\n\n");

                writer.write("    @Override\n");
                writer.write("    public String getCode() { return code; }\n\n");

                writer.write("    @Override\n");
                writer.write("    public String getValue() { return value; }\n\n");

                writer.write("    public static " + enumClassName + " of(String code) {\n");
                writer.write("        return Arrays.stream(" + enumClassName + ".values())\n");
                writer.write("                .filter(v -> v.getCode().equals(code))\n");
                writer.write("                .findAny()\n");
                writer.write("                .orElseThrow(() ->\n");
                writer.write("                        new BizErrorException(ResponseType.INVALID_ENUM_VALUE)\n");
                writer.write("                                .addData(\"code\", code)\n");
                writer.write("                                .addData(\"values\", Arrays.stream(values()).collect(Collectors.toMap(CodeValue::getCode, CodeValue::getValue)))\n");
                writer.write("                );\n");
                writer.write("    }\n\n");

                writer.write("    public static " + enumClassName + " ofNullable(String code, " + enumClassName + "... allowStatus) {\n");
                writer.write("        if (code == null) return null;\n");
                writer.write("        if (allowStatus != null && allowStatus.length > 0) {\n");
                writer.write("            if (Arrays.stream(allowStatus).noneMatch(v -> v.getCode().equals(code))) {\n");
                writer.write("                throw new BizErrorException(ResponseType.NOT_ALLOWED_ENUM_VALUE)\n");
                writer.write("                        .addData(\"code\", code)\n");
                writer.write("                        .addData(\"values\", Arrays.stream(allowStatus).collect(Collectors.toMap(CodeValue::getCode, CodeValue::getValue)));\n");
                writer.write("            }\n");
                writer.write("        }\n");
                writer.write("        return of(code);\n");
                writer.write("    }\n");

                writer.write("}\n");
            }

            System.out.println("✔ Enum 생성 완료: " + file.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("Enum 생성 실패", e);
        }
    }
}
