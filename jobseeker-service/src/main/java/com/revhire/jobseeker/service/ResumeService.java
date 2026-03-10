package com.revhire.jobseeker.service;

import com.revhire.jobseeker.client.AuthServiceClient;
import com.revhire.jobseeker.dto.ResumeRequest;
import com.revhire.jobseeker.dto.ResumeResponse;
import com.revhire.jobseeker.dto.ResumeUploadResponse;
import com.revhire.jobseeker.dto.UserDTO;
import com.revhire.jobseeker.entity.Resume;
import com.revhire.jobseeker.exception.BadRequestException;
import com.revhire.jobseeker.exception.ForbiddenException;
import com.revhire.jobseeker.exception.NotFoundException;
import com.revhire.jobseeker.repository.ResumeRepository;
import com.revhire.jobseeker.util.InputSanitizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    private static final long MAX_FILE_SIZE = 2L * 1024 * 1024;
    private static final Set<String> ALLOWED_TYPES = Set.of(
            "application/pdf",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".pdf", ".docx");
    private static final int MIN_YEAR = 1980;
    private static final Pattern YEAR_PATTERN = Pattern.compile("\\b(19\\d{2}|20\\d{2})\\b");
    private static final Pattern YEARS_PATTERN = Pattern.compile("(?i)(\\d+)\\s*years?");

    private final ResumeRepository resumeRepository;
    private final AuthServiceClient authServiceClient;

    public ResumeService(ResumeRepository resumeRepository,
                         AuthServiceClient authServiceClient) {
        this.resumeRepository = resumeRepository;
        this.authServiceClient = authServiceClient;
    }

    @Transactional(readOnly = true)
    public ResumeResponse getResume(Long userId) {
        getUserAndValidateRole(userId);
        Resume resume = resumeRepository.findByUserId(userId)
                .orElseGet(() -> createEmptyResume(userId));
        return toResponse(resume);
    }

    @Transactional
    public ResumeResponse createOrUpdateResume(Long userId, ResumeRequest request) {
        getUserAndValidateRole(userId);

        Resume resume = resumeRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Resume newResume = new Resume();
                    newResume.setUserId(userId);
                    return newResume;
                });

        validateTextResume(request);

        resume.setObjective(InputSanitizer.require(request.getObjective(), "objective"));
        resume.setEducationCsv(toCsv(request.getEducation()));
        resume.setExperienceCsv(toCsv(request.getExperience()));
        resume.setProjectsCsv(toCsv(request.getProjects()));
        resume.setCertificationsCsv(toCsv(request.getCertifications()));
        resume.setSkills(InputSanitizer.sanitize(request.getSkills(), "skills"));

        resumeRepository.save(resume);
        return toResponse(resume);
    }

    @Transactional
    public ResumeUploadResponse uploadResume(Long userId, MultipartFile file) {
        getUserAndValidateRole(userId);

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Resume file is required");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size must be up to 2MB");
        }

        String originalName = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().trim();
        String contentType = file.getContentType() == null ? "" : file.getContentType().trim();

        if (!isAllowedFile(originalName, contentType)) {
            throw new BadRequestException("Allowed formats: pdf, docx");
        }

        Resume resume = resumeRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Resume newResume = new Resume();
                    newResume.setUserId(userId);
                    return newResume;
                });

        byte[] fileData;
        try {
            fileData = file.getBytes();
        } catch (java.io.IOException ex) {
            throw new BadRequestException("Unable to process resume file");
        }

        resume.setUploadedFileName(originalName);
        resume.setUploadedFileType(contentType);
        resume.setUploadedFileSize(file.getSize());
        resume.setUploadedFileReference("resume_" + UUID.randomUUID());
        resume.setUploadedFileData(fileData);

        resumeRepository.save(resume);

        return new ResumeUploadResponse(
                "Resume file uploaded successfully",
                originalName,
                contentType,
                file.getSize(),
                resume.getUploadedFileReference()
        );
    }

    @Transactional(readOnly = true)
    public boolean hasResume(Long userId) {
        return resumeRepository.existsByUserId(userId);
    }

    private boolean isAllowedFile(String originalName, String contentType) {
        String lowerName = originalName.toLowerCase();
        boolean extensionAllowed = ALLOWED_EXTENSIONS.stream().anyMatch(lowerName::endsWith);
        boolean contentTypeAllowed = ALLOWED_TYPES.contains(contentType);
        return extensionAllowed && contentTypeAllowed;
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

    private Resume createEmptyResume(Long userId) {
        Resume resume = new Resume();
        resume.setUserId(userId);
        return resume;
    }

    private ResumeResponse toResponse(Resume resume) {
        ResumeResponse response = new ResumeResponse();
        response.setObjective(resume.getObjective());
        response.setEducation(resume.getEducationCsv());
        response.setExperience(resume.getExperienceCsv());
        response.setProjects(resume.getProjectsCsv());
        response.setCertifications(resume.getCertificationsCsv());
        response.setSkills(resume.getSkills());
        response.setUploadedFileName(resume.getUploadedFileName());
        response.setUploadedFileType(resume.getUploadedFileType());
        response.setUploadedFileSize(resume.getUploadedFileSize());
        response.setUploadedFileReference(resume.getUploadedFileReference());
        return response;
    }

    private String toCsv(String value) {
        String cleaned = InputSanitizer.sanitize(value, "value");
        if (cleaned == null) {
            return null;
        }
        return Arrays.stream(cleaned.split(","))
                .map(String::trim)
                .filter(part -> !part.isEmpty())
                .collect(Collectors.joining(","));
    }

    private void validateTextResume(ResumeRequest request) {
        String objective = InputSanitizer.require(request.getObjective(), "objective");
        if (objective.length() > 500) {
            throw new BadRequestException("Objective must be up to 500 characters");
        }

        String education = InputSanitizer.require(request.getEducation(), "education");
        validateEducationEntries(education);

        String experience = InputSanitizer.sanitize(request.getExperience(), "experience");
        validateExperienceEntries(experience);

        String projects = InputSanitizer.sanitize(request.getProjects(), "projects");
        validateProjectEntries(projects);
    }

    private void validateEducationEntries(String education) {
        String[] entries = education.split(",");
        int currentYear = LocalDate.now().getYear();

        for (String entry : entries) {
            String trimmed = entry.trim();
            if (trimmed.isEmpty()) {
                continue;
            }

            if (!trimmed.matches(".*[A-Za-z].*")) {
                throw new BadRequestException("Education entry must include a degree");
            }

            Matcher matcher = YEAR_PATTERN.matcher(trimmed);
            if (!matcher.find()) {
                throw new BadRequestException("Education entry must include a year");
            }

            int year = Integer.parseInt(matcher.group(1));
            if (year < MIN_YEAR || year > currentYear) {
                throw new BadRequestException("Education year must be between 1980 and current year");
            }
        }
    }

    private void validateExperienceEntries(String experience) {
        if (experience == null || experience.isBlank()) {
            return;
        }

        String[] entries = experience.split(",");
        int currentYear = LocalDate.now().getYear();

        for (String entry : entries) {
            String trimmed = entry.trim();
            if (trimmed.isEmpty()) {
                continue;
            }

            Matcher yearsMatcher = YEARS_PATTERN.matcher(trimmed);
            if (yearsMatcher.find()) {
                int years = Integer.parseInt(yearsMatcher.group(1));
                if (years < 0) {
                    throw new BadRequestException("Experience years must be at least 0");
                }
            }

            Matcher matcher = YEAR_PATTERN.matcher(trimmed);
            int[] years = matcher.results()
                    .limit(2)
                    .mapToInt(result -> Integer.parseInt(result.group(1)))
                    .toArray();

            if (years.length >= 2) {
                int start = years[0];
                int end = years[1];
                if (start > end) {
                    throw new BadRequestException("Experience end year must be after start year");
                }
                if (start < MIN_YEAR || end > currentYear) {
                    throw new BadRequestException("Experience years must be between 1980 and current year");
                }
            }
        }
    }

    private void validateProjectEntries(String projects) {
        if (projects == null || projects.isBlank()) {
            return;
        }

        long count = Arrays.stream(projects.split(","))
                .map(String::trim)
                .filter(part -> !part.isEmpty())
                .count();

        if (count > 5) {
            throw new BadRequestException("Projects cannot exceed 5 entries");
        }
    }
}
