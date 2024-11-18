package com.npi.microservices.controller;



import com.npi.microservices.dto.convert.ConvertRequestDto;
import com.npi.microservices.dto.convert.ConvertResponseDto;
import com.npi.microservices.dto.load.LoadRatesRequestDto;
import com.npi.microservices.service.CurrencyExchangeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import static com.npi.microservices.constant.Constants.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(ENDPOINT_CURRENCY_EXCHANGE_REQUEST_MAPPING)
public class CurrencyExchangeController {

    private final CurrencyExchangeService currencyExchangeService;

    @PostMapping(value = ENDPOINT_CURRENCY_EXCHANGE_MAPPING_CONVERT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConvertResponseDto> convert(@Valid @RequestBody ConvertRequestDto requestDto) {
        return ResponseEntity.ok().body(currencyExchangeService.convert(requestDto));
    }

    @PostMapping(value = ENDPOINT_CURRENCY_EXCHANGE_MAPPING_LOAD_RATES, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> loadRates(@Valid @RequestBody LoadRatesRequestDto requestDto) {
        currencyExchangeService.loadRates(requestDto);
        return ResponseEntity.ok().build();
    }


}
