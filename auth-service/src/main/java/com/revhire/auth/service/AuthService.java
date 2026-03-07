package com.revhire.auth.service;

import com.revhire.auth.dto.AuthResponse;
import com.revhire.auth.dto.ChangePasswordRequest;
import com.revhire.auth.dto.LoginRequest;
import com.revhire.auth.dto.RegisterRequest;
import com.revhire.auth.dto.UserDTO;
import com.revhire.auth.entity.PasswordResetToken;
import com.revhire.auth.entity.User;
import com.revhire.auth.exception.BadRequestException;
import com.revhire.auth.exception.ConflictException;
import com.revhire.auth.exception.NotFoundException;
import com.revhire.auth.repository.PasswordResetTokenRepository;
import com.revhire.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final int PASSWORD_RESET_EXPIRY_HOURS = 1;

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordResetTokenRepository passwordResetTokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        logger.info("Attempting to register user: {}", request.getUsername());

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        if (userRepository.existsByUsernameIgnoreCase(request.getUsername())) {
            throw new ConflictException("Username already exists");
        }

        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setFullName(request.getFullName());
        user.setMobileNumber(request.getMobileNumber());
        user.setSecurityQuestion(request.getSecurityQuestion());
        user.setSecurityAnswer(passwordEncoder.encode(request.getSecurityAnswer()));
        user.setLocation(request.getLocation());
        user.setEmploymentStatus(request.getEmploymentStatus());

        user = userRepository.save(user);

        String token = jwtService.generateToken(user.getId(), user.getUsername(), user.getRole());

        logger.info("User registered successfully: {}", user.getUsername());

        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getMobileNumber(),
                user.getLocation(),
                user.getEmploymentStatus() != null ? user.getEmploymentStatus().name() : null,
                user.getRole(),
                token
        );
    }

    public AuthResponse login(LoginRequest request) {
        logger.info("Attempting login for: {}", request.getUsernameOrEmail());

        User user = userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(
                request.getUsernameOrEmail(),
                request.getUsernameOrEmail()
        ).orElseThrow(() -> new BadCredentialsException("Invalid username/email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username/email or password");
        }

        String token = jwtService.generateToken(user.getId(), user.getUsername(), user.getRole());

        logger.info("User logged in successfully: {}", user.getUsername());

        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getMobileNumber(),
                user.getLocation(),
                user.getEmploymentStatus() != null ? user.getEmploymentStatus().name() : null,
                user.getRole(),
                token
        );
    }

    public void logout(Long userId) {
        logger.info("User logged out: {}", userId);
        // In a stateless JWT system, logout is typically handled client-side
        // This method can be used for logging or cleanup tasks
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        logger.info("Attempting to change password for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }

        if (request.getOldPassword().equals(request.getNewPassword())) {
            throw new BadRequestException("New password must be different from old password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        logger.info("Password changed successfully for user ID: {}", userId);
    }

    @Transactional
    public String forgotPassword(String email) {
        logger.info("Processing forgot password request for email: {}", email);

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotFoundException("No account found with this email"));

        // Delete any existing tokens for this user
        passwordResetTokenRepository.deleteByUser(user);

        // Create new token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryAt(LocalDateTime.now().plusHours(PASSWORD_RESET_EXPIRY_HOURS));

        passwordResetTokenRepository.save(resetToken);

        logger.info("Password reset token created for user: {}", user.getUsername());

        // In a real application, you would send this token via email
        return token;
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        logger.info("Attempting to reset password with token");

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid password reset token"));

        if (resetToken.getExpiryAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Password reset token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);

        logger.info("Password reset successfully for user: {}", user.getUsername());
    }

    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }

    public UserDTO getUserById(Long userId) {
        logger.info("Fetching user by ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setMobileNumber(user.getMobileNumber());
        dto.setLocation(user.getLocation());
        dto.setEmploymentStatus(user.getEmploymentStatus() != null ? user.getEmploymentStatus().name() : null);
        dto.setRole(user.getRole());
        dto.setProfileCompleted(user.isProfileCompleted());

        return dto;
    }
}
