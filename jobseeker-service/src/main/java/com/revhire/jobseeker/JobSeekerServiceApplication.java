package com.revhire.jobseeker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class JobSeekerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobSeekerServiceApplication.class, args);
    }
}
