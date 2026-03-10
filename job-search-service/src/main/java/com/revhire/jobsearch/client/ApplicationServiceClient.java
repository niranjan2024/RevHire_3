package com.revhire.jobsearch.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign client to communicate with Application Service
 */
@FeignClient(name = "application-service")
public interface ApplicationServiceClient {

    /**
     * Check if a user has already applied to a specific job
     */
    @GetMapping("/api/internal/applications/check")
    Boolean hasUserApplied(@RequestParam("userId") Long userId, @RequestParam("jobId") Long jobId);

    /**
     * Get application count for a specific job
     */
    @GetMapping("/api/internal/applications/job/{jobId}/count")
    Long getApplicationCount(@PathVariable("jobId") Long jobId);
}
