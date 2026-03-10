package com.revhire.jobsearch.controller;

import com.revhire.jobsearch.dto.JobDetailResponse;
import com.revhire.jobsearch.dto.JobSearchRequest;
import com.revhire.jobsearch.dto.JobSearchResponse;
import com.revhire.jobsearch.service.JobSearchService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authenticated endpoints for job seekers to search jobs
 * In a real microservices architecture, this would extract userId from JWT token
 */
@RestController
@RequestMapping("/api/jobseeker/jobs")
@CrossOrigin(origins = "*")
public class JobSearchController {

    private final JobSearchService jobSearchService;

    public JobSearchController(JobSearchService jobSearchService) {
        this.jobSearchService = jobSearchService;
    }

    /**
     * Advanced job search with filters (authenticated)
     * This endpoint includes application status for the authenticated user
     */
    @PostMapping("/search")
    public ResponseEntity<JobSearchResponse> searchJobs(
            @Valid @RequestBody JobSearchRequest request,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        // In production, userId would be extracted from JWT token via security context
        // For now, accepting it as a header for testing
        JobSearchResponse response = jobSearchService.searchJobs(request, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get job details with application status (authenticated)
     */
    @GetMapping("/{jobId}")
    public ResponseEntity<JobDetailResponse> getJobDetailsWithApplicationStatus(
            @PathVariable Long jobId,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        // In production, userId would be extracted from JWT token via security context
        JobDetailResponse response = jobSearchService.getJobDetails(jobId, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Simple search endpoint with query parameters (authenticated)
     */
    @GetMapping("/search")
    public ResponseEntity<JobSearchResponse> searchJobsWithParams(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String jobType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        JobSearchRequest request = new JobSearchRequest();
        request.setTitle(title);
        request.setLocation(location);
        request.setCompanyName(companyName);
        request.setJobType(jobType);
        request.setPage(page);
        request.setSize(size);

        JobSearchResponse response = jobSearchService.searchJobs(request, userId);
        return ResponseEntity.ok(response);
    }
}
