package io.origemite.lib.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import io.origemite.lib.common.util.StringUtils;
import io.origemite.lib.common.validator.enums.CommonCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommonCodeValidator implements ConstraintValidator<CommonCode, String> {

    @Override
    public void initialize(CommonCode annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext cxt) {
        if( StringUtils.isEmpty(value) ) return true;
        return value.matches("^[A-Z0-9]{10}$");
    }

}