package io.diddda.lib.webflux.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DtoConverter {

    private final ObjectMapper objectMapper;

    public DtoConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T convert(Map<String, Object> map, Class<T> clazz) {
        return TransformUtils.convert(map, clazz);
    }

    public <T> List<T> convertList(Object source, Class<T> clazz) {
        if (!(source instanceof List<?> rawList)) {
            throw new IllegalArgumentException("List<Map<String, Object>> 타입이어야 합니다.");
        }

        return rawList.stream()
                .map(item -> {
                    if (item instanceof Map<?, ?> mapItem) {
                        // Map<?, ?> → Map<String, Object> 로 변환
                        Map<String, Object> castedMap = new HashMap<>();
                        for (Map.Entry<?, ?> entry : mapItem.entrySet()) {
                            castedMap.put(String.valueOf(entry.getKey()), entry.getValue());
                        }
                        return TransformUtils.convert(castedMap, clazz);
                    } else {
                        throw new IllegalArgumentException("리스트 항목이 Map이 아닙니다: " + item);
                    }
                })
                .collect(Collectors.toList());
    }
}
