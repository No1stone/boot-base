package com.origemite.lib.common.validator;

import com.origemite.lib.common.util.TransformUtils;
import com.origemite.lib.common.validator.enums.JsonString;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class JsonStringValidator implements ConstraintValidator<JsonString, String> {

    @Override
    public void initialize(JsonString annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext cxt) {
        return TransformUtils.isValidJsonString(value);
    }

}