package com.atguigu.gulimall.common.validator;

import com.atguigu.gulimall.common.validator.annotation.ListValue;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

public class ListValueValidator implements ConstraintValidator<ListValue, Integer> {

    private final Set<Integer> set = new HashSet<>();

    @Override
    public void initialize(ListValue constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        for (int value : constraintAnnotation.values()) {
            set.add(value);
        }
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return set.contains(value);
    }
}
