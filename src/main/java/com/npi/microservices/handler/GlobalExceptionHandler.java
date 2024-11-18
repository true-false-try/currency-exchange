package com.npi.microservices.handler;


import com.npi.microservices.dto.ExceptionResponseDto;
import com.npi.microservices.exception.ConvertPosixException;
import com.npi.microservices.exception.CsvParseException;
import com.npi.microservices.exception.DuplicatedDateException;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.time.DateTimeException;
import java.util.ArrayList;

@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({HttpClientErrorException.class, IndexOutOfBoundsException.class})
    public ResponseEntity<Object> handleDataTimeException(Exception ex, WebRequest request) {
        String exceptionMessage = "Field date not correct";
        log.error(ex.getMessage(), ex);
        ExceptionResponseDto exceptionResponseDto = loadExceptionInList(String.valueOf(HttpStatus.BAD_REQUEST.value()), exceptionMessage);
        return handleExceptionInternal(ex, exceptionResponseDto, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(CsvParseException.class)
    public ResponseEntity<Object> handleDataTimeException(CsvParseException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ExceptionResponseDto exceptionResponseDto = loadExceptionInList(String.valueOf(HttpStatus.NOT_FOUND.value()), ex.getMessage());
        return handleExceptionInternal(ex, exceptionResponseDto, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ConvertPosixException.class)
    public ResponseEntity<Object> handleDataTimeException(ConvertPosixException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ExceptionResponseDto exceptionResponseDto = loadExceptionInList(String.valueOf(HttpStatus.NOT_FOUND.value()), ex.getMessage());
        return handleExceptionInternal(ex, exceptionResponseDto, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DuplicatedDateException.class)
    public ResponseEntity<Object> handleDuplicateDateException(DuplicatedDateException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ExceptionResponseDto exceptionResponseDto = loadExceptionInList(String.valueOf(HttpStatus.BAD_REQUEST.value()), ex.getMessage());
        return handleExceptionInternal(ex, exceptionResponseDto, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }


    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Object> handleSqlException(SQLException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ExceptionResponseDto exceptionResponseDto = loadExceptionInList(String.valueOf(HttpStatus.NOT_FOUND.value()), ex.getMessage());
        return handleExceptionInternal(ex, exceptionResponseDto, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NotNull MethodArgumentNotValidException ex,
                                                                  @NotNull HttpHeaders headers,
                                                                  @NotNull HttpStatusCode status,
                                                                  @NotNull WebRequest request) {
        log.error(ex.getMessage(), ex);
        String errorMessage = String.format("Validation error for field '%s'.",
                ex.getBindingResult().getFieldErrors().get(0).getField());

        return handleExceptionInternal(ex,
                loadExceptionInList(String.valueOf(HttpStatus.BAD_REQUEST.value()), errorMessage),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NotNull HttpMessageNotReadableException ex,
                                                                  @NotNull HttpHeaders headers,
                                                                  @NotNull HttpStatusCode status,
                                                                  @NotNull WebRequest request) {
        log.error(ex.getMessage(), ex);
        return handleExceptionInternal(ex,
                loadExceptionInList(String.valueOf(HttpStatus.BAD_REQUEST.value()), ex.getMessage()),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }


    private ExceptionResponseDto loadExceptionInList(String code, String message) {
        ArrayList<ExceptionResponseDto.ErrorDetails> errorList = new ArrayList<>();
        errorList.add(new ExceptionResponseDto.ErrorDetails(code, message));
        return new ExceptionResponseDto(errorList);
    }


}

