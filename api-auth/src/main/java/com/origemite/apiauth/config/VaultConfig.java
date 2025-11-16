package com.origemite.apiauth.config;

import com.origemite.apiauth.auth.service.VaultService;
import com.origemite.lib.common.util.TransformUtils;
import com.origemite.lib.model.auth.VaultKey;
import com.origemite.lib.model.enums.auth.EnVaultType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;


@Configuration
@RequiredArgsConstructor
public class VaultConfig {

    private final VaultService vaultService;

    @PostConstruct
    void init() {
        vaultService.initVaultForRedis();
    }

}
