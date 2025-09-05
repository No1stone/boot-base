package com.origemite.lib.common.util;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class BeanUtils {

    public static void map(Object source, Object destination) {
        map(source, destination, null, null);
    }

    public static void mapWithIgnoreFields(Object source, Object destination, List<String> ignoreFields) {
        map(source, destination, ignoreFields, null);
    }

    public static void mapWithNullableFields(Object source, Object destination, List<String> nullableFields) {
        map(source, destination, null, nullableFields);
    }

    public static void map(Object source, Object destination, List<String> ignoreFields, List<String> nullableFields) {
        Set<String> ignoreSet = new HashSet<>(Optional.ofNullable(ignoreFields).orElse(Collections.emptyList()));

        Class<?> sourceClass = source.getClass();
        Class<?> destinationClass = destination.getClass();

        Field[] sourceFields = sourceClass.getDeclaredFields();
        Field[] destinationFields = destinationClass.getDeclaredFields();

        for (Field sourceField : sourceFields) {

            if (ignoreSet.contains(sourceField.getName())) {
                continue;
            }

            if (isAnnotatedWith(sourceField, OneToMany.class) || isAnnotatedWith(sourceField, OneToOne.class)) {
                continue;
            }

            for (Field destinationField : destinationFields) {
                if (sourceField.getName().equals(destinationField.getName())) {
                    sourceField.setAccessible(true);
                    destinationField.setAccessible(true);
                    try {
                        Object value = sourceField.get(source);

                        if( !(!CollectionUtils.isEmpty(nullableFields) && nullableFields.contains(sourceField.getName())) && value == null) break;
                        destinationField.set(destination, value);
                    } catch (IllegalAccessException ignored) {}
                }
            }
        }
    }

    public static Map<String, Object> diff(Object oldObj, Object newObj)  {
        return diff(oldObj, newObj, false);
    }

    public static Map<String, Object> diff(Object oldObj, Object newObj, boolean nullSkip)  {
        Map<String, Object> diffMap = new HashMap<>();

        // 동일한 클래스 타입인지 확인
        if (!oldObj.getClass().equals(newObj.getClass())) {
            throw new IllegalArgumentException("Objects must be of the same type");
        }

        // 클래스의 모든 필드를 가져옴
        Field[] fields = oldObj.getClass().getDeclaredFields();

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object oldValue = field.get(oldObj);
                Object newValue = field.get(newObj);

                if (nullSkip && oldValue == null) {
                    continue;
                }

                if (isAnnotatedWith(field, OneToMany.class) || isAnnotatedWith(field, ManyToOne.class)) {
                    continue;
                }

                if (newValue != null && (oldValue == null || !oldValue.equals(newValue))) {
                    diffMap.put(field.getName(), newValue);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return diffMap;
    }

    private static boolean isAnnotatedWith(Field field, Class<? extends Annotation> annotationClass) {
        return field.isAnnotationPresent(annotationClass);
    }
}
