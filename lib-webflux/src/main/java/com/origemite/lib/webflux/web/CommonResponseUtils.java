package com.origemite.lib.webflux.web;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonResponseUtils {

    public static <T> CommonResponse<T> responseSuccess() {
        return response(ResponseType.SUCCESS, getMessage(ResponseType.SUCCESS), null);
    }

    public static <T> CommonResponse<T> responseSuccess(T data) {
        return response(ResponseType.SUCCESS, getMessage(ResponseType.SUCCESS), data);
    }

    public static <T> CommonResponse<T> response(ResponseType responseType) {
        return response(responseType, getMessage(responseType), null);
    }


    public static <T> CommonResponse<T> response(ResponseType responseType, List<Object> args) {
        return response(responseType, getMessage(responseType, args), null);
    }

    public static <T> CommonResponse<T> response(ResponseType responseType, T data) {
        return response(responseType, getMessage(responseType), data);
    }

    public static <T> CommonResponse<T> response(ResponseType responseType, String message) {
        return response(responseType, message, null);
    }

    public static <T> CommonResponse<T> response(ResponseType responseType, String message, T data) {
        return CommonResponse.<T>builder().code(responseType.getCode()).message(message).data(data).build();
    }

    private static String getMessage(ResponseType responseType) {
        return getMessage(responseType, null);
    }

    private static String getMessage(ResponseType responseType, List<Object> args) {
        return defaultIfEmpty(MessageUtils.getMessage(responseType.getCode(), args), MessageFormat.format(responseType.getDesc(), args));
    }
    public static String defaultIfEmpty(String str, String defaultStr) {
        if (isEmpty(str)) return defaultStr;
        return str;
    }
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
