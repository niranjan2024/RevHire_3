package com.revature.jobservice.repository;

import com.revature.jobservice.entity.JobPosting;
import com.revature.jobservice.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    List<JobPosting> findByEmployerIdOrderByCreatedAtDesc(Long employerId);

    List<JobPosting> findByStatusOrderByCreatedAtDesc(JobStatus status);

    Long countByEmployerId(Long employerId);

    Long countByEmployerIdAndStatus(Long employerId, JobStatus status);
}
