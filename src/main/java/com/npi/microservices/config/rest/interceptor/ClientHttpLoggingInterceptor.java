package com.npi.microservices.config.rest.interceptor;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ClientHttpLoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public @NotNull ClientHttpResponse intercept(@NotNull HttpRequest request, byte @NotNull [] body, ClientHttpRequestExecution execution) throws IOException {
        traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        try {
            traceResponse(response);
        } catch (IOException e) {
            log.warn("ClientHttpLoggingInterceptor exception: {}", e.getMessage(), e);
        }
        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body) {
        log.info("path: {}, method: {}, body: {}, headers: {}, type: request",
                request.getURI(),
                request.getMethod(),
                new String(body, StandardCharsets.UTF_8),
                request.getHeaders());
    }

    private void traceResponse(ClientHttpResponse response) throws IOException {
        log.info("status: {}, body: {}, type: response",
                response.getStatusCode(),
                getBody(response));
    }

    private String getBody(ClientHttpResponse response) throws IOException  {
        StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8));
        String line = bufferedReader.readLine();
        while (line != null) {
            inputStringBuilder.append(line);
            inputStringBuilder.append('\n');
            line = bufferedReader.readLine();
        }
        return inputStringBuilder.toString();
    }

}
