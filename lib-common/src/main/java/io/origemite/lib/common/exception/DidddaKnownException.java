package io.origemite.lib.common.exception;

import io.origemite.lib.common.web.SystemServiceModule;
import io.origemite.lib.common.web.ResponseTypeInterface;

public class DidddaKnownException extends DidddaException {

    public DidddaKnownException(SystemServiceModule systemServiceModule, ResponseTypeInterface responseTypeInterface) {
        super(systemServiceModule, responseTypeInterface);
    }

    public DidddaKnownException(SystemServiceModule systemServiceModule, ResponseTypeInterface responseTypeInterface, Throwable e) {
        super(systemServiceModule, responseTypeInterface, e);
    }

    public DidddaKnownException(SystemServiceModule systemServiceModule, ResponseTypeInterface responseTypeInterface, Object... args) {
        super(systemServiceModule, responseTypeInterface, args);
    }

    public DidddaKnownException(SystemServiceModule systemServiceModule, ResponseTypeInterface responseTypeInterface, Throwable e, Object... args) {
        super(systemServiceModule, responseTypeInterface, e, args);
    }
}