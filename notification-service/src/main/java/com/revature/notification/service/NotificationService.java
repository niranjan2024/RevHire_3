package com.revature.notification.service;

import com.revature.notification.client.AuthServiceClient;
import com.revature.notification.dto.ApiResponse;
import com.revature.notification.dto.CreateNotificationRequest;
import com.revature.notification.dto.NotificationResponse;
import com.revature.notification.entity.Notification;
import com.revature.notification.enums.NotificationType;
import com.revature.notification.exception.ApiException;
import com.revature.notification.exception.NotFoundException;
import com.revature.notification.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final AuthServiceClient authServiceClient;

    // Constructor for final fields
    public NotificationService(NotificationRepository notificationRepository, AuthServiceClient authServiceClient) {
        this.notificationRepository = notificationRepository;
        this.authServiceClient = authServiceClient;
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(Long userId) {
        log.info("Fetching notifications for user: {}", userId);

        List<Notification> notifications = notificationRepository
                .findByRecipientIdOrderByCreatedAtDesc(userId);

        return notifications.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long getUnreadCount(Long userId) {
        log.info("Fetching unread notification count for user: {}", userId);
        return notificationRepository.countByRecipientIdAndIsReadFalse(userId);
    }

    @Transactional
    public NotificationResponse markAsRead(Long userId, Long notificationId) {
        log.info("Marking notification {} as read for user: {}", notificationId, userId);

        Notification notification = notificationRepository
                .findByIdAndRecipientId(notificationId, userId)
                .orElseThrow(() -> new NotFoundException(
                        "Notification not found with id: " + notificationId));

        if (notification.getIsRead()) {
            throw new ApiException(
                    "Notification is already marked as read",
                    HttpStatus.BAD_REQUEST);
        }

        notification.setIsRead(true);
        Notification savedNotification = notificationRepository.save(notification);

        log.info("Notification {} marked as read successfully", notificationId);
        return convertToResponse(savedNotification);
    }

    @Transactional
    public NotificationResponse createNotification(CreateNotificationRequest request) {
        log.info("Creating notification for recipient: {}", request.getRecipientId());

        // Validate recipient exists
        validateUserExists(request.getRecipientId());

        Notification notification = new Notification();
        notification.setRecipientId(request.getRecipientId());
        notification.setJobId(request.getJobId());
        notification.setType(request.getType());
        notification.setMessage(request.getMessage());
        notification.setIsRead(false);

        Notification savedNotification = notificationRepository.save(notification);
        log.info("Notification created successfully with id: {}", savedNotification.getId());

        return convertToResponse(savedNotification);
    }

    @Transactional
    public NotificationResponse notifyApplicationReceived(
            Long employerId,
            Long jobId,
            String jobTitle,
            String applicantName) {

        log.info("Creating application received notification for employer: {}", employerId);

        String message = String.format(
                "New application received from %s for job: %s",
                applicantName,
                jobTitle);

        CreateNotificationRequest request = new CreateNotificationRequest(
                employerId,
                jobId,
                NotificationType.APPLICATION_RECEIVED,
                message
        );

        return createNotification(request);
    }

    @Transactional
    public NotificationResponse notifyApplicationStatusChange(
            Long jobSeekerId,
            Long jobId,
            String jobTitle,
            String newStatus) {

        log.info("Creating application status change notification for job seeker: {}", jobSeekerId);

        String message = String.format(
                "Your application for '%s' has been updated to: %s",
                jobTitle,
                newStatus);

        CreateNotificationRequest request = new CreateNotificationRequest(
                jobSeekerId,
                jobId,
                NotificationType.APPLICATION_UPDATE,
                message
        );

        return createNotification(request);
    }

    private void validateUserExists(Long userId) {
        try {
            ApiResponse<Boolean> response = authServiceClient.checkUserExists(userId);
            if (response == null || response.getData() == null || !response.getData()) {
                throw new NotFoundException("User not found with id: " + userId);
            }
        } catch (Exception e) {
            log.error("Error validating user existence: {}", e.getMessage());
            throw new ApiException(
                    "Unable to validate user: " + e.getMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private NotificationResponse convertToResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getRecipientId(),
                notification.getJobId(),
                notification.getType(),
                notification.getMessage(),
                notification.getIsRead(),
                notification.getCreatedAt()
        );
    }
}
