# RevHire API Gateway - Architecture Documentation

## Overview

The RevHire API Gateway serves as the single entry point for all client requests in the microservices architecture. It provides routing, authentication, load balancing, and cross-cutting concerns for the entire platform.

## Architecture Components

### 1. Gateway Layer

**GatewayConfig.java**
- Defines route mappings using Spring Cloud Gateway
- Maps URL patterns to backend microservices
- Uses Eureka service discovery for dynamic routing
- Load balances requests across service instances

### 2. Security Layer

**AuthenticationFilter.java**
- Global filter that intercepts all requests
- Validates JWT tokens for secured endpoints
- Extracts user information and forwards to downstream services
- Returns 401 for invalid/missing tokens

**RouteValidator.java**
- Determines which endpoints require authentication
- Maintains a whitelist of open endpoints
- Allows public access to authentication and job listing endpoints

**JwtUtil.java**
- Validates JWT token signatures
- Extracts claims (username, role)
- Verifies token expiration
- Uses HMAC-SHA256 algorithm

### 3. Configuration Layer

**application.yml**
- Server configuration (port 8080)
- Eureka client settings
- Gateway routes and CORS settings
- Actuator endpoints

**CorsConfig.java**
- Configures Cross-Origin Resource Sharing
- Allows frontend applications to access the API
- Configures allowed origins, methods, and headers

## Request Flow

```
1. Client Request
   |
   v
2. API Gateway (Port 8080)
   |
   v
3. RouteValidator
   |-- If open endpoint --> Skip to step 6
   |-- If secured endpoint --> Continue to step 4
   v
4. AuthenticationFilter
   |-- Extract JWT from Authorization header
   |-- Validate JWT signature and expiration
   |-- Extract username and role
   |
   v
5. Add Custom Headers
   |-- X-User-Username
   |-- X-User-Role
   |
   v
6. Eureka Service Discovery
   |-- Lookup target service
   |-- Load balance across instances
   |
   v
7. Route to Microservice
   |
   v
8. Return Response to Client
```

## Service Routes

| Route Pattern | Service | Port | Authentication |
|--------------|---------|------|----------------|
| `/api/auth/**` | AUTH-SERVICE | 8081 | No |
| `/api/jobseeker/**` | JOBSEEKER-SERVICE | 8082 | Yes |
| `/api/employer/**` | EMPLOYER-SERVICE | 8083 | Yes |
| `/api/jobs/**` | JOB-SERVICE | 8084 | GET: No, Others: Yes |
| `/api/applications/**` | APPLICATION-SERVICE | 8085 | Yes |
| `/api/notifications/**` | NOTIFICATION-SERVICE | 8086 | Yes |
| `/api/resume/**` | JOBSEEKER-SERVICE | 8082 | Yes |

## Security

### JWT Authentication

1. **Token Validation**
   - Signature verification using HS256
   - Expiration check
   - Claims extraction

2. **User Context Propagation**
   - Username forwarded in `X-User-Username` header
   - Role forwarded in `X-User-Role` header
   - Downstream services can use these headers for authorization

3. **Open Endpoints**
   - `/api/auth/register` - User registration
   - `/api/auth/login` - User login
   - `/api/auth/refresh` - Token refresh
   - `/api/jobs` (GET) - Public job listings
   - `/eureka/**` - Service registry
   - `/actuator/**` - Health monitoring

### CORS Configuration

- Allows all origins (configure restrictively in production)
- Supports standard HTTP methods
- Exposes custom headers for authentication
- Max age: 1 hour

## Monitoring and Observability

### Actuator Endpoints

- `/actuator/health` - Gateway health status
- `/actuator/info` - Application information
- `/actuator/gateway/routes` - Active route configurations

### Logging

- Request/response logging
- Authentication failures
- Routing decisions
- Service discovery events

## Deployment

### Local Development

```bash
mvn spring-boot:run
```

### Docker

```bash
docker build -t revhire/api-gateway:1.0.0 .
docker run -p 8080:8080 revhire/api-gateway:1.0.0
```

### Docker Compose

```bash
docker-compose up -d
```

## Configuration Properties

### Required Environment Variables

- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` - Eureka server URL
- `JWT_SECRET` - Secret key for JWT validation
- `SPRING_PROFILES_ACTIVE` - Active profile (dev, docker, prod)

### Optional Configuration

- `SERVER_PORT` - Gateway port (default: 8080)
- `LOGGING_LEVEL_COM_REVHIRE_GATEWAY` - Log level (default: DEBUG)

## Error Handling

### HTTP Status Codes

- `200 OK` - Successful request
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - Valid token but insufficient permissions
- `404 Not Found` - Service or endpoint not found
- `500 Internal Server Error` - Gateway or service error
- `503 Service Unavailable` - Target service is down

### Error Response Format

```json
{
  "timestamp": "2024-03-06T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Missing authorization header",
  "path": "/api/jobseeker/profile"
}
```

## Performance Considerations

1. **Connection Pooling**
   - Gateway maintains connection pools to backend services
   - Reduces latency for subsequent requests

2. **Load Balancing**
   - Round-robin load balancing via Eureka
   - Health checks ensure requests only go to healthy instances

3. **Caching**
   - Service discovery cache (Eureka)
   - Route configuration cache

4. **Timeouts**
   - Configure appropriate timeouts for backend services
   - Prevent cascading failures

## Security Best Practices

1. Use HTTPS in production
2. Rotate JWT secret keys regularly
3. Implement rate limiting
4. Configure strict CORS policies
5. Enable request/response logging
6. Monitor for suspicious patterns
7. Implement circuit breakers for resilience
8. Use secure secret management (Vault, AWS Secrets Manager)

## Future Enhancements

1. Rate limiting per user/IP
2. Request caching
3. Circuit breaker pattern
4. Distributed tracing (Zipkin/Jaeger)
5. API versioning support
6. GraphQL gateway
7. WebSocket support
8. OAuth2 integration
9. API analytics and monitoring
10. Request/response transformation

## Dependencies

- Spring Boot 3.3.2
- Spring Cloud Gateway 2023.0.2
- Spring Cloud Netflix Eureka Client
- JJWT 0.11.5
- Spring Boot Actuator

## Testing

### Unit Tests
- JwtUtil functionality
- RouteValidator logic
- Filter behavior

### Integration Tests
- End-to-end routing
- Authentication flow
- Service discovery

### Load Tests
- Concurrent request handling
- Throughput benchmarking
- Latency measurements

## Troubleshooting

### Common Issues

1. **503 Service Unavailable**
   - Check if Eureka server is running
   - Verify target service is registered

2. **401 Unauthorized**
   - Verify JWT token is valid
   - Check JWT secret matches auth service

3. **Connection Refused**
   - Check Eureka server URL
   - Verify network connectivity

4. **CORS Errors**
   - Review CORS configuration
   - Check allowed origins

## Support and Maintenance

For issues, questions, or contributions:
- Create an issue in the project repository
- Contact the DevOps team
- Review the troubleshooting guide
- Check application logs

## License

Copyright (c) 2024 Revature. All rights reserved.
