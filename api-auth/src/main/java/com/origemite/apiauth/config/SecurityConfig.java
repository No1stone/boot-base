package com.origemite.apiauth.config;

import com.origemite.apiauth.support.security.JwtAuthenticationEntryPoint;
import com.origemite.apiauth.support.security.filter.JwtTokenAuthenticationFilter;
import com.origemite.apiauth.support.security.handler.JwtAccessDeniedHandler;
import com.origemite.apiauth.support.security.provider.JwtTokenAuthenticationProvider;
import com.origemite.lib.common.filter.ExceptionHandlerFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableMethodSecurity
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter;
    private final JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider;
    //private final AuthorizationFilter authorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))// 세션 사용 x
                .addFilterBefore(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(), JwtTokenAuthenticationFilter.class)
                //.addFilterAfter(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/error",
                                "/actuator/**",
                                "/v3/**",
                                "/swagger-ui/**",
                                "/sample/api/**",
                                "/portal/members/*", // 처음 로그인 시
                                "/portal/login/**", // TODO. GW 토큰 시 수정
                                "/target/**", // TODO. GW 토큰 시 수정
                                "/portal/second-authentications/**", // 처음 로그인 시
                                "/user/**",
                                "/auth/**",
                                "/member/api-safe"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(jwtTokenAuthenticationProvider)
        ;
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // 모든 도메인 허용
        configuration.setAllowedMethods(List.of("*")); // 모든 메소드 허용
        configuration.setAllowedHeaders(List.of("*")); // 모든 헤더 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
