package com.revhire.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ApplyRequest {

    @NotNull(message = "Job ID is required")
    private Long jobId;

    @Size(max = 1000, message = "Cover letter must not exceed 1000 characters")
    private String coverLetter;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }
}
