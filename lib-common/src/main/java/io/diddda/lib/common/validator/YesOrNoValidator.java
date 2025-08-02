package io.diddda.lib.common.validator;

import io.diddda.lib.common.validator.enums.YesOrNo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class YesOrNoValidator implements ConstraintValidator<YesOrNo, String> {

    @Override
    public void initialize(YesOrNo constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && (value.equals("Y") || value.equals("N"));
    }
}