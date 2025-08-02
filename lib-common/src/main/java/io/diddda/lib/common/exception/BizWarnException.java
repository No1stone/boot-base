package io.diddda.lib.common.exception;

import io.diddda.lib.common.web.ResponseType;

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
