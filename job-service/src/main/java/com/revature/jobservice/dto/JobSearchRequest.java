package com.revature.jobservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class JobSearchRequest {

    private String title;
    private String location;
    private String companyName;
    private String jobType;
    private Integer maxExperienceYears;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private LocalDate datePostedAfter;

    public JobSearchRequest() {
    }

    public JobSearchRequest(String title, String location, String companyName, String jobType,
                            Integer maxExperienceYears, BigDecimal minSalary, BigDecimal maxSalary,
                            LocalDate datePostedAfter) {
        this.title = title;
        this.location = location;
        this.companyName = companyName;
        this.jobType = jobType;
        this.maxExperienceYears = maxExperienceYears;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.datePostedAfter = datePostedAfter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Integer getMaxExperienceYears() {
        return maxExperienceYears;
    }

    public void setMaxExperienceYears(Integer maxExperienceYears) {
        this.maxExperienceYears = maxExperienceYears;
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

    public LocalDate getDatePostedAfter() {
        return datePostedAfter;
    }

    public void setDatePostedAfter(LocalDate datePostedAfter) {
        this.datePostedAfter = datePostedAfter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobSearchRequest that = (JobSearchRequest) o;
        return Objects.equals(title, that.title) &&
               Objects.equals(location, that.location) &&
               Objects.equals(companyName, that.companyName) &&
               Objects.equals(jobType, that.jobType) &&
               Objects.equals(maxExperienceYears, that.maxExperienceYears) &&
               Objects.equals(minSalary, that.minSalary) &&
               Objects.equals(maxSalary, that.maxSalary) &&
               Objects.equals(datePostedAfter, that.datePostedAfter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, location, companyName, jobType, maxExperienceYears,
                           minSalary, maxSalary, datePostedAfter);
    }

    @Override
    public String toString() {
        return "JobSearchRequest{" +
                "title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", companyName='" + companyName + '\'' +
                ", jobType='" + jobType + '\'' +
                ", maxExperienceYears=" + maxExperienceYears +
                ", minSalary=" + minSalary +
                ", maxSalary=" + maxSalary +
                ", datePostedAfter=" + datePostedAfter +
                '}';
    }
}
