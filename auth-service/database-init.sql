-- ============================================
-- RevHire Auth Service Database Initialization
-- ============================================

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS revhire_auth_db;
USE revhire_auth_db;

-- ============================================
-- Users Table
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    full_name VARCHAR(100),
    mobile_number VARCHAR(15),
    security_question VARCHAR(255),
    security_answer VARCHAR(255),
    location VARCHAR(100),
    employment_status VARCHAR(20),
    profile_completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Password Reset Tokens Table
-- ============================================
CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    expiry_at TIMESTAMP NOT NULL,

    INDEX idx_token (token),
    INDEX idx_user_id (user_id),
    INDEX idx_expiry_at (expiry_at),

    CONSTRAINT fk_password_reset_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Sample Data (Development/Testing Only)
-- ============================================

-- Note: Passwords are BCrypt encoded
-- Plain text passwords for reference:
-- jobseeker1: password123
-- employer1: password123

-- Sample Job Seeker User
INSERT INTO users (username, email, password, role, full_name, mobile_number, security_question, security_answer, location, employment_status, profile_completed, created_at)
VALUES (
    'jobseeker1',
    'jobseeker1@example.com',
    '$2a$10$YourBCryptHashHere',  -- Replace with actual BCrypt hash
    'JOB_SEEKER',
    'John Seeker',
    '1234567890',
    'What is your favorite color?',
    '$2a$10$YourBCryptHashForAnswerHere',  -- Replace with actual BCrypt hash
    'New York, NY',
    'EMPLOYED',
    TRUE,
    CURRENT_TIMESTAMP
) ON DUPLICATE KEY UPDATE id=id;

-- Sample Employer User
INSERT INTO users (username, email, password, role, full_name, mobile_number, security_question, security_answer, location, employment_status, profile_completed, created_at)
VALUES (
    'employer1',
    'employer1@example.com',
    '$2a$10$YourBCryptHashHere',  -- Replace with actual BCrypt hash
    'EMPLOYER',
    'ABC Corporation',
    '0987654321',
    'What year was your company founded?',
    '$2a$10$YourBCryptHashForAnswerHere',  -- Replace with actual BCrypt hash
    'San Francisco, CA',
    NULL,
    TRUE,
    CURRENT_TIMESTAMP
) ON DUPLICATE KEY UPDATE id=id;

-- ============================================
-- Cleanup Job for Expired Tokens
-- ============================================

-- Create event to automatically delete expired tokens
-- (Requires event scheduler to be enabled)

-- Enable event scheduler
SET GLOBAL event_scheduler = ON;

-- Create cleanup event
DELIMITER //
CREATE EVENT IF NOT EXISTS cleanup_expired_tokens
ON SCHEDULE EVERY 1 HOUR
DO
BEGIN
    DELETE FROM password_reset_tokens WHERE expiry_at < NOW();
END//
DELIMITER ;

-- ============================================
-- Useful Queries for Development
-- ============================================

-- View all users
-- SELECT id, username, email, role, profile_completed, created_at FROM users;

-- View all active reset tokens
-- SELECT prt.id, prt.token, u.username, u.email, prt.expiry_at
-- FROM password_reset_tokens prt
-- JOIN users u ON prt.user_id = u.id
-- WHERE prt.expiry_at > NOW();

-- Count users by role
-- SELECT role, COUNT(*) as count FROM users GROUP BY role;

-- Find incomplete profiles
-- SELECT id, username, email, role, created_at
-- FROM users
-- WHERE profile_completed = FALSE;

-- ============================================
-- Database Backup Command
-- ============================================

-- To backup the database:
-- mysqldump -u root -p revhire_auth_db > backup_revhire_auth_db.sql

-- To restore from backup:
-- mysql -u root -p revhire_auth_db < backup_revhire_auth_db.sql

-- ============================================
-- Performance Optimization
-- ============================================

-- Analyze tables for optimization
ANALYZE TABLE users;
ANALYZE TABLE password_reset_tokens;

-- Check table status
-- SHOW TABLE STATUS LIKE 'users';
-- SHOW TABLE STATUS LIKE 'password_reset_tokens';

-- ============================================
-- Security Recommendations
-- ============================================

-- 1. Create a dedicated database user for the application
-- CREATE USER 'auth_service_user'@'localhost' IDENTIFIED BY 'secure_password';
-- GRANT SELECT, INSERT, UPDATE, DELETE ON revhire_auth_db.* TO 'auth_service_user'@'localhost';
-- FLUSH PRIVILEGES;

-- 2. Ensure strong password policy
-- SET GLOBAL validate_password.policy = STRONG;

-- 3. Regular backups
-- Schedule daily backups of the database

-- 4. Monitor failed login attempts
-- Consider implementing rate limiting at application level

-- ============================================
-- End of Initialization Script
-- ============================================
