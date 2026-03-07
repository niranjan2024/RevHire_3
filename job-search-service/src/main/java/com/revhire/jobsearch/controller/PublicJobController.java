package com.revhire.jobsearch.controller;

import com.revhire.jobsearch.dto.JobDetailResponse;
import com.revhire.jobsearch.dto.JobSearchResponse;
import com.revhire.jobsearch.service.JobSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Public endpoints for job listings (no authentication required)
 */
@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class PublicJobController {

    private final JobSearchService jobSearchService;

    public PublicJobController(JobSearchService jobSearchService) {
        this.jobSearchService = jobSearchService;
    }

    /**
     * Get all open jobs (paginated)
     * Public endpoint - no authentication required
     */
    @GetMapping
    public ResponseEntity<JobSearchResponse> getAllOpenJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        JobSearchResponse response = jobSearchService.getOpenJobs(page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Get job details by ID
     * Public endpoint - no authentication required
     */
    @GetMapping("/{jobId}")
    public ResponseEntity<JobDetailResponse> getJobDetails(@PathVariable Long jobId) {
        JobDetailResponse response = jobSearchService.getJobDetails(jobId, null);
        return ResponseEntity.ok(response);
    }

    /**
     * Get distinct locations for filter dropdown
     */
    @GetMapping("/filters/locations")
    public ResponseEntity<List<String>> getDistinctLocations() {
        List<String> locations = jobSearchService.getDistinctLocations();
        return ResponseEntity.ok(locations);
    }

    /**
     * Get distinct job types for filter dropdown
     */
    @GetMapping("/filters/job-types")
    public ResponseEntity<List<String>> getDistinctJobTypes() {
        List<String> jobTypes = jobSearchService.getDistinctJobTypes();
        return ResponseEntity.ok(jobTypes);
    }

    /**
     * Get distinct company names for filter dropdown
     */
    @GetMapping("/filters/companies")
    public ResponseEntity<List<String>> getDistinctCompanyNames() {
        List<String> companies = jobSearchService.getDistinctCompanyNames();
        return ResponseEntity.ok(companies);
    }
}
