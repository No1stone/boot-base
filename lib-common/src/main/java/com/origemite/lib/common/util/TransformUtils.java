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
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.module.SimpleModule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class TransformUtils {
    private final static JsonMapper mapper;

    // API용 변환기와 분리된 파일용 공통 인스턴스. ISO 날짜 및 소수 초를 유지한다.
    private static final JsonMapper fileMapper = createFileMapper();

    private static JsonMapper createFileMapper() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, new ValueDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) {
                String value = parser.getText();
                if (value == null || value.isBlank()) return null;
                // 기존 공백 구분 날짜와 ISO 날짜 모두 허용한다.
                return LocalDateTime.parse(value.replace(' ', 'T'));
            }
        });
        return JsonMapper.builder()
                .addModule(module)
                .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.INDENT_OUTPUT)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();
    }

    /** 문자열도 JSON 문자열로 인코딩하며, 줄바꿈 없는 JSON 한 건을 생성한다. */
    public static String stringifyFileValue(Object value) {
        return fileMapper.writeValueAsString(value);
    }

    /** 파일의 JSON 한 건을 읽으며 알 수 없는 필드는 무시한다. */
    public static <T> T parseFileValue(String json, Class<T> type) {
        return fileMapper.readValue(json, type);
    }

    /** JSON 문법을 검사하여 읽고 변환 실패를 호출부에 전달한다. */
    public static <T> T parseJson(String json, Class<T> type) {
        return mapper.readerFor(type)
                .without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .readValue(json);
    }

    /** JSON을 트리로 읽으며 잘못된 JSON은 변환 예외로 전달한다. */
    public static JsonNode parseTree(String json) {
        return mapper.readTree(json);
    }

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
