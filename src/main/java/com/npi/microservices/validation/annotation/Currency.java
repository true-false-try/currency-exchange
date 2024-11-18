package com.npi.microservices.validation.annotation;

import com.npi.microservices.validation.CurrencyValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CurrencyValidator.class) // Связываем с валидатором
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Currency {

    String message() default "Invalid currency code"; // Сообщение об ошибке

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}