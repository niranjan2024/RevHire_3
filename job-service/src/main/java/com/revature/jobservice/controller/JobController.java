package com.revature.jobservice.controller;

import com.revature.jobservice.dto.ApiResponse;
import com.revature.jobservice.dto.JobResponse;
import com.revature.jobservice.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobResponse>>> getAllOpenJobs() {
        List<JobResponse> jobs = jobService.getAllOpenJobs();
        return ResponseEntity.ok(ApiResponse.success("Jobs retrieved successfully", jobs));
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<ApiResponse<JobResponse>> getJobById(@PathVariable Long jobId) {
        JobResponse job = jobService.getJobById(jobId);
        return ResponseEntity.ok(ApiResponse.success("Job retrieved successfully", job));
    }
}
