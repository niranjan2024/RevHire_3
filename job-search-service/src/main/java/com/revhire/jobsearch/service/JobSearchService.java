package com.revhire.jobsearch.service;

import com.revhire.jobsearch.client.ApplicationServiceClient;
import com.revhire.jobsearch.dto.JobDetailResponse;
import com.revhire.jobsearch.dto.JobSearchRequest;
import com.revhire.jobsearch.dto.JobSearchResponse;
import com.revhire.jobsearch.entity.JobPosting;
import com.revhire.jobsearch.entity.JobStatus;
import com.revhire.jobsearch.exception.NotFoundException;
import com.revhire.jobsearch.repository.JobPostingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class JobSearchService {

    private static final Logger logger = LoggerFactory.getLogger(JobSearchService.class);

    private final JobPostingRepository jobPostingRepository;
    private final ApplicationServiceClient applicationServiceClient;

    public JobSearchService(JobPostingRepository jobPostingRepository,
                          ApplicationServiceClient applicationServiceClient) {
        this.jobPostingRepository = jobPostingRepository;
        this.applicationServiceClient = applicationServiceClient;
    }

    /**
     * Search jobs with multiple filters
     */
    public JobSearchResponse searchJobs(JobSearchRequest request, Long userId) {
        logger.info("Searching jobs with filters: title={}, location={}, jobType={}",
                request.getTitle(), request.getLocation(), request.getJobType());

        Pageable pageable = createPageable(request);

        Page<JobPosting> jobPage = jobPostingRepository.searchJobs(
                request.getTitle(),
                request.getLocation(),
                request.getCompanyName(),
                request.getJobType(),
                request.getMaxExperience(),
                request.getMinSalary(),
                request.getMaxSalary(),
                request.getPostedAfter(),
                JobStatus.OPEN,
                pageable
        );

        List<JobDetailResponse> jobs = jobPage.getContent().stream()
                .map(job -> mapToDetailResponse(job, userId))
                .collect(Collectors.toList());

        return new JobSearchResponse(
                jobs,
                jobPage.getNumber(),
                jobPage.getTotalPages(),
                jobPage.getTotalElements(),
                jobPage.getSize()
        );
    }

    /**
     * Get all open jobs (public endpoint)
     */
    public JobSearchResponse getOpenJobs(int page, int size) {
        logger.info("Fetching open jobs - page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<JobPosting> jobPage = jobPostingRepository.findAllOpenJobs(pageable);

        List<JobDetailResponse> jobs = jobPage.getContent().stream()
                .map(job -> mapToDetailResponse(job, null))
                .collect(Collectors.toList());

        return new JobSearchResponse(
                jobs,
                jobPage.getNumber(),
                jobPage.getTotalPages(),
                jobPage.getTotalElements(),
                jobPage.getSize()
        );
    }

    /**
     * Get job details by ID
     */
    public JobDetailResponse getJobDetails(Long jobId, Long userId) {
        logger.info("Fetching job details for jobId: {}, userId: {}", jobId, userId);

        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new NotFoundException("Job posting not found with ID: " + jobId));

        return mapToDetailResponse(job, userId);
    }

    /**
     * Get distinct locations for filtering
     */
    public List<String> getDistinctLocations() {
        return jobPostingRepository.findDistinctLocations();
    }

    /**
     * Get distinct job types for filtering
     */
    public List<String> getDistinctJobTypes() {
        return jobPostingRepository.findDistinctJobTypes();
    }

    /**
     * Get distinct company names for filtering
     */
    public List<String> getDistinctCompanyNames() {
        return jobPostingRepository.findDistinctCompanyNames();
    }

    /**
     * Map JobPosting entity to JobDetailResponse DTO
     */
    private JobDetailResponse mapToDetailResponse(JobPosting job, Long userId) {
        JobDetailResponse response = new JobDetailResponse();
        response.setId(job.getId());
        response.setEmployerId(job.getEmployerId());
        response.setCompanyName(job.getCompanyName());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setSkills(job.getSkills());
        response.setEducation(job.getEducation());
        response.setMaxExperienceYears(job.getMaxExperienceYears());
        response.setLocation(job.getLocation());
        response.setMinSalary(job.getMinSalary());
        response.setMaxSalary(job.getMaxSalary());
        response.setJobType(job.getJobType());
        response.setOpenings(job.getOpenings());
        response.setApplicationDeadline(job.getApplicationDeadline());
        response.setStatus(job.getStatus());
        response.setCreatedAt(job.getCreatedAt());
        response.setUpdatedAt(job.getUpdatedAt());

        // Check if user has already applied (only if userId is provided)
        if (userId != null) {
            try {
                Boolean hasApplied = applicationServiceClient.hasUserApplied(userId, job.getId());
                response.setHasApplied(hasApplied);
            } catch (Exception e) {
                logger.warn("Failed to check application status for user {} and job {}: {}",
                        userId, job.getId(), e.getMessage());
                response.setHasApplied(false);
            }
        }

        return response;
    }

    /**
     * Create pageable object from request
     */
    private Pageable createPageable(JobSearchRequest request) {
        Sort sort = Sort.by(
                "DESC".equalsIgnoreCase(request.getSortDirection())
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                request.getSortBy()
        );

        return PageRequest.of(
                request.getPage(),
                Math.min(request.getSize(), 100), // Max 100 items per page
                sort
        );
    }
}
