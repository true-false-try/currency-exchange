package com.npi.microservices.dto.external_endpoint;

import com.npi.microservices.convertor.CommaDecimalConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

import static com.npi.microservices.constant.Constants.*;


@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExternalEndpointResponseMdDto {

    Long date;

    @CsvBindByName(column = CSV_FIELD_CURRENCY)
    String currency;

    @CsvBindByName(column = CSV_FIELD_CODE)
    String code;

    @CsvBindByName(column = CSV_FIELD_SHORT_CODE)
    String shortCode;

    @CsvBindByName(column = CSV_FIELD_NOMINAL)
    int nominal;

    @CsvCustomBindByName(column = CSV_FIELD_RATE, converter = CommaDecimalConverter.class)
    BigDecimal rate;
}
