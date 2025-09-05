package com.origemite.lib.common.exception;

import com.origemite.lib.common.web.CommonResponseType;
import com.origemite.lib.common.web.SystemServiceModule;
import com.origemite.lib.common.web.ResponseTypeInterface;
import com.origemite.lib.common.util.MessageUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public class DidddaException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "";

    private final SystemServiceModule systemServiceModule;
    private final HttpStatus httpStatus;
    private final ResponseTypeInterface responseTypeInterface;
    private final String errorMessage;
    private Map<String, Object> data;

    protected DidddaException() {
        super(DEFAULT_ERROR_MESSAGE);
        this.systemServiceModule = SystemServiceModule.DEFAULT;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.responseTypeInterface = CommonResponseType.UNKNOWN;
        this.errorMessage = DEFAULT_ERROR_MESSAGE;
    }


    protected DidddaException(SystemServiceModule systemServiceModule, ResponseTypeInterface responseTypeInterface) {
        this.systemServiceModule = systemServiceModule;
        this.httpStatus = responseTypeInterface.getHttpStatus();
        this.responseTypeInterface = responseTypeInterface;
        this.errorMessage = MessageUtils.getMessage(responseTypeInterface.getCode());
    }

    protected DidddaException(SystemServiceModule systemServiceModule, ResponseTypeInterface responseTypeInterface, Throwable e) {
        super(e);
        this.systemServiceModule = systemServiceModule;
        this.httpStatus = responseTypeInterface.getHttpStatus();
        this.responseTypeInterface = responseTypeInterface;
        this.errorMessage = MessageUtils.getMessage(responseTypeInterface);
    }

    protected DidddaException(SystemServiceModule systemServiceModule, ResponseTypeInterface responseTypeInterface, Object... args) {
        this.systemServiceModule = systemServiceModule;
        this.httpStatus = responseTypeInterface.getHttpStatus();
        this.responseTypeInterface = responseTypeInterface;
        this.errorMessage = MessageUtils.getMessage(responseTypeInterface.getCode(), args);
    }

    protected DidddaException(SystemServiceModule systemServiceModule, ResponseTypeInterface responseTypeInterface, Throwable e, Object... args) {
        super(e);
        this.systemServiceModule = systemServiceModule;
        this.httpStatus = responseTypeInterface.getHttpStatus();
        this.responseTypeInterface = responseTypeInterface;
        this.errorMessage = MessageUtils.getMessage(responseTypeInterface.getCode(), args);
    }

    public DidddaException addData(String key, Object value) {
        if( data == null ) {
            data = new HashMap<>();
        }
        data.put(key, value);
        return this;
    }

}
