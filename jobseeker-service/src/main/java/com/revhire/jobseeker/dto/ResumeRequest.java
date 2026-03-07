package com.revhire.jobseeker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResumeRequest {

    @NotBlank(message = "Objective is required")
    @Size(max = 500, message = "Objective must not exceed 500 characters")
    private String objective;

    @Size(max = 2000, message = "Education must not exceed 2000 characters")
    private String education;

    @Size(max = 2000, message = "Experience must not exceed 2000 characters")
    private String experience;

    @Size(max = 2000, message = "Projects must not exceed 2000 characters")
    private String projects;

    @Size(max = 2000, message = "Certifications must not exceed 2000 characters")
    private String certifications;

    @Size(max = 2000, message = "Skills must not exceed 2000 characters")
    private String skills;

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    public String getCertifications() {
        return certifications;
    }

    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }
}
