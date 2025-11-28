package com.origemite.graphqldgs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@Slf4j
@EnableConfigurationProperties
@SpringBootApplication(
        scanBasePackages = {
                "com.origemite.graphqldgs",
                //시큐리티 부분 의존성걸려서 우선제외
//                "com.origemite.lib.common",
//                "com.origemite.lib.model",
        },
        exclude = {SecurityAutoConfiguration.class})
public class GraphqlDgsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphqlDgsApplication.class, args);
    }

}
