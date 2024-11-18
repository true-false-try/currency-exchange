package com.npi.microservices.processor;

import com.npi.microservices.dto.convert.ConvertRequestDto;
import com.npi.microservices.entity.ExchangeRatesEntity;
import com.npi.microservices.entity.ExternalEndpointEntity;
import com.npi.microservices.exception.ConvertPosixException;
import com.npi.microservices.exception.DuplicatedDateException;
import com.npi.microservices.service.ExchangeRatesService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ExchangeRatesProcessor {

   private final Map<String, ExchangeRatesService> exchangeRatesServiceMap;
   private static final String EXCEPTION_MESSAGE = "No ExchangeRatesService found for country: %s";

    public void processLoadExchangeRates(ExternalEndpointEntity externalEndpointEntity, Long posixDate, String convertDateToUrl) throws DuplicatedDateException {
        String country = externalEndpointEntity.getCountry();
        ExchangeRatesService exchangeRatesService = exchangeRatesServiceMap.get(country);
        if (exchangeRatesService == null) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE.formatted(country));
        }
        exchangeRatesService.processLoadExchangeRates(externalEndpointEntity, posixDate, convertDateToUrl);
    }

    public BigDecimal processConvertExchangeRates(ConvertRequestDto requestDto, ExchangeRatesEntity exchangeRates, String country) {
        ExchangeRatesService exchangeRatesService = exchangeRatesServiceMap.get(country);
        if (exchangeRatesService == null) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE.formatted(country));
        }
        return exchangeRatesService.processConvertExchangeRates(requestDto, exchangeRates);
    }

    public BigDecimal processConvertExchangeRatesAcrossExternalEndpoints(ConvertRequestDto requestDto, ExternalEndpointEntity externalEndpointEntity, Long posixDate, String convertDateToUrl) {
        String country = externalEndpointEntity.getCountry();
        ExchangeRatesService exchangeRatesService = exchangeRatesServiceMap.get(country);
        if (exchangeRatesService == null) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE.formatted(country));
        }
        return exchangeRatesService.processConvertExchangeRatesAcrossExternalEndpoints(requestDto, externalEndpointEntity, posixDate, convertDateToUrl);
    }
}
