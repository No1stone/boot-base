package io.diddda.lib.common.secruity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Builder
public class CustomUser implements UserDetails {
    private String id;                      // memberId ("I")
    private String token;                   // 원본 JWT
    private String sessionId;               // ("S")
    private String tokenType;               // ("M": access, otp 등)

    // 구조화된 역할 정보 (서비스, 유형, bitmask)
    private List<RoleInfo> roles;           // ("R")

    private Collection<? extends GrantedAuthority> authorities;

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return token; }
    @Override public String getUsername() { return id; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @AllArgsConstructor
    public static class RoleInfo {
        private String service;    // ex: "PS"
        private Integer role;      // bitmask, ex: 7
    }
}
