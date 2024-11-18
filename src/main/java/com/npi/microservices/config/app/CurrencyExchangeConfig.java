package com.npi.microservices.config.app;

import com.npi.microservices.dto.external_endpoint.ExternalEndpointResponseMdDto;
import com.npi.microservices.dto.external_endpoint.ExternalEndpointResponsePlDto;
import com.npi.microservices.dto.external_endpoint.ExternalEndpointResponseUaDto;
import com.npi.microservices.mapper.ExchangeRatesMapper;
import com.npi.microservices.service.ExchangeRatesService;
import com.npi.microservices.service.CrudExchangeRatesService;
import com.npi.microservices.service.ExternalApiService;
import com.npi.microservices.service.impl.MoldovaExchangeRatesService;
import com.npi.microservices.service.impl.PolandExchangeRatesService;
import com.npi.microservices.service.impl.UkraineExchangeRatesService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import static com.npi.microservices.constant.Constants.*;

@Configuration
public class CurrencyExchangeConfig {

    @Bean(BEAN_UA)
    public ExchangeRatesService ukraineService(ExchangeRatesMapper mapper,
                                               CrudExchangeRatesService exchangeRatesService,
                                               ExternalApiService<ExternalEndpointResponseUaDto> externalApiService) {
        return new UkraineExchangeRatesService(mapper, exchangeRatesService, externalApiService);
    }

    @Bean(BEAN_MD)
    public ExchangeRatesService moldovaService(ExchangeRatesMapper mapper,
                                               CrudExchangeRatesService exchangeRatesService,
                                               ExternalApiService<ExternalEndpointResponseMdDto> externalApiService) {
        return new MoldovaExchangeRatesService(mapper, exchangeRatesService, externalApiService);
    }

    @Bean(BEAN_PL)
    public ExchangeRatesService polandService(ExchangeRatesMapper mapper,
                                              CrudExchangeRatesService exchangeRatesService,
                                              ExternalApiService<ExternalEndpointResponsePlDto> externalApiService) {
        return new PolandExchangeRatesService(mapper, exchangeRatesService, externalApiService);
    }

}
