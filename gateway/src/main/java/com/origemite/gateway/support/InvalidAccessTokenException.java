package com.origemite.gateway.support;

public class InvalidAccessTokenException extends RuntimeException {

    public InvalidAccessTokenException() {
        super("유효하지 않은 엑세스 토큰입니다.");
    }

    public InvalidAccessTokenException(Throwable cause) {
        super("유효하지 않은 엑세스 토큰입니다.", cause);
    }
}
