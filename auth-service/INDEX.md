# Auth Service - Project Index

Complete file and feature index for the RevHire Auth Service microservice.

## Quick Navigation

- [Get Started](#get-started)
- [Documentation](#documentation)
- [Source Code](#source-code)
- [Configuration](#configuration)
- [Database](#database)
- [Testing](#testing)
- [Deployment](#deployment)

---

## Get Started

**New to this project? Start here:**

1. **[QUICKSTART.md](QUICKSTART.md)** - Get running in 5 minutes
2. **[README.md](README.md)** - Main project documentation
3. **[SETUP_GUIDE.md](SETUP_GUIDE.md)** - Detailed setup instructions

---

## Documentation

### User Guides

| File | Description | When to Use |
|------|-------------|-------------|
| **[README.md](README.md)** | Main project documentation | First-time overview |
| **[QUICKSTART.md](QUICKSTART.md)** | 5-minute quick start guide | Fast setup |
| **[SETUP_GUIDE.md](SETUP_GUIDE.md)** | Detailed setup instructions | Full installation |
| **[API_DOCUMENTATION.md](API_DOCUMENTATION.md)** | Complete API reference | API integration |
| **[ARCHITECTURE.md](ARCHITECTURE.md)** | Architecture & design | Understanding structure |

### Developer Guides

| File | Description | When to Use |
|------|-------------|-------------|
| **[CONTRIBUTING.md](CONTRIBUTING.md)** | Contribution guidelines | Contributing code |
| **[PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)** | Project overview & metrics | Quick reference |

---

## Source Code

### Package Structure

```
com.revature.revhire.authservice/
├── controller/          # REST API endpoints
├── dto/                 # Data Transfer Objects
├── entity/              # JPA entities
├── enums/               # Enumerations
├── exception/           # Custom exceptions
├── repository/          # Data access layer
├── security/            # Security configuration
├── service/             # Business logic
└── util/                # Utility classes
```

### Controllers (1 file)

| File | Endpoints | Description |
|------|-----------|-------------|
| **[AuthController.java](src/main/java/com/revature/revhire/authservice/controller/AuthController.java)** | 10 endpoints | Main REST controller |

**Endpoints:**
- POST `/api/auth/register` - Register user
- POST `/api/auth/login` - User login
- POST `/api/auth/logout` - User logout
- POST `/api/auth/change-password` - Change password
- POST `/api/auth/forgot-password` - Forgot password
- POST `/api/auth/reset-password` - Reset password
- POST `/api/auth/profile-completion/{userId}` - Complete profile
- GET `/api/auth/user/{userId}` - Get user
- GET `/api/auth/validate` - Validate token
- GET `/api/auth/health` - Health check

### DTOs (10 files)

| File | Type | Purpose |
|------|------|---------|
| **[RegisterRequest.java](src/main/java/com/revature/revhire/authservice/dto/RegisterRequest.java)** | Request | User registration |
| **[LoginRequest.java](src/main/java/com/revature/revhire/authservice/dto/LoginRequest.java)** | Request | User login |
| **[AuthResponse.java](src/main/java/com/revature/revhire/authservice/dto/AuthResponse.java)** | Response | Login/Register response |
| **[ChangePasswordRequest.java](src/main/java/com/revature/revhire/authservice/dto/ChangePasswordRequest.java)** | Request | Password change |
| **[ForgotPasswordRequest.java](src/main/java/com/revature/revhire/authservice/dto/ForgotPasswordRequest.java)** | Request | Forgot password |
| **[ForgotPasswordResponse.java](src/main/java/com/revature/revhire/authservice/dto/ForgotPasswordResponse.java)** | Response | Reset token response |
| **[ResetPasswordRequest.java](src/main/java/com/revature/revhire/authservice/dto/ResetPasswordRequest.java)** | Request | Password reset |
| **[ProfileCompletionRequest.java](src/main/java/com/revature/revhire/authservice/dto/ProfileCompletionRequest.java)** | Request | Profile completion |
| **[ApiResponse.java](src/main/java/com/revature/revhire/authservice/dto/ApiResponse.java)** | Response | Generic API response |
| **[UserDto.java](src/main/java/com/revature/revhire/authservice/dto/UserDto.java)** | Response | User information |

### Entities (2 files)

| File | Table | Description |
|------|-------|-------------|
| **[User.java](src/main/java/com/revature/revhire/authservice/entity/User.java)** | `users` | User entity with credentials |
| **[PasswordResetToken.java](src/main/java/com/revature/revhire/authservice/entity/PasswordResetToken.java)** | `password_reset_tokens` | Reset token entity |

### Enums (2 files)

| File | Values | Usage |
|------|--------|-------|
| **[Role.java](src/main/java/com/revature/revhire/authservice/enums/Role.java)** | JOB_SEEKER, EMPLOYER | User roles |
| **[EmploymentStatus.java](src/main/java/com/revature/revhire/authservice/enums/EmploymentStatus.java)** | FRESHER, EMPLOYED, UNEMPLOYED | Employment status |

### Repositories (2 files)

| File | Entity | Key Methods |
|------|--------|-------------|
| **[UserRepository.java](src/main/java/com/revature/revhire/authservice/repository/UserRepository.java)** | User | findByUsername, findByEmail, existsByUsername |
| **[PasswordResetTokenRepository.java](src/main/java/com/revature/revhire/authservice/repository/PasswordResetTokenRepository.java)** | PasswordResetToken | findByToken, deleteByUser |

### Services (3 files)

| File | Responsibility |
|------|----------------|
| **[AuthService.java](src/main/java/com/revature/revhire/authservice/service/AuthService.java)** | Main authentication business logic |
| **[JwtService.java](src/main/java/com/revature/revhire/authservice/service/JwtService.java)** | JWT token generation & validation |
| **[CustomUserDetailsService.java](src/main/java/com/revature/revhire/authservice/service/CustomUserDetailsService.java)** | Spring Security user details |

### Security (2 files)

| File | Purpose |
|------|---------|
| **[JwtAuthenticationFilter.java](src/main/java/com/revature/revhire/authservice/security/JwtAuthenticationFilter.java)** | JWT token validation filter |
| **[SecurityConfig.java](src/main/java/com/revature/revhire/authservice/security/SecurityConfig.java)** | Spring Security configuration |

### Exceptions (6 files)

| File | HTTP Status | Usage |
|------|-------------|-------|
| **[ApiException.java](src/main/java/com/revature/revhire/authservice/exception/ApiException.java)** | Base | Base exception class |
| **[BadRequestException.java](src/main/java/com/revature/revhire/authservice/exception/BadRequestException.java)** | 400 | Invalid input |
| **[NotFoundException.java](src/main/java/com/revature/revhire/authservice/exception/NotFoundException.java)** | 404 | Resource not found |
| **[ConflictException.java](src/main/java/com/revature/revhire/authservice/exception/ConflictException.java)** | 409 | Duplicate resource |
| **[ForbiddenException.java](src/main/java/com/revature/revhire/authservice/exception/ForbiddenException.java)** | 403 | Access denied |
| **[GlobalExceptionHandler.java](src/main/java/com/revature/revhire/authservice/exception/GlobalExceptionHandler.java)** | - | Global exception handler |

### Utilities (1 file)

| File | Methods | Purpose |
|------|---------|---------|
| **[InputSanitizer.java](src/main/java/com/revature/revhire/authservice/util/InputSanitizer.java)** | sanitize, validate | Input validation & XSS prevention |

### Main Application (1 file)

| File | Purpose |
|------|---------|
| **[AuthServiceApplication.java](src/main/java/com/revature/revhire/authservice/AuthServiceApplication.java)** | Spring Boot main class |

---

## Configuration

### Application Configuration (3 files)

| File | Environment | Purpose |
|------|-------------|---------|
| **[application.yml](src/main/resources/application.yml)** | Default | Main configuration |
| **[application-dev.yml](src/main/resources/application-dev.yml)** | Development | Dev settings |
| **[application-prod.yml](src/main/resources/application-prod.yml)** | Production | Production settings |

**Key Configurations:**
- Server port: 8081
- Database: MySQL (revhire_auth_db)
- JWT secret & expiration
- Eureka client settings
- Logging levels

### Build Configuration (1 file)

| File | Purpose |
|------|---------|
| **[pom.xml](pom.xml)** | Maven dependencies & build |

**Key Dependencies:**
- Spring Boot 3.3.2
- Spring Security
- Spring Data JPA
- MySQL Connector
- JWT (JJWT) 0.11.5
- Eureka Client
- Lombok

---

## Database

### Database Scripts (1 file)

| File | Purpose | Tables Created |
|------|---------|----------------|
| **[database-init.sql](database-init.sql)** | Database initialization | users, password_reset_tokens |

**Features:**
- Table creation scripts
- Index definitions
- Foreign key constraints
- Sample data (commented)
- Cleanup event scheduler
- Useful queries

---

## Testing

### Test Files (1 file)

| File | Type | Coverage |
|------|------|----------|
| **[AuthServiceApplicationTests.java](src/test/java/com/revature/revhire/authservice/AuthServiceApplicationTests.java)** | Integration | Context loading |

### Testing Tools

| File | Purpose |
|------|---------|
| **[postman_collection.json](postman_collection.json)** | Postman API collection |

**Postman Features:**
- All 10 endpoints
- Auto-token capture
- Environment variables
- Test scenarios

---

## Deployment

### Docker (2 files)

| File | Purpose |
|------|---------|
| **[Dockerfile](Dockerfile)** | Container image definition |
| **[docker-compose.yml](docker-compose.yml)** | Multi-container setup |

**Docker Compose Services:**
- MySQL database container
- Auth Service container
- Network configuration
- Volume management

### Deployment Commands

```bash
# Build
mvn clean package

# Docker build
docker build -t auth-service:latest .

# Docker run
docker-compose up -d
```

---

## Other Files

### Version Control (1 file)

| File | Purpose |
|------|---------|
| **[.gitignore](.gitignore)** | Git ignore patterns |

**Ignores:**
- target/ directory
- IDE files (.idea, *.iml)
- Log files
- OS files (.DS_Store)

---

## File Statistics

### Summary

| Category | Count |
|----------|-------|
| **Total Files** | 46 |
| **Java Source Files** | 27 |
| **Configuration Files** | 5 |
| **Documentation Files** | 8 |
| **Database Scripts** | 1 |
| **Docker Files** | 2 |
| **Test Files** | 1 |
| **Build Files** | 1 |
| **Other Files** | 1 |

### Lines of Code (Approximate)

| Type | LOC |
|------|-----|
| Java | 2,500+ |
| YAML | 150+ |
| SQL | 200+ |
| Markdown | 3,000+ |
| Total | 5,850+ |

---

## Feature Index

### Authentication Features

- [x] User Registration
- [x] User Login
- [x] User Logout
- [x] JWT Token Generation
- [x] JWT Token Validation
- [x] Role-Based Access Control

**Files Involved:**
- Controllers: AuthController.java
- Services: AuthService.java, JwtService.java
- DTOs: RegisterRequest.java, LoginRequest.java, AuthResponse.java

### Password Management Features

- [x] Change Password
- [x] Forgot Password
- [x] Reset Password
- [x] Security Question/Answer

**Files Involved:**
- Controllers: AuthController.java
- Services: AuthService.java
- Entities: PasswordResetToken.java
- DTOs: ChangePasswordRequest.java, ForgotPasswordRequest.java, ResetPasswordRequest.java

### Profile Management Features

- [x] Profile Completion
- [x] User Information Retrieval
- [x] Employment Status Tracking

**Files Involved:**
- Controllers: AuthController.java
- Services: AuthService.java
- DTOs: ProfileCompletionRequest.java, UserDto.java

### Security Features

- [x] BCrypt Password Encryption
- [x] Input Sanitization
- [x] XSS Prevention
- [x] SQL Injection Prevention
- [x] Token Expiration
- [x] Security Filter Chain

**Files Involved:**
- Security: SecurityConfig.java, JwtAuthenticationFilter.java
- Utils: InputSanitizer.java
- Services: JwtService.java

---

## Common Tasks

### Adding a New Endpoint

1. Add method in **AuthController.java**
2. Implement business logic in **AuthService.java**
3. Create DTOs if needed
4. Update **API_DOCUMENTATION.md**
5. Add to **postman_collection.json**

### Adding a New Entity

1. Create entity in **entity/** package
2. Create repository in **repository/** package
3. Update **database-init.sql**
4. Add DTOs for requests/responses
5. Update service layer

### Modifying Security

1. Update **SecurityConfig.java** for URL patterns
2. Update **JwtAuthenticationFilter.java** for token logic
3. Update **JwtService.java** for token management

### Changing Database

1. Update **application.yml** datasource
2. Modify **database-init.sql**
3. Update entity classes if schema changes
4. Update **pom.xml** for new driver (if needed)

---

## Related Documentation

### Internal Links

- [Main README](README.md)
- [Quick Start](QUICKSTART.md)
- [Setup Guide](SETUP_GUIDE.md)
- [API Documentation](API_DOCUMENTATION.md)
- [Architecture](ARCHITECTURE.md)
- [Contributing](CONTRIBUTING.md)
- [Project Summary](PROJECT_SUMMARY.md)

### External Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [JWT.io](https://jwt.io/)
- [MySQL Documentation](https://dev.mysql.com/doc/)

---

## Support & Contacts

**Module Owner:** Niranjan
**Project:** RevHire Platform
**Service:** Authentication & Authorization

For questions or issues, refer to:
- [CONTRIBUTING.md](CONTRIBUTING.md) - Contribution guidelines
- [SETUP_GUIDE.md](SETUP_GUIDE.md) - Setup troubleshooting

---

**Last Updated:** March 2024
**Project Status:** Production Ready ✅
