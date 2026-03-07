package com.revhire.application.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {

    @GetMapping("/internal/users/{userId}/validate")
    Boolean validateUser(@PathVariable("userId") Long userId);

    @GetMapping("/internal/users/{userId}/role")
    String getUserRole(@PathVariable("userId") Long userId);
}
