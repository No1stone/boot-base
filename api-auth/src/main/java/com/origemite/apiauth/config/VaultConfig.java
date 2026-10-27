package com.origemite.apiauth.config;

import com.origemite.apiauth.support.token.JwtSigningKeySync;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class VaultConfig {

    private final JwtSigningKeySync jwtSigningKeySync;

    @PostConstruct
    public void init() {
        jwtSigningKeySync.syncAuthJwtKeys();
    }
}
