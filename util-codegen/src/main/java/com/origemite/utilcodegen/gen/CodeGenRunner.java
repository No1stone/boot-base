package com.origemite.utilcodegen.gen;

import com.origemite.utilcodegen.gen.generator.*;
import com.origemite.utilcodegen.gen.metadata.TableMetadata;
import com.origemite.utilcodegen.gen.metadata.TableMetadataReader;

import java.util.Arrays;

public class CodeGenRunner {
    public static void run(String[] args) {
        String tableName = getArgumentValue(args, "--table");
        String schemaName = getArgumentValue(args, "--schemaName");
        String url = getArgumentValue(args, "--url");
        String userName = getArgumentValue(args, "--username");
        String password = getArgumentValue(args, "--password");
        String outputDir = getArgumentValue(args, "--output", "util-codegen/src/main/java/com/origemite/utilcodegen/created/");

        TableMetadata metadata = TableMetadataReader.read(schemaName, tableName,url,userName,password);

        new EntityGenerator().generate(metadata, outputDir);
        new RepositoryGenerator().generate(metadata, outputDir);
        new DtoGenerator().generate(metadata, outputDir);
        new ServiceGenerator().generate(metadata, outputDir);
        new ControllerGenerator().generate(metadata, outputDir);
        new SpecGenerator().generate(metadata, outputDir);
        new ControllerTestGenerator().generate(metadata, outputDir);
        new ServiceTestGenerator().generate(metadata, outputDir);

        System.out.println("✅ 코드 생성 완료!");
    }

    private static String getArgumentValue(String[] args, String key) {
        return Arrays.stream(args)
                .filter(arg -> arg.startsWith(key + "="))
                .map(arg -> arg.split("=")[1])
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Missing argument: " + key));
    }

    private static String getArgumentValue(String[] args, String key, String defaultVal) {
        return Arrays.stream(args)
                .filter(arg -> arg.startsWith(key + "="))
                .map(arg -> arg.split("=")[1])
                .findFirst()
                .orElse(defaultVal);
    }
}
