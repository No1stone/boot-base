package com.origemite.apiauth.auth.facade;

import com.origemite.apiauth.config.prop.JwtTokenProp;
import com.origemite.apiauth.support.security.provider.JwtTokenAuthenticationProvider;
import com.origemite.lib.common.util.DateUtils;
import com.origemite.lib.common.util.WebUtils;
import com.origemite.lib.model.auth.JwtToken;
import com.origemite.lib.model.auth.LoginReq;
import com.origemite.lib.model.auth.dto.AppMemberLoginHistoryReq;
import com.origemite.lib.model.auth.dto.AppMemberLoginHistoryRes;
import com.origemite.lib.model.auth.dto.AppMemberSessionReq;
import com.origemite.lib.model.auth.dto.AppMemberSessionRes;
import com.origemite.lib.model.auth.service.AppMemberLoginHistoryService;
import com.origemite.lib.model.auth.service.AppMemberSessionService;
import com.origemite.lib.model.enums.common.EnYn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginFacade {

    private final JwtTokenProp jwtTokenProp;
    private final AppMemberLoginHistoryService appMemberLoginHistoryService;
    private final AppMemberSessionService appMemberSessionService;
    private final JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider;


    @Transactional
    public JwtToken login(LoginReq.LoginForIdPw login) {

        /** 로그인 처리
         *  1. 사용자 조회
         *  2. 암호 확인
         *  3. 세션 저장
         *  4. 토큰생성
         *  5. 로그인 히스토리 저장
         */

        // step 1
        // 조회

        // step 2
        // 비밀번호 검증

        // step 3
        // 발급
        JwtToken jwtToken = tokenProcess(login.getLoginId(), true);

        return jwtToken;
    }
    private JwtToken tokenProcess(String LoginId, boolean history){
        JwtToken jwtToken = jwtTokenAuthenticationProvider.createToken(LoginId, jwtTokenProp.getAccessTokenExpiration(), jwtTokenProp.getRefreshTokenExpiration());

        // step 4
        AppMemberSessionReq.Create sessionCreate = AppMemberSessionReq.Create.builder()
                .id(jwtToken.getLoginSessionId())
                .memberId(LoginId)
                .refreshToken(jwtToken.getRefreshToken())
                .expiration(jwtToken.getRefreshTokenExpiration())
                .build();
        AppMemberSessionRes.Id sesseionId = appMemberSessionService.save(sessionCreate);

        // step 5
        if(history){
            AppMemberLoginHistoryReq.Create create = AppMemberLoginHistoryReq.Create.builder()
                    .memberId(LoginId)
                    .loginAt(DateUtils.now())
                    .loginHistoryUserAgent(WebUtils.getUserAgent())
                    .loginHistoryIp(WebUtils.getRemoteAddr())
                    .loginSuccessYn(EnYn.Y.getCode())
                    .build();
            AppMemberLoginHistoryRes.Id saveId = appMemberLoginHistoryService.save(create);
        }
        return jwtToken;
    }


    public JwtToken refreshToken(LoginReq.RefreshToken refreshToken) {
        AppMemberSessionRes.Single session = appMemberSessionService.loginByRefreshToken(refreshToken.getRefreshToken());
        return tokenProcess(session.getMemberId(), true);
    }

}