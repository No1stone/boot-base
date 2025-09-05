package com.origemite.lib.webflux.exception;


import com.origemite.lib.webflux.web.ResponseType;

public class BizErrorException extends BizException {

    public BizErrorException(String message) {
        super(message);
    }

    public BizErrorException(String code, String message) {
        super(code, message);
    }

    public BizErrorException(ResponseType responseType) {
        super(responseType);
    }

    public BizErrorException(ResponseType responseType, Object... args) {
        super(responseType, args);
    }


}
