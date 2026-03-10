package com.revhire.jobseeker.controller;

import com.revhire.jobseeker.dto.JobSeekerProfileRequest;
import com.revhire.jobseeker.dto.JobSeekerProfileResponse;
import com.revhire.jobseeker.service.JobSeekerProfileService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobseeker/profile")
public class JobSeekerProfileController {

    private final JobSeekerProfileService profileService;

    public JobSeekerProfileController(JobSeekerProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public JobSeekerProfileResponse getProfile(@RequestHeader("X-User-Id") Long userId) {
        return profileService.getProfile(userId);
    }

    @PutMapping
    public JobSeekerProfileResponse updateProfile(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody JobSeekerProfileRequest request) {
        return profileService.createOrUpdateProfile(userId, request);
    }
}
