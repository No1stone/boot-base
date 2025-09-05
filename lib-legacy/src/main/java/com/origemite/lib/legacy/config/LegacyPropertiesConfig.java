package com.origemite.lib.legacy.config;

import com.origemite.lib.common.factory.YamlPropertySourceFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(ignoreResourceNotFound = true,
    value = {
        "classpath:application-legacy.yml",
        "classpath:application-legacy-${spring.profiles.active}.yml"
    }, factory = YamlPropertySourceFactory.class)
public class LegacyPropertiesConfig {
}