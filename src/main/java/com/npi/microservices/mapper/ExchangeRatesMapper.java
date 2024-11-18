package com.npi.microservices.mapper;

import com.npi.microservices.dto.external_endpoint.ExternalEndpointResponseMdDto;
import com.npi.microservices.dto.external_endpoint.ExternalEndpointResponsePlDto;
import com.npi.microservices.dto.external_endpoint.ExternalEndpointResponseUaDto;
import com.npi.microservices.entity.ExchangeRatesEntity;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.npi.microservices.constant.Constants.*;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ExchangeRatesMapper {

    // UA (Ukraine)
    @Mapping(target = "currencyCode", source = "cc")
    @Mapping(target = "rate", expression = "java(Double.valueOf(dto.getRate().doubleValue()))")
    @Mapping(target = "factor", constant = "1")
    ExchangeRatesEntity.Rate toRatesUa(ExternalEndpointResponseUaDto dto);

    // MD (Moldova)
    @Mapping(target = "currencyCode", source = "shortCode")
    @Mapping(target = "rate", expression = "java(Double.valueOf(dto.getRate().doubleValue()))")
    @Mapping(target = "factor", expression = "java(dto.getNominal())")
    ExchangeRatesEntity.Rate toRatesMd(ExternalEndpointResponseMdDto dto);

    // PL (Poland)
    @Mapping(target = "currencyCode", source = "code")
    @Mapping(target = "rate", source = "mid")
    @Mapping(target = "factor", constant = "1")
    ExchangeRatesEntity.Rate toRatesPl(ExternalEndpointResponsePlDto.Rate dto);

    default ExchangeRatesEntity toExchangeRatesEntityUa(List<ExternalEndpointResponseUaDto> dtoList, Long posixDate) {

        Set<ExchangeRatesEntity.Rate> rates = dtoList.stream()
                .map(this::toRatesUa)
                .collect(Collectors.toSet());

        ExchangeRatesEntity entity = new ExchangeRatesEntity();

        entity.setCountry(BEAN_UA);
        entity.setDate(posixDate);
        entity.setRates(rates);

        return entity;
    }

    default ExchangeRatesEntity toExchangeRatesEntityPl(List<ExternalEndpointResponsePlDto> dtoList, Long posixDate) {

        Set<ExchangeRatesEntity.Rate> rates = dtoList.stream()
                .flatMap(dto -> dto.getRates().stream())
                .map(this::toRatesPl)
                .collect(Collectors.toSet());

        ExchangeRatesEntity entity = new ExchangeRatesEntity();

        entity.setCountry(BEAN_PL);
        entity.setDate(posixDate);
        entity.setRates(rates);
        return entity;
    }

    default ExchangeRatesEntity toExchangeRatesEntityMd(List<ExternalEndpointResponseMdDto> dtoList, Long posixDate) {

        Set<ExchangeRatesEntity.Rate> rates = dtoList.stream()
                .map(this::toRatesMd)
                .collect(Collectors.toSet());


        ExchangeRatesEntity entity = new ExchangeRatesEntity();

        entity.setCountry(BEAN_MD);
        entity.setDate(posixDate);
        entity.setRates(rates);

        return entity;
    }

}