package com.revhire.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Gateway configuration for routing requests to microservices
 * Uses Spring Cloud Gateway to route based on path patterns
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth Service Routes
                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .uri("lb://AUTH-SERVICE"))

                // Jobseeker Service Routes
                .route("jobseeker-service", r -> r
                        .path("/api/jobseeker/**")
                        .uri("lb://JOBSEEKER-SERVICE"))

                // Resume Routes (also handled by Jobseeker Service)
                .route("resume-service", r -> r
                        .path("/api/resume/**")
                        .uri("lb://JOBSEEKER-SERVICE"))

                // Employer Service Routes
                .route("employer-service", r -> r
                        .path("/api/employer/**")
                        .uri("lb://EMPLOYER-SERVICE"))

                // Job Service Routes
                .route("job-service", r -> r
                        .path("/api/jobs/**")
                        .uri("lb://JOB-SERVICE"))

                // Application Service Routes
                .route("application-service", r -> r
                        .path("/api/applications/**")
                        .uri("lb://APPLICATION-SERVICE"))

                // Notification Service Routes
                .route("notification-service", r -> r
                        .path("/api/notifications/**")
                        .uri("lb://NOTIFICATION-SERVICE"))

                .build();
    }
}
