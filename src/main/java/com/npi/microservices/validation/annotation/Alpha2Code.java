package com.npi.microservices.validation.annotation;

import com.npi.microservices.validation.CountryAlpha2CodeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CountryAlpha2CodeValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Alpha2Code {
    String message() default "Invalid country code";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
