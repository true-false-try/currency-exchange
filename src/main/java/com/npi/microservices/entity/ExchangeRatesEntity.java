package com.npi.microservices.entity;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Set;

import static com.npi.microservices.constant.Constants.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = COLLECTION_NAME)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExchangeRatesEntity {
    @Id
    ObjectId id;
    Long date;
    String country;
    Set<Rate> rates;

    @Getter
    @Setter
    @ToString
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Rate {
        @Field(FIELD_CURRENCY_CODE)
        String currencyCode;
        Integer factor;
        Double rate;
    }

}
