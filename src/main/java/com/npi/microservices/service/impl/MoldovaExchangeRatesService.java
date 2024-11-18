package com.npi.microservices.service.impl;

import com.npi.microservices.dto.convert.ConvertRequestDto;
import com.npi.microservices.dto.external_endpoint.ExternalEndpointResponseMdDto;
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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.npi.microservices.constant.Constants.BEAN_MD;


@Slf4j
@Service
@RequiredArgsConstructor
public class MoldovaExchangeRatesService implements ExchangeRatesService {
    private final ExchangeRatesMapper mapper;
    private final CrudExchangeRatesService crudOperationExchangeRateService;
    private final ExternalApiService<ExternalEndpointResponseMdDto> externalApiService;

    @Override
    public void processLoadExchangeRates(@Qualifier(BEAN_MD) ExternalEndpointEntity externalEndpointEntity, Long posixDate, String convertDateToUrl) throws DuplicatedDateException {
        List<ExternalEndpointResponseMdDto> responseMdDtoList = externalApiService.getExternalResponse(externalEndpointEntity, posixDate, convertDateToUrl, new ParameterizedTypeReference<>(){});
        ExchangeRatesEntity exchangeRates = mapper.toExchangeRatesEntityMd(responseMdDtoList, posixDate);
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
        List<ExternalEndpointResponseMdDto> responseUaDtoList = externalApiService.getExternalResponse(externalEndpointEntity, posixDate, convertDateToUrl, new ParameterizedTypeReference<>() {
        });
        ExchangeRatesEntity exchangeRates = mapper.toExchangeRatesEntityMd(responseUaDtoList, posixDate);
        log.info("Before filter documents: {} ", exchangeRates);
        return amountResult(requestDto, exchangeRates);
    }



}
