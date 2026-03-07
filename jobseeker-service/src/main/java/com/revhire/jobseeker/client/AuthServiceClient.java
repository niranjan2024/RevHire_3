package com.revhire.jobseeker.client;

import com.revhire.jobseeker.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {

    @GetMapping("/internal/users/{userId}")
    UserDTO getUserById(@PathVariable("userId") Long userId);

    @GetMapping("/internal/users/username/{username}")
    UserDTO getUserByUsername(@PathVariable("username") String username);
}
