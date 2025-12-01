package com.origemite.apiauth.autoconfig;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;

@ImportAutoConfiguration({
        org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class
})
public class RedisAutoConfig {
}
