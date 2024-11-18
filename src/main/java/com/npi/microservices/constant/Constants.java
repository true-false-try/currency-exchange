package com.npi.microservices.constant;

import java.util.*;

public class Constants {

    /*Endpoint*/
    public static final String  ENDPOINT_CURRENCY_EXCHANGE_REQUEST_MAPPING = "currency_exchange";
    public static final String  ENDPOINT_CURRENCY_EXCHANGE_MAPPING_CONVERT = "convert";
    public static final String  ENDPOINT_CURRENCY_EXCHANGE_MAPPING_LOAD_RATES = "load_rates";

    /** Fields to ConvertRequestDto **/
    public static final String FIELD_CURRENCY_SOURCE = "currency_source";
    public static final String FIELD_CURRENCY_TARGET = "currency_target";


    /*Bean as countries from request*/
    public static final String BEAN_UA = "UA";
    public static final String BEAN_MD = "MD";
    public static final String BEAN_PL = "PL";

    /*Date convert pattern for uri*/
    public static final String CONVERT_DATE_UA = "yyyyMMdd";
    public static final String CONVERT_DATE_MD = "dd.MM.yyyy";
    public static final String CONVERT_DATE_PL = "yyyy-MM-dd";

    /*Map to associate beans with date formats*/
    public static final Map<String, String> BEAN_TO_DATE_FORMAT = new HashMap<>(
            Map.of(BEAN_UA, CONVERT_DATE_UA, BEAN_MD, CONVERT_DATE_MD, BEAN_PL, CONVERT_DATE_PL)
    );

    /**Common date pattern to ExchangeRates**/
    public static final String TAMPLATE_DATE_TO_COOLECTION_EXCHANGE_RATES = "yyyy-MM-dd";

    /*CSV fields*/
    public static final String CSV_FIELD_DATE = "Дата";
    public static final String CSV_FIELD_CURRENCY = "Валюта";
    public static final String CSV_FIELD_CODE = "Код";
    public static final String CSV_FIELD_SHORT_CODE = "Сокр";
    public static final String CSV_FIELD_NOMINAL = "Номинал";
    public static final String CSV_FIELD_RATE = "Курс";

    /*Http*/
    public static final String CONTENT_TYPE_CSV = "application/csv";

    /**ExchangeRateEntity**/
    public static final String COLLECTION_NAME = "ExchangeRates";
    public static final String FIELD_CURRENCY_CODE = "currency_code";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_COUNTRY = "country";

    /**ExternalEndpointEntity**/
    public static final String TABLE_NAME = "external_endpoints";
    public static final String COLUMN_HTTP_METHOD = "http_method";

    /**Validation**/
    //Fields witch was reserved in ISO 4217
    public static final String RESERVED_XXX = "XXX";
    public static final String RESERVED_XTS= "XTS";

    /*Replacer*/
    public static final String REPLACER_DATE_FROM_URL = "{date}";

    /*Logging*/
    public static final String ACTUATOR = "/actuator";
    public static final String ACTUATOR_PROMETHEUS = "/actuator/prometheus";


}
