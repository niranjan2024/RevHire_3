package com.revhire.jobseeker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class JobSeekerProfileRequest {

    @NotBlank(message = "Skills are required")
    @Size(max = 1000, message = "Skills must not exceed 1000 characters")
    private String skills;

    @Size(max = 1000, message = "Education must not exceed 1000 characters")
    private String education;

    @Size(max = 1000, message = "Certifications must not exceed 1000 characters")
    private String certifications;

    @Size(max = 150, message = "Headline must not exceed 150 characters")
    private String headline;

    @Size(max = 500, message = "Summary must not exceed 500 characters")
    private String summary;

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

    public String getCertifications() {
        return certifications;
    }

    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
