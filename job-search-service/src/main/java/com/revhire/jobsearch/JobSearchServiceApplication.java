package com.revhire.jobsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Job Search Service - Main Application
 * Developer: Anil
 * Responsibility: Job Search and Filtering
 */
@SpringBootApplication
@EnableFeignClients
public class JobSearchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobSearchServiceApplication.class, args);
    }
}
