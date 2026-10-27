package com.origemite.lib.model.member.config;

import com.origemite.lib.cache.spring.CachePortTtlCustomizer;
import com.origemite.lib.model.member.facade.MemberFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * 멤버 캐시 TTL. lib-cache default와 별개로 도메인(model)에서 추가한다.
 */
@Configuration
public class MemberCacheTtlConfig {

    @Bean
    public CachePortTtlCustomizer memberCacheTtlCustomizer() {
        return ttlByCacheName -> ttlByCacheName.put(MemberFacade.CACHE_NAME, Duration.ofMinutes(30));
    }
}
