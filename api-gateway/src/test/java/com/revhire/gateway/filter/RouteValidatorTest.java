package com.revhire.gateway.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;

import static org.junit.jupiter.api.Assertions.*;

class RouteValidatorTest {

    private RouteValidator routeValidator;

    @BeforeEach
    void setUp() {
        routeValidator = new RouteValidator();
    }

    @Test
    void testIsSecured_OpenEndpoint_Register() {
        ServerHttpRequest request = MockServerHttpRequest
                .get("/api/auth/register")
                .build();
        assertFalse(routeValidator.isSecured.test(request));
    }

    @Test
    void testIsSecured_OpenEndpoint_Login() {
        ServerHttpRequest request = MockServerHttpRequest
                .get("/api/auth/login")
                .build();
        assertFalse(routeValidator.isSecured.test(request));
    }

    @Test
    void testIsSecured_OpenEndpoint_Jobs() {
        ServerHttpRequest request = MockServerHttpRequest
                .get("/api/jobs")
                .build();
        assertFalse(routeValidator.isSecured.test(request));
    }

    @Test
    void testIsSecured_OpenEndpoint_Eureka() {
        ServerHttpRequest request = MockServerHttpRequest
                .get("/eureka/apps")
                .build();
        assertFalse(routeValidator.isSecured.test(request));
    }

    @Test
    void testIsSecured_OpenEndpoint_Actuator() {
        ServerHttpRequest request = MockServerHttpRequest
                .get("/actuator/health")
                .build();
        assertFalse(routeValidator.isSecured.test(request));
    }

    @Test
    void testIsSecured_SecuredEndpoint_Jobseeker() {
        ServerHttpRequest request = MockServerHttpRequest
                .get("/api/jobseeker/profile")
                .build();
        assertTrue(routeValidator.isSecured.test(request));
    }

    @Test
    void testIsSecured_SecuredEndpoint_Employer() {
        ServerHttpRequest request = MockServerHttpRequest
                .get("/api/employer/profile")
                .build();
        assertTrue(routeValidator.isSecured.test(request));
    }

    @Test
    void testIsSecured_SecuredEndpoint_Applications() {
        ServerHttpRequest request = MockServerHttpRequest
                .get("/api/applications")
                .build();
        assertTrue(routeValidator.isSecured.test(request));
    }
}
