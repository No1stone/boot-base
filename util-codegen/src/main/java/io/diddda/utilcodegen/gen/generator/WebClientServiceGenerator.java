package io.diddda.utilcodegen.gen.generator;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.parser.OpenAPIV3Parser;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

@Slf4j
public class WebClientServiceGenerator {

    public static void generateServiceFromSwagger(String swaggerUrl, String outputDir) {
        OpenAPI openAPI = new OpenAPIV3Parser().read(swaggerUrl);
        if (openAPI == null || openAPI.getPaths() == null) {
            log.error("❌ OpenAPI 문서 파싱 실패: {}", swaggerUrl);
            return;
        }

        Map<String, List<Endpoint>> tagToEndpoints = new HashMap<>();

        Paths paths = openAPI.getPaths();
        for (Map.Entry<String, PathItem> pathEntry : paths.entrySet()) {
            String path = pathEntry.getKey();
            PathItem pathItem = pathEntry.getValue();

            for (Map.Entry<PathItem.HttpMethod, Operation> opEntry : pathItem.readOperationsMap().entrySet()) {
                PathItem.HttpMethod method = opEntry.getKey();
                Operation operation = opEntry.getValue();

                String tag = operation.getTags() != null && !operation.getTags().isEmpty() ? operation.getTags().get(0) : "Default";
                tagToEndpoints.computeIfAbsent(tag, k -> new ArrayList<>()).add(new Endpoint(method, operation, path));
            }
        }

        tagToEndpoints.forEach((tag, endpoints) -> {
            String className = toPascalCase(tag) + "Client";
            StringBuilder sb = new StringBuilder();
            sb.append("package io.diddda.utilcodegen.created.webflux;\n\n")
                    .append("import lombok.RequiredArgsConstructor;\n")
                    .append("import lombok.extern.slf4j.Slf4j;\n")
                    .append("import org.springframework.http.ResponseEntity;\n")
                    .append("import org.springframework.stereotype.Service;\n")
                    .append("import org.springframework.web.bind.annotation.RequestHeader;\n")
                    .append("import org.springframework.web.reactive.function.BodyInserters;\n")
                    .append("import org.springframework.web.reactive.function.client.WebClient;\n")
                    .append("import org.springframework.core.ParameterizedTypeReference;\n")
                    .append("import reactor.core.publisher.Mono;\n")
                    .append("import java.util.Map;\n")
                    .append("import java.util.HashMap;\n")
                    .append("@Service\n")
                    .append("@Slf4j\n")
                    .append("@RequiredArgsConstructor\n")
                    .append("public class ").append(className).append(" {\n\n")
                    .append("    private final WebClient.Builder webClientBuilder;\n")
                    .append("    private final static String BASE_URL = \"https://reqres.in\";\n\n");

            for (Endpoint endpoint : endpoints) {
                String methodName = toCamelCase(endpoint.operation.getOperationId() != null ? endpoint.operation.getOperationId() : endpoint.path);
                String httpMethod = endpoint.method.name().toLowerCase();

                boolean hasRequestBody = endpoint.operation.getRequestBody() != null;

                sb.append("    public Mono<Map<String, Object>> ").append(methodName)
                        .append("(@RequestHeader(\"Authorization\") String token");

                if (httpMethod.equals("get") || httpMethod.equals("delete")) {
                    sb.append(", Map<String, Object> params");
                } else if (hasRequestBody) {
                    sb.append(", Map<String, Object> body, Map<String, Object> params");
                }

                sb.append(") {\n")
                        .append("        return webClientBuilder.baseUrl(BASE_URL).build()\n")
                        .append("                .").append(httpMethod).append("()\n")
                        .append("                .uri(uriBuilder -> {\n")
                        .append("                    String path = \"").append(endpoint.path).append("\";\n")
                        .append("                    Map<String, Object> pathVars = new HashMap<>();\n")
                        .append("                    uriBuilder.path(path);\n")
                        .append("                    if (params != null) {\n")
                        .append("                        for (Map.Entry<String, Object> entry : params.entrySet()) {\n")
                        .append("                            if (!path.contains(\"{\" + entry.getKey() + \"}\")) {\n")
                        .append("                                uriBuilder.queryParam(entry.getKey(), entry.getValue());\n")
                        .append("                            } else {\n")
                        .append("                                pathVars.put(entry.getKey(), entry.getValue());\n")
                        .append("                            }\n")
                        .append("                        }\n")
                        .append("                    }\n")
                        .append("                    return uriBuilder.build(pathVars);\n")
                        .append("                })\n")
                        .append("                .header(\"Authorization\", token)\n");

                if (hasRequestBody && !(httpMethod.equals("get") || httpMethod.equals("delete"))) {
                    sb.append("                .body(BodyInserters.fromValue(body))\n");
                }

                sb.append("                .retrieve()\n")
                        .append("                .bodyToMono(new ParameterizedTypeReference<>() {});\n")
                        .append("    }\n\n");
            }

            sb.append("}\n");

            try {
                File file = new File(outputDir + "/" + className + ".java");
                file.getParentFile().mkdirs();
                FileWriter writer = new FileWriter(file);
                writer.write(sb.toString());
                writer.close();
            } catch (Exception e) {
                log.error("❌ WebClient 서비스 파일 생성 실패", e);
            }
        });
    }

    private static String toCamelCase(String input) {
        input = input.replaceAll("[^a-zA-Z0-9]", "_");
        String[] parts = input.split("_");
        StringBuilder camelCaseString = new StringBuilder(parts[0].toLowerCase());
        for (int i = 1; i < parts.length; i++) {
            camelCaseString.append(parts[i].substring(0, 1).toUpperCase()).append(parts[i].substring(1).toLowerCase());
        }
        return camelCaseString.toString();
    }

    private static String toPascalCase(String input) {
        input = input.replaceAll("[^a-zA-Z0-9]", "_");
        String[] parts = input.split("_");
        StringBuilder pascalCaseString = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                pascalCaseString.append(part.substring(0, 1).toUpperCase()).append(part.substring(1).toLowerCase());
            }
        }
        return pascalCaseString.toString();
    }

    private record Endpoint(PathItem.HttpMethod method, Operation operation, String path) {}
}
