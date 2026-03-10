package com.revature.notification.client;

import com.revature.notification.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "AUTH-SERVICE")
public interface AuthServiceClient {

    @GetMapping("/api/users/{userId}/exists")
    ApiResponse<Boolean> checkUserExists(@PathVariable("userId") Long userId);
}
