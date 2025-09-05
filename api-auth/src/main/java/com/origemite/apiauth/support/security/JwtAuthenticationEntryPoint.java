package com.origemite.apiauth.support.security;

import io.origemite.lib.common.exception.BizErrorException;
import io.origemite.lib.common.web.ResponseType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
//        System.out.println("JwtAuthenticationEntryPoint::::::::::::::::");
//        log.error("Not authentication Request:" + authException);
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Jwt authentication failed");

        throw new BizErrorException(ResponseType.UNAUTHORIZED);
    }
}
