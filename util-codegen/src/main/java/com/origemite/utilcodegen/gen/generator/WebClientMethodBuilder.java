package com.origemite.utilcodegen.gen.generator;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.media.Schema;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WebClientMethodBuilder {

    public static String buildMethod(String baseUrl, String path, String httpMethod, Operation operation) {
        String methodName = operation.getOperationId() != null
                ? operation.getOperationId()
                : "call" + path.replaceAll("[{}]", "").replace("/", "_");

        List<Parameter> parameters = operation.getParameters();

        // 파라미터 타입 매핑
        String paramList = (parameters == null ? "" : parameters.stream()
                .map(p -> toJavaType(p.getSchema()) + " " + p.getName())
                .collect(Collectors.joining(", ")))
                + (parameters != null && !parameters.isEmpty() ? ", " : "") + "String jwtToken";

        String queryParams = parameters == null ? "" : parameters.stream()
                .filter(p -> "query".equalsIgnoreCase(p.getIn()))
                .map(p -> ".queryParam(\"" + p.getName() + "\", " + p.getName() + ")")
                .collect(Collectors.joining("\n                    "));

        String pathArgs = parameters == null ? "" : parameters.stream()
                .filter(p -> "path".equalsIgnoreCase(p.getIn()))
                .map(Parameter::getName)
                .collect(Collectors.joining(", "));

        return String.format("""
        public Mono<Object> %s(%s) {
            return WebClient.builder()
                .baseUrl(\"%s\")
                .build()
                .method(HttpMethod.%s)
                .uri(uriBuilder -> uriBuilder
                    .path(\"%s\")
                    %s
                    .build(%s))
                .headers(headers -> headers.setBearerAuth(jwtToken))
                .retrieve()
                .bodyToMono(Object.class);
        }

        """,
                methodName,
                paramList,
                baseUrl,
                httpMethod.toUpperCase(),
                path,
                queryParams,
                pathArgs);
    }

    private static String toJavaType(Schema<?> schema) {
        if (schema == null) return "String"; // 기본값
        String type = schema.getType();
        if (type == null) return "String"; // 방어 코드 추가

        switch (type) {
            case "integer":
                return "Integer";
            case "boolean":
                return "Boolean";
            case "array":
                Schema<?> items = (Schema<?>) schema.getItems();
                return "List<" + toJavaType(items) + ">";
            case "number":
                return "Double";
            case "string":
            default:
                return "String";
        }
    }
}
