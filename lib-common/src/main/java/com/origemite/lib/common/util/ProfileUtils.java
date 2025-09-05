package com.origemite.lib.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Profiles;

@Slf4j
public class ProfileUtils {

    public static boolean isLocal() {
        return EnvUtils.getEnvironment().acceptsProfiles(Profiles.of("local"));
    }

    public static boolean isDev() {
        return EnvUtils.getEnvironment().acceptsProfiles(Profiles.of("dev"));
    }

    public static boolean isStg() {
        return EnvUtils.getEnvironment().acceptsProfiles(Profiles.of("stg"));
    }

    public static boolean isProd() {
        return EnvUtils.getEnvironment().acceptsProfiles(Profiles.of("prod"));
    }

}
