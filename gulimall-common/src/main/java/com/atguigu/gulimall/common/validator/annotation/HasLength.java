package com.atguigu.gulimall.common.validator.annotation;

import com.atguigu.gulimall.common.validator.HasLengthValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {HasLengthValidator.class})
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface HasLength {
    String message() default "{com.atguigu.gulimall.common.validator.annotation.HasLength.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

//    String value() default "{}";
}
