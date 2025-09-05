package com.origemite.lib.webflux.exception;

import com.origemite.lib.webflux.web.MessageUtils;
import com.origemite.lib.webflux.web.ResponseType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BizException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "";

    private final HttpStatus httpStatus;
    private final ResponseType responseType;
    private final String errorMessage;
    private Map<String, Object> data;

    protected BizException() {
        super(DEFAULT_ERROR_MESSAGE);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.responseType = ResponseType.UNKNOWN;
        this.errorMessage = DEFAULT_ERROR_MESSAGE;
    }


    protected BizException(String message) {
        ResponseType responseType = ResponseType.UNDEFINED;
        this.httpStatus = responseType.getHttpStatus();
        this.responseType = responseType;
        this.errorMessage = message;
    }

    protected BizException(String code, String message){
        ResponseType responseType = ResponseType.of(code);
        this.httpStatus = responseType.getHttpStatus();
        this.responseType = responseType;
        this.errorMessage = message;
    }

    protected BizException(ResponseType responseType) {
        this.httpStatus = responseType.getHttpStatus();
        this.responseType = responseType;
        this.errorMessage = MessageUtils.getMessage(responseType.getCode());
    }

    protected BizException(ResponseType responseType, Object... args) {
        this.httpStatus = responseType.getHttpStatus();
        this.responseType = responseType;
        this.errorMessage = MessageUtils.getMessage(responseType.getCode(), args);
    }

    public BizException addData(String key, Object value) {
        if( data == null ) {
            data = new HashMap<>();
        }
        data.put(key, value);
        return this;
    }

}
