package io.origemite.lib.common.exception;

import io.origemite.lib.common.web.SystemServiceModule;
import io.origemite.lib.common.web.ResponseTypeInterface;

public class DidddaWarnException extends DidddaException {

    public DidddaWarnException(SystemServiceModule systemServiceModule, ResponseTypeInterface responseTypeInterface) {
        super(systemServiceModule, responseTypeInterface);
    }

    public DidddaWarnException(SystemServiceModule systemServiceModule, ResponseTypeInterface responseTypeInterface, Throwable e) {
        super(systemServiceModule, responseTypeInterface, e);
    }

    public DidddaWarnException(SystemServiceModule systemServiceModule, ResponseTypeInterface responseTypeInterface, Object... args) {
        super(systemServiceModule, responseTypeInterface, args);
    }

    public DidddaWarnException(SystemServiceModule systemServiceModule, ResponseTypeInterface responseTypeInterface, Throwable e, Object... args) {
        super(systemServiceModule, responseTypeInterface, e, args);
    }
}
