package io.diddda.lib.common.exception;

import io.diddda.lib.common.web.SystemServiceModule;
import io.diddda.lib.common.web.ResponseTypeInterface;

public class DidddaErrorException extends DidddaException {

    public DidddaErrorException(SystemServiceModule systemServiceModule, ResponseTypeInterface responseTypeInterface) {
        super(systemServiceModule, responseTypeInterface);
    }

    public DidddaErrorException(SystemServiceModule systemServiceModule, ResponseTypeInterface responseTypeInterface, Throwable e) {
        super(systemServiceModule, responseTypeInterface, e);
    }

    public DidddaErrorException(SystemServiceModule systemServiceModule, ResponseTypeInterface responseTypeInterface, Object... args) {
        super(systemServiceModule, responseTypeInterface, args);
    }

    public DidddaErrorException(SystemServiceModule systemServiceModule, ResponseTypeInterface responseTypeInterface, Throwable e, Object... args) {
        super(systemServiceModule, responseTypeInterface, e, args);
    }
}
