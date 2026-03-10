package com.revature.notification.controller;

import com.revature.notification.dto.ApiResponse;
import com.revature.notification.dto.CreateNotificationRequest;
import com.revature.notification.dto.NotificationResponse;
import com.revature.notification.service.NotificationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;

    // Constructor for final fields
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotificationsByUserId(
            @PathVariable Long userId) {
        log.info("GET /api/notifications/user/{} - Fetching notifications", userId);

        List<NotificationResponse> notifications = notificationService.getNotifications(userId);

        return ResponseEntity.ok(ApiResponse.success(
                "Notifications retrieved successfully",
                notifications));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(
            @RequestHeader("X-User-Id") Long userId) {
        log.info("GET /api/notifications/unread-count - User: {}", userId);

        Long unreadCount = notificationService.getUnreadCount(userId);

        return ResponseEntity.ok(ApiResponse.success(
                "Unread count retrieved successfully",
                unreadCount));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(
            @PathVariable Long notificationId,
            @RequestHeader("X-User-Id") Long userId) {
        log.info("PATCH /api/notifications/{}/read - User: {}", notificationId, userId);

        NotificationResponse notification = notificationService.markAsRead(userId, notificationId);

        return ResponseEntity.ok(ApiResponse.success(
                "Notification marked as read",
                notification));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationResponse>> createNotification(
            @Valid @RequestBody CreateNotificationRequest request) {
        log.info("POST /api/notifications - Creating notification for recipient: {}",
                request.getRecipientId());

        NotificationResponse notification = notificationService.createNotification(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Notification created successfully", notification));
    }

    @PostMapping("/application-received")
    public ResponseEntity<ApiResponse<NotificationResponse>> notifyApplicationReceived(
            @RequestBody Map<String, Object> payload) {
        log.info("POST /api/notifications/application-received - Creating notification");

        Long employerId = Long.valueOf(payload.get("employerId").toString());
        Long jobId = Long.valueOf(payload.get("jobId").toString());
        String jobTitle = payload.get("jobTitle").toString();
        String applicantName = payload.get("applicantName").toString();

        NotificationResponse notification = notificationService.notifyApplicationReceived(
                employerId, jobId, jobTitle, applicantName);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Application received notification created",
                        notification));
    }

    @PostMapping("/application-status-change")
    public ResponseEntity<ApiResponse<NotificationResponse>> notifyApplicationStatusChange(
            @RequestBody Map<String, Object> payload) {
        log.info("POST /api/notifications/application-status-change - Creating notification");

        Long jobSeekerId = Long.valueOf(payload.get("jobSeekerId").toString());
        Long jobId = Long.valueOf(payload.get("jobId").toString());
        String jobTitle = payload.get("jobTitle").toString();
        String newStatus = payload.get("newStatus").toString();

        NotificationResponse notification = notificationService.notifyApplicationStatusChange(
                jobSeekerId, jobId, jobTitle, newStatus);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Application status change notification created",
                        notification));
    }
}
