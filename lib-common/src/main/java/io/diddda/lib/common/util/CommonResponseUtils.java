package io.diddda.lib.common.util;


import io.diddda.lib.common.web.CommonResponse;
import io.diddda.lib.common.web.CommonResponseType;
import io.diddda.lib.common.web.SystemServiceModule;
import io.diddda.lib.common.web.ResponseTypeInterface;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonResponseUtils {

    public static <T> CommonResponse<T> responseSuccess() {
        return response(SystemServiceModule.DEFAULT, CommonResponseType.SUCCESS_BEFORE, getMessage(CommonResponseType.SUCCESS_BEFORE), null);
    }

    public static <T> CommonResponse<T> responseSuccess(T data) {
        return response(SystemServiceModule.DEFAULT, CommonResponseType.SUCCESS_BEFORE, getMessage(CommonResponseType.SUCCESS_BEFORE), data);
    }

    public static <T> CommonResponse<T> responseSuccess(SystemServiceModule systemServiceModule, T data) {
        return response(systemServiceModule, CommonResponseType.SUCCESS, getMessage(CommonResponseType.SUCCESS), data);
    }

    public static <T> CommonResponse<T> response(ResponseTypeInterface responseTypeInterface) {
        return response(SystemServiceModule.DEFAULT, responseTypeInterface, getMessage(responseTypeInterface), null);
    }

    public static <T> CommonResponse<T> response(SystemServiceModule systemServiceModule, ResponseTypeInterface responseTypeInterface) {
        return response(systemServiceModule, responseTypeInterface, getMessage(responseTypeInterface), null);
    }


    public static <T> CommonResponse<T> response(SystemServiceModule systemServiceModule, ResponseTypeInterface responseTypeInterface, List<Object> args) {
        return response(systemServiceModule, responseTypeInterface, getMessage(responseTypeInterface, args), null);
    }

    public static <T> CommonResponse<T> response(SystemServiceModule systemServiceModule, ResponseTypeInterface responseTypeInterface, T data) {
        return response(systemServiceModule, responseTypeInterface, getMessage(responseTypeInterface), data);
    }

    public static <T> CommonResponse<T> response(ResponseTypeInterface responseTypeInterface, String message) {
        return response(SystemServiceModule.DEFAULT, responseTypeInterface, message, null);
    }

    public static <T> CommonResponse<T> response(SystemServiceModule systemServiceModule, ResponseTypeInterface responseTypeInterface, String message) {
        return response(systemServiceModule, responseTypeInterface, message, null);
    }

    public static <T> CommonResponse<T> response(SystemServiceModule systemServiceModule, ResponseTypeInterface responseTypeInterface, String message, T data) {
        if(responseTypeInterface.getClass() == CommonResponseType.class) {
            return CommonResponse.<T>builder().code(systemServiceModule.getPrefix() + responseTypeInterface.getCode()).message(message).data(data).build();
        }
        return  CommonResponse.<T>builder().code(responseTypeInterface.getCode()).message(message).data(data).build();
    }

    private static String getMessage(ResponseTypeInterface responseTypeInterface) {
        return getMessage(responseTypeInterface, null);
    }

    private static String getMessage(ResponseTypeInterface responseTypeInterface, List<Object> args) {
        return StringUtils.defaultIfEmpty(MessageUtils.getMessage(responseTypeInterface.getCode(), args), MessageFormat.format(responseTypeInterface.getDesc(), args));
    }

}
