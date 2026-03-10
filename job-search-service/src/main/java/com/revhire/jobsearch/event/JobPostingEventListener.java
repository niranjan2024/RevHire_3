package com.revhire.jobsearch.event;

import com.revhire.jobsearch.entity.JobPosting;
import com.revhire.jobsearch.service.JobSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka listener for job posting events from Employer Service
 */
@Component
public class JobPostingEventListener {

    private static final Logger logger = LoggerFactory.getLogger(JobPostingEventListener.class);

    private final JobSyncService jobSyncService;

    public JobPostingEventListener(JobSyncService jobSyncService) {
        this.jobSyncService = jobSyncService;
    }

    /**
     * Listen to job posting events from Employer Service
     */
    @KafkaListener(topics = "${kafka.topic.job-postings}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleJobPostingEvent(JobPostingEvent event) {
        logger.info("Received job posting event: type={}, jobId={}", event.getEventType(), event.getId());

        try {
            switch (event.getEventType()) {
                case CREATED, UPDATED, STATUS_CHANGED -> {
                    JobPosting jobPosting = mapEventToEntity(event);
                    jobSyncService.syncJobPosting(jobPosting);
                }
                case DELETED -> jobSyncService.deleteJobPosting(event.getId());
                default -> logger.warn("Unknown event type: {}", event.getEventType());
            }
        } catch (Exception e) {
            logger.error("Error processing job posting event: {}", event.getId(), e);
            // In production, you might want to send this to a dead letter queue
            throw e;
        }
    }

    /**
     * Map event to entity
     */
    private JobPosting mapEventToEntity(JobPostingEvent event) {
        JobPosting jobPosting = new JobPosting();
        jobPosting.setId(event.getId());
        jobPosting.setEmployerId(event.getEmployerId());
        jobPosting.setCompanyName(event.getCompanyName());
        jobPosting.setTitle(event.getTitle());
        jobPosting.setDescription(event.getDescription());
        jobPosting.setSkills(event.getSkills());
        jobPosting.setEducation(event.getEducation());
        jobPosting.setMaxExperienceYears(event.getMaxExperienceYears());
        jobPosting.setLocation(event.getLocation());
        jobPosting.setMinSalary(event.getMinSalary());
        jobPosting.setMaxSalary(event.getMaxSalary());
        jobPosting.setJobType(event.getJobType());
        jobPosting.setOpenings(event.getOpenings());
        jobPosting.setApplicationDeadline(event.getApplicationDeadline());
        jobPosting.setStatus(event.getStatus());
        jobPosting.setCreatedAt(event.getCreatedAt());
        jobPosting.setUpdatedAt(event.getUpdatedAt());
        return jobPosting;
    }
}
