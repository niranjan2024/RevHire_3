package com.revhire.application.repository;

import com.revhire.application.entity.ApplicationStatus;
import com.revhire.application.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByJobSeekerIdOrderByAppliedAtDesc(Long jobSeekerId);

    List<JobApplication> findByJobIdOrderByAppliedAtDesc(Long jobId);

    Optional<JobApplication> findByJobSeekerIdAndJobId(Long jobSeekerId, Long jobId);

    boolean existsByJobIdAndJobSeekerId(Long jobId, Long jobSeekerId);

    long countByJobId(Long jobId);

    long countByJobIdAndStatus(Long jobId, ApplicationStatus status);

    @Query("SELECT ja.jobId FROM JobApplication ja WHERE ja.jobSeekerId = :jobSeekerId")
    List<Long> findAppliedJobIdsByJobSeekerId(@Param("jobSeekerId") Long jobSeekerId);
}
