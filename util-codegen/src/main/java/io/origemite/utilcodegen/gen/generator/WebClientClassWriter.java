package io.origemite.utilcodegen.gen.generator;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;

public class WebClientClassWriter {

    public static void writeClass(String basePath, String tag, String classBody) {
        String context = tag.contains("-") ? tag.split("-")[0] : "common";
        String className = toPascalCase(tag) + "Client";

        String packagePath = "io.diddda.api." + context + ".client";
        String filePath = Paths.get(basePath, context, "client", className + ".java").toString();

        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();

            try (FileWriter writer = new FileWriter(file)) {
                writer.write("package " + packagePath + ";\n\n");
                writer.write("import lombok.RequiredArgsConstructor;\n");
                writer.write("import org.springframework.stereotype.Component;\n");
                writer.write("import org.springframework.web.reactive.function.client.WebClient;\n");
                writer.write("import reactor.core.publisher.Mono;\n\n");
                writer.write("import org.springframework.http.HttpMethod;\n\n");
                writer.write("import java.util.List;\n\n");
                writer.write("@Component\n@RequiredArgsConstructor\n");
                writer.write("public class " + className + " {\n\n");
                writer.write("    private final WebClient.Builder webClientBuilder;\n");
                writer.write(classBody);
                writer.write("\n}");
            }

        } catch (Exception e) {
            throw new RuntimeException("WebClient 클래스 생성 실패: " + filePath, e);
        }
    }

    private static String toPascalCase(String text) {
        String[] parts = text.replaceAll("[^a-zA-Z0-9]", "_").split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty())
                sb.append(part.substring(0, 1).toUpperCase()).append(part.substring(1).toLowerCase());
        }
        return sb.toString();
    }
}
