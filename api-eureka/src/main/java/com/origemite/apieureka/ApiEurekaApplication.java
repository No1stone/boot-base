package com.origemite.apieureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@EnableConfigurationProperties
@SpringBootApplication(
        scanBasePackages = {
                "com.origemite.apieureka",
        },
        exclude = {SecurityAutoConfiguration.class})
@EnableEurekaServer
public class ApiEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiEurekaApplication.class, args);
    }

}
