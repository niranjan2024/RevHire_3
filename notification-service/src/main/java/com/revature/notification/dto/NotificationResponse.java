package com.revature.notification.dto;

import com.revature.notification.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.Objects;

public class NotificationResponse {

    private Long id;
    private Long recipientId;
    private Long jobId;
    private NotificationType type;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;

    // No-args constructor
    public NotificationResponse() {
    }

    // All-args constructor
    public NotificationResponse(Long id, Long recipientId, Long jobId, NotificationType type, String message, Boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.recipientId = recipientId;
        this.jobId = jobId;
        this.type = type;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public Long getJobId() {
        return jobId;
    }

    public NotificationType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationResponse that = (NotificationResponse) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(recipientId, that.recipientId) &&
               Objects.equals(jobId, that.jobId) &&
               type == that.type &&
               Objects.equals(message, that.message) &&
               Objects.equals(isRead, that.isRead) &&
               Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recipientId, jobId, type, message, isRead, createdAt);
    }

    // toString
    @Override
    public String toString() {
        return "NotificationResponse{" +
                "id=" + id +
                ", recipientId=" + recipientId +
                ", jobId=" + jobId +
                ", type=" + type +
                ", message='" + message + '\'' +
                ", isRead=" + isRead +
                ", createdAt=" + createdAt +
                '}';
    }
}
