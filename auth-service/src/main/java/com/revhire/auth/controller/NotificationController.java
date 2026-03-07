package com.revhire.auth.controller;

import com.revhire.auth.dto.ApiResponse;
import com.revhire.auth.dto.NotificationResponse;
import com.revhire.auth.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(
            @RequestHeader("X-User-Id") Long userId) {
        logger.info("Fetching all notifications for user ID: {}", userId);
        List<NotificationResponse> notifications = notificationService.getNotificationsByUser(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(
            @RequestHeader("X-User-Id") Long userId) {
        logger.info("Fetching unread notifications for user ID: {}", userId);
        List<NotificationResponse> notifications = notificationService.getUnreadNotificationsByUser(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread/count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @RequestHeader("X-User-Id") Long userId) {
        logger.info("Fetching unread notification count for user ID: {}", userId);
        long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse> markAsRead(
            @PathVariable Long notificationId,
            @RequestHeader("X-User-Id") Long userId) {
        logger.info("Marking notification {} as read for user ID: {}", notificationId, userId);
        notificationService.markAsRead(notificationId, userId);
        return ResponseEntity.ok(new ApiResponse("Notification marked as read"));
    }

    @PatchMapping("/read-all")
    public ResponseEntity<ApiResponse> markAllAsRead(
            @RequestHeader("X-User-Id") Long userId) {
        logger.info("Marking all notifications as read for user ID: {}", userId);
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(new ApiResponse("All notifications marked as read"));
    }
}
