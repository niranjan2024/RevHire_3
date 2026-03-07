package com.revhire.application.controller;

import com.revhire.application.dto.ApplicationResponse;
import com.revhire.application.dto.ApplicationStatisticsResponse;
import com.revhire.application.dto.ApplicationStatusUpdateRequest;
import com.revhire.application.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employer")
public class EmployerApplicationController {

    private final ApplicationService applicationService;

    public EmployerApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/jobs/{jobId}/applications")
    public List<ApplicationResponse> getJobApplications(
            @RequestHeader("X-User-Id") Long employerId,
            @PathVariable Long jobId) {
        // Note: Job ownership validation happens in the service layer via Feign client
        return applicationService.getApplicationsByJob(jobId);
    }

    @GetMapping("/applications/{applicationId}")
    public ApplicationResponse getApplication(
            @RequestHeader("X-User-Id") Long employerId,
            @PathVariable Long applicationId) {
        // Note: Ownership validation can be added if needed
        return applicationService.getApplication(applicationId);
    }

    @PatchMapping("/applications/{applicationId}/status")
    public ApplicationResponse updateApplicationStatus(
            @RequestHeader("X-User-Id") Long employerId,
            @PathVariable Long applicationId,
            @Valid @RequestBody ApplicationStatusUpdateRequest request) {
        return applicationService.updateStatus(employerId, applicationId, request);
    }

    @GetMapping("/jobs/{jobId}/statistics")
    public ApplicationStatisticsResponse getApplicationStatistics(
            @RequestHeader("X-User-Id") Long employerId,
            @PathVariable Long jobId) {
        return applicationService.getApplicationStatistics(jobId);
    }
}
