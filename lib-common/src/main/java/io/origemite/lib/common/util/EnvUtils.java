package io.origemite.lib.common.util;

import lombok.Getter;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
public class EnvUtils {

    @Getter
    private static Environment environment;

    public EnvUtils(Environment environment) {
        EnvUtils.environment = environment;
    }

    public static String getProperty(String key) {
        return environment.getProperty(key);
    }
}
