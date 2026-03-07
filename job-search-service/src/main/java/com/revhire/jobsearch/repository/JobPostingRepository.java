package com.revhire.jobsearch.repository;

import com.revhire.jobsearch.entity.JobPosting;
import com.revhire.jobsearch.entity.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    /**
     * Advanced search with multiple filter criteria
     */
    @Query("SELECT j FROM JobPosting j WHERE " +
           "(:title IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:companyName IS NULL OR LOWER(j.companyName) LIKE LOWER(CONCAT('%', :companyName, '%'))) AND " +
           "(:jobType IS NULL OR j.jobType = :jobType) AND " +
           "(:maxExperience IS NULL OR j.maxExperienceYears <= :maxExperience) AND " +
           "(:minSalary IS NULL OR j.maxSalary >= :minSalary) AND " +
           "(:maxSalary IS NULL OR j.minSalary <= :maxSalary) AND " +
           "(:postedAfter IS NULL OR DATE(j.createdAt) >= :postedAfter) AND " +
           "(:status IS NULL OR j.status = :status)")
    Page<JobPosting> searchJobs(
            @Param("title") String title,
            @Param("location") String location,
            @Param("companyName") String companyName,
            @Param("jobType") String jobType,
            @Param("maxExperience") Integer maxExperience,
            @Param("minSalary") BigDecimal minSalary,
            @Param("maxSalary") BigDecimal maxSalary,
            @Param("postedAfter") LocalDate postedAfter,
            @Param("status") JobStatus status,
            Pageable pageable
    );

    /**
     * Find all open jobs (public endpoint)
     */
    @Query("SELECT j FROM JobPosting j WHERE j.status = 'OPEN' AND " +
           "(j.applicationDeadline IS NULL OR j.applicationDeadline >= CURRENT_DATE) " +
           "ORDER BY j.createdAt DESC")
    Page<JobPosting> findAllOpenJobs(Pageable pageable);

    /**
     * Find jobs by title (partial match, case-insensitive)
     */
    @Query("SELECT j FROM JobPosting j WHERE LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%')) " +
           "AND j.status = 'OPEN'")
    Page<JobPosting> findByTitleContainingIgnoreCase(@Param("title") String title, Pageable pageable);

    /**
     * Find jobs by location (partial match)
     */
    @Query("SELECT j FROM JobPosting j WHERE LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%')) " +
           "AND j.status = 'OPEN'")
    Page<JobPosting> findByLocationContainingIgnoreCase(@Param("location") String location, Pageable pageable);

    /**
     * Find jobs by company name (partial match)
     */
    @Query("SELECT j FROM JobPosting j WHERE LOWER(j.companyName) LIKE LOWER(CONCAT('%', :companyName, '%')) " +
           "AND j.status = 'OPEN'")
    Page<JobPosting> findByCompanyNameContainingIgnoreCase(@Param("companyName") String companyName, Pageable pageable);

    /**
     * Find jobs by job type
     */
    Page<JobPosting> findByJobTypeAndStatus(String jobType, JobStatus status, Pageable pageable);

    /**
     * Find jobs by experience requirement
     */
    @Query("SELECT j FROM JobPosting j WHERE j.maxExperienceYears <= :maxExperience AND j.status = 'OPEN'")
    Page<JobPosting> findByMaxExperienceYearsLessThanEqual(@Param("maxExperience") Integer maxExperience, Pageable pageable);

    /**
     * Find jobs by salary range (overlap check)
     */
    @Query("SELECT j FROM JobPosting j WHERE " +
           "j.maxSalary >= :minSalary AND j.minSalary <= :maxSalary AND j.status = 'OPEN'")
    Page<JobPosting> findBySalaryRange(
            @Param("minSalary") BigDecimal minSalary,
            @Param("maxSalary") BigDecimal maxSalary,
            Pageable pageable
    );

    /**
     * Find jobs posted on or after a specific date
     */
    @Query("SELECT j FROM JobPosting j WHERE DATE(j.createdAt) >= :postedAfter AND j.status = 'OPEN'")
    Page<JobPosting> findByCreatedAtGreaterThanEqual(@Param("postedAfter") LocalDate postedAfter, Pageable pageable);

    /**
     * Find jobs by employer ID
     */
    List<JobPosting> findByEmployerId(Long employerId);

    /**
     * Find jobs by status
     */
    Page<JobPosting> findByStatus(JobStatus status, Pageable pageable);

    /**
     * Get distinct locations
     */
    @Query("SELECT DISTINCT j.location FROM JobPosting j WHERE j.status = 'OPEN' ORDER BY j.location")
    List<String> findDistinctLocations();

    /**
     * Get distinct job types
     */
    @Query("SELECT DISTINCT j.jobType FROM JobPosting j WHERE j.status = 'OPEN' ORDER BY j.jobType")
    List<String> findDistinctJobTypes();

    /**
     * Get distinct company names
     */
    @Query("SELECT DISTINCT j.companyName FROM JobPosting j WHERE j.status = 'OPEN' AND j.companyName IS NOT NULL ORDER BY j.companyName")
    List<String> findDistinctCompanyNames();
}
