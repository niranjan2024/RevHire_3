package com.revhire.application.dto;

public class ApplicationStatisticsResponse {
    private Long jobId;
    private long totalApplications;
    private long appliedCount;
    private long underReviewCount;
    private long shortlistedCount;
    private long rejectedCount;
    private long withdrawnCount;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public long getAppliedCount() {
        return appliedCount;
    }

    public void setAppliedCount(long appliedCount) {
        this.appliedCount = appliedCount;
    }

    public long getUnderReviewCount() {
        return underReviewCount;
    }

    public void setUnderReviewCount(long underReviewCount) {
        this.underReviewCount = underReviewCount;
    }

    public long getShortlistedCount() {
        return shortlistedCount;
    }

    public void setShortlistedCount(long shortlistedCount) {
        this.shortlistedCount = shortlistedCount;
    }

    public long getRejectedCount() {
        return rejectedCount;
    }

    public void setRejectedCount(long rejectedCount) {
        this.rejectedCount = rejectedCount;
    }

    public long getWithdrawnCount() {
        return withdrawnCount;
    }

    public void setWithdrawnCount(long withdrawnCount) {
        this.withdrawnCount = withdrawnCount;
    }
}
