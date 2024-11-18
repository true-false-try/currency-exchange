package com.npi.microservices.dto.external_endpoint;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExternalEndpointResponsePlDto {
    String effectiveDate;
    Set<Rate> rates;

    @Getter
    @Setter
    @ToString
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Rate {
        String currency;
        String code;
        BigDecimal mid;
    }
}
