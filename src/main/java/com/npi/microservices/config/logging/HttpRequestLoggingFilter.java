package com.npi.microservices.config.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.npi.microservices.constant.Constants.ACTUATOR;
import static com.npi.microservices.constant.Constants.ACTUATOR_PROMETHEUS;

@Slf4j
@Component
public class HttpRequestLoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) {
        try {
            RepeatableContentCachingRequestWrapper requestWrapper = new RepeatableContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

            logRequest(requestWrapper);
            filterChain.doFilter(requestWrapper, responseWrapper);
            logResponse(responseWrapper);
        } catch (IOException | ServletException e) {
            log.warn("HttpRequestLoggingFilter exception: {}", e.getMessage(), e);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return (ACTUATOR.equals(path) || ACTUATOR_PROMETHEUS.equals(path));
    }

    private void logRequest(RepeatableContentCachingRequestWrapper requestWrapper) throws IOException {
        log.info("server: {}, path: {}, method: {}, body: {}, headers: {}, type: request",
                requestWrapper.getServerName(),
                requestWrapper.getRequestURI(),
                requestWrapper.getMethod(),
                requestWrapper.readInputAndDuplicate(),
                getHeaders(requestWrapper));
    }

    private Map<String, String> getHeaders(RepeatableContentCachingRequestWrapper requestWrapper) {
        Enumeration<String> headerNames = requestWrapper.getHeaderNames();
        Map<String, String> headers = new HashMap<>();
        while(headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, requestWrapper.getHeader(name));
        }
        return headers;
    }

    private void logResponse(ContentCachingResponseWrapper responseWrapper) throws IOException {
        log.info("status: {}, body: {}, type: response",
                responseWrapper.getStatus(),
                new String(responseWrapper.getContentAsByteArray()));
        responseWrapper.copyBodyToResponse();
    }

}
