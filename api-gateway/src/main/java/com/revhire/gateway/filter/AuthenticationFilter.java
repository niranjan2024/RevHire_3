package com.revhire.gateway.filter;

import com.revhire.gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Global filter for JWT authentication
 * Validates JWT tokens and forwards user information to downstream services
 */
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Skip authentication for open endpoints
        if (routeValidator.isSecured.test(request)) {
            // Special case: Allow GET requests to /api/jobs without authentication
            if (request.getURI().getPath().startsWith("/api/jobs") &&
                request.getMethod() == HttpMethod.GET) {
                return chain.filter(exchange);
            }

            // Check if Authorization header is present
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "Missing authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            } else {
                return onError(exchange, "Invalid authorization header format", HttpStatus.UNAUTHORIZED);
            }

            try {
                // Validate JWT token
                jwtUtil.validateToken(authHeader);

                // Extract user information from token
                String username = jwtUtil.extractUsername(authHeader);
                String role = jwtUtil.extractRole(authHeader);

                // Add user information to request headers for downstream services
                ServerHttpRequest modifiedRequest = exchange.getRequest()
                        .mutate()
                        .header("X-User-Username", username)
                        .header("X-User-Role", role)
                        .build();

                return chain.filter(exchange.mutate().request(modifiedRequest).build());

            } catch (Exception e) {
                return onError(exchange, "Unauthorized access: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
            }
        }

        return chain.filter(exchange);
    }

    /**
     * Helper method to return error response
     */
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1; // Highest priority
    }
}
