package com.npi.microservices.validation;

import java.util.Currency;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;
import java.util.stream.Collectors;

import static com.npi.microservices.constant.Constants.RESERVED_XTS;
import static com.npi.microservices.constant.Constants.RESERVED_XXX;

public class CurrencyValidator implements ConstraintValidator<com.npi.microservices.validation.annotation.Currency, String> {

    //  Give codes all currency by ISO 4217
    private static final Set<String> availableCurrencies = Currency.getAvailableCurrencies()
            .stream()
            .map(Currency::getCurrencyCode)
            .filter(code -> !code.equals(RESERVED_XXX) && !code.equals(RESERVED_XTS))
            .collect(Collectors.toSet());

    @Override
    public boolean isValid(String currencyCode, ConstraintValidatorContext constraintValidatorContext) {
        if (currencyCode == null || currencyCode.trim().isEmpty()) {
            return false;
        }
        return availableCurrencies.contains(currencyCode);
    }
}
