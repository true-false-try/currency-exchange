package com.npi.microservices.service.impl;

import com.npi.microservices.entity.ExternalEndpointEntity;
import com.npi.microservices.repository.ExternalEndpointsRepository;
import com.npi.microservices.service.ExternalEndpointsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Optional;

import static com.npi.microservices.constant.Constants.TABLE_NAME;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalEndpointsServiceImpl implements ExternalEndpointsService {
    private final ExternalEndpointsRepository repository;

    @Override
    public ExternalEndpointEntity getByCountry(String country) throws SQLException {
        Optional<ExternalEndpointEntity> externalEndpoint =  repository.getByCountry(country);
        if (externalEndpoint.isEmpty()) {
            throw new SQLException("Don't find country in table: %s".formatted(TABLE_NAME));
        } else {
            log.info("Have this endpoint: {}.", externalEndpoint.get());
            return externalEndpoint.get();
        }
    }
}
