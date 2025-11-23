package com.origemite.apiauth.auth;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "target/apis", description = "Api")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test1")
public class Test1PublicController {

    @GetMapping("/traffic")
    public boolean traffic() {
        return true;
    }
}
