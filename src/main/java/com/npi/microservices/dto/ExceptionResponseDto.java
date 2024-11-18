package com.npi.microservices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionResponseDto {
    private List<ErrorDetails> errors;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ErrorDetails {
        private  String code;
        private  String message;

    }
}
