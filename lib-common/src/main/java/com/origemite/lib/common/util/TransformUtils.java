package com.origemite.lib.common.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.origemite.lib.common.exception.JsonParseException;
import com.origemite.lib.common.serializer.CustomLocalDateDeserializer;
import com.origemite.lib.common.serializer.CustomLocalDateSerializer;
import com.origemite.lib.common.serializer.CustomLocalDateTimeDeserializer;
import com.origemite.lib.common.serializer.CustomLocalDateTimeSerializer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class TransformUtils {
    private final static JsonMapper mapper;

    static {
        SimpleModule dateTimeModule = new SimpleModule();
        dateTimeModule.addSerializer(LocalDate.class, new CustomLocalDateSerializer());
        dateTimeModule.addDeserializer(LocalDate.class, new CustomLocalDateDeserializer());
        dateTimeModule.addSerializer(LocalDateTime.class, new CustomLocalDateTimeSerializer());
        dateTimeModule.addDeserializer(LocalDateTime.class, new CustomLocalDateTimeDeserializer());

        mapper = JsonMapper.builder()
                .addModule(dateTimeModule)
                .changeDefaultVisibility(vc -> vc.withVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY))
                .build();
    }

    public static <T> T convert(Object formValue, Class<T> clazz) {
        return mapper.convertValue(formValue, clazz);
    }

    public static <T> T convert(Object formValue, TypeReference<T> toValueTypeRef) {
        return mapper.convertValue(formValue, toValueTypeRef);
    }

    public static MultiValueMap<String, Object> multiValueMap(Object formValue) {
        MultiValueMap<String, Object> result = new LinkedMultiValueMap<>();
        Map<String, Object> map = mapper.convertValue(formValue, new TypeReference<>() {});
        result.setAll(map);
        return result;
    }

    public static String stringify(Object object) {
        return stringify(object, null);
    }

    public static String stringify(Object object, String defaultValue) {
        if (object == null) return defaultValue;
        if (object instanceof String e) {
            return e;
        }
        try {
            return mapper.writeValueAsString(object);
        } catch (JacksonException e) {
            throw new JsonParseException("An error occurred while parsing the object.");
        }
    }

    public static <T> T parse(String jsonString, TypeReference<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) return null;

        try {
            return mapper.readValue(jsonString, clazz);
        } catch (JacksonException e) {
            throw new JsonParseException("An error occurred while parsing the object.");
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T parse(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) return null;
        if (clazz.isAssignableFrom(String.class)) return (T) jsonString;

        try {
            return mapper.readValue(jsonString, clazz);
        } catch (JacksonException e) {
            throw new JsonParseException("An error occurred while parsing the object.");
        }
    }

    public static boolean isValidJsonString(String jsonString) {
        try {
            if (StringUtils.isEmpty(jsonString)) return false;
            mapper.readTree(jsonString);
            return true;
        } catch (JacksonException e) {
            return false;
        }
    }

    public static <T> String toString(T clazz) {
        try {
            return mapper.writeValueAsString(clazz);
        } catch (JacksonException e) {
            throw new JsonParseException("An error occurred while parsing the object.");
        }
    }
}
