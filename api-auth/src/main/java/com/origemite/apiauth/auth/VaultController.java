package com.origemite.apiauth.auth;


import com.origemite.apiauth.auth.service.VaultService;
import com.origemite.lib.model.auth.VaultKey;
import com.origemite.lib.model.enums.auth.EnVaultType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "target/vault", description = "vault")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/vault")
public class VaultController {

    private final VaultService vaultService;

    //Jenkins Pipeline으로 vault를 리레쉬 갱신하는 api
    @GetMapping("/member-refresh-transit")
    public ResponseEntity valutTest() {
        vaultService.initVaultForRedis();
        return ResponseEntity.ok().build();
    }

}
