package com.revhire.gateway.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class GlobalExceptionHandler extends DefaultErrorAttributes {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = new HashMap<>();
        Throwable error = getError(request);

        log.error("Global exception handler caught error: ", error);

        errorAttributes.put("timestamp", System.currentTimeMillis());
        errorAttributes.put("path", request.path());
        errorAttributes.put("method", request.method().name());

        if (error instanceof org.springframework.web.server.ResponseStatusException) {
            org.springframework.web.server.ResponseStatusException ex =
                (org.springframework.web.server.ResponseStatusException) error;
            errorAttributes.put("status", ex.getStatusCode().value());
            errorAttributes.put("error", ex.getStatusCode().toString());
            errorAttributes.put("message", ex.getReason() != null ? ex.getReason() : "An error occurred");
        } else if (error instanceof org.springframework.cloud.gateway.support.NotFoundException) {
            errorAttributes.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
            errorAttributes.put("error", "Service Unavailable");
            errorAttributes.put("message", "Service is currently unavailable. Please try again later.");
        } else if (error instanceof io.netty.channel.ConnectTimeoutException ||
                   error.getCause() instanceof io.netty.channel.ConnectTimeoutException) {
            errorAttributes.put("status", HttpStatus.GATEWAY_TIMEOUT.value());
            errorAttributes.put("error", "Gateway Timeout");
            errorAttributes.put("message", "Service connection timeout. Please try again later.");
        } else {
            errorAttributes.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorAttributes.put("error", "Internal Server Error");
            errorAttributes.put("message", error.getMessage() != null ? error.getMessage() : "An unexpected error occurred");
        }

        return errorAttributes;
    }
}
