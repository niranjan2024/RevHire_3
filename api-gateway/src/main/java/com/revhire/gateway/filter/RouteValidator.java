package com.revhire.gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

/**
 * Validates which routes require authentication
 * Open endpoints bypass JWT validation
 */
@Component
public class RouteValidator {

    /**
     * List of open API endpoints that don't require authentication
     */
    public static final List<String> openApiEndpoints = List.of(
            "/api/auth/register",
            "/api/auth/login",
            "/api/auth/refresh",
            "/eureka",
            "/actuator",
            "/api/jobs"  // GET requests to view jobs - will be validated in filter
    );

    /**
     * Predicate to check if a request requires authentication
     */
    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
