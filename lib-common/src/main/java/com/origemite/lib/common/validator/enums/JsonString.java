package com.origemite.lib.common.validator.enums;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import com.origemite.lib.common.validator.JsonStringValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = JsonStringValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonString {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}