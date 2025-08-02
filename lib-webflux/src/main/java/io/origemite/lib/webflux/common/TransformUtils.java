package io.origemite.lib.webflux.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.diddda.lib.webflux.common.serializer.*;
import io.origemite.lib.webflux.common.serializer.CustomLocalDateDeserializer;
import io.origemite.lib.webflux.common.serializer.CustomLocalDateSerializer;
import io.origemite.lib.webflux.common.serializer.CustomLocalDateTimeDeserializer;
import io.origemite.lib.webflux.common.serializer.CustomLocalDateTimeSerializer;
import io.origemite.lib.webflux.exception.JsonParseException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;


public class TransformUtils {
    private final static ObjectMapper mapper;

    static
    {
        mapper = new ObjectMapper();

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new CustomLocalDateSerializer());
        javaTimeModule.addDeserializer(LocalDate.class, new CustomLocalDateDeserializer());
        javaTimeModule.addSerializer(LocalDateTime.class, new CustomLocalDateTimeSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new CustomLocalDateTimeDeserializer());

        mapper.registerModule(javaTimeModule);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

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
        if( object instanceof String e) {
            return e;
        };
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonParseException("An error occurred while parsing the object.");
        }
    }

    public static <T> T parse(String jsonString, TypeReference<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) return null;

        try {
            return mapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            throw new JsonParseException("An error occurred while parsing the object.");
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T parse(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) return null;
        if (clazz.isAssignableFrom(String.class)) return (T) jsonString;

        try {
            return mapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            throw new JsonParseException("An error occurred while parsing the object.");
        }
    }

    public static boolean isValidJsonString(String jsonString) {
        try {
            if(StringUtils.isEmpty(jsonString)) return false;
            mapper.readTree(jsonString);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    public static <T> String toString(T clazz) {
        try {
           return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(clazz);
        }catch (JsonProcessingException e){
            throw new JsonParseException("An error occurred while parsing the object.");
        }
    }
}
