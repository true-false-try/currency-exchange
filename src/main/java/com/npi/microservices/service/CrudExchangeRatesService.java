package com.npi.microservices.service;

import com.npi.microservices.entity.ExchangeRatesEntity;
import com.npi.microservices.exception.ConvertPosixException;
import com.npi.microservices.exception.DuplicatedDateException;
import org.springframework.core.convert.ConverterNotFoundException;

import java.util.Optional;

public interface CrudExchangeRatesService {
    void insert(ExchangeRatesEntity externalResponse) throws DuplicatedDateException;
    ExchangeRatesEntity getRates(String country, Long date);

}
