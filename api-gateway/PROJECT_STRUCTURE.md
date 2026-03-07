# RevHire API Gateway - Project Structure

## Complete File Structure

```
p3-api-gateway/
├── .gitignore
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties
├── pom.xml
├── Dockerfile
├── docker-compose.yml
├── README.md
├── QUICKSTART.md
├── ARCHITECTURE.md
├── API_ROUTES.md
├── PROJECT_STRUCTURE.md
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── revhire/
│   │   │           └── gateway/
│   │   │               ├── ApiGatewayApplication.java
│   │   │               ├── config/
│   │   │               │   ├── GatewayConfig.java
│   │   │               │   └── CorsConfig.java
│   │   │               ├── filter/
│   │   │               │   ├── AuthenticationFilter.java
│   │   │               │   └── RouteValidator.java
│   │   │               ├── util/
│   │   │               │   └── JwtUtil.java
│   │   │               └── exception/
│   │   │                   └── UnauthorizedException.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-docker.yml
│   │       ├── logback-spring.xml
│   │       └── banner.txt
│   │
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── revhire/
│       │           └── gateway/
│       │               ├── ApiGatewayApplicationTests.java
│       │               ├── filter/
│       │               │   └── RouteValidatorTest.java
│       │               └── util/
│       │                   └── JwtUtilTest.java
│       └── resources/
│           └── application-test.yml
```

## File Descriptions

### Root Level Files

| File | Purpose |
|------|---------|
| `pom.xml` | Maven project configuration with Spring Boot 3.3.2, Spring Cloud Gateway, Eureka Client, and JWT dependencies |
| `.gitignore` | Git ignore patterns for Maven, IDE files, and build artifacts |
| `Dockerfile` | Multi-stage Docker build configuration for containerization |
| `docker-compose.yml` | Docker Compose configuration for local deployment with Eureka |
| `README.md` | Main project documentation and overview |
| `QUICKSTART.md` | Quick start guide for developers |
| `ARCHITECTURE.md` | Detailed architecture documentation |
| `API_ROUTES.md` | Complete API route documentation |
| `PROJECT_STRUCTURE.md` | This file - project structure documentation |

### Source Code - Main Application

| File | Purpose |
|------|---------|
| `ApiGatewayApplication.java` | Spring Boot main application class with @EnableDiscoveryClient |

### Source Code - Configuration

| File | Purpose |
|------|---------|
| `GatewayConfig.java` | Route definitions for all microservices using RouteLocator |
| `CorsConfig.java` | CORS configuration for cross-origin requests |

### Source Code - Filters

| File | Purpose |
|------|---------|
| `AuthenticationFilter.java` | Global filter for JWT authentication, validates tokens and forwards user info |
| `RouteValidator.java` | Determines which routes require authentication |

### Source Code - Utilities

| File | Purpose |
|------|---------|
| `JwtUtil.java` | JWT token validation, claims extraction, and signature verification |

### Source Code - Exceptions

| File | Purpose |
|------|---------|
| `UnauthorizedException.java` | Custom exception for unauthorized access attempts |

### Resources - Configuration

| File | Purpose |
|------|---------|
| `application.yml` | Main application configuration (server port, Eureka, JWT, CORS, logging) |
| `application-docker.yml` | Docker-specific configuration overrides |
| `logback-spring.xml` | Logging configuration with console and file appenders |
| `banner.txt` | Custom startup banner |

### Test Code

| File | Purpose |
|------|---------|
| `ApiGatewayApplicationTests.java` | Basic Spring Boot context loading test |
| `RouteValidatorTest.java` | Unit tests for route validation logic |
| `JwtUtilTest.java` | Unit tests for JWT utility methods |
| `application-test.yml` | Test-specific configuration (disables Eureka) |

## Technology Stack

### Core Dependencies

| Dependency | Version | Purpose |
|------------|---------|---------|
| Spring Boot | 3.3.2 | Base framework |
| Spring Cloud Gateway | 2023.0.2 | API Gateway implementation |
| Spring Cloud Netflix Eureka Client | 2023.0.2 | Service discovery |
| Spring Boot Actuator | 3.3.2 | Health monitoring and metrics |
| JJWT API | 0.11.5 | JWT token handling |
| JJWT Impl | 0.11.5 | JWT implementation |
| JJWT Jackson | 0.11.5 | JWT JSON processing |

### Build Tool

- **Maven 3.6+** with Spring Boot Maven Plugin

## Key Features Implemented

### 1. Service Routing
- Routes requests to 7 different microservices
- Load balancing via Eureka service discovery
- Dynamic routing configuration

### 2. Authentication & Authorization
- JWT token validation
- Open endpoint configuration
- User context propagation via custom headers

### 3. Cross-Cutting Concerns
- CORS configuration
- Request/response logging
- Health monitoring
- Error handling

### 4. Deployment Support
- Docker containerization
- Docker Compose orchestration
- Environment-specific configurations

### 5. Testing
- Unit tests for core components
- Integration test setup
- Test-specific configuration

## Configuration Properties

### Server Configuration
```yaml
server.port: 8080
spring.application.name: api-gateway
```

### Eureka Configuration
```yaml
eureka.client.service-url.defaultZone: http://localhost:8761/eureka/
eureka.client.register-with-eureka: true
eureka.client.fetch-registry: true
```

### JWT Configuration
```yaml
jwt.secret: <base64-encoded-secret>
```

### Gateway Routes
- `/api/auth/**` → AUTH-SERVICE
- `/api/jobseeker/**` → JOBSEEKER-SERVICE
- `/api/employer/**` → EMPLOYER-SERVICE
- `/api/jobs/**` → JOB-SERVICE
- `/api/applications/**` → APPLICATION-SERVICE
- `/api/notifications/**` → NOTIFICATION-SERVICE
- `/api/resume/**` → JOBSEEKER-SERVICE

## Security Implementation

### JWT Validation Flow
1. Extract token from Authorization header
2. Validate signature using HS256 algorithm
3. Check expiration date
4. Extract username and role
5. Forward to downstream services in custom headers

### Open Endpoints (No Auth Required)
- `/api/auth/register`
- `/api/auth/login`
- `/api/auth/refresh`
- `/api/jobs` (GET only)
- `/eureka/**`
- `/actuator/**`

## Build Commands

### Development
```bash
mvn spring-boot:run
```

### Production Build
```bash
mvn clean package
java -jar target/api-gateway-1.0.0.jar
```

### Docker Build
```bash
docker build -t revhire/api-gateway:1.0.0 .
```

### Run Tests
```bash
mvn test
```

## Monitoring Endpoints

| Endpoint | Purpose |
|----------|---------|
| `/actuator/health` | Application health status |
| `/actuator/info` | Application information |
| `/actuator/gateway/routes` | View configured routes |

## Port Configuration

| Environment | Port |
|-------------|------|
| Local Development | 8080 |
| Docker | 8080 (mapped from container) |
| Production | Configurable via environment variable |

## Dependencies Integration

### Eureka Server
- Required for service discovery
- Must be running on port 8761
- Gateway registers itself and discovers other services

### Auth Service
- Provides JWT token generation
- Shares JWT secret with gateway
- Handles user authentication

### Other Microservices
- Register with Eureka
- Receive user context from gateway headers
- Handle business logic independently

## Development Workflow

1. Ensure Eureka Server is running
2. Start API Gateway
3. Start individual microservices
4. Access all services through gateway (port 8080)
5. Monitor health via Actuator endpoints

## Deployment Options

### Local
```bash
mvn spring-boot:run
```

### Docker
```bash
docker-compose up -d
```

### Kubernetes (Future)
- Helm charts to be created
- ConfigMaps for configuration
- Secrets for JWT keys

## Future Enhancements

1. Rate limiting
2. Request caching
3. Circuit breaker pattern
4. Distributed tracing (Zipkin/Jaeger)
5. API versioning
6. GraphQL support
7. WebSocket routing
8. OAuth2 integration
9. API analytics
10. Request/response transformation

## Documentation Files

| File | Content |
|------|---------|
| `README.md` | Overview, features, and basic setup |
| `QUICKSTART.md` | Step-by-step guide to get started |
| `ARCHITECTURE.md` | Detailed architecture and design decisions |
| `API_ROUTES.md` | Complete API endpoint documentation |
| `PROJECT_STRUCTURE.md` | This file - project organization |

## Support and Maintenance

- Check logs in `/tmp/api-gateway.log`
- Monitor health via Actuator endpoints
- Review Eureka dashboard at `http://localhost:8761`
- Contact DevOps team for production issues

## License

Copyright (c) 2024 Revature. All rights reserved.

---

**Last Updated:** March 6, 2024
**Version:** 1.0.0
**Maintainer:** RevHire Development Team
