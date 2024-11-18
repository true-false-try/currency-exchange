package com.npi.microservices.service;

import com.npi.microservices.dto.convert.ConvertRequestDto;
import com.npi.microservices.dto.convert.ConvertResponseDto;
import com.npi.microservices.dto.load.LoadRatesRequestDto;
import com.npi.microservices.exception.ConvertPosixException;
import com.npi.microservices.exception.DuplicatedDateException;

import java.sql.SQLException;

public interface CurrencyExchangeService {
    void loadRates(LoadRatesRequestDto loadRatesRequestDto);
    ConvertResponseDto convert(ConvertRequestDto convertRequestDto);
}
