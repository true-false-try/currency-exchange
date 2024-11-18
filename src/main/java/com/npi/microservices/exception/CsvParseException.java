package com.npi.microservices.exception;

public class CsvParseException extends RuntimeException {
    public CsvParseException(String message) {
        super(message);
    }
}
