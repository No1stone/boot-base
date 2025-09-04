package com.origemite.gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigFilter {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public ConfigFilter(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    //https://spring.io/projects/spring-cloud-gateway
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth", r -> r.path("/auth/**")
                        .filters(
                                f   -> f
                                        .filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config()))
                                        .rewritePath("/auth/(?<segment>.*)", "/${segment}")
                        )
                        .uri("lb://auth"))
                .build();
    }
//                                .rewritePath("/internal/(?<segment>.*)", "/${segment}")
}