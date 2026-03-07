# RevHire Config Server

Centralized configuration management server for the RevHire recruitment platform microservices architecture.

## Overview

The Config Server provides a centralized location for managing configuration properties for all microservices in the RevHire platform. It uses Spring Cloud Config Server with native profile to serve configuration files from the classpath.

## Technology Stack

- **Spring Boot**: 3.3.2
- **Spring Cloud**: 2023.0.2
- **Java**: 17
- **Spring Cloud Config Server**: For centralized configuration management
- **Eureka Client**: For service discovery and registration

## Port Configuration

- **Server Port**: 8888

## Features

- Centralized configuration management for all microservices
- Native file system backend for configuration storage
- Service registration with Eureka Server
- Health check and monitoring endpoints
- Automatic configuration refresh capability

## Configuration Files

The server manages configurations for the following services:

1. **auth-service** (Port: 8081) - Authentication and authorization
2. **jobseeker-service** (Port: 8082) - Job seeker management
3. **employer-service** (Port: 8083) - Employer management
4. **job-service** (Port: 8084) - Job posting management
5. **application-service** (Port: 8085) - Job application management
6. **notification-service** (Port: 8086) - Notification management

## Common Configuration Properties

Each service configuration includes:

- **Database Configuration**: MySQL connection to `revhire_db`
- **JWT Settings**:
  - Secret key for token signing
  - Expiration time: 24 hours (86400000 ms)
- **Eureka Client**: Service discovery configuration
- **JPA/Hibernate**: Database ORM settings
- **Management Endpoints**: Health, info, metrics, and prometheus

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+ (for downstream services)
- Eureka Server running on port 8761

## Build and Run

### Build the project
```bash
mvn clean install
```

### Run the application
```bash
mvn spring-boot:run
```

Or run the JAR directly:
```bash
java -jar target/config-server-1.0.0.jar
```

## Accessing Configuration

Microservices can access their configuration by connecting to:
```
http://localhost:8888/{application-name}/default
```

Example:
```
http://localhost:8888/auth-service/default
```

## Management Endpoints

- Health: `http://localhost:8888/actuator/health`
- Info: `http://localhost:8888/actuator/info`
- Environment: `http://localhost:8888/actuator/env`
- Refresh: `http://localhost:8888/actuator/refresh`

## Project Structure

```
p3-config-server/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── revhire/
│       │           └── config/
│       │               └── ConfigServerApplication.java
│       └── resources/
│           ├── application.yml
│           └── config/
│               ├── auth-service.yml
│               ├── jobseeker-service.yml
│               ├── employer-service.yml
│               ├── job-service.yml
│               ├── application-service.yml
│               └── notification-service.yml
├── pom.xml
└── README.md
```

## Integration with Microservices

Each microservice should include the following dependency in their `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

And configure `application.yml` or `bootstrap.yml`:

```yaml
spring:
  application:
    name: {service-name}
  config:
    import: optional:configserver:http://localhost:8888
```

## Security Considerations

- The JWT secret key should be changed in production
- Database credentials should be externalized using environment variables
- Consider enabling authentication for the Config Server in production
- Use encrypted property values for sensitive data

## Author

RevHire Development Team

## Version

1.0.0
