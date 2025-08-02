package io.diddda.lib.common.base;

import io.diddda.lib.common.secruity.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CustomAuditorAware implements AuditorAware<String> {

    @Value("${spring.application.name:unknown}")
    private String applicationName;

    @Override
    public Optional<String> getCurrentAuditor() {
        SecurityContext context = SecurityContextHolder.getContext(); //TODO 배치의 경우는 context null일 수 있으므로 확인 필요
        Authentication authentication = context.getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of(applicationName);
        }
        
        Object principal = authentication.getPrincipal();
        if (principal == null || !(principal instanceof CustomUser)) {
            return Optional.of(applicationName);
        }
        
        return Optional.ofNullable(((CustomUser) principal).getUsername());
    }
}