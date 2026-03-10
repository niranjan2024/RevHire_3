package com.revhire.jobseeker.controller;

import com.revhire.jobseeker.service.ResumeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/jobseeker")
public class InternalJobSeekerController {

    private final ResumeService resumeService;

    public InternalJobSeekerController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @GetMapping("/users/{userId}/has-resume")
    public boolean hasResume(@PathVariable Long userId) {
        return resumeService.hasResume(userId);
    }
}
