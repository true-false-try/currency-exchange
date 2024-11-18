package com.npi.microservices.service.impl;

import com.npi.microservices.convertor.DateConvertor;
import com.npi.microservices.dto.convert.ConvertRequestDto;
import com.npi.microservices.dto.convert.ConvertResponseDto;
import com.npi.microservices.dto.load.LoadRatesRequestDto;
import com.npi.microservices.entity.ExchangeRatesEntity;
import com.npi.microservices.entity.ExternalEndpointEntity;
import com.npi.microservices.exception.ConvertPosixException;
import com.npi.microservices.exception.DuplicatedDateException;
import com.npi.microservices.processor.ExchangeRatesProcessor;
import com.npi.microservices.service.CrudExchangeRatesService;
import com.npi.microservices.service.CurrencyExchangeService;
import com.npi.microservices.service.ExternalEndpointsService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLException;


@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

    private final DateConvertor dateConvertor;
    private final ExchangeRatesProcessor processor;
    private final CrudExchangeRatesService crudExchangeRatesService;
    private final ExternalEndpointsService microCurrencyExchangeService;

    @Override
    @Transactional
    @SneakyThrows({DuplicatedDateException.class, SQLException.class, ConvertPosixException.class})
    public void loadRates(LoadRatesRequestDto loadRatesRequestDto) {
        try {
            String country = loadRatesRequestDto.getCountry();
            Long posixDate = loadRatesRequestDto.getDate();
            String posixConvertToUrl = dateConvertor.posixConvertAtDateForUri(country, posixDate);
            ExternalEndpointEntity externalEndpointEntity = microCurrencyExchangeService.getByCountry(country);
            processor.processLoadExchangeRates(externalEndpointEntity, posixDate, posixConvertToUrl);
        } catch (DuplicatedDateException | SQLException | ConvertPosixException ex) {
            log.error("Transaction has exception: {}", ex.getClass());
            throw ex;
        }
    }
    @Override
    @Transactional(readOnly = true)
    @SneakyThrows({SQLException.class, ConvertPosixException.class})
    public ConvertResponseDto convert(ConvertRequestDto convertRequestDto) {
        try {
            String country = convertRequestDto.getCountry();
            Long posixDate = convertRequestDto.getDate();
            String posixConvert = dateConvertor.posixConvertAtDateForUri(country, posixDate);
            String convertToCollection = dateConvertor.dateConvertFromExternalEndpointUriToExchangeRates(country, posixConvert);
            ExchangeRatesEntity exchangeRatesEntity = crudExchangeRatesService.getRates(country, convertRequestDto.getDate());

            if (exchangeRatesEntity == null) {
                ExternalEndpointEntity externalEndpointEntity = microCurrencyExchangeService.getByCountry(country);
                BigDecimal amountResponse = processor.processConvertExchangeRatesAcrossExternalEndpoints(convertRequestDto, externalEndpointEntity, posixDate, convertToCollection);
                return new ConvertResponseDto(amountResponse);
            } else {
                ExchangeRatesEntity exchangeRates = crudExchangeRatesService.getRates(country, convertRequestDto.getDate());
                BigDecimal amountResponse = processor.processConvertExchangeRates(convertRequestDto,exchangeRates,country);
                return new ConvertResponseDto(amountResponse);
            }
        }
        catch (SQLException | ConvertPosixException ex) {
                log.error("Transaction has exception: {}", ex.getClass());
                throw ex;
            }
    }
}
