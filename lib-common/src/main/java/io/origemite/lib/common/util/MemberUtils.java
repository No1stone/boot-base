package io.origemite.lib.common.util;

import io.origemite.lib.common.secruity.CustomUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MemberUtils {

    public static String getCurrentMemberId() {
        return AuthContextUtils.getId();
    }

    public static List<CustomUser.RoleInfo> getCurrentServiceRole() {

        return AuthContextUtils.getServiceRole();
    }


}
