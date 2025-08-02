package io.origemite.utilcodegen.gen;

import io.diddda.utilcodegen.gen.generator.MultiPkControllerGenerator;
import io.diddda.utilcodegen.gen.generator.ace.*;
import io.origemite.utilcodegen.gen.generator.ace.*;
import io.resumt.utilcodegen.gen.generator.ace.*;
import io.origemite.utilcodegen.gen.metadata.TableMetadata;
import io.origemite.utilcodegen.gen.metadata.TableMetadataReader;

import java.util.Arrays;

public class MultiPkCodeGenRunner {

    public static void run(String[] args) {
        // 1. 파라미터 추출
        String tableName = getArgumentValue(args, "--table");
        String schemaName = getArgumentValue(args, "--schemaName");
        String url = getArgumentValue(args, "--url");
        String userName = getArgumentValue(args, "--username");
        String password = getArgumentValue(args, "--password");
        String outputDir = getArgumentValue(args, "--output", "util-codegen/src/main/java/io/diddda/utilcodegen/created/");

        // 2. 메타데이터 로딩
        TableMetadata metadata = TableMetadataReader.read(schemaName, tableName, url, userName, password);

        // 3. PK 개수 체크
        long pkCount = metadata.columns().stream()
                .filter(c -> Boolean.TRUE.equals(c.primaryKey()))
                .count();

        if (pkCount == 0) {
            System.out.println("❌ PK 없는 테이블은 무시됨: " + tableName);
            return;
        }

// 단일/복합 관계없이 같은 제너레이터 사용
        if (pkCount >= 2) {
            new IdClassGenerator().generate(metadata, outputDir); // ✅ 복합키일 때만
        }

        // 4. 코드 생성기 실행 (복합키용)
        new MultiPkEntityGenerator().generate(metadata, outputDir);
        new MultiPkDtoGenerator().generate(metadata, outputDir);
        new MultiPkRepositoryGenerator().generate(metadata, outputDir);
        new MultiPkServiceGenerator().generate(metadata, outputDir);
        new MultiPkControllerGenerator().generate(metadata, outputDir);
        new MultiPkSpecGenerator().generate(metadata, outputDir);
        System.out.println("✅ 복합키 코드 생성 완료!");
    }

    private static String getArgumentValue(String[] args, String key) {
        return Arrays.stream(args)
                .filter(arg -> arg.startsWith(key + "="))
                .map(arg -> arg.split("=", 2)[1])
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Missing argument: " + key));
    }

    private static String getArgumentValue(String[] args, String key, String defaultVal) {
        return Arrays.stream(args)
                .filter(arg -> arg.startsWith(key + "="))
                .map(arg -> arg.split("=", 2)[1])
                .findFirst()
                .orElse(defaultVal);
    }
}
