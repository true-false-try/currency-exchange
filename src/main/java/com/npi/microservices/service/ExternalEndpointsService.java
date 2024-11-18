package com.npi.microservices.service;

import com.npi.microservices.entity.ExternalEndpointEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public interface ExternalEndpointsService {
    ExternalEndpointEntity getByCountry(String country) throws SQLException;
}
