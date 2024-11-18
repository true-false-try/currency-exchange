package com.npi.microservices.exception;

public class DuplicatedDateException extends Exception {

    public DuplicatedDateException(String message) {
        super(message);
    }
}
