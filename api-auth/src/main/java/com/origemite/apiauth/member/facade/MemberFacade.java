package com.origemite.apiauth.member.facade;


import com.origemite.apiauth.support.security.provider.JwtTokenAuthenticationProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberFacade {

    private final HttpServletRequest request;
    private final JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider;

    @Transactional
    public void login(){}


    @Transactional
    public void refreshToken(){}


    @Transactional
    public void create(){}


    @Transactional
    public void update(){}

}
