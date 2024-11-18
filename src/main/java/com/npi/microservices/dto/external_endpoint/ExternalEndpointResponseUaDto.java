package com.npi.microservices.dto.external_endpoint;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExternalEndpointResponseUaDto {
    private int r030;
    private String txt;
    private BigDecimal rate;
    private String cc;
    private String exchangedate;
}
