package com.revhire.auth.repository;

import com.revhire.auth.entity.Notification;
import com.revhire.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipientOrderByCreatedAtDesc(User recipient);

    List<Notification> findByRecipientAndIsReadOrderByCreatedAtDesc(User recipient, boolean isRead);

    long countByRecipientAndIsRead(User recipient, boolean isRead);
}
