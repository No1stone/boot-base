package com.origemite.apiauth.autoconfig;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;

@ImportAutoConfiguration({
        org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration.class
})
public class RedisAutoConfig {
}
