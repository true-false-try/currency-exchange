package com.npi.microservices.service;
import com.npi.microservices.dto.convert.ConvertRequestDto;
import com.npi.microservices.entity.ExchangeRatesEntity;
import com.npi.microservices.entity.ExternalEndpointEntity;
import com.npi.microservices.exception.DuplicatedDateException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface ExchangeRatesService {
    void processLoadExchangeRates(ExternalEndpointEntity externalEndpointEntity, Long posixDate, String convertDateToUrl) throws DuplicatedDateException;

    BigDecimal processConvertExchangeRates(ConvertRequestDto requestDto, ExchangeRatesEntity exchangeRates);

    BigDecimal processConvertExchangeRatesAcrossExternalEndpoints(ConvertRequestDto requestDto, ExternalEndpointEntity externalEndpointEntity,Long posixDate,  String convertDateToUrl);

    default BigDecimal amountResult(ConvertRequestDto requestDto, ExchangeRatesEntity exchangeRates) {
        Map<String, List<BigDecimal>> currencySourceAndCurrencyTargetMap = new HashMap<>();
        String currencySourceKey = "currencySource";
        String currencyTargetKey = "currencyTarget";

        currencySourceAndCurrencyTargetMap.put(currencySourceKey, (List<BigDecimal>) getRatesForCurrency(requestDto.getCurrencySource(), exchangeRates));
        currencySourceAndCurrencyTargetMap.put(currencyTargetKey, (List<BigDecimal>) getRatesForCurrency(requestDto.getCurrencyTarget(), exchangeRates));

        List<BigDecimal> currencySourceList = currencySourceAndCurrencyTargetMap.get(currencySourceKey);
        List<BigDecimal> currencyTargetList = currencySourceAndCurrencyTargetMap.get(currencyTargetKey);
        
        
        return requestDto.getAmount()
                .multiply(currencySourceList.get(0))
                .divide(currencySourceList.get(1), RoundingMode.HALF_UP)
                .divide(currencyTargetList.get(0), RoundingMode.HALF_UP)
                .multiply(currencyTargetList.get(1));
    }

    private List<? extends Serializable> getRatesForCurrency(String currencyCode, ExchangeRatesEntity exchangeRates) {
        return exchangeRates.getRates().stream()
                .filter(rate -> rate.getCurrencyCode().equals(currencyCode))
                .findFirst()
                .map(rate -> List.of(rate.getRate(), BigDecimal.valueOf(rate.getFactor())))
                .orElse(List.of(BigDecimal.ONE, BigDecimal.ONE));
    }

}
