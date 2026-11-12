package com.origemite.lib.common.exception;

import com.origemite.lib.common.util.MessageUtils;
import com.origemite.lib.common.web.ResponseType;
import com.origemite.lib.common.web.ResponseTypeInterface;
import com.origemite.lib.common.web.SystemServiceModule;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BizException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "";

    private final SystemServiceModule systemServiceModule;
    private final HttpStatus httpStatus;
    private final ResponseTypeInterface responseType;
    private final String errorMessage;
    private Map<String, Object> data;

    protected BizException() {
        super(DEFAULT_ERROR_MESSAGE);
        this.systemServiceModule = SystemServiceModule.DEFAULT;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.responseType = ResponseType.UNKNOWN;
        this.errorMessage = DEFAULT_ERROR_MESSAGE;
    }

    protected BizException(ResponseTypeInterface responseType) {
        this(SystemServiceModule.DEFAULT, responseType);
    }

    protected BizException(ResponseTypeInterface responseType, Object... args) {
        this(SystemServiceModule.DEFAULT, responseType, args);
    }

    protected BizException(ResponseTypeInterface responseType, Throwable cause) {
        this(SystemServiceModule.DEFAULT, responseType, cause);
    }

    protected BizException(ResponseTypeInterface responseType, Throwable cause, Object... args) {
        this(SystemServiceModule.DEFAULT, responseType, cause, args);
    }

    protected BizException(SystemServiceModule systemServiceModule, ResponseTypeInterface responseType) {
        this.systemServiceModule = systemServiceModule;
        this.httpStatus = responseType.getHttpStatus();
        this.responseType = responseType;
        this.errorMessage = MessageUtils.getMessage(responseType.getCode());
    }

    protected BizException(SystemServiceModule systemServiceModule, ResponseTypeInterface responseType, Throwable cause) {
        super(cause);
        this.systemServiceModule = systemServiceModule;
        this.httpStatus = responseType.getHttpStatus();
        this.responseType = responseType;
        this.errorMessage = MessageUtils.getMessage(responseType);
    }

    protected BizException(SystemServiceModule systemServiceModule, ResponseTypeInterface responseType, Object... args) {
        this.systemServiceModule = systemServiceModule;
        this.httpStatus = responseType.getHttpStatus();
        this.responseType = responseType;
        this.errorMessage = MessageUtils.getMessage(responseType.getCode(), args);
    }

    protected BizException(SystemServiceModule systemServiceModule, ResponseTypeInterface responseType, Throwable cause, Object... args) {
        super(cause);
        this.systemServiceModule = systemServiceModule;
        this.httpStatus = responseType.getHttpStatus();
        this.responseType = responseType;
        this.errorMessage = MessageUtils.getMessage(responseType.getCode(), args);
    }

    public BizException addData(String key, Object value) {
        if (data == null) {
            data = new HashMap<>();
        }
        data.put(key, value);
        return this;
    }

}
