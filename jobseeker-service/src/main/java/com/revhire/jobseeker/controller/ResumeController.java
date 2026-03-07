package com.revhire.jobseeker.controller;

import com.revhire.jobseeker.dto.ResumeRequest;
import com.revhire.jobseeker.dto.ResumeResponse;
import com.revhire.jobseeker.dto.ResumeUploadResponse;
import com.revhire.jobseeker.service.ResumeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @GetMapping
    public ResumeResponse getResume(@RequestHeader("X-User-Id") Long userId) {
        return resumeService.getResume(userId);
    }

    @PutMapping
    public ResumeResponse updateResume(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody ResumeRequest request) {
        return resumeService.createOrUpdateResume(userId, request);
    }

    @PostMapping("/upload")
    public ResumeUploadResponse uploadFormattedResume(
            @RequestHeader("X-User-Id") Long userId,
            @RequestPart("file") MultipartFile file) {
        return resumeService.uploadResume(userId, file);
    }
}
