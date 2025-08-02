package io.origemite.lib.common.util;

import java.util.ArrayList;
import java.util.List;

public class RoleUtils {

    /**
     * 15 -> [1,2,4,8]
     * 권한 전개
     * @param role
     * @return
     */
    public static List<Integer> spread(Integer role) {
        if( role == null ) return null;

        List<Integer> roles = new ArrayList<>();

        for (int i = 0; role > 0; i++) {
            int bitValue = 1 << i;
            if ((role & bitValue) != 0) {
                roles.add(bitValue);
            }
            role &= ~bitValue;
        }
        return roles;
    }

    /**
     * 권한목록에서 권한이 존재하지는지 체크
     * @param roles 권한목록
     * @param role 권한
     * @return
     */
    public static boolean hasRole(Integer roles, Integer role) {
        return (roles & role) == role;
    }
}
