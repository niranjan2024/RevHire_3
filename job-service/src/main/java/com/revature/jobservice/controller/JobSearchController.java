package com.revature.jobservice.controller;

import com.revature.jobservice.dto.ApiResponse;
import com.revature.jobservice.dto.JobResponse;
import com.revature.jobservice.dto.JobSearchRequest;
import com.revature.jobservice.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobseeker/jobs")
public class JobSearchController {

    private final JobService jobService;

    public JobSearchController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<JobResponse>>> searchJobs(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @ModelAttribute JobSearchRequest searchRequest) {

        List<JobResponse> jobs = jobService.searchJobs(searchRequest);
        return ResponseEntity.ok(ApiResponse.success("Jobs retrieved successfully", jobs));
    }
}
