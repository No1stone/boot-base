package com.origemite.apiauth.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/oauth")
@Slf4j
public class OauthController {

    @GetMapping("/index")
    public String index(@AuthenticationPrincipal OidcUser user, Model model) {
        log.info("user={}", user);
        model.addAttribute("user", user); // 로그인 안 했으면 null
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @ResponseBody
    @GetMapping("/me")
    public Object me(@AuthenticationPrincipal OidcUser oidc,
                     @AuthenticationPrincipal OAuth2User oauth2) {
        return oidc != null ? oidc.getClaims() :
                oauth2 != null ? oauth2.getAttributes() : "null";
    }

}
