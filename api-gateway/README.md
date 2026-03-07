# RevHire API Gateway

API Gateway for the RevHire recruitment platform microservices architecture. This gateway handles routing, authentication, and load balancing for all backend services.

## Features

- **Service Routing**: Routes requests to appropriate microservices based on path patterns
- **JWT Authentication**: Validates JWT tokens for secured endpoints
- **Load Balancing**: Distributes requests across service instances via Eureka
- **CORS Configuration**: Handles cross-origin requests
- **Service Discovery**: Integrates with Eureka for dynamic service discovery

## Technology Stack

- Spring Boot 3.3.2
- Spring Cloud Gateway
- Spring Cloud Netflix Eureka Client
- JWT (JJWT 0.11.5)
- Spring Boot Actuator

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Eureka Server running on port 8761

## Configuration

The gateway runs on port **8080** and connects to Eureka at `http://localhost:8761/eureka/`.

### Route Mappings

| Path Pattern | Target Service | Authentication Required |
|-------------|----------------|------------------------|
| `/api/auth/**` | AUTH-SERVICE | No |
| `/api/jobseeker/**` | JOBSEEKER-SERVICE | Yes |
| `/api/employer/**` | EMPLOYER-SERVICE | Yes |
| `/api/jobs/**` | JOB-SERVICE | GET: No, Others: Yes |
| `/api/applications/**` | APPLICATION-SERVICE | Yes |
| `/api/notifications/**` | NOTIFICATION-SERVICE | Yes |
| `/api/resume/**` | JOBSEEKER-SERVICE | Yes |

### Open Endpoints (No Authentication)

- `/api/auth/register`
- `/api/auth/login`
- `/api/auth/refresh`
- `/api/jobs` (GET requests only)
- `/eureka/**`
- `/actuator/**`

## Running the Application

### Using Maven

```bash
mvn spring-boot:run
```

### Using JAR

```bash
mvn clean package
java -jar target/api-gateway-1.0.0.jar
```

## JWT Authentication

The gateway validates JWT tokens for secured endpoints and forwards user information to downstream services via custom headers:

- `X-User-Username`: The authenticated user's username
- `X-User-Role`: The authenticated user's role

### JWT Secret

Default JWT secret is configured in `application.yml`. For production, use environment variables:

```bash
export JWT_SECRET=your-secret-key
```

## Health Check

Access the health endpoint at:
```
http://localhost:8080/actuator/health
```

## Gateway Routes

View configured routes at:
```
http://localhost:8080/actuator/gateway/routes
```

## Development

### Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/revhire/gateway/
│   │       ├── config/
│   │       │   └── GatewayConfig.java
│   │       ├── filter/
│   │       │   ├── AuthenticationFilter.java
│   │       │   └── RouteValidator.java
│   │       ├── util/
│   │       │   └── JwtUtil.java
│   │       └── ApiGatewayApplication.java
│   └── resources/
│       └── application.yml
└── test/
    └── java/
        └── com/revhire/gateway/
```

## Error Handling

The gateway returns appropriate HTTP status codes:

- `401 Unauthorized`: Missing or invalid JWT token
- `404 Not Found`: Service not found
- `503 Service Unavailable`: Target service is down

## Monitoring

Spring Boot Actuator provides monitoring endpoints:

- `/actuator/health` - Application health status
- `/actuator/info` - Application information
- `/actuator/gateway/routes` - Gateway route configurations

## Security Notes

1. Always use HTTPS in production
2. Keep JWT secret secure and rotate regularly
3. Configure appropriate CORS settings for production
4. Enable rate limiting for production deployments
5. Monitor and log all gateway access

## Support

For issues or questions, contact the RevHire development team.
