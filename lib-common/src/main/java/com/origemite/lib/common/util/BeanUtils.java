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

        List<Field> sourceFields = declaredFieldsIncludingSuper(source.getClass());
        Map<String, Field> destinationByName = new HashMap<>();
        for (Field destinationField : declaredFieldsIncludingSuper(destination.getClass())) {
            destinationByName.putIfAbsent(destinationField.getName(), destinationField);
        }

        for (Field sourceField : sourceFields) {

            if (ignoreSet.contains(sourceField.getName())) {
                continue;
            }

            if (isAnnotatedWith(sourceField, OneToMany.class) || isAnnotatedWith(sourceField, OneToOne.class)) {
                continue;
            }

            Field destinationField = destinationByName.get(sourceField.getName());
            if (destinationField == null) {
                continue;
            }
            if (isAnnotatedWith(destinationField, OneToMany.class) || isAnnotatedWith(destinationField, OneToOne.class)) {
                continue;
            }

            sourceField.setAccessible(true);
            destinationField.setAccessible(true);
            try {
                Object value = sourceField.get(source);

                boolean allowNullable = !CollectionUtils.isEmpty(nullableFields) && nullableFields.contains(sourceField.getName());
                if (!allowNullable && value == null) {
                    continue;
                }

                destinationField.set(destination, value);
            } catch (IllegalAccessException ignored) {
            }
        }
    }

    public static Map<String, Object> diff(Object oldObj, Object newObj) {
        return diff(oldObj, newObj, false);
    }

    public static Map<String, Object> diff(Object oldObj, Object newObj, boolean nullSkip) {
        Map<String, Object> diffMap = new HashMap<>();

        if (!oldObj.getClass().equals(newObj.getClass())) {
            throw new IllegalArgumentException("Objects must be of the same type");
        }

        try {
            for (Field field : declaredFieldsIncludingSuper(oldObj.getClass())) {
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

    /** Item extends Single 처럼 상위 선언 필드까지 포함한다. Object 는 제외. */
    private static List<Field> declaredFieldsIncludingSuper(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        Class<?> current = type;
        while (current != null && current != Object.class) {
            Collections.addAll(fields, current.getDeclaredFields());
            current = current.getSuperclass();
        }
        return fields;
    }

    private static boolean isAnnotatedWith(Field field, Class<? extends Annotation> annotationClass) {
        return field.isAnnotationPresent(annotationClass);
    }
}
