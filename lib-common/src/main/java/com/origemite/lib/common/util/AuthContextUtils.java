package com.origemite.lib.common.util;

import com.origemite.lib.common.secruity.CustomUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class AuthContextUtils {

    public static String getId() {
        CustomUser userDetails = getUser();
        if( userDetails == null ) return null;
        return userDetails.getId();
    }

    public static List<String> getAvailableServiceIds() {
        CustomUser user = getUser();
        if (user == null || user.getRoles() == null) return List.of();

        return user.getRoles().stream()
                .map(CustomUser.RoleInfo::getService)
                .distinct()
                .toList();
    }

    public static Integer getRole() {
        CustomUser userDetails = getUser();
        if( userDetails == null ) return null;
        return Integer.parseInt(userDetails.getAuthorities().iterator().next().getAuthority());
    }

    public static List<CustomUser.RoleInfo> getServiceRole() {
        CustomUser userDetails = getUser();
        if( userDetails == null ) return null;
        return userDetails.getRoles();
    }

    public static String getJwtToken() {
        CustomUser user = getUser();
        if( user == null ) return null;
        return user.getPassword();
    }

    private static CustomUser getUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if( authentication == null ) return null;
        return (CustomUser)authentication.getPrincipal();
    }
}
