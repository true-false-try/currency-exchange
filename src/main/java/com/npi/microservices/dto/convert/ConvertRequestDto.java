package com.npi.microservices.dto.convert;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.npi.microservices.validation.annotation.Alpha2Code;
import com.npi.microservices.validation.annotation.Posix;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import com.npi.microservices.validation.annotation.Currency;

import java.math.BigDecimal;

import static com.npi.microservices.constant.Constants.FIELD_CURRENCY_SOURCE;
import static com.npi.microservices.constant.Constants.FIELD_CURRENCY_TARGET;

@Posix
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConvertRequestDto {
    Long date;

    //This field has default value as 0
    @Min(0)
    @Max(10)
    Integer precision = 0;

    @NotNull
    BigDecimal amount;

    @NotNull
    @NotEmpty
    @Currency
    @Size(min = 3, max = 3)
    @JsonProperty(FIELD_CURRENCY_SOURCE)
    String currencySource;

    @NotNull
    @NotEmpty
    @Currency
    @Size(min = 3, max = 3)
    @JsonProperty(FIELD_CURRENCY_TARGET)
    String currencyTarget;

    @NotNull
    @NotEmpty
    @Alpha2Code
    String country;

}
