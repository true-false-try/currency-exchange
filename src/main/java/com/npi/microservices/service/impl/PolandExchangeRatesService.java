package com.npi.microservices.service.impl;

import com.npi.microservices.dto.convert.ConvertRequestDto;
import com.npi.microservices.dto.external_endpoint.ExternalEndpointResponsePlDto;
import com.npi.microservices.entity.ExchangeRatesEntity;
import com.npi.microservices.entity.ExternalEndpointEntity;
import com.npi.microservices.exception.DuplicatedDateException;
import com.npi.microservices.mapper.ExchangeRatesMapper;
import com.npi.microservices.service.CrudExchangeRatesService;
import com.npi.microservices.service.ExchangeRatesService;
import com.npi.microservices.service.ExternalApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static com.npi.microservices.constant.Constants.BEAN_UA;

@Slf4j
@Component
@RequiredArgsConstructor
public class PolandExchangeRatesService implements ExchangeRatesService {
    private final ExchangeRatesMapper mapper;
    private final CrudExchangeRatesService crudOperationExchangeRateService;
    private final ExternalApiService<ExternalEndpointResponsePlDto> externalApiService;

    @Override
    public void processLoadExchangeRates(@Qualifier(BEAN_UA) ExternalEndpointEntity externalEndpointEntity, Long posixDate, String convertDateToUrl) throws DuplicatedDateException {
        List<ExternalEndpointResponsePlDto> responseUaDtoList = externalApiService.getExternalResponse(externalEndpointEntity, posixDate, convertDateToUrl, new ParameterizedTypeReference<>(){});
        ExchangeRatesEntity exchangeRates = mapper.toExchangeRatesEntityPl(responseUaDtoList, posixDate);
        log.info("Before save documents: {} ", exchangeRates);
        crudOperationExchangeRateService.insert(exchangeRates);
    }

    @Override
    public BigDecimal processConvertExchangeRates(ConvertRequestDto requestDto, ExchangeRatesEntity exchangeRates) {
        log.info("Before filter documents: {} ", exchangeRates);
        return amountResult(requestDto, exchangeRates);
    }

    @Override
    public BigDecimal processConvertExchangeRatesAcrossExternalEndpoints(ConvertRequestDto requestDto, ExternalEndpointEntity externalEndpointEntity, Long posixDate, String convertDateToUrl) {
        List<ExternalEndpointResponsePlDto> responseUaDtoList = externalApiService.getExternalResponse(externalEndpointEntity, posixDate, convertDateToUrl, new ParameterizedTypeReference<>() {
        });
        ExchangeRatesEntity exchangeRates = mapper.toExchangeRatesEntityPl(responseUaDtoList, posixDate);
        log.info("Before filter documents: {} ", exchangeRates);
        return amountResult(requestDto, exchangeRates);
    }


}
