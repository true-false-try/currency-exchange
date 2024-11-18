package com.npi.microservices.repository;

import com.npi.microservices.entity.ExternalEndpointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.Optional;

@Repository
public interface ExternalEndpointsRepository extends JpaRepository<ExternalEndpointEntity, Integer> {
    Optional<ExternalEndpointEntity> getByCountry(String country) throws SQLException;
}
