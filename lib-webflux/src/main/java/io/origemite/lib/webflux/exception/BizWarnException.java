package io.origemite.lib.webflux.exception;

import io.origemite.lib.webflux.web.ResponseType;

public class BizWarnException extends BizException {

    public BizWarnException(String message) {
        super(message);
    }

    public BizWarnException(ResponseType errorCodeType) {
        super(errorCodeType);
    }

    public BizWarnException(ResponseType errorCodeType, Object... args) {
        super(errorCodeType, args);
    }

}
