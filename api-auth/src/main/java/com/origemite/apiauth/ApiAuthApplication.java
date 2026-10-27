package com.origemite.apiauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.grpc.autoconfigure.server.security.OAuth2ClientAutoConfiguration;


@Slf4j
@EnableConfigurationProperties
@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication(
        scanBasePackages = {
                "com.origemite.apiauth",
                "com.origemite.lib.common",
                "com.origemite.lib.model",
                "com.origemite.lib.proto"
        },
        // spring-grpc 0.11은 Boot3 OAuth2ClientProperties를 참조 → Boot 4.1 기동 실패
        exclude = {SecurityAutoConfiguration.class, OAuth2ClientAutoConfiguration.class})
public class ApiAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiAuthApplication.class, args);
    }

}
