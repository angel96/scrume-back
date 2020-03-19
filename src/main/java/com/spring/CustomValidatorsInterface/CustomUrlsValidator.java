package com.spring.CustomValidatorsInterface;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.spring.CustomValidatorsClass.CustomUrlsValidatorClass;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Constraint(validatedBy = CustomUrlsValidatorClass.class)
public @interface CustomUrlsValidator {
    String message() default "{Urls mal introducidas}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
