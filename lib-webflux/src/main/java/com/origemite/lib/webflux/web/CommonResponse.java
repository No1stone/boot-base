package com.origemite.lib.webflux.web;

import com.origemite.lib.webflux.exception.BizErrorException;
import lombok.*;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = PRIVATE)
@ToString(exclude = {"data"})
@EqualsAndHashCode(of = {"code", "message"})
public class CommonResponse<T> {
    String code;
    String message;
    T data;

    public CommonResponse(T t) {
        message = ResponseType.SUCCESS.name();
        code = ResponseType.SUCCESS.getCode();
        data = t;
    }

    public CommonResponse(ResponseType type, String message) {
        this.message = message;
        code = type.getCode();
        data = null;
    }

    public CommonResponse(ResponseType type, String message, T t) {
        this.message = message;
        code = type.getCode();
        data = t;
    }

    public T orElseThrow() {
        if (!code.equals(ResponseType.SUCCESS.getCode())) {
            throw new BizErrorException(code, message);
        }
        return data;
    }

    public T orElse(T other) {
        return data != null ? data : other;
    }

    public static <T> CommonResponse<T> ok(T data) {
        return new CommonResponse<>(ResponseType.SUCCESS, ResponseType.SUCCESS.name(), data);
    }
}
