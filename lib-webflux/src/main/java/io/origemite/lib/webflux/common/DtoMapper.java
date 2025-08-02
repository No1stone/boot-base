package io.origemite.lib.webflux.common;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DtoMapper {

    public static Map<String, Object> toParamMap(Object dto) {
        Map<String, Object> map = new HashMap<>();
        if (dto == null) return map;

        Class<?> clazz = dto.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(dto);
                if (value != null) {
                    map.put(field.getName(), value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to map DTO field: " + field.getName(), e);
            }
        }
        return map;
    }
}
