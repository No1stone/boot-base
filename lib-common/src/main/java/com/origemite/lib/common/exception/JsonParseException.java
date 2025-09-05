package com.origemite.lib.common.exception;

public class JsonParseException extends RuntimeException {

    public JsonParseException() {
        super();
    }

    public JsonParseException(String s) {
        super(s);
    }

    public JsonParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonParseException(Throwable cause) {
        super(cause);
    }
}
