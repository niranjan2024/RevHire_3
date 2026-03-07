# Auth Service Architecture

## Overview

The Auth Service is a Spring Boot microservice responsible for authentication, authorization, and user management in the RevHire platform. It follows a layered architecture pattern with clear separation of concerns.

## Architecture Layers

```
┌─────────────────────────────────────────────────┐
│              API Gateway Layer                   │
│         (Routes requests to services)            │
└─────────────────┬───────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────┐
│           Controller Layer                       │
│    (AuthController - REST endpoints)             │
│    - Request validation                          │
│    - Response formatting                         │
│    - HTTP status handling                        │
└─────────────────┬───────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────┐
│           Security Layer                         │
│    - JwtAuthenticationFilter                     │
│    - SecurityConfig                              │
│    - Token validation                            │
└─────────────────┬───────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────┐
│           Service Layer                          │
│    - AuthService (Business logic)                │
│    - JwtService (Token management)               │
│    - CustomUserDetailsService                    │
│    - Input validation & sanitization            │
└─────────────────┬───────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────┐
│         Repository Layer                         │
│    - UserRepository                              │
│    - PasswordResetTokenRepository                │
│    - Spring Data JPA                             │
└─────────────────┬───────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────┐
│           Database Layer                         │
│         MySQL Database                           │
│    - users table                                 │
│    - password_reset_tokens table                 │
└──────────────────────────────────────────────────┘
```

## Component Diagram

```
┌──────────────────────────────────────────────────────────┐
│                    Auth Service                           │
│                                                           │
│  ┌────────────────────────────────────────────────────┐  │
│  │              Controllers                           │  │
│  │  • AuthController (REST API endpoints)            │  │
│  └────────────────────────────────────────────────────┘  │
│                          │                                │
│  ┌────────────────────────────────────────────────────┐  │
│  │              Security                              │  │
│  │  • JwtAuthenticationFilter                        │  │
│  │  • SecurityConfig                                 │  │
│  └────────────────────────────────────────────────────┘  │
│                          │                                │
│  ┌────────────────────────────────────────────────────┐  │
│  │              Services                              │  │
│  │  • AuthService                                     │  │
│  │  • JwtService                                      │  │
│  │  • CustomUserDetailsService                        │  │
│  └────────────────────────────────────────────────────┘  │
│                          │                                │
│  ┌────────────────────────────────────────────────────┐  │
│  │            Repositories                            │  │
│  │  • UserRepository                                  │  │
│  │  • PasswordResetTokenRepository                    │  │
│  └────────────────────────────────────────────────────┘  │
│                          │                                │
│  ┌────────────────────────────────────────────────────┐  │
│  │              Entities                              │  │
│  │  • User                                            │  │
│  │  • PasswordResetToken                              │  │
│  └────────────────────────────────────────────────────┘  │
│                                                           │
│  ┌────────────────────────────────────────────────────┐  │
│  │              DTOs                                  │  │
│  │  • Request DTOs (RegisterRequest, LoginRequest)    │  │
│  │  • Response DTOs (AuthResponse, ApiResponse)       │  │
│  └────────────────────────────────────────────────────┘  │
│                                                           │
│  ┌────────────────────────────────────────────────────┐  │
│  │           Exception Handlers                       │  │
│  │  • GlobalExceptionHandler                          │  │
│  │  • Custom Exceptions                               │  │
│  └────────────────────────────────────────────────────┘  │
│                                                           │
└──────────────────────────────────────────────────────────┘
                          │
                          ▼
                  ┌───────────────┐
                  │  MySQL DB     │
                  └───────────────┘
```

## Authentication Flow

### Registration Flow

```
User → Controller → Service → Validation → Repository → Database
                                    │
                                    ▼
                            Hash Password
                                    │
                                    ▼
                            Generate JWT Token
                                    │
                                    ▼
                            Return AuthResponse
```

### Login Flow

```
User Credentials → Controller → AuthenticationManager
                                        │
                                        ▼
                                Verify Credentials
                                        │
                                ┌───────┴────────┐
                                │                │
                            Success          Failure
                                │                │
                                ▼                ▼
                        Generate JWT     BadCredentials
                                │           Exception
                                ▼
                        Return AuthResponse
```

### Token Validation Flow (Protected Endpoints)

```
Request + Token → JwtAuthenticationFilter
                          │
                          ▼
                  Extract Token from Header
                          │
                          ▼
                  Validate Token (JwtService)
                          │
                  ┌───────┴────────┐
                  │                │
              Valid            Invalid
                  │                │
                  ▼                ▼
        Set Authentication    403 Forbidden
                  │
                  ▼
            Process Request
```

### Password Reset Flow

```
Forgot Password Request
        │
        ▼
Verify Email & Security Answer
        │
        ▼
Generate Reset Token (UUID)
        │
        ▼
Store Token with 30min Expiry
        │
        ▼
Return Reset Token
        │
        ▼
User Submits Reset Password
        │
        ▼
Validate Token & Expiry
        │
        ▼
Update Password
        │
        ▼
Delete Used Token
```

## Database Schema

### Users Table
```sql
users
├── id (BIGINT, PK, AUTO_INCREMENT)
├── username (VARCHAR(50), UNIQUE, NOT NULL)
├── email (VARCHAR(100), UNIQUE, NOT NULL)
├── password (VARCHAR(255), NOT NULL)
├── role (VARCHAR(20), NOT NULL)
├── full_name (VARCHAR(100))
├── mobile_number (VARCHAR(15))
├── security_question (VARCHAR(255))
├── security_answer (VARCHAR(255))
├── location (VARCHAR(100))
├── employment_status (VARCHAR(20))
├── profile_completed (BOOLEAN, DEFAULT FALSE)
└── created_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)

Indexes:
- idx_username (username)
- idx_email (email)
- idx_role (role)
- idx_created_at (created_at)
```

### Password Reset Tokens Table
```sql
password_reset_tokens
├── id (BIGINT, PK, AUTO_INCREMENT)
├── token (VARCHAR(255), UNIQUE, NOT NULL)
├── user_id (BIGINT, FK -> users.id)
└── expiry_at (TIMESTAMP, NOT NULL)

Indexes:
- idx_token (token)
- idx_user_id (user_id)
- idx_expiry_at (expiry_at)

Foreign Keys:
- fk_password_reset_user (user_id -> users.id ON DELETE CASCADE)
```

## Security Architecture

### JWT Token Structure
```
Header:
{
  "alg": "HS256",
  "typ": "JWT"
}

Payload:
{
  "sub": "username",
  "iat": 1234567890,
  "exp": 1234654290
}

Signature:
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret
)
```

### Security Measures

1. **Password Security**
   - BCrypt hashing (cost factor: 10)
   - Minimum 6 characters
   - Never logged or exposed

2. **Token Security**
   - 256-bit secret key
   - 24-hour expiration
   - Stateless authentication
   - Signature validation

3. **Input Security**
   - Input sanitization
   - XSS prevention
   - SQL injection prevention
   - Bean validation

4. **Network Security**
   - HTTPS (in production)
   - CORS configuration
   - CSRF protection disabled (stateless API)

## Service Integration

### Communication with Other Services

```
┌─────────────────┐
│  API Gateway    │
└────────┬────────┘
         │
    ┌────▼────────────────────────────────┐
    │                                     │
    │         Auth Service                │
    │    (Port: 8081)                     │
    │                                     │
    └──┬──────┬──────┬──────┬──────┬─────┘
       │      │      │      │      │
   ┌───▼──┐ ┌▼──────▼┐ ┌──▼─────┐ │
   │Job   │ │JobSeeker│ │Employer│ │
   │Service│ │Service │ │Service │ │
   └──────┘ └─────────┘ └────────┘ │
                                ┌──▼────────┐
                                │Application│
                                │Service    │
                                └───────────┘
```

### Inter-Service Endpoints

1. **Token Validation**
   - Endpoint: `GET /api/auth/validate?token={token}`
   - Used by: All microservices
   - Purpose: Validate JWT tokens

2. **User Information**
   - Endpoint: `GET /api/auth/user/{userId}`
   - Used by: All microservices
   - Purpose: Fetch user details

## Scalability Considerations

### Horizontal Scaling
```
        Load Balancer
              │
    ┌─────────┼─────────┐
    │         │         │
┌───▼───┐ ┌──▼────┐ ┌──▼────┐
│Auth   │ │Auth   │ │Auth   │
│Inst 1 │ │Inst 2 │ │Inst 3 │
└───┬───┘ └───┬───┘ └───┬───┘
    │         │         │
    └────────┬┴─────────┘
             │
      ┌──────▼───────┐
      │  MySQL DB    │
      │  (Primary)   │
      └──────┬───────┘
             │
      ┌──────▼───────┐
      │  MySQL DB    │
      │  (Replica)   │
      └──────────────┘
```

### Caching Strategy
- Cache user details in Redis
- Cache token validation results
- TTL: 5 minutes for user data

### Database Optimization
- Connection pooling (HikariCP)
- Query optimization with indexes
- Read replicas for scaling reads

## Monitoring and Observability

### Logging
- SLF4J with Logback
- Structured logging
- Log levels: DEBUG, INFO, WARN, ERROR

### Metrics
- Spring Boot Actuator
- Prometheus integration
- Custom metrics for:
  - Login attempts
  - Token generation
  - Failed authentications

### Health Checks
- Database connectivity
- Eureka registration status
- JVM metrics

## Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| Language | Java | 17 |
| Framework | Spring Boot | 3.3.2 |
| Security | Spring Security | 3.3.2 |
| Database | MySQL | 8.0+ |
| ORM | Spring Data JPA | 3.3.2 |
| JWT | JJWT | 0.11.5 |
| Service Discovery | Eureka Client | 2023.0.2 |
| Build Tool | Maven | 3.6+ |
| Containerization | Docker | Latest |

## Design Patterns Used

1. **Layered Architecture**
   - Clear separation of concerns
   - Controller → Service → Repository

2. **Dependency Injection**
   - Constructor injection with Lombok
   - Loose coupling

3. **DTO Pattern**
   - Separate request/response objects
   - Entity encapsulation

4. **Repository Pattern**
   - Data access abstraction
   - Spring Data JPA

5. **Filter Chain Pattern**
   - JWT authentication filter
   - Security filter chain

6. **Builder Pattern**
   - Response object creation
   - Lombok @Builder

7. **Exception Handling**
   - Global exception handler
   - Custom exceptions

## Future Enhancements

1. **OAuth2 Integration**
   - Google/GitHub login
   - Social authentication

2. **Multi-Factor Authentication (MFA)**
   - SMS/Email OTP
   - Authenticator app support

3. **Rate Limiting**
   - Login attempt limiting
   - API rate limiting

4. **Token Refresh**
   - Refresh token mechanism
   - Long-lived sessions

5. **Audit Logging**
   - Track all auth events
   - Compliance requirements

6. **Password Policy**
   - Configurable password rules
   - Password history

7. **Session Management**
   - Active session tracking
   - Force logout capability
