package com.revhire.jobseeker.repository;

import com.revhire.jobseeker.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobSeekerProfileRepository extends JpaRepository<JobSeekerProfile, Long> {

    Optional<JobSeekerProfile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
