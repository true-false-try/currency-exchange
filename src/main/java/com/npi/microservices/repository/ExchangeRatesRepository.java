package com.npi.microservices.repository;

import com.npi.microservices.entity.ExchangeRatesEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExchangeRatesRepository extends MongoRepository<ExchangeRatesEntity, ObjectId> {
    ExchangeRatesEntity findAllByCountryAndDate(String country, Long date);
}
