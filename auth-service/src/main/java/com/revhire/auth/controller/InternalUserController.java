package com.revhire.auth.controller;

import com.revhire.auth.dto.NotificationResponse;
import com.revhire.auth.dto.UserDTO;
import com.revhire.auth.entity.NotificationType;
import com.revhire.auth.service.AuthService;
import com.revhire.auth.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/users")
public class InternalUserController {

    private static final Logger logger = LoggerFactory.getLogger(InternalUserController.class);

    private final AuthService authService;
    private final NotificationService notificationService;

    public InternalUserController(AuthService authService,
                                  NotificationService notificationService) {
        this.authService = authService;
        this.notificationService = notificationService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        logger.info("Internal request: Fetching user by ID: {}", userId);
        UserDTO user = authService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{userId}/notifications")
    public ResponseEntity<NotificationResponse> createNotification(
            @PathVariable Long userId,
            @RequestParam(required = false) Long jobId,
            @RequestParam NotificationType type,
            @RequestParam String message) {
        logger.info("Internal request: Creating notification for user ID: {}", userId);
        NotificationResponse notification = notificationService.createNotification(userId, jobId, type, message);
        return ResponseEntity.ok(notification);
    }
}
