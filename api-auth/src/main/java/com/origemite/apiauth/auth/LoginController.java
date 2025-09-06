package com.origemite.apiauth.auth;


import com.origemite.apiauth.auth.facade.LoginFacade;
import com.origemite.lib.model.auth.JwtToken;
import com.origemite.lib.model.auth.LoginReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "target/apis", description = "Api")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class LoginController {

    private final LoginFacade loginFacade;

    @Operation(summary = "로그인", description = "")
    @PostMapping("/login")
    public JwtToken login(@RequestBody @Valid LoginReq.LoginForIdPw login) {
        return loginFacade.login(login);
    }

    @Operation(summary = "회원가입", description = "")
    @PostMapping("/join")
    public ResponseEntity join() {
        return ResponseEntity.ok().build();
    }

}
