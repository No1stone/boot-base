package com.origemite.gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigFilter {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${gateway.routes.api-provisioning.url}")
    private String apiProvisioning;
    @Value("${gateway.routes.gw-codeshop.url}")
    private String gwCodeshop;

    public ConfigFilter(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    //https://spring.io/projects/spring-cloud-gateway
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("route-swagger", r -> r
                        .path("/swagger-ui/**")
                        .uri(apiProvisioning))
//                .route("route-find-by-recognition-code", r -> r
//                        .path("/content/codeshop-contents/find-by-recognition-code")
//                        .filters( f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config()))
//                        )
//                        .uri(gwCodeshop))
//                .route("route-content", r -> r
//                        .path("/auth/**")
//                        .filters( f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
//                        .uri(apiProvisioning))
                .build();
    }
//                                .rewritePath("/internal/(?<segment>.*)", "/${segment}")
}