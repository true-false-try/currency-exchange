package com.npi.microservices.service;

import com.npi.microservices.dto.external_endpoint.ExternalEndpointResponseUaDto;
import com.npi.microservices.entity.ExternalEndpointEntity;
import com.npi.microservices.exception.ConvertPosixException;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

public interface ExternalApiService<T>{
    List<T> getExternalResponse(ExternalEndpointEntity entity, Long posixDate, String convertDateToUrl, ParameterizedTypeReference<List<T>> typeReference);
}
