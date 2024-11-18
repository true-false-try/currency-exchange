package com.npi.microservices.service.impl;

import com.npi.microservices.convertor.CurrencyRateConvertor;
import com.npi.microservices.entity.ExternalEndpointEntity;
import com.npi.microservices.service.ExternalApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

import static com.npi.microservices.constant.Constants.CONTENT_TYPE_CSV;
import static com.npi.microservices.constant.Constants.REPLACER_DATE_FROM_URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalApiServiceImpl<T> implements ExternalApiService<T>{
    private final RestTemplate restTemplate;
    private final CurrencyRateConvertor currencyRateConvertor;

    @Override
    public List<T> getExternalResponse(ExternalEndpointEntity externalEndpointEntity, Long posixDate, String convertDateToUrl, ParameterizedTypeReference<List<T>> typeReference) {
        String url = insertDateInUrl(externalEndpointEntity.getUrl(), convertDateToUrl);
        HttpMethod httpMethod = HttpMethod.valueOf(externalEndpointEntity.getHttpMethod());
        log.info("Request API's {} bank : method:{}, url:{}",externalEndpointEntity.getCountry(), httpMethod, url);
        ResponseEntity<String> response = restTemplate.exchange(url, httpMethod, null, String.class);
        String contentType = Objects.requireNonNull(response.getHeaders().getContentType()).toString();

        if (contentType.contains(CONTENT_TYPE_CSV)) {
            return (List<T>) currencyRateConvertor.parseCSV(response.getBody(), posixDate);
        } else {
            ResponseEntity<List<T>> jsonResponse = restTemplate.exchange(url, httpMethod, null, typeReference);
            log.info("JSON response body: {}", response.getBody());
            return jsonResponse.getBody();
        }
    }

    private String insertDateInUrl(String url, String date) {
        return url.replace(REPLACER_DATE_FROM_URL, date);
    }
}
