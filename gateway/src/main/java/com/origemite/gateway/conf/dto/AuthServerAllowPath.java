package com.origemite.gateway.conf.dto;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AuthServerAllowPath {
    //나중에 Spring Config server 사용하면 properties 로 변경가능..
    public static final Set<String> ALLOWED_PATHS = new HashSet<>(Arrays.asList(
            "/auth/api/v1/token/**",
            "/auth/api/v1/test/**",
            "/auth/swagger-ui/**",
            "/auth/swagger-resources/**",
            "/auth/favicon.ico",
            "/auth/v3/api-docs/**",
            "/auth/test1/**"
    ));
}
