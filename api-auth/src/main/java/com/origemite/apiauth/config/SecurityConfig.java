package com.origemite.apiauth.config;

import com.origemite.apiauth.support.security.JwtAuthenticationEntryPoint;
import com.origemite.apiauth.support.security.filter.JwtTokenAuthenticationFilter;
import com.origemite.apiauth.support.security.handler.JwtAccessDeniedHandler;
import com.origemite.apiauth.support.security.provider.JwtTokenAuthenticationProvider;
import com.origemite.lib.common.filter.ExceptionHandlerFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
                                "/member/api-safe",
                                "/oauth/login",
                                "/oauth/index",
                                "/oauth/me"
                        )

                        .permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/oauth/login") // 커스텀 로그인 페이지(선택)
//                        .defaultSuccessUrl("/oauth/index", true)
                                .successHandler((request, response, authentication) -> {
                                    var p = (authentication.getPrincipal() instanceof org.springframework.security.oauth2.core.oidc.user.OidcUser oidc)
                                            ? oidc.getClaims() :
                                            ((org.springframework.security.oauth2.core.user.OAuth2User) authentication.getPrincipal()).getAttributes();

                                    String sub = String.valueOf(p.get("sub"));
                                    String email = String.valueOf(p.getOrDefault("email",""));
                                    String name = String.valueOf(p.getOrDefault("name",""));
                                    String picture = String.valueOf(p.getOrDefault("picture",""));

                                    log.info("sub={}, email={}, name={}, picture={}", sub, email, name, picture);


                                    String json = String.format("{\"name\":\"%s\",\"email\":\"%s\",\"picture\":\"%s\",\"sub\":\"%s\"}",
                                            esc(name), esc(email), esc(picture), esc(sub));
                                    String b64 = java.util.Base64.getUrlEncoder().withoutPadding()
                                            .encodeToString(json.getBytes(java.nio.charset.StandardCharsets.UTF_8));
                                    response.setCharacterEncoding("UTF-8");
                                    response.setContentType("application/json; charset=UTF-8");
                                    addCookie(response, "UI_USER", b64, false, 300); // 5분
                                    response.sendRedirect(request.getContextPath() + "/oauth/index");
                                })
                        // 실패 시 이동
                        .failureUrl("/oauth/login?error")
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/oauth/logout").permitAll()
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

    private static void addCookie(jakarta.servlet.http.HttpServletResponse res,
                                  String name, String val, boolean httpOnly, int maxAge) {
        var c = new jakarta.servlet.http.Cookie(name, val);
        c.setPath("/");
        c.setMaxAge(maxAge);
        c.setHttpOnly(httpOnly); // 화면에서 읽어야 하니 false
        // 배포 시 HTTPS면:
        // c.setSecure(true);
        // SameSite=None은 서버/프록시에서 Set-Cookie 헤더로 설정
        res.addCookie(c);
    }
    private static String esc(String s){ return s==null?"":s.replace("\\","\\\\").replace("\"","\\\""); }

}
