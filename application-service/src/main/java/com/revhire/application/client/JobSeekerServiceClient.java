package com.revhire.application.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "jobseeker-service")
public interface JobSeekerServiceClient {

    @GetMapping("/internal/jobseekers/{jobSeekerId}/resume/exists")
    Boolean hasResume(@PathVariable("jobSeekerId") Long jobSeekerId);

    @GetMapping("/internal/jobseekers/{jobSeekerId}/validate")
    Boolean validateJobSeeker(@PathVariable("jobSeekerId") Long jobSeekerId);
}
