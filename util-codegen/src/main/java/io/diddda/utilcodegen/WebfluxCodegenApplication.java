package io.diddda.utilcodegen;

import io.diddda.utilcodegen.gen.SwaggerRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebfluxCodegenApplication {

    public static void main(String[] args) {
        String[] localSwagger = {
                "http://localhost:3041/v3/api-docs"
//                "https://shared-api.diddda.io/v3/api-docs"
        };
        SwaggerRunner.run(localSwagger);
    }

}
