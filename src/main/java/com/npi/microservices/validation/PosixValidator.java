package com.npi.microservices.validation;

import com.npi.microservices.validation.annotation.Posix;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class PosixValidator implements ConstraintValidator<Posix, Object> {
    private static final String METHOD_GET_DATE = "getDate";
    private static final String METHOD_SET_DATE = "setDate";
    private static final Logger log = LoggerFactory.getLogger(PosixValidator.class);

    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        Long date = getDateFromDto(dto);
        if (date == null || date == 0) {
            Long dateAtNow = Instant.now().getEpochSecond();
            setDateForDto(dto,  dateAtNow);
            log.info("Loaded posix date have been setting from current date, data {}", dateAtNow);
            return true;
        }
        log.info("Loaded posix date have been set from the request, data: {}", date);
        return true;
    }

    private Long getDateFromDto(Object dto) {
        try {
            return (Long) dto.getClass().getMethod(METHOD_GET_DATE).invoke(dto);
        } catch (Exception ex) {
            log.error("Failed to get date from DTO", ex);
            return null;
        }
    }

    private void setDateForDto(Object dto, Long date) {
        try {
            dto.getClass().getMethod(METHOD_SET_DATE, Long.class).invoke(dto, date);
        } catch (Exception ex) {
            log.error("Failed to set date for DTO", ex);
        }
    }

}