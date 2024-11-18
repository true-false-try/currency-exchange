package com.npi.microservices.convertor;

import com.npi.microservices.exception.ConvertPosixException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.npi.microservices.constant.Constants.*;


@Slf4j
@Component
public class DateConvertor {

    public String posixConvertAtDateForUri(String country, Long posixDate) throws ConvertPosixException {
        String dateFormat = BEAN_TO_DATE_FORMAT.get(country);
        if (dateFormat == null) {
            throw new ConvertPosixException("Posix convert to date not success to country: %s".formatted(country));
        }
        ZonedDateTime dateTime = Instant.ofEpochSecond(posixDate).atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String convertDate = dateTime.format(formatter);
        log.info("Posix convert was successful for country: {}, date: {}", country, convertDate);
        return convertDate;
    }

    public String dateConvertFromExternalEndpointUriToExchangeRates(String country, String dateUri) {
        String dateFormat = BEAN_TO_DATE_FORMAT.get(country);

        LocalDate date = LocalDate.parse(dateUri, DateTimeFormatter.ofPattern(dateFormat));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TAMPLATE_DATE_TO_COOLECTION_EXCHANGE_RATES);
        String convertDate = date.format(formatter);

        log.info("Convert was successful for ExchangeRates for country: {}, date: {}", country, convertDate);
        return convertDate;
    }

}
