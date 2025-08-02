package io.origemite.utilcodegen.gen;

import io.origemite.utilcodegen.gen.generator.WebClientServiceGeneratorRefact;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class SwaggerRunner {

    public static void run(String[] swaggerUrls) {
        String path = "util-codegen/src/main/java/io/diddda/utilcodegen/created/webflux";

        Arrays.stream(swaggerUrls).forEach(url -> {
//            WebClientDtoGenerator.generateDtoFromSwagger(url,path);
//            FilterBasedResponseDtoGenerator.generateDtoFromSwagger(url, path);
//            WebClientServiceGenerator.generateServiceFromSwagger(url, path);
            WebClientServiceGeneratorRefact.generateServiceFromSwagger(url, path);
        });

        log.info("✅ Swagger 기반 코드 생성 완료");
    }
}