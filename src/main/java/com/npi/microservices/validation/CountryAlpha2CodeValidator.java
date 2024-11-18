package com.npi.microservices.validation;

import com.npi.microservices.validation.annotation.Alpha2Code;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class CountryAlpha2CodeValidator implements ConstraintValidator<Alpha2Code, String> {

    private static final Set<String> ISO_COUNTRIES_ALPHA_2_CODE = new HashSet<>(Arrays.asList(Locale.getISOCountries()));

    @Override
    public boolean isValid(String country, ConstraintValidatorContext context) {
        if (country != null && ISO_COUNTRIES_ALPHA_2_CODE.contains(country)) {
            log.info("Validation field country:{}, was succeed", country);
            return true;
        }
        log.error("Validation error invalid country is cannot be as a: '{}' for alpha-2 code", country);
        return false;
    }
}
