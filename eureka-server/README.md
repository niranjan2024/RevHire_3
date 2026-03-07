# RevHire Eureka Server

Service Discovery Server for the RevHire recruitment platform microservices architecture.

## Overview

This Eureka Server provides centralized service registration and discovery for all RevHire microservices. It enables microservices to find and communicate with each other without hardcoding hostnames and ports.

## Technology Stack

- **Spring Boot**: 3.3.2
- **Spring Cloud**: 2023.0.2
- **Netflix Eureka Server**: Service Discovery
- **Java**: 17

## Configuration

The Eureka Server runs on port **8761** by default.

### Key Configuration Settings

- **Port**: 8761
- **Register with Eureka**: false (server doesn't register with itself)
- **Fetch Registry**: false (server is the registry)
- **Hostname**: localhost

## Running the Application

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Build and Run

```bash
# Build the application
mvn clean install

# Run the application
mvn spring-boot:run
```

Alternatively, run the JAR directly:

```bash
java -jar target/eureka-server-1.0.0.jar
```

## Accessing the Dashboard

Once started, access the Eureka Dashboard at:

```
http://localhost:8761
```

The dashboard shows:
- All registered services
- Instance status and health
- Service metadata
- Replica information

## Registered Services

The following RevHire microservices will register with this Eureka server:

- **auth-service** - Authentication and authorization
- **job-service** - Job posting management
- **jobseeker-service** - Job seeker profile management
- **employer-service** - Employer profile management
- **application-service** - Job application management
- **notification-service** - Email and notification handling
- **api-gateway** - API Gateway and routing

## Client Configuration

Other microservices should include this configuration to register with Eureka:

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    register-with-eureka: true
    fetch-registry: true
  instance:
    prefer-ip-address: true
```

## Health Check

Check if the Eureka Server is running:

```bash
curl http://localhost:8761/actuator/health
```

## Project Structure

```
p3-eureka-server/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/revhire/eureka/
│   │   │       └── EurekaServerApplication.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/
│           └── com/revhire/eureka/
├── pom.xml
└── README.md
```

## Development Notes

- Self-preservation mode is disabled in development for easier testing
- Eviction interval is set to 3 seconds for faster service deregistration
- In production, enable self-preservation mode to handle network partitions

## Support

For issues or questions regarding the Eureka Server, contact the RevHire Platform Team.
