package com.origemite.gateway.conf;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .version("v1.0.0")
                .title("GW 문서")
                .description("### 테스트 진행 방법 \n\n" +
                        "1. ATM 등록(로그인) API를 통해 AccessToken을 발급 받는다\n" +
                        "2. 발급된 accessToken을 복사하고, 페이지 우측 상단 Authorize를 클릭하여 Value에 해당 내용을 붙여넣고 Authorize 버튼을 클릭한다.\n" +
                        "3. 로그인이 되었다면 close를 누르고 다른 API를 호출하여 테스트를 진행한다.");
        String jwtSchemeName = "jwtAuth";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP) // HTTP 방식
                        .scheme("Bearer")
                        .bearerFormat("JWT")); // 토큰 형식을 지정하는 임의의 문자(Optional)
        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }
    @Bean
    public OpenApiCustomizer globalHeaderCustomizer() {
        return openApi -> {
            if (openApi.getPaths() != null) {
                openApi.getPaths().values().forEach(pathItem -> {
                    pathItem.readOperations().forEach(operation -> {
                        operation.addParametersItem(createHeader("X-LANGUAGE", "사용 언어", ""));
                        operation.addParametersItem(createHeader("X-PLATFORM", "플랫폼 (e.g. android, ios)", ""));
                        operation.addParametersItem(createHeader("X-UUID", "디바이스 고유 ID", ""));
                        operation.addParametersItem(createHeader("X-CLIENT_IP", "클라이언트 IP", ""));
                        operation.addParametersItem(createHeader("X-APP_VERSION", "앱 버전", ""));
                        operation.addParametersItem(createHeader("X-TRACE-ID", "트레이스 ID", ""));
                        operation.addParametersItem(createHeader("X-USER_AGENT", "User-Agent", ""));
                        operation.addParametersItem(createHeader("X-USER_ID", "유저 ID", ""));
                        operation.addParametersItem(createHeader("X-SERVICE_ID", "서비스 ID", ""));
                    });
                });
            }
        };
    }

    private Parameter createHeader(String name, String description, String defaultValue) {
        return new HeaderParameter()
                .name(name)
                .description(description)
                .required(false)
                .schema(new StringSchema()._default(defaultValue));
    }
}
