package com.revhire.application.service;

import com.revhire.application.client.AuthServiceClient;
import com.revhire.application.client.EmployerServiceClient;
import com.revhire.application.client.JobSeekerServiceClient;
import com.revhire.application.client.NotificationClient;
import com.revhire.application.dto.*;
import com.revhire.application.entity.ApplicationStatus;
import com.revhire.application.entity.JobApplication;
import com.revhire.application.exception.BadRequestException;
import com.revhire.application.exception.ConflictException;
import com.revhire.application.exception.ForbiddenException;
import com.revhire.application.exception.NotFoundException;
import com.revhire.application.repository.JobApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);

    private final JobApplicationRepository applicationRepository;
    private final JobSeekerServiceClient jobSeekerClient;
    private final EmployerServiceClient employerClient;
    private final AuthServiceClient authClient;
    private final NotificationClient notificationClient;

    // Valid status transitions
    private static final Set<ApplicationStatus> TERMINAL_STATUSES = Set.of(
            ApplicationStatus.WITHDRAWN,
            ApplicationStatus.REJECTED
    );

    public ApplicationService(JobApplicationRepository applicationRepository,
                              JobSeekerServiceClient jobSeekerClient,
                              EmployerServiceClient employerClient,
                              AuthServiceClient authClient,
                              NotificationClient notificationClient) {
        this.applicationRepository = applicationRepository;
        this.jobSeekerClient = jobSeekerClient;
        this.employerClient = employerClient;
        this.authClient = authClient;
        this.notificationClient = notificationClient;
    }

    @Transactional
    public ApplicationResponse apply(Long jobSeekerId, ApplyRequest request) {
        logger.info("Job seeker {} applying to job {}", jobSeekerId, request.getJobId());

        // Validate job seeker exists
        Boolean jobSeekerExists = jobSeekerClient.validateJobSeeker(jobSeekerId);
        if (!Boolean.TRUE.equals(jobSeekerExists)) {
            throw new NotFoundException("Job seeker not found");
        }

        // Check if resume exists
        Boolean hasResume = jobSeekerClient.hasResume(jobSeekerId);
        if (!Boolean.TRUE.equals(hasResume)) {
            throw new BadRequestException("Please upload a resume before applying to jobs");
        }

        // Validate job exists and get details
        JobDetailsResponse jobDetails = employerClient.getJobDetails(request.getJobId());
        if (jobDetails == null) {
            throw new NotFoundException("Job not found");
        }

        // Check if job is OPEN
        if (!"OPEN".equalsIgnoreCase(jobDetails.getStatus())) {
            throw new BadRequestException("This job is no longer accepting applications");
        }

        // Check if already applied
        if (applicationRepository.existsByJobIdAndJobSeekerId(request.getJobId(), jobSeekerId)) {
            throw new ConflictException("You have already applied to this job");
        }

        // Create application
        JobApplication application = new JobApplication();
        application.setJobId(request.getJobId());
        application.setJobSeekerId(jobSeekerId);
        application.setCoverLetter(request.getCoverLetter());
        application.setStatus(ApplicationStatus.APPLIED);

        application = applicationRepository.save(application);

        // Send notification to employer
        try {
            NotificationRequest notification = new NotificationRequest(
                    jobDetails.getEmployerId(),
                    "NEW_APPLICATION",
                    "New application received for job: " + jobDetails.getTitle()
            );
            notificationClient.sendNotification(notification);
        } catch (Exception e) {
            logger.warn("Failed to send notification to employer", e);
        }

        return toResponse(application);
    }

    @Transactional
    public ApplicationResponse withdraw(Long jobSeekerId, Long applicationId, WithdrawRequest request) {
        logger.info("Job seeker {} withdrawing application {}", jobSeekerId, applicationId);

        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application not found"));

        // Verify ownership
        if (!application.getJobSeekerId().equals(jobSeekerId)) {
            throw new ForbiddenException("You can only withdraw your own applications");
        }

        // Check if already in terminal state
        if (TERMINAL_STATUSES.contains(application.getStatus())) {
            throw new BadRequestException("Cannot withdraw application in " + application.getStatus() + " status");
        }

        application.setStatus(ApplicationStatus.WITHDRAWN);
        application.setWithdrawReason(request.getReason());
        application = applicationRepository.save(application);

        // Send notification to employer
        try {
            Long employerId = employerClient.getJobEmployerId(application.getJobId());
            NotificationRequest notification = new NotificationRequest(
                    employerId,
                    "APPLICATION_WITHDRAWN",
                    "An applicant has withdrawn their application"
            );
            notificationClient.sendNotification(notification);
        } catch (Exception e) {
            logger.warn("Failed to send withdrawal notification", e);
        }

        return toResponse(application);
    }

    @Transactional
    public ApplicationResponse updateStatus(Long employerId, Long applicationId,
                                           ApplicationStatusUpdateRequest request) {
        logger.info("Employer {} updating application {} to status {}",
                   employerId, applicationId, request.getStatus());

        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application not found"));

        // Verify the job belongs to this employer
        Long jobEmployerId = employerClient.getJobEmployerId(application.getJobId());
        if (!employerId.equals(jobEmployerId)) {
            throw new ForbiddenException("You can only update applications for your own jobs");
        }

        // Validate status transition
        validateStatusTransition(application.getStatus(), request.getStatus());

        ApplicationStatus oldStatus = application.getStatus();
        application.setStatus(request.getStatus());
        if (request.getNotes() != null) {
            application.setNotes(request.getNotes());
        }
        application = applicationRepository.save(application);

        // Send notification to job seeker
        try {
            NotificationRequest notification = new NotificationRequest(
                    application.getJobSeekerId(),
                    "APPLICATION_STATUS_CHANGED",
                    "Your application status has been updated to: " + request.getStatus()
            );
            notificationClient.sendNotification(notification);
        } catch (Exception e) {
            logger.warn("Failed to send status change notification", e);
        }

        return toResponse(application);
    }

    public List<ApplicationResponse> getApplicationsBySeeker(Long jobSeekerId) {
        return applicationRepository.findByJobSeekerIdOrderByAppliedAtDesc(jobSeekerId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ApplicationResponse> getApplicationsByJob(Long jobId) {
        return applicationRepository.findByJobIdOrderByAppliedAtDesc(jobId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ApplicationResponse getApplication(Long applicationId) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application not found"));
        return toResponse(application);
    }

    public List<Long> getAppliedJobIds(Long jobSeekerId) {
        return applicationRepository.findAppliedJobIdsByJobSeekerId(jobSeekerId);
    }

    public ApplicationStatisticsResponse getApplicationStatistics(Long jobId) {
        ApplicationStatisticsResponse stats = new ApplicationStatisticsResponse();
        stats.setJobId(jobId);
        stats.setTotalApplications(applicationRepository.countByJobId(jobId));
        stats.setAppliedCount(applicationRepository.countByJobIdAndStatus(jobId, ApplicationStatus.APPLIED));
        stats.setUnderReviewCount(applicationRepository.countByJobIdAndStatus(jobId, ApplicationStatus.UNDER_REVIEW));
        stats.setShortlistedCount(applicationRepository.countByJobIdAndStatus(jobId, ApplicationStatus.SHORTLISTED));
        stats.setRejectedCount(applicationRepository.countByJobIdAndStatus(jobId, ApplicationStatus.REJECTED));
        stats.setWithdrawnCount(applicationRepository.countByJobIdAndStatus(jobId, ApplicationStatus.WITHDRAWN));
        return stats;
    }

    private void validateStatusTransition(ApplicationStatus currentStatus, ApplicationStatus newStatus) {
        // Cannot change terminal statuses
        if (TERMINAL_STATUSES.contains(currentStatus)) {
            throw new BadRequestException("Cannot change status from " + currentStatus);
        }

        // Validate valid transitions based on current status
        switch (currentStatus) {
            case APPLIED:
                if (newStatus != ApplicationStatus.UNDER_REVIEW &&
                    newStatus != ApplicationStatus.SHORTLISTED &&
                    newStatus != ApplicationStatus.REJECTED) {
                    throw new BadRequestException("Invalid status transition from APPLIED to " + newStatus);
                }
                break;
            case UNDER_REVIEW:
                if (newStatus != ApplicationStatus.SHORTLISTED &&
                    newStatus != ApplicationStatus.REJECTED) {
                    throw new BadRequestException("Invalid status transition from UNDER_REVIEW to " + newStatus);
                }
                break;
            case SHORTLISTED:
                if (newStatus != ApplicationStatus.REJECTED) {
                    throw new BadRequestException("Invalid status transition from SHORTLISTED to " + newStatus);
                }
                break;
            default:
                throw new BadRequestException("Invalid current status: " + currentStatus);
        }
    }

    private ApplicationResponse toResponse(JobApplication application) {
        ApplicationResponse response = new ApplicationResponse();
        response.setId(application.getId());
        response.setJobId(application.getJobId());
        response.setJobSeekerId(application.getJobSeekerId());
        response.setStatus(application.getStatus());
        response.setNotes(application.getNotes());
        response.setCoverLetter(application.getCoverLetter());
        response.setWithdrawReason(application.getWithdrawReason());
        response.setAppliedAt(application.getAppliedAt());
        return response;
    }
}
