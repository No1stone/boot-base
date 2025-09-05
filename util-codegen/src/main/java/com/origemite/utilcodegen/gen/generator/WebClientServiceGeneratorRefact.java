package com.origemite.utilcodegen.gen.generator;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.parser.OpenAPIV3Parser;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

@Slf4j
public class WebClientServiceGeneratorRefact {

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

            log.info("tag - - - {}", tag);
            log.info("length - - - {}", tag.split("/").length );
            log.info("toPascalCase - - - {}", tag.split("/").length > 1 ? toPascalCase(tag.split("/")[1]) : toPascalCase(tag));

            String routeKey = "api-" + tag.toLowerCase();     // 예: api-codeshop
            String context = tag.toLowerCase();               // 예: codeshop
            String className = tag.split("/").length > 1 ? toPascalCase(tag.split("/")[1]) : toPascalCase(tag);
            log.info(">> className: {}", className);
            log.info(">> routeKey: {}", routeKey);
            log.info(">> context: {}", context);

            // 여기에 .replace(...) 템플릿 처리 및 파일 생성

            StringBuilder sb = new StringBuilder();
            sb.append("package io.diddda.utilcodegen.created.webflux;\n\n");
            sb.append("\n");
            sb.append("import io.diddda.lib.webflux.common.ApiResponseDto;\n");
            sb.append("import io.diddda.lib.webflux.common.PageResult;\n");
            sb.append("import io.diddda.lib.webflux.config.ReactiveRequestContext;\n");
            sb.append("import lombok.RequiredArgsConstructor;\n");
            sb.append("import lombok.extern.slf4j.Slf4j;\n");
            sb.append("import org.springframework.beans.factory.annotation.Value;\n");
            sb.append("import org.springframework.core.ParameterizedTypeReference;\n");
            sb.append("import org.springframework.data.domain.Page;\n");
            sb.append("import org.springframework.stereotype.Service;\n");
            sb.append("import org.springframework.web.bind.annotation.RequestHeader;\n");
            sb.append("import org.springframework.web.reactive.function.BodyInserters;\n");
            sb.append("import org.springframework.web.reactive.function.client.WebClient;\n");
            sb.append("import reactor.core.publisher.Mono;\n");
            sb.append("\n");
            sb.append("import java.util.HashMap;\n");
            sb.append("import java.util.List;\n");
            sb.append("import java.util.Map;\n");
            sb.append("\n");
            sb.append("@Service\n");
            sb.append("@Slf4j\n");
            sb.append("@RequiredArgsConstructor\n");
            sb.append("public class ${ClassName}Client {\n");
            sb.append("\n");
            sb.append("    private final WebClient.Builder webClientBuilder;\n");
            sb.append("\n");
            sb.append("    @Value(\"${gateway.routes.api-xxxxxxxxxxxxxxxxx.url}\")\n");
            sb.append("    private String BASE_URL;\n");
            sb.append("\n");
            sb.append("    private static String uri = \"/" + UUID.randomUUID().toString() + "\";\n");
            sb.append("\n");

            sb.append("    public Mono<ApiResponseDto<${ClassName}Res.Item>> findbyid1(Integer id) {\n");
            sb.append("        return webClientBuilder.baseUrl(BASE_URL).build()\n");
            sb.append("                .get()\n");
            sb.append("                .uri(uriBuilder -> {\n");
            sb.append("                    String path = uri + \"/{id}\";\n");
            sb.append("                    Map<String, Object> pathVars = new HashMap<>();\n");
            sb.append("                    pathVars.put(\"id\", id);\n");
            sb.append("                    uriBuilder.path(path);\n");
            sb.append("                    return uriBuilder.build(pathVars);\n");
            sb.append("                })\n");
            sb.append("                .header(\"Authorization\", token)\n");
            sb.append("                .retrieve()\n");
            sb.append("                .bodyToMono(new ParameterizedTypeReference<ApiResponseDto<${ClassName}Res.Item>>() {\n");
            sb.append("                });\n");
            sb.append("\n");
            sb.append("    }\n");
            sb.append("\n");
            sb.append("    public Mono<ApiResponseDto<CodeshopRes.Id>> save1(@RequestHeader(\"Authorization\") String token, Map<String, Object> body) {\n");
            sb.append("        return webClientBuilder.baseUrl(BASE_URL).build()\n");
            sb.append("                .post()\n");
            sb.append("                .uri(uriBuilder -> {\n");
            sb.append("                    String path = uri;\n");
            sb.append("                    uriBuilder.path(path);\n");
            sb.append("                    return uriBuilder.build();\n");
            sb.append("                })\n");
            sb.append("                .header(\"Authorization\", token)\n");
            sb.append("                .body(BodyInserters.fromValue(body))\n");
            sb.append("                .retrieve()\n");
            sb.append("                .bodyToMono(new ParameterizedTypeReference<ApiResponseDto<${ClassName}Res.Id>>() {\n");
            sb.append("                });\n");
            sb.append("    }\n");
            sb.append("\n");
            sb.append("    public Mono<ApiResponseDto<${ClassName}Res.Id>> update(@RequestHeader(\"Authorization\") String token, Map<String, Object> body, Integer id) {\n");
            sb.append("        return webClientBuilder.baseUrl(BASE_URL).build()\n");
            sb.append("                .put()\n");
            sb.append("                .uri(uriBuilder -> {\n");
            sb.append("                    String path = uri + \"/{id}\";\n");
            sb.append("                    Map<String, Object> pathVars = new HashMap<>();\n");
            sb.append("                    pathVars.put(\"id\", id);\n");
            sb.append("                    uriBuilder.path(path);\n");
            sb.append("                    return uriBuilder.build(pathVars);\n");
            sb.append("                })\n");
            sb.append("                .header(\"Authorization\", token)\n");
            sb.append("                .body(BodyInserters.fromValue(body))\n");
            sb.append("                .retrieve()\n");
            sb.append("                .bodyToMono(new ParameterizedTypeReference<ApiResponseDto<${ClassName}Res.Id>>() {\n");
            sb.append("                });\n");
            sb.append("    }\n");
            sb.append("\n");
            sb.append("\n");
            sb.append("    public Mono<ApiResponseDto<${ClassName}Res.Id>> deletebyid1(@RequestHeader(\"Authorization\") String token, Integer id) {\n");
            sb.append("        return webClientBuilder.baseUrl(BASE_URL).build()\n");
            sb.append("                .delete()\n");
            sb.append("                .uri(uriBuilder -> {\n");
            sb.append("                    String path = uri + \"/{id}\";\n");
            sb.append("                    Map<String, Object> pathVars = new HashMap<>();\n");
            sb.append("                    pathVars.put(\"id\", id);\n");
            sb.append("                    uriBuilder.path(path);\n");
            sb.append("                    return uriBuilder.build(pathVars);\n");
            sb.append("                })\n");
            sb.append("                .header(\"Authorization\", token)\n");
            sb.append("                .retrieve()\n");
            sb.append("                .bodyToMono(new ParameterizedTypeReference<ApiResponseDto<${ClassName}Res.Id>>() {\n");
            sb.append("                });\n");
            sb.append("    }\n");
            sb.append("\n");
            sb.append("    public Mono<ApiResponseDto<Page<${ClassName}Res.Item>>> search1(@RequestHeader(\"Authorization\") String token, Map<String, Object> params) {\n");
            sb.append("        return webClientBuilder.baseUrl(BASE_URL).build()\n");
            sb.append("                .get()\n");
            sb.append("                .uri(uriBuilder -> {\n");
            sb.append("                    String path = uri;\n");
            sb.append("                    Map<String, Object> pathVars = new HashMap<>();\n");
            sb.append("                    uriBuilder.path(path);\n");
            sb.append("                    if (params != null) {\n");
            sb.append("                        for (Map.Entry<String, Object> entry : params.entrySet()) {\n");
            sb.append("                            if (!path.contains(\"{\" + entry.getKey() + \"}\")) {\n");
            sb.append("                                uriBuilder.queryParam(entry.getKey(), entry.getValue());\n");
            sb.append("                            } else {\n");
            sb.append("                                pathVars.put(entry.getKey(), entry.getValue());\n");
            sb.append("                            }\n");
            sb.append("                        }\n");
            sb.append("                    }\n");
            sb.append("                    return uriBuilder.build(pathVars);\n");
            sb.append("                })\n");
            sb.append("                .header(\"Authorization\", token)\n");
            sb.append("                .retrieve()\n");
            sb.append("                .bodyToMono(new ParameterizedTypeReference<ApiResponseDto<Page<${ClassName}Res.Item>>>() {\n");
            sb.append("                });\n");
            sb.append("    }\n");
            sb.append("\n");
            sb.append("\n");
            sb.append("    public Mono<ApiResponseDto<PageResult<${ClassName}Res.Name>>> searchname1(@RequestHeader(\"Authorization\") String token, Map<String, Object> params) {\n");
            sb.append("        return webClientBuilder.baseUrl(BASE_URL).build()\n");
            sb.append("                .get()\n");
            sb.append("                .uri(uriBuilder -> {\n");
            sb.append("                    String path = uri + \"/search-name\";\n");
            sb.append("                    Map<String, Object> pathVars = new HashMap<>();\n");
            sb.append("                    uriBuilder.path(path);\n");
            sb.append("                    if (params != null) {\n");
            sb.append("                        for (Map.Entry<String, Object> entry : params.entrySet()) {\n");
            sb.append("                            if (!path.contains(\"{\" + entry.getKey() + \"}\")) {\n");
            sb.append("                                uriBuilder.queryParam(entry.getKey(), entry.getValue());\n");
            sb.append("                            } else {\n");
            sb.append("                                pathVars.put(entry.getKey(), entry.getValue());\n");
            sb.append("                            }\n");
            sb.append("                        }\n");
            sb.append("                    }\n");
            sb.append("                    return uriBuilder.build(pathVars);\n");
            sb.append("                })\n");
            sb.append("                .header(\"Authorization\", token)\n");
            sb.append("                .retrieve()\n");
            sb.append("                .bodyToMono(new ParameterizedTypeReference<ApiResponseDto<PageResult<${ClassName}Res.Name>>>() {});\n");
            sb.append("\n");
            sb.append("    }\n");
            sb.append("\n");
            sb.append("    public Mono<ApiResponseDto<List<${ClassName}Res.Item>>> findByIds(@RequestHeader(\"Authorization\") String token, List<Integer> ids) {\n");
            sb.append("        return webClientBuilder.baseUrl(BASE_URL).build()\n");
            sb.append("                .get()\n");
            sb.append("                .uri(uriBuilder -> {\n");
            sb.append("                    uriBuilder.path(uri + \"/find-by-ids\");\n");
            sb.append("                    ids.forEach(id -> uriBuilder.queryParam(\"ids\", id));\n");
            sb.append("                    return uriBuilder.build();\n");
            sb.append("                })\n");
            sb.append("                .header(\"Authorization\", token)\n");
            sb.append("                .retrieve()\n");
            sb.append("                .bodyToMono(new ParameterizedTypeReference<ApiResponseDto<List<${ClassName}Res.Item>>>() {\n");
            sb.append("                });\n");
            sb.append("    }\n");
            sb.append("\n");
            sb.append("    public Mono<ApiResponseDto<List<${ClassName}Res.Item>>> findall1(@RequestHeader(\"Authorization\") String token, Map<String, Object> params) {\n");
            sb.append("        return webClientBuilder.baseUrl(BASE_URL).build()\n");
            sb.append("                .get()\n");
            sb.append("                .uri(uriBuilder -> {\n");
            sb.append("                    String path = uri + \"/find-all\";\n");
            sb.append("                    Map<String, Object> pathVars = new HashMap<>();\n");
            sb.append("                    uriBuilder.path(path);\n");
            sb.append("                    if (params != null) {\n");
            sb.append("                        for (Map.Entry<String, Object> entry : params.entrySet()) {\n");
            sb.append("                            if (!path.contains(\"{\" + entry.getKey() + \"}\")) {\n");
            sb.append("                                uriBuilder.queryParam(entry.getKey(), entry.getValue());\n");
            sb.append("                            } else {\n");
            sb.append("                                pathVars.put(entry.getKey(), entry.getValue());\n");
            sb.append("                            }\n");
            sb.append("                        }\n");
            sb.append("                    }\n");
            sb.append("                    return uriBuilder.build(pathVars);\n");
            sb.append("                })\n");
            sb.append("                .header(\"Authorization\", token)\n");
            sb.append("                .retrieve()\n");
            sb.append("                .bodyToMono(new ParameterizedTypeReference<ApiResponseDto<List<${ClassName}Res.Item>>>() {\n");
            sb.append("                });\n");
            sb.append("    }\n");
            sb.append("\n");
            sb.append("    public Mono<ApiResponseDto<List<${ClassName}Res.Name>>> findallname1(@RequestHeader(\"Authorization\") String token, Map<String, Object> params) {\n");
            sb.append("        return webClientBuilder.baseUrl(BASE_URL).build()\n");
            sb.append("                .get()\n");
            sb.append("                .uri(uriBuilder -> {\n");
            sb.append("                    String path = uri + \"/find-all-name\";\n");
            sb.append("                    Map<String, Object> pathVars = new HashMap<>();\n");
            sb.append("                    uriBuilder.path(path);\n");
            sb.append("                    if (params != null) {\n");
            sb.append("                        for (Map.Entry<String, Object> entry : params.entrySet()) {\n");
            sb.append("                            if (!path.contains(\"{\" + entry.getKey() + \"}\")) {\n");
            sb.append("                                uriBuilder.queryParam(entry.getKey(), entry.getValue());\n");
            sb.append("                            } else {\n");
            sb.append("                                pathVars.put(entry.getKey(), entry.getValue());\n");
            sb.append("                            }\n");
            sb.append("                        }\n");
            sb.append("                    }\n");
            sb.append("                    return uriBuilder.build(pathVars);\n");
            sb.append("                })\n");
            sb.append("                .header(\"Authorization\", token)\n");
            sb.append("                .retrieve()\n");
            sb.append("                .bodyToMono(new ParameterizedTypeReference<ApiResponseDto<List<${ClassName}Res.Name>>>() {\n");
            sb.append("                });\n");
            sb.append("    }\n");
            sb.append("\n");
            sb.append("    public Mono<ApiResponseDto<?>> deletebyids1(@RequestHeader(\"Authorization\") String token, List<Integer> ids) {\n");
            sb.append("        return webClientBuilder.baseUrl(BASE_URL).build()\n");
            sb.append("                .delete()\n");
            sb.append("                .uri(uriBuilder -> {\n");
            sb.append("                    uriBuilder.path(uri + \"/find-by-ids\");\n");
            sb.append("                    ids.forEach(id -> uriBuilder.queryParam(\"ids\", id));\n");
            sb.append("                    return uriBuilder.build();\n");
            sb.append("                })\n");
            sb.append("                .header(\"Authorization\", token)\n");
            sb.append("                .retrieve()\n");
            sb.append("                .bodyToMono(new ParameterizedTypeReference<ApiResponseDto<?>>() {\n");
            sb.append("                });\n");
            sb.append("    }\n");
            sb.append("\n");
            sb.append("}\n");

            sb.toString().replace("${ClassName}", className)
                    .replace("${routeKey}", routeKey)
                    .replace("${context}", context);
            try {
                File file = new File(outputDir + "/" + className + "Client.java");
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

    private record Endpoint(PathItem.HttpMethod method, Operation operation, String path) {
    }

    private String extractResponseType(Endpoint endpoint) {
        // Swagger spec에서 response type을 추출 (예: ApiResponseDto<CodeshopRes.Item>)
        return "ApiResponseDto<" + toPascalCase(endpoint.operation.getResponses().getClass().getName()) + "Res.Item>";
    }

    private String extractMethodParams(Endpoint endpoint) {
        // path, query, body에서 method param 정의
        // 예시: @PathVariable Integer id, @RequestParam String name
        return "Integer id";
    }

    private String extractQueryParams(Endpoint endpoint) {
        // uriBuilder.queryParam("foo", foo) 형식 반환
        return ""; // or 실제 구현
    }

    private String extractPathParams(Endpoint endpoint) {
        // 경로에 치환할 파라미터 문자열만 추출 (예: "id")
        return "id";
    }
}
