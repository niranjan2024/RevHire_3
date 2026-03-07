# Auth Service - RevHire Platform

## Overview
The Authentication & Notification microservice handles user authentication, authorization, and notification management for the RevHire platform.

**Developer:** Niranjan
**Responsibilities:** Authentication and Notification Management

## Technology Stack
- **Framework:** Spring Boot 3.2.5
- **Language:** Java 17
- **Security:** Spring Security with JWT
- **Database:** MySQL
- **Service Discovery:** Eureka Client
- **Configuration:** Spring Cloud Config Client
- **Inter-service Communication:** OpenFeign

## Features

### Authentication
- User registration (Job Seeker & Employer)
- User login with JWT token generation
- Password change
- Forgot password with token-based reset
- Token validation

### Notification Management
- Create notifications for users
- Get all notifications for a user
- Get unread notifications
- Mark notification as read
- Mark all notifications as read
- Get unread notification count

### Internal APIs
- Get user by ID (for inter-service communication)
- Create notification (for inter-service communication)

## Database Schema

### Users Table
- id (auth_id)
- username (unique)
- email (unique)
- password (encrypted)
- role (JOB_SEEKER/EMPLOYER)
- fullName
- mobileNumber
- securityQuestion
- securityAnswer (encrypted)
- location
- employmentStatus (FRESHER/EXPERIENCED/EMPLOYED)
- profileCompleted
- createdAt

### Notifications Table
- id
- recipient_id (FK to users)
- job_id
- type (APPLICATION_RECEIVED/APPLICATION_UPDATE/JOB_RECOMMENDATION/SYSTEM)
- message
- isRead
- createdAt

### Password Reset Tokens Table
- id
- token (unique)
- user_id (FK to users)
- expiryAt

## API Endpoints

### Public Authentication Endpoints
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user
- `POST /api/auth/forgot-password` - Request password reset
- `POST /api/auth/reset-password` - Reset password with token

### Authenticated Endpoints
- `POST /api/auth/logout` - Logout user
- `POST /api/auth/change-password` - Change password
- `GET /api/auth/validate` - Validate JWT token

### Notification Endpoints (Authenticated)
- `GET /api/notifications` - Get all notifications
- `GET /api/notifications/unread` - Get unread notifications
- `GET /api/notifications/unread/count` - Get unread count
- `PATCH /api/notifications/{id}/read` - Mark as read
- `PATCH /api/notifications/read-all` - Mark all as read

### Internal Service-to-Service Endpoints
- `GET /internal/users/{userId}` - Get user by ID
- `POST /internal/users/{userId}/notifications` - Create notification

## Configuration

### Application Properties
- **Port:** 8081
- **Database:** revhire_auth_db
- **Eureka Server:** http://localhost:8761/eureka/
- **JWT Secret:** Configured in application.yml
- **JWT Expiration:** 24 hours

## Security
- JWT-based stateless authentication
- BCrypt password encoding
- Role-based access control (ROLE_JOB_SEEKER, ROLE_EMPLOYER)
- CORS enabled for localhost:4200 and localhost:3000
- Password validation (8-64 chars, uppercase, lowercase, number, special char)

## Running the Service

### Prerequisites
1. MySQL server running on localhost:3306
2. Create database: `revhire_auth_db`
3. Eureka Server running on port 8761 (optional for standalone)

### Build
```bash
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

Or with Java:
```bash
java -jar target/auth-service-1.0.0.jar
```

## Error Handling
- Validation errors: 400 Bad Request
- Authentication errors: 401 Unauthorized
- Resource not found: 404 Not Found
- Conflict errors: 409 Conflict
- Internal errors: 500 Internal Server Error

## Logging
- Log Level: INFO for application, DEBUG for security
- Pattern: `%d{yyyy-MM-dd HH:mm:ss} - %msg%n`
- Package: com.revhire.auth

## Future Enhancements
- Email integration for password reset
- SMS notifications
- OAuth2 integration (Google, LinkedIn)
- Refresh token mechanism
- Rate limiting
- Account lockout after failed attempts
