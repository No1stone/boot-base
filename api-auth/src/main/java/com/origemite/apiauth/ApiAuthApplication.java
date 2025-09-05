package com.origemite.apiauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@Slf4j
@EnableConfigurationProperties
@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication(
        scanBasePackages = {
                "com.origemite.apiauth",
                "com.origemite.lib.common",
                "com.origemite.lib.model",
//                "io.origemite.lib.legacy"
        },
        exclude = {SecurityAutoConfiguration.class})
public class ApiAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiAuthApplication.class, args);
    }

}
