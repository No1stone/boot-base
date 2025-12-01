package com.origemite.apiauth.autoconfig;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;

@ImportAutoConfiguration({
        org.springframework.cloud.vault.config.VaultAutoConfiguration.class,
})
public class VaultAutoConfig {
}
