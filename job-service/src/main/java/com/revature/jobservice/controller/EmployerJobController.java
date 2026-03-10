package com.revature.jobservice.controller;

import com.revature.jobservice.dto.*;
import com.revature.jobservice.exception.BadRequestException;
import com.revature.jobservice.service.JobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employer/jobs")
public class EmployerJobController {

    private final JobService jobService;

    public EmployerJobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobResponse>>> getEmployerJobs(
            @RequestHeader("X-User-Id") Long userId) {

        if (userId == null) {
            throw new BadRequestException("User ID is required");
        }

        List<JobResponse> jobs = jobService.getEmployerJobs(userId);
        return ResponseEntity.ok(ApiResponse.success("Jobs retrieved successfully", jobs));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<JobResponse>> createJob(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody JobRequest request) {

        if (userId == null) {
            throw new BadRequestException("User ID is required");
        }

        JobResponse job = jobService.createJob(userId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Job created successfully", job));
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<ApiResponse<JobResponse>> updateJob(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long jobId,
            @Valid @RequestBody JobRequest request) {

        if (userId == null) {
            throw new BadRequestException("User ID is required");
        }

        JobResponse job = jobService.updateJob(userId, jobId, request);
        return ResponseEntity.ok(ApiResponse.success("Job updated successfully", job));
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long jobId) {

        if (userId == null) {
            throw new BadRequestException("User ID is required");
        }

        jobService.deleteJob(userId, jobId);
        return ResponseEntity.ok(ApiResponse.success("Job deleted successfully", null));
    }

    @PatchMapping("/{jobId}/close")
    public ResponseEntity<ApiResponse<JobResponse>> closeJob(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long jobId) {

        if (userId == null) {
            throw new BadRequestException("User ID is required");
        }

        JobResponse job = jobService.closeJob(userId, jobId);
        return ResponseEntity.ok(ApiResponse.success("Job closed successfully", job));
    }

    @PatchMapping("/{jobId}/reopen")
    public ResponseEntity<ApiResponse<JobResponse>> reopenJob(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long jobId) {

        if (userId == null) {
            throw new BadRequestException("User ID is required");
        }

        JobResponse job = jobService.reopenJob(userId, jobId);
        return ResponseEntity.ok(ApiResponse.success("Job reopened successfully", job));
    }

    @PatchMapping("/{jobId}/fill")
    public ResponseEntity<ApiResponse<JobResponse>> fillJob(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long jobId) {

        if (userId == null) {
            throw new BadRequestException("User ID is required");
        }

        JobResponse job = jobService.fillJob(userId, jobId);
        return ResponseEntity.ok(ApiResponse.success("Job marked as filled successfully", job));
    }

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<JobStatisticsResponse>> getJobStatistics(
            @RequestHeader("X-User-Id") Long userId) {

        if (userId == null) {
            throw new BadRequestException("User ID is required");
        }

        JobStatisticsResponse stats = jobService.getEmployerJobStats(userId);
        return ResponseEntity.ok(ApiResponse.success("Statistics retrieved successfully", stats));
    }
}
