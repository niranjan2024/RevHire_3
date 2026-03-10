package com.revature.notification.dto;

import com.revature.notification.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class CreateNotificationRequest {

    @NotNull(message = "Recipient ID is required")
    private Long recipientId;

    private Long jobId;

    @NotNull(message = "Notification type is required")
    private NotificationType type;

    @NotBlank(message = "Message is required")
    private String message;

    // No-args constructor
    public CreateNotificationRequest() {
    }

    // All-args constructor
    public CreateNotificationRequest(Long recipientId, Long jobId, NotificationType type, String message) {
        this.recipientId = recipientId;
        this.jobId = jobId;
        this.type = type;
        this.message = message;
    }

    // Getters
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

    // Setters
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

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateNotificationRequest that = (CreateNotificationRequest) o;
        return Objects.equals(recipientId, that.recipientId) &&
               Objects.equals(jobId, that.jobId) &&
               type == that.type &&
               Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipientId, jobId, type, message);
    }

    // toString
    @Override
    public String toString() {
        return "CreateNotificationRequest{" +
                "recipientId=" + recipientId +
                ", jobId=" + jobId +
                ", type=" + type +
                ", message='" + message + '\'' +
                '}';
    }
}
