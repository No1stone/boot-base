package com.origemite.lib.common.web;

import org.springframework.http.HttpStatus;

public interface ResponseTypeInterface {
    String getCode();
    String getDesc();
    HttpStatus getHttpStatus();
}
