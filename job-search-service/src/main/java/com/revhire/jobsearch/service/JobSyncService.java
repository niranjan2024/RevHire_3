package com.revhire.jobsearch.service;

import com.revhire.jobsearch.entity.JobPosting;
import com.revhire.jobsearch.repository.JobPostingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to sync job postings from Employer Service
 */
@Service
@Transactional
public class JobSyncService {

    private static final Logger logger = LoggerFactory.getLogger(JobSyncService.class);

    private final JobPostingRepository jobPostingRepository;

    public JobSyncService(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;
    }

    /**
     * Create or update job posting from employer service event
     */
    public void syncJobPosting(JobPosting jobPosting) {
        logger.info("Syncing job posting with ID: {}", jobPosting.getId());

        try {
            jobPostingRepository.save(jobPosting);
            logger.info("Successfully synced job posting: {}", jobPosting.getId());
        } catch (Exception e) {
            logger.error("Failed to sync job posting {}: {}", jobPosting.getId(), e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Delete job posting (when deleted from employer service)
     */
    public void deleteJobPosting(Long jobId) {
        logger.info("Deleting job posting with ID: {}", jobId);

        try {
            if (jobPostingRepository.existsById(jobId)) {
                jobPostingRepository.deleteById(jobId);
                logger.info("Successfully deleted job posting: {}", jobId);
            } else {
                logger.warn("Job posting {} not found for deletion", jobId);
            }
        } catch (Exception e) {
            logger.error("Failed to delete job posting {}: {}", jobId, e.getMessage(), e);
            throw e;
        }
    }
}
