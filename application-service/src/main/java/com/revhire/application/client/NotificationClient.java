package com.revhire.application.client;

import com.revhire.application.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface NotificationClient {

    @PostMapping("/internal/notifications")
    void sendNotification(@RequestBody NotificationRequest request);
}
