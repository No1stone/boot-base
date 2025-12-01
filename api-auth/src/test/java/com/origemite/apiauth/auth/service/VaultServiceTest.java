package com.origemite.apiauth.auth.service;

import com.origemite.apiauth.autoconfig.RedisAutoConfig;
import com.origemite.apiauth.autoconfig.VaultAutoConfig;
import com.origemite.lib.common.util.TransformUtils;
import com.origemite.lib.model.auth.VaultKey;
import com.origemite.lib.model.enums.auth.EnVaultType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = VaultService.class)
@ActiveProfiles({"test"})
@Import({VaultAutoConfig.class, RedisAutoConfig.class})
public class VaultServiceTest {

    @Autowired
    VaultService vaultService;

    @Test
    @DisplayName("Init Test")
    public void test() {
        vaultService.initVaultForRedis();
    }

    @Test
    @DisplayName("public key ")
    public void key() {
       VaultKey.Response res = vaultService.getValutTransitKeys(EnVaultType.AUTH_JWT);
       System.out.println("result ="+ TransformUtils.toString(res.getKeys())); ;
    }
}
