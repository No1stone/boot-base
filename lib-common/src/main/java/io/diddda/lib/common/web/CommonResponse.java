package io.diddda.lib.common.web;

import io.diddda.lib.common.exception.DidddaErrorException;
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
        message = CommonResponseType.SUCCESS_BEFORE.name();
        code = CommonResponseType.SUCCESS_BEFORE.getCode();
        data = t;
    }

    public CommonResponse(ResponseTypeInterface type, String message) {
        this.message = message;
        code = type.getCode();
        data = null;
    }

    public CommonResponse(ResponseTypeInterface type, String message, T t) {
        this.message = message;
        code = type.getCode();
        data = t;
    }

    public T orElseThrow() {
        if (!code.equals(CommonResponseType.SUCCESS_BEFORE.getCode())) {
            throw new DidddaErrorException(SystemServiceModule.DEFAULT, CommonResponseType.EXCEPTION);
        }
        return data;
    }

    public T orElse(T other) {
        return data != null ? data : other;
    }

    public static <T> CommonResponse<T> ok(T data) {
        return new CommonResponse<>(CommonResponseType.SUCCESS_BEFORE, CommonResponseType.SUCCESS_BEFORE.name(), data);
    }

}
