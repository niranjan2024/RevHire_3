package com.revature.jobservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "EMPLOYER-SERVICE")
public interface EmployerServiceClient {

    @GetMapping("/api/employers/{employerId}")
    Map<String, Object> getEmployerDetails(@PathVariable Long employerId);
}
