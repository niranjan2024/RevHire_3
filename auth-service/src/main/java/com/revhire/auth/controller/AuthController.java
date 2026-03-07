package com.revhire.auth.controller;

import com.revhire.auth.dto.ApiResponse;
import com.revhire.auth.dto.AuthResponse;
import com.revhire.auth.dto.ChangePasswordRequest;
import com.revhire.auth.dto.ForgotPasswordRequest;
import com.revhire.auth.dto.LoginRequest;
import com.revhire.auth.dto.RegisterRequest;
import com.revhire.auth.dto.ResetPasswordRequest;
import com.revhire.auth.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        logger.info("Registration request received for username: {}", request.getUsername());
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        logger.info("Login request received");
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            logger.info("Logout request received for user: {}", authentication.getName());
            // In JWT-based auth, logout is primarily client-side (remove token)
            // This endpoint can be used for logging or cleanup
        }
        return ResponseEntity.ok(new ApiResponse("Logged out successfully"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        logger.info("Change password request received for user ID: {}", userId);
        authService.changePassword(userId, request);
        return ResponseEntity.ok(new ApiResponse("Password changed successfully"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        logger.info("Forgot password request received for email: {}", request.getEmail());
        String token = authService.forgotPassword(request.getEmail());
        // In production, you would send this token via email instead of returning it
        return ResponseEntity.ok(new ApiResponse("Password reset instructions sent to your email. Token: " + token));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        logger.info("Reset password request received");
        authService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(new ApiResponse("Password reset successfully"));
    }

    @GetMapping("/validate")
    public ResponseEntity<ApiResponse> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("Invalid token"));
        }

        String token = authHeader.substring(7);
        boolean isValid = authService.validateToken(token);

        if (isValid) {
            return ResponseEntity.ok(new ApiResponse("Token is valid"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("Invalid or expired token"));
        }
    }
}
