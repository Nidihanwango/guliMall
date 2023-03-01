package com.atguigu.gulimall.common.validator;

import com.atguigu.gulimall.common.validator.annotation.HasLength;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HasLengthValidator implements ConstraintValidator<HasLength, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.length() > 0;
    }
}
