package com.origemite.apiauth.auth;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "target/apis", description = "Api")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/apis")
public class TestController {

    @Operation(summary = "목록 조회")
    @GetMapping
    public String search() {
        return "test";
    }
}
