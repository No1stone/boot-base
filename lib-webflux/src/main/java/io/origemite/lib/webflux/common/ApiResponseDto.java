package io.origemite.lib.webflux.common;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponseDto<T> {
    String code;
    String message;
    T data; // 단일 객체 or 리스트
}
