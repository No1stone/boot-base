package io.diddda.lib.webflux.common;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.diddda.lib.webflux.exception.BizErrorException;
import io.diddda.lib.webflux.exception.BizException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponseUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static <T> List<T> extractList(ApiResponseDto<?> response, Class<T> clazz) {
        validateResponse(response);
        Object data = response.getData();

        if (data instanceof List<?> list) {
            return OBJECT_MAPPER.convertValue(
                    list,
                    OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz)
            );
        } else {
            T single = OBJECT_MAPPER.convertValue(data, clazz);
            return List.of(single);
        }
    }
//    public static <T> PageResult<T> extractPageResult(ApiResponseDto<?> response, Class<T> clazz) {
//        validateResponse(response);
//        Object data = response.getData();
//
//        return OBJECT_MAPPER.convertValue(
//                data,
//                OBJECT_MAPPER.getTypeFactory().constructParametricType(PageResult.class, clazz)
//        );
//    }
    public static <T> PageResult<T> extractPageResult(ApiResponseDto<?> response, Class<T> clazz) {
        validateResponse(response);
        Object data = response.getData();

        return OBJECT_MAPPER.convertValue(
                data,
                OBJECT_MAPPER.getTypeFactory().constructParametricType(PageResult.class, clazz)
        );
    }




    public static <T> T extractSingle(ApiResponseDto<?> response, Class<T> clazz) {
        validateResponse(response);
        Object data = response.getData();

        if (data instanceof List<?> list && !list.isEmpty()) {
            return OBJECT_MAPPER.convertValue(list.get(0), clazz);
        } else {
            return OBJECT_MAPPER.convertValue(data, clazz);
        }
    }

    private static void validateResponse(ApiResponseDto<?> response) {
        if (response == null) {
            throw new BizErrorException("응답이 없습니다.");
        }
    }
}
