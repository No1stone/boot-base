package com.origemite.lib.common.web;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ValidationResponse {
    private String field;
    private Object value;
    private String error;
    private Object args;
}
