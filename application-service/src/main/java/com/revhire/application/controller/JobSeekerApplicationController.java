package com.revhire.application.controller;

import com.revhire.application.dto.ApiResponse;
import com.revhire.application.dto.ApplicationResponse;
import com.revhire.application.dto.ApplyRequest;
import com.revhire.application.dto.WithdrawRequest;
import com.revhire.application.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobseeker")
public class JobSeekerApplicationController {

    private final ApplicationService applicationService;

    public JobSeekerApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/jobs/apply")
    public ApplicationResponse apply(
            @RequestHeader("X-User-Id") Long jobSeekerId,
            @Valid @RequestBody ApplyRequest request) {
        return applicationService.apply(jobSeekerId, request);
    }

    @PostMapping("/applications/{applicationId}/withdraw")
    public ApplicationResponse withdraw(
            @RequestHeader("X-User-Id") Long jobSeekerId,
            @PathVariable Long applicationId,
            @Valid @RequestBody WithdrawRequest request) {
        return applicationService.withdraw(jobSeekerId, applicationId, request);
    }

    @GetMapping("/applications")
    public List<ApplicationResponse> getMyApplications(
            @RequestHeader("X-User-Id") Long jobSeekerId) {
        return applicationService.getApplicationsBySeeker(jobSeekerId);
    }

    @GetMapping("/applications/{applicationId}")
    public ApplicationResponse getApplication(
            @RequestHeader("X-User-Id") Long jobSeekerId,
            @PathVariable Long applicationId) {
        ApplicationResponse response = applicationService.getApplication(applicationId);
        // Verify ownership
        if (!response.getJobSeekerId().equals(jobSeekerId)) {
            throw new com.revhire.application.exception.ForbiddenException(
                "You can only view your own applications");
        }
        return response;
    }

    @GetMapping("/jobs/applied-ids")
    public List<Long> getAppliedJobIds(
            @RequestHeader("X-User-Id") Long jobSeekerId) {
        return applicationService.getAppliedJobIds(jobSeekerId);
    }
}
