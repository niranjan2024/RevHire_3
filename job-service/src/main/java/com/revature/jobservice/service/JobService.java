package com.revature.jobservice.service;

import com.revature.jobservice.client.EmployerServiceClient;
import com.revature.jobservice.client.NotificationServiceClient;
import com.revature.jobservice.dto.*;
import com.revature.jobservice.entity.JobPosting;
import com.revature.jobservice.enums.JobStatus;
import com.revature.jobservice.exception.BadRequestException;
import com.revature.jobservice.exception.ForbiddenException;
import com.revature.jobservice.exception.NotFoundException;
import com.revature.jobservice.repository.JobPostingRepository;
import com.revature.jobservice.util.InputSanitizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JobService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JobService.class);

    private final JobPostingRepository jobPostingRepository;
    private final EmployerServiceClient employerServiceClient;
    private final NotificationServiceClient notificationServiceClient;

    public JobService(JobPostingRepository jobPostingRepository,
                      EmployerServiceClient employerServiceClient,
                      NotificationServiceClient notificationServiceClient) {
        this.jobPostingRepository = jobPostingRepository;
        this.employerServiceClient = employerServiceClient;
        this.notificationServiceClient = notificationServiceClient;
    }

    public List<JobResponse> getAllOpenJobs() {
        log.info("Fetching all open jobs");
        List<JobPosting> jobs = jobPostingRepository.findByStatusOrderByCreatedAtDesc(JobStatus.OPEN);
        return jobs.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<JobResponse> searchJobs(JobSearchRequest request) {
        log.info("Searching jobs with filters: {}", request);
        List<JobPosting> jobs = jobPostingRepository.findByStatusOrderByCreatedAtDesc(JobStatus.OPEN);

        return jobs.stream()
                .filter(job -> matchesSearchCriteria(job, request))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<JobResponse> getEmployerJobs(Long employerId) {
        log.info("Fetching jobs for employer: {}", employerId);
        List<JobPosting> jobs = jobPostingRepository.findByEmployerIdOrderByCreatedAtDesc(employerId);
        return jobs.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public JobResponse getJobById(Long jobId) {
        log.info("Fetching job by ID: {}", jobId);
        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new NotFoundException("Job not found with ID: " + jobId));
        return convertToResponse(job);
    }

    @Transactional
    public JobResponse createJob(Long employerId, JobRequest request) {
        log.info("Creating new job for employer: {}", employerId);

        // Validate salary range
        if (request.getMinSalary() != null && request.getMaxSalary() != null &&
                request.getMinSalary().compareTo(request.getMaxSalary()) > 0) {
            throw new BadRequestException("Min salary cannot be greater than max salary");
        }

        // Get employer details
        Map<String, Object> employerResponse;
        String companyName;
        try {
            employerResponse = employerServiceClient.getEmployerDetails(employerId);
            Map<String, Object> employerData = (Map<String, Object>) employerResponse.get("data");
            companyName = (String) employerData.get("companyName");
        } catch (Exception e) {
            log.error("Failed to fetch employer details: {}", e.getMessage());
            throw new BadRequestException("Invalid employer ID or employer service unavailable");
        }

        // Create job posting
        JobPosting job = new JobPosting();
        job.setEmployerId(employerId);
        job.setCompanyName(companyName);
        job.setTitle(InputSanitizer.sanitize(request.getTitle()));
        job.setDescription(InputSanitizer.sanitize(request.getDescription()));
        job.setSkills(InputSanitizer.sanitize(request.getSkills()));
        job.setEducation(InputSanitizer.sanitize(request.getEducation()));
        job.setMaxExperienceYears(request.getMaxExperienceYears());
        job.setLocation(InputSanitizer.sanitize(request.getLocation()));
        job.setMinSalary(request.getMinSalary());
        job.setMaxSalary(request.getMaxSalary());
        job.setJobType(InputSanitizer.sanitize(request.getJobType()));
        job.setOpenings(request.getOpenings());
        job.setApplicationDeadline(request.getApplicationDeadline());
        job.setStatus(JobStatus.OPEN);

        JobPosting savedJob = jobPostingRepository.save(job);
        log.info("Job created successfully with ID: {}", savedJob.getId());

        // Send notification asynchronously
        try {
            sendJobCreatedNotification(savedJob);
        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage());
        }

        return convertToResponse(savedJob);
    }

    @Transactional
    public JobResponse updateJob(Long employerId, Long jobId, JobRequest request) {
        log.info("Updating job ID: {} for employer: {}", jobId, employerId);

        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new NotFoundException("Job not found with ID: " + jobId));

        // Verify ownership
        if (!job.getEmployerId().equals(employerId)) {
            throw new ForbiddenException("You are not authorized to update this job");
        }

        // Validate salary range
        if (request.getMinSalary() != null && request.getMaxSalary() != null &&
                request.getMinSalary().compareTo(request.getMaxSalary()) > 0) {
            throw new BadRequestException("Min salary cannot be greater than max salary");
        }

        // Update fields
        job.setTitle(InputSanitizer.sanitize(request.getTitle()));
        job.setDescription(InputSanitizer.sanitize(request.getDescription()));
        job.setSkills(InputSanitizer.sanitize(request.getSkills()));
        job.setEducation(InputSanitizer.sanitize(request.getEducation()));
        job.setMaxExperienceYears(request.getMaxExperienceYears());
        job.setLocation(InputSanitizer.sanitize(request.getLocation()));
        job.setMinSalary(request.getMinSalary());
        job.setMaxSalary(request.getMaxSalary());
        job.setJobType(InputSanitizer.sanitize(request.getJobType()));
        job.setOpenings(request.getOpenings());
        job.setApplicationDeadline(request.getApplicationDeadline());

        JobPosting updatedJob = jobPostingRepository.save(job);
        log.info("Job updated successfully: {}", jobId);

        return convertToResponse(updatedJob);
    }

    @Transactional
    public void deleteJob(Long employerId, Long jobId) {
        log.info("Deleting job ID: {} for employer: {}", jobId, employerId);

        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new NotFoundException("Job not found with ID: " + jobId));

        // Verify ownership
        if (!job.getEmployerId().equals(employerId)) {
            throw new ForbiddenException("You are not authorized to delete this job");
        }

        jobPostingRepository.delete(job);
        log.info("Job deleted successfully: {}", jobId);
    }

    @Transactional
    public JobResponse closeJob(Long employerId, Long jobId) {
        log.info("Closing job ID: {} for employer: {}", jobId, employerId);

        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new NotFoundException("Job not found with ID: " + jobId));

        // Verify ownership
        if (!job.getEmployerId().equals(employerId)) {
            throw new ForbiddenException("You are not authorized to close this job");
        }

        if (job.getStatus() == JobStatus.CLOSED) {
            throw new BadRequestException("Job is already closed");
        }

        job.setStatus(JobStatus.CLOSED);
        JobPosting updatedJob = jobPostingRepository.save(job);
        log.info("Job closed successfully: {}", jobId);

        return convertToResponse(updatedJob);
    }

    @Transactional
    public JobResponse reopenJob(Long employerId, Long jobId) {
        log.info("Reopening job ID: {} for employer: {}", jobId, employerId);

        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new NotFoundException("Job not found with ID: " + jobId));

        // Verify ownership
        if (!job.getEmployerId().equals(employerId)) {
            throw new ForbiddenException("You are not authorized to reopen this job");
        }

        if (job.getStatus() == JobStatus.OPEN) {
            throw new BadRequestException("Job is already open");
        }

        if (job.getStatus() == JobStatus.FILLED) {
            throw new BadRequestException("Cannot reopen a filled job");
        }

        job.setStatus(JobStatus.OPEN);
        JobPosting updatedJob = jobPostingRepository.save(job);
        log.info("Job reopened successfully: {}", jobId);

        return convertToResponse(updatedJob);
    }

    @Transactional
    public JobResponse fillJob(Long employerId, Long jobId) {
        log.info("Marking job as filled ID: {} for employer: {}", jobId, employerId);

        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new NotFoundException("Job not found with ID: " + jobId));

        // Verify ownership
        if (!job.getEmployerId().equals(employerId)) {
            throw new ForbiddenException("You are not authorized to mark this job as filled");
        }

        if (job.getStatus() == JobStatus.FILLED) {
            throw new BadRequestException("Job is already filled");
        }

        job.setStatus(JobStatus.FILLED);
        JobPosting updatedJob = jobPostingRepository.save(job);
        log.info("Job marked as filled successfully: {}", jobId);

        return convertToResponse(updatedJob);
    }

    public JobStatisticsResponse getEmployerJobStats(Long employerId) {
        log.info("Fetching job statistics for employer: {}", employerId);

        Long totalJobs = jobPostingRepository.countByEmployerId(employerId);
        Long openJobs = jobPostingRepository.countByEmployerIdAndStatus(employerId, JobStatus.OPEN);
        Long closedJobs = jobPostingRepository.countByEmployerIdAndStatus(employerId, JobStatus.CLOSED);
        Long filledJobs = jobPostingRepository.countByEmployerIdAndStatus(employerId, JobStatus.FILLED);

        return JobStatisticsResponse.builder()
                .totalJobs(totalJobs)
                .openJobs(openJobs)
                .closedJobs(closedJobs)
                .filledJobs(filledJobs)
                .build();
    }

    private boolean matchesSearchCriteria(JobPosting job, JobSearchRequest request) {
        // Filter by title
        if (request.getTitle() != null && !request.getTitle().isEmpty()) {
            if (!job.getTitle().toLowerCase().contains(request.getTitle().toLowerCase())) {
                return false;
            }
        }

        // Filter by location
        if (request.getLocation() != null && !request.getLocation().isEmpty()) {
            if (!job.getLocation().toLowerCase().contains(request.getLocation().toLowerCase())) {
                return false;
            }
        }

        // Filter by company name
        if (request.getCompanyName() != null && !request.getCompanyName().isEmpty()) {
            if (!job.getCompanyName().toLowerCase().contains(request.getCompanyName().toLowerCase())) {
                return false;
            }
        }

        // Filter by job type
        if (request.getJobType() != null && !request.getJobType().isEmpty()) {
            if (job.getJobType() == null || !job.getJobType().equalsIgnoreCase(request.getJobType())) {
                return false;
            }
        }

        // Filter by experience
        if (request.getMaxExperienceYears() != null) {
            if (job.getMaxExperienceYears() != null &&
                job.getMaxExperienceYears() < request.getMaxExperienceYears()) {
                return false;
            }
        }

        // Filter by minimum salary
        if (request.getMinSalary() != null) {
            if (job.getMinSalary() == null ||
                job.getMinSalary().compareTo(request.getMinSalary()) < 0) {
                return false;
            }
        }

        // Filter by maximum salary
        if (request.getMaxSalary() != null) {
            if (job.getMaxSalary() == null ||
                job.getMaxSalary().compareTo(request.getMaxSalary()) > 0) {
                return false;
            }
        }

        // Filter by date posted
        if (request.getDatePostedAfter() != null) {
            LocalDate jobPostDate = job.getCreatedAt().toLocalDate();
            if (jobPostDate.isBefore(request.getDatePostedAfter())) {
                return false;
            }
        }

        return true;
    }

    private void sendJobCreatedNotification(JobPosting job) {
        try {
            Map<String, Object> notification = new HashMap<>();
            notification.put("type", "JOB_CREATED");
            notification.put("jobId", job.getId());
            notification.put("employerId", job.getEmployerId());
            notification.put("title", job.getTitle());
            notification.put("companyName", job.getCompanyName());
            notification.put("message", "New job posting: " + job.getTitle() + " at " + job.getCompanyName());

            notificationServiceClient.sendNotification(notification);
        } catch (Exception e) {
            log.error("Failed to send job created notification: {}", e.getMessage());
        }
    }

    private JobResponse convertToResponse(JobPosting job) {
        return JobResponse.builder()
                .id(job.getId())
                .employerId(job.getEmployerId())
                .companyName(job.getCompanyName())
                .title(job.getTitle())
                .description(job.getDescription())
                .skills(job.getSkills())
                .education(job.getEducation())
                .maxExperienceYears(job.getMaxExperienceYears())
                .location(job.getLocation())
                .minSalary(job.getMinSalary())
                .maxSalary(job.getMaxSalary())
                .jobType(job.getJobType())
                .openings(job.getOpenings())
                .applicationDeadline(job.getApplicationDeadline())
                .status(job.getStatus())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }
}
