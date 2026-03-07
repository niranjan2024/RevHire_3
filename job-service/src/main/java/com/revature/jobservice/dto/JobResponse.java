package com.revature.jobservice.dto;

import com.revature.jobservice.enums.JobStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class JobResponse {

    private Long id;
    private Long employerId;
    private String companyName;
    private String title;
    private String description;
    private String skills;
    private String education;
    private Integer maxExperienceYears;
    private String location;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private String jobType;
    private Integer openings;
    private LocalDate applicationDeadline;
    private JobStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public JobResponse() {
    }

    public JobResponse(Long id, Long employerId, String companyName, String title, String description,
                       String skills, String education, Integer maxExperienceYears, String location,
                       BigDecimal minSalary, BigDecimal maxSalary, String jobType, Integer openings,
                       LocalDate applicationDeadline, JobStatus status, LocalDateTime createdAt,
                       LocalDateTime updatedAt) {
        this.id = id;
        this.employerId = employerId;
        this.companyName = companyName;
        this.title = title;
        this.description = description;
        this.skills = skills;
        this.education = education;
        this.maxExperienceYears = maxExperienceYears;
        this.location = location;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.jobType = jobType;
        this.openings = openings;
        this.applicationDeadline = applicationDeadline;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Long employerId) {
        this.employerId = employerId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public Integer getMaxExperienceYears() {
        return maxExperienceYears;
    }

    public void setMaxExperienceYears(Integer maxExperienceYears) {
        this.maxExperienceYears = maxExperienceYears;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(BigDecimal minSalary) {
        this.minSalary = minSalary;
    }

    public BigDecimal getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(BigDecimal maxSalary) {
        this.maxSalary = maxSalary;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Integer getOpenings() {
        return openings;
    }

    public void setOpenings(Integer openings) {
        this.openings = openings;
    }

    public LocalDate getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(LocalDate applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobResponse that = (JobResponse) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(employerId, that.employerId) &&
               Objects.equals(companyName, that.companyName) &&
               Objects.equals(title, that.title) &&
               Objects.equals(description, that.description) &&
               Objects.equals(skills, that.skills) &&
               Objects.equals(education, that.education) &&
               Objects.equals(maxExperienceYears, that.maxExperienceYears) &&
               Objects.equals(location, that.location) &&
               Objects.equals(minSalary, that.minSalary) &&
               Objects.equals(maxSalary, that.maxSalary) &&
               Objects.equals(jobType, that.jobType) &&
               Objects.equals(openings, that.openings) &&
               Objects.equals(applicationDeadline, that.applicationDeadline) &&
               status == that.status &&
               Objects.equals(createdAt, that.createdAt) &&
               Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employerId, companyName, title, description, skills, education,
                           maxExperienceYears, location, minSalary, maxSalary, jobType, openings,
                           applicationDeadline, status, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "JobResponse{" +
                "id=" + id +
                ", employerId=" + employerId +
                ", companyName='" + companyName + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", skills='" + skills + '\'' +
                ", education='" + education + '\'' +
                ", maxExperienceYears=" + maxExperienceYears +
                ", location='" + location + '\'' +
                ", minSalary=" + minSalary +
                ", maxSalary=" + maxSalary +
                ", jobType='" + jobType + '\'' +
                ", openings=" + openings +
                ", applicationDeadline=" + applicationDeadline +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long employerId;
        private String companyName;
        private String title;
        private String description;
        private String skills;
        private String education;
        private Integer maxExperienceYears;
        private String location;
        private BigDecimal minSalary;
        private BigDecimal maxSalary;
        private String jobType;
        private Integer openings;
        private LocalDate applicationDeadline;
        private JobStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder employerId(Long employerId) {
            this.employerId = employerId;
            return this;
        }

        public Builder companyName(String companyName) {
            this.companyName = companyName;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder skills(String skills) {
            this.skills = skills;
            return this;
        }

        public Builder education(String education) {
            this.education = education;
            return this;
        }

        public Builder maxExperienceYears(Integer maxExperienceYears) {
            this.maxExperienceYears = maxExperienceYears;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder minSalary(BigDecimal minSalary) {
            this.minSalary = minSalary;
            return this;
        }

        public Builder maxSalary(BigDecimal maxSalary) {
            this.maxSalary = maxSalary;
            return this;
        }

        public Builder jobType(String jobType) {
            this.jobType = jobType;
            return this;
        }

        public Builder openings(Integer openings) {
            this.openings = openings;
            return this;
        }

        public Builder applicationDeadline(LocalDate applicationDeadline) {
            this.applicationDeadline = applicationDeadline;
            return this;
        }

        public Builder status(JobStatus status) {
            this.status = status;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public JobResponse build() {
            return new JobResponse(id, employerId, companyName, title, description, skills, education,
                                  maxExperienceYears, location, minSalary, maxSalary, jobType, openings,
                                  applicationDeadline, status, createdAt, updatedAt);
        }
    }
}
