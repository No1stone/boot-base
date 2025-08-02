package io.origemite.utilcodegen.gen;

import io.origemite.utilcodegen.gen.generator.EnumGenerator;

import java.util.Arrays;

public class EnumGenRenner {

    public static void run(String[] args) {
        String tableName = getArgumentValue(args, "--classname");
        String path = "util-codegen/src/main/java/io/diddda/utilcodegen/created/enums";
        new EnumGenerator().generate(tableName, path);
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
