package com.origemite.apiauth.auth;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "target/apis", description = "Api")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test2")
public class Test2TokenTrafficController {

    @GetMapping("/traffic")
    public boolean traffic() {
        return true;
    }
}
