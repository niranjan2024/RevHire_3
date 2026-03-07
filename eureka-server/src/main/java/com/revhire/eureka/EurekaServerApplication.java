package com.revhire.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka Service Discovery Server for RevHire Microservices Platform
 *
 * This application provides service registration and discovery capabilities
 * for all RevHire microservices including:
 * - Auth Service
 * - Job Service
 * - JobSeeker Service
 * - Employer Service
 * - Application Service
 * - Notification Service
 * - API Gateway
 *
 * All microservices will register themselves with this Eureka server
 * and can discover other services through it.
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }

}
