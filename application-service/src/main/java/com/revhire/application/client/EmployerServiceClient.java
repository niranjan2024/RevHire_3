package com.revhire.application.client;

import com.revhire.application.dto.JobDetailsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employer-service")
public interface EmployerServiceClient {

    @GetMapping("/internal/jobs/{jobId}")
    JobDetailsResponse getJobDetails(@PathVariable("jobId") Long jobId);

    @GetMapping("/internal/jobs/{jobId}/validate")
    Boolean validateJob(@PathVariable("jobId") Long jobId);

    @GetMapping("/internal/jobs/{jobId}/employer")
    Long getJobEmployerId(@PathVariable("jobId") Long jobId);
}
