package com.revhire.jobseeker.service;

import com.revhire.jobseeker.client.AuthServiceClient;
import com.revhire.jobseeker.dto.JobSeekerProfileRequest;
import com.revhire.jobseeker.dto.JobSeekerProfileResponse;
import com.revhire.jobseeker.dto.UserDTO;
import com.revhire.jobseeker.entity.JobSeekerProfile;
import com.revhire.jobseeker.exception.BadRequestException;
import com.revhire.jobseeker.exception.ForbiddenException;
import com.revhire.jobseeker.exception.NotFoundException;
import com.revhire.jobseeker.repository.JobSeekerProfileRepository;
import com.revhire.jobseeker.util.InputSanitizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
public class JobSeekerProfileService {

    private final JobSeekerProfileRepository profileRepository;
    private final AuthServiceClient authServiceClient;

    public JobSeekerProfileService(JobSeekerProfileRepository profileRepository,
                                   AuthServiceClient authServiceClient) {
        this.profileRepository = profileRepository;
        this.authServiceClient = authServiceClient;
    }

    @Transactional(readOnly = true)
    public JobSeekerProfileResponse getProfile(Long userId) {
        UserDTO user = getUserAndValidateRole(userId);
        JobSeekerProfile profile = profileRepository.findByUserId(userId)
                .orElseGet(() -> createEmptyProfile(userId));
        return toResponse(user, profile);
    }

    @Transactional
    public JobSeekerProfileResponse createOrUpdateProfile(Long userId, JobSeekerProfileRequest request) {
        UserDTO user = getUserAndValidateRole(userId);

        JobSeekerProfile profile = profileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    JobSeekerProfile newProfile = new JobSeekerProfile();
                    newProfile.setUserId(userId);
                    return newProfile;
                });

        String skills = InputSanitizer.require(request.getSkills(), "skills");
        validateSkills(skills);

        profile.setSkills(InputSanitizer.sanitize(skills, "skills"));
        profile.setEducation(InputSanitizer.sanitize(request.getEducation(), "education"));
        profile.setCertifications(InputSanitizer.sanitize(request.getCertifications(), "certifications"));
        profile.setHeadline(InputSanitizer.sanitize(request.getHeadline(), "headline"));
        profile.setSummary(InputSanitizer.sanitize(request.getSummary(), "summary"));

        profileRepository.save(profile);
        return toResponse(user, profile);
    }

    public void validateSkills(String skills) {
        int count = (int) Arrays.stream(skills.split("[,\\s]+"))
                .map(String::trim)
                .filter(token -> !token.isBlank())
                .count();

        if (count < 1) {
            throw new BadRequestException("At least one skill is required");
        }
        if (count > 50) {
            throw new BadRequestException("Skills cannot exceed 50 items");
        }
    }

    private UserDTO getUserAndValidateRole(Long userId) {
        UserDTO user = authServiceClient.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        if (!"JOB_SEEKER".equals(user.getRole())) {
            throw new ForbiddenException("Only job seekers can access this resource");
        }
        return user;
    }

    private JobSeekerProfile createEmptyProfile(Long userId) {
        JobSeekerProfile profile = new JobSeekerProfile();
        profile.setUserId(userId);
        return profile;
    }

    private JobSeekerProfileResponse toResponse(UserDTO user, JobSeekerProfile profile) {
        JobSeekerProfileResponse response = new JobSeekerProfileResponse();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setMobileNumber(user.getMobileNumber());
        response.setSkills(profile.getSkills());
        response.setEducation(profile.getEducation());
        response.setCertifications(profile.getCertifications());
        response.setHeadline(profile.getHeadline());
        response.setSummary(profile.getSummary());
        return response;
    }
}
