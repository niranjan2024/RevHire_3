package com.revhire.auth.service;

import com.revhire.auth.dto.NotificationResponse;
import com.revhire.auth.entity.Notification;
import com.revhire.auth.entity.NotificationType;
import com.revhire.auth.entity.User;
import com.revhire.auth.exception.NotFoundException;
import com.revhire.auth.repository.NotificationRepository;
import com.revhire.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository,
                               UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public NotificationResponse createNotification(Long recipientId, Long jobId, NotificationType type, String message) {
        logger.info("Creating notification for user ID: {}", recipientId);

        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new NotFoundException("Recipient user not found"));

        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setJobId(jobId);
        notification.setType(type);
        notification.setMessage(message);

        notification = notificationRepository.save(notification);

        logger.info("Notification created with ID: {}", notification.getId());

        return mapToResponse(notification);
    }

    public List<NotificationResponse> getNotificationsByUser(Long userId) {
        logger.info("Fetching all notifications for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Notification> notifications = notificationRepository.findByRecipientOrderByCreatedAtDesc(user);

        return notifications.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<NotificationResponse> getUnreadNotificationsByUser(Long userId) {
        logger.info("Fetching unread notifications for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Notification> notifications = notificationRepository.findByRecipientAndIsReadOrderByCreatedAtDesc(user, false);

        return notifications.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        logger.info("Marking notification {} as read for user ID: {}", notificationId, userId);

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException("Notification not found"));

        if (!notification.getRecipient().getId().equals(userId)) {
            throw new NotFoundException("Notification not found");
        }

        notification.setRead(true);
        notificationRepository.save(notification);

        logger.info("Notification {} marked as read", notificationId);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        logger.info("Marking all notifications as read for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Notification> unreadNotifications = notificationRepository.findByRecipientAndIsReadOrderByCreatedAtDesc(user, false);

        unreadNotifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(unreadNotifications);

        logger.info("All notifications marked as read for user ID: {}", userId);
    }

    public long getUnreadCount(Long userId) {
        logger.info("Getting unread notification count for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return notificationRepository.countByRecipientAndIsRead(user, false);
    }

    private NotificationResponse mapToResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setJobId(notification.getJobId());
        response.setMessage(notification.getMessage());
        response.setType(notification.getType().name());
        response.setRead(notification.isRead());
        response.setCreatedAt(notification.getCreatedAt());
        return response;
    }
}
