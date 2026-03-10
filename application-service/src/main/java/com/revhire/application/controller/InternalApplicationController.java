package com.revhire.application.controller;

import com.revhire.application.dto.ApplicationResponse;
import com.revhire.application.dto.ApplicationStatisticsResponse;
import com.revhire.application.service.ApplicationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/applications")
public class InternalApplicationController {

    private final ApplicationService applicationService;

    public InternalApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/job/{jobId}")
    public List<ApplicationResponse> getApplicationsByJob(@PathVariable Long jobId) {
        return applicationService.getApplicationsByJob(jobId);
    }

    @GetMapping("/jobseeker/{jobSeekerId}")
    public List<ApplicationResponse> getApplicationsByJobSeeker(@PathVariable Long jobSeekerId) {
        return applicationService.getApplicationsBySeeker(jobSeekerId);
    }

    @GetMapping("/{applicationId}")
    public ApplicationResponse getApplication(@PathVariable Long applicationId) {
        return applicationService.getApplication(applicationId);
    }

    @GetMapping("/job/{jobId}/count")
    public Long getApplicationCount(@PathVariable Long jobId) {
        ApplicationStatisticsResponse stats = applicationService.getApplicationStatistics(jobId);
        return stats.getTotalApplications();
    }

    @GetMapping("/job/{jobId}/statistics")
    public ApplicationStatisticsResponse getStatistics(@PathVariable Long jobId) {
        return applicationService.getApplicationStatistics(jobId);
    }

    @GetMapping("/jobseeker/{jobSeekerId}/applied-jobs")
    public List<Long> getAppliedJobIds(@PathVariable Long jobSeekerId) {
        return applicationService.getAppliedJobIds(jobSeekerId);
    }
}
