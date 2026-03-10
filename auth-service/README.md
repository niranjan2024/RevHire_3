# Auth Service - RevHire Platform

Authentication and Authorization microservice for the RevHire recruitment platform.

## Overview

The Auth Service handles user registration, login, JWT token generation/validation, password management, and profile completion for both Job Seekers and Employers.

## Developer

**Module Owner:** Niranjan

## Technology Stack

- Java 17
- Spring Boot 3.3.2
- Spring Security
- Spring Data JPA
- MySQL
- JWT (JSON Web Tokens) - JJWT 0.11.5
- Spring Cloud Netflix Eureka Client
- Lombok

## Features

### User Management
- User registration with role-based access (JOB_SEEKER/EMPLOYER)
- User login with JWT token generation
- User logout (token invalidation on client-side)
- Profile completion workflow

### Security
- JWT-based authentication
- BCrypt password encoding
- Security question/answer for password recovery
- Input sanitization to prevent XSS and SQL injection

### Password Management
- Change password (requires current password)
- Forgot password with security answer verification
- Reset password using time-limited reset tokens (30 minutes expiry)

### Inter-Service Communication
- Token validation endpoint for other microservices
- User information lookup endpoint

## Database Schema

### Users Table
- `id` - Primary key
- `username` - Unique username
- `email` - Unique email address
- `password` - BCrypt encrypted password
- `role` - User role (JOB_SEEKER/EMPLOYER)
- `full_name` - User's full name
- `mobile_number` - Contact number
- `security_question` - Security question for password recovery
- `security_answer` - Encrypted security answer
- `location` - User location
- `employment_status` - Employment status (FRESHER/EMPLOYED/UNEMPLOYED)
- `profile_completed` - Profile completion flag
- `created_at` - Account creation timestamp

### Password Reset Tokens Table
- `id` - Primary key
- `token` - Unique reset token (UUID)
- `user_id` - Foreign key to users table
- `expiry_at` - Token expiration timestamp

## API Endpoints

### Public Endpoints (No Authentication Required)

#### Register User
```
POST /api/auth/register
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123",
  "role": "JOB_SEEKER",
  "fullName": "John Doe",
  "mobileNumber": "1234567890",
  "securityQuestion": "What is your favorite color?",
  "securityAnswer": "Blue"
}
```

#### Login
```
POST /api/auth/login
Content-Type: application/json

{
  "username": "johndoe",
  "password": "password123"
}
```

#### Forgot Password
```
POST /api/auth/forgot-password
Content-Type: application/json

{
  "email": "john@example.com",
  "securityAnswer": "Blue"
}
```

#### Reset Password
```
POST /api/auth/reset-password
Content-Type: application/json

{
  "resetToken": "uuid-token-here",
  "newPassword": "newpassword123"
}
```

#### Validate Token (Inter-Service)
```
GET /api/auth/validate?token={jwt-token}
```

### Protected Endpoints (Authentication Required)

#### Logout
```
POST /api/auth/logout
Authorization: Bearer {jwt-token}
```

#### Change Password
```
POST /api/auth/change-password
Authorization: Bearer {jwt-token}
Content-Type: application/json

{
  "currentPassword": "password123",
  "newPassword": "newpassword123"
}
```

#### Update Profile Completion
```
POST /api/auth/profile-completion/{userId}
Authorization: Bearer {jwt-token}
Content-Type: application/json

{
  "fullName": "John Doe",
  "mobileNumber": "1234567890",
  "location": "New York",
  "employmentStatus": "EMPLOYED"
}
```

#### Get User by ID (Inter-Service)
```
GET /api/auth/user/{userId}
Authorization: Bearer {jwt-token}
```

## Configuration

### application.yml

```yaml
server:
  port: 8081

spring:
  application:
    name: AUTH-SERVICE
  datasource:
    url: jdbc:mysql://localhost:3306/revhire_auth_db
    username: root
    password: root

jwt:
  secret: {base64-encoded-secret}
  expiration: 86400000  # 24 hours

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

## Building and Running

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Eureka Server running on port 8761

### Database Setup
```sql
CREATE DATABASE revhire_auth_db;
```

### Build
```bash
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

The service will start on port 8081.

## Security Features

1. **JWT Authentication**: Stateless token-based authentication
2. **BCrypt Password Encoding**: Secure password storage
3. **Input Sanitization**: Protection against XSS and SQL injection
4. **Security Questions**: Additional layer for password recovery
5. **Token Expiration**: Time-limited reset tokens for security

## Role-Based Access

- **JOB_SEEKER**: Job seekers looking for opportunities
- **EMPLOYER**: Employers posting job openings

## Error Handling

The service provides comprehensive error handling with appropriate HTTP status codes:
- `200 OK` - Successful requests
- `201 Created` - Successful registration
- `400 Bad Request` - Validation errors or invalid input
- `401 Unauthorized` - Invalid credentials
- `403 Forbidden` - Access denied (e.g., wrong security answer)
- `404 Not Found` - User or resource not found
- `409 Conflict` - Username or email already exists
- `500 Internal Server Error` - Server errors

## Testing

Run tests with:
```bash
mvn test
```

## Integration with Other Services

This Auth Service integrates with:
- **Eureka Server**: Service discovery and registration
- **API Gateway**: Centralized routing and authentication
- **Job Seeker Service**: User profile management
- **Employer Service**: Employer profile management
- **Job Service**: Job posting authorization
- **Application Service**: Application submission authorization

## License

Copyright (c) 2024 Revature. All rights reserved.
