package com.npi.microservices.service.impl;

import com.npi.microservices.convertor.DateConvertor;
import com.npi.microservices.entity.ExchangeRatesEntity;
import com.npi.microservices.exception.ConvertPosixException;
import com.npi.microservices.exception.DuplicatedDateException;
import com.npi.microservices.repository.ExchangeRatesRepository;
import com.npi.microservices.service.CrudExchangeRatesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;



@Slf4j
@Service
@RequiredArgsConstructor
public class CrudExchangeRatesServiceImpl implements CrudExchangeRatesService {
    private final ExchangeRatesRepository repository;
    @Override
    public void insert(ExchangeRatesEntity entity) throws DuplicatedDateException {
        try {
            repository.insert(entity);
            log.info("Successfully load 1 document with {} rates", entity.getRates().size());
        } catch(RuntimeException ex) {
            throw new DuplicatedDateException(ex.getMessage());
        }
    }

    @Override
    public ExchangeRatesEntity getRates(String country, Long date) {
        ExchangeRatesEntity exchangeRates = repository.findAllByCountryAndDate(country, date);
        log.info("Success 1 document equal criteria country: {}, date: {}", country, date);
        log.info("Document: {}", exchangeRates);
        return exchangeRates;
    }
}
