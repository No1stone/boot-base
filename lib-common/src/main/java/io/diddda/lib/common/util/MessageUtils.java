package io.diddda.lib.common.util;

import io.diddda.lib.common.web.ResponseTypeInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class MessageUtils {
    private static MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }

    public static String getMessage(ResponseTypeInterface code) {
        return getMessage(code, new Object());
    }

    public static String getMessage(ResponseTypeInterface code, Object... args) {
        return getMessage(code.getCode(), args);
    }

    public static String getMessage(String code, Object... args) {
        if (messageSource == null) {
            return format("Can't initialize messageSource: code: %s, args: %s", code, Arrays.toString(args));
        } else {
            return messageSource.getMessage(code, args, "", LocaleContextHolder.getLocale());
        }
    }

    public static String getExceptionMessage(Class<? extends Throwable> exceptionClass) {
        return getExceptionMessage(exceptionClass, new Object());
    }

    public static String getExceptionMessage(Class<? extends Throwable> exceptionClass, Object... args) {
        var code = "exception." + exceptionClass.getSimpleName();
        var message = getMessage(code, args);
        if (message.equals(code) && !exceptionClass.getSimpleName().equals("Throwable")) {
            return getExceptionMessage((Class<? extends Throwable>) exceptionClass.getSuperclass(), args);
        }
        return message;
    }

    public static String getWarningMessage(String code, Object... args) {
        return getMessage("warning." + code, args);
    }


}

