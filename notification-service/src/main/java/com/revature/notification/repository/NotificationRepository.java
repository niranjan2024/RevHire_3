package com.revature.notification.repository;

import com.revature.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);

    Long countByRecipientIdAndIsReadFalse(Long recipientId);

    Optional<Notification> findByIdAndRecipientId(Long id, Long recipientId);
}
