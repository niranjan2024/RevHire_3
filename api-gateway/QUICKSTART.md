# RevHire API Gateway - Quick Start Guide

## Prerequisites

Before running the API Gateway, ensure you have:

1. **Java 17** or higher installed
2. **Maven 3.6+** installed
3. **Eureka Server** running on `http://localhost:8761`

## Quick Start

### 1. Clone and Navigate to Project

```bash
cd /Users/vimalkrishnan/Workspace/revature/2353/review/p2/repos/p3-api-gateway
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

The API Gateway will start on **http://localhost:8080**

### 4. Verify the Gateway is Running

Check the health endpoint:
```bash
curl http://localhost:8080/actuator/health
```

Expected response:
```json
{
  "status": "UP"
}
```

## Testing the Gateway

### 1. Public Endpoints (No Authentication)

#### View Job Listings
```bash
curl http://localhost:8080/api/jobs
```

#### Register a New User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe@example.com",
    "password": "SecurePassword123",
    "role": "JOBSEEKER"
  }'
```

#### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe@example.com",
    "password": "SecurePassword123"
  }'
```

Expected response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "john.doe@example.com",
  "role": "JOBSEEKER"
}
```

### 2. Protected Endpoints (Requires Authentication)

#### Get Jobseeker Profile
```bash
curl http://localhost:8080/api/jobseeker/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### Get Applications
```bash
curl http://localhost:8080/api/applications \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Using Docker

### Build Docker Image

```bash
docker build -t revhire/api-gateway:1.0.0 .
```

### Run with Docker Compose

```bash
docker-compose up -d
```

### Stop Services

```bash
docker-compose down
```

## Configuration

### Environment Variables

You can override default configurations using environment variables:

```bash
export EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka/
export JWT_SECRET=your-custom-secret-key
export SERVER_PORT=8080
```

### Application Profiles

- **default** - Local development
- **docker** - Docker container deployment
- **test** - Unit and integration testing

Activate a profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=docker
```

## Viewing Active Routes

```bash
curl http://localhost:8080/actuator/gateway/routes | jq
```

## Common Commands

### Clean and Build
```bash
mvn clean package
```

### Run Tests
```bash
mvn test
```

### Run with Specific Profile
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=docker
```

### Build and Skip Tests
```bash
mvn clean package -DskipTests
```

### Run JAR File
```bash
java -jar target/api-gateway-1.0.0.jar
```

## Troubleshooting

### Port Already in Use

If port 8080 is already in use, change it:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Cannot Connect to Eureka

1. Verify Eureka server is running on port 8761
2. Check the Eureka URL in `application.yml`
3. Check network connectivity

```bash
curl http://localhost:8761/eureka/apps
```

### JWT Validation Fails

1. Ensure JWT secret matches the auth service
2. Verify token is not expired
3. Check Authorization header format: `Bearer <token>`

### Service Not Found (404)

1. Check if the target service is registered with Eureka
2. Verify the route configuration in `GatewayConfig.java`
3. Check service discovery logs

## Monitoring

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Application Info
```bash
curl http://localhost:8080/actuator/info
```

### Gateway Routes
```bash
curl http://localhost:8080/actuator/gateway/routes
```

## Next Steps

1. Start other microservices (Auth, Jobseeker, Employer, Job, Application, Notification)
2. Configure CORS for your frontend application
3. Set up monitoring and logging
4. Configure rate limiting (future enhancement)
5. Set up distributed tracing

## Support

For help and support:
- Check logs: `tail -f /tmp/api-gateway.log`
- Review documentation in `README.md` and `ARCHITECTURE.md`
- Contact the development team

## Useful Links

- Spring Cloud Gateway: https://spring.io/projects/spring-cloud-gateway
- Eureka: https://spring.io/projects/spring-cloud-netflix
- JJWT: https://github.com/jwtk/jjwt
- Spring Boot Actuator: https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html
