package io.diddda.lib.common.converter;

import io.diddda.lib.common.util.TransformUtils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

@Slf4j
@Converter
public class JsonToMapConverter implements AttributeConverter<Map<String, Object>, String> {

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        if (attribute == null || attribute.isEmpty()) return "{}";
        try {
            return TransformUtils.stringify(attribute, "{}");
        } catch (Exception e) {
            log.error("Error converting map to JSON string", e);
            throw new IllegalArgumentException("Failed to convert Map to JSON String", e);
        }
    }


    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) return Map.of();
        try {
            return TransformUtils.parse(dbData, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            log.error("Failed to convert JSON string to Map", e);
            throw new IllegalArgumentException("Could not convert JSON to map", e);
        }
    }
}