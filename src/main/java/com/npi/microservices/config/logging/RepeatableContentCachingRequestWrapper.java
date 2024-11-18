package com.npi.microservices.config.logging;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

public class RepeatableContentCachingRequestWrapper extends ContentCachingRequestWrapper {
    private SimpleServletInputStream inputStream;

    public RepeatableContentCachingRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public @NotNull ServletInputStream getInputStream() {
        return this.inputStream;
    }

    public String readInputAndDuplicate() throws IOException {
        if (inputStream == null) {
            byte[] body = super.getInputStream().readAllBytes();
            this.inputStream = new SimpleServletInputStream(body);
        }
        return new String(super.getContentAsByteArray());
    }
}