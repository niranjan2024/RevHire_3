# RevHire Config Server - Service Configuration Reference

## Overview

This document provides a comprehensive reference for all service configurations managed by the RevHire Config Server.

## Service Registry

| Service Name | Port | Configuration File | Description |
|--------------|------|-------------------|-------------|
| auth-service | 8081 | auth-service.yml | Authentication and authorization service |
| jobseeker-service | 8082 | jobseeker-service.yml | Job seeker profile and management |
| employer-service | 8083 | employer-service.yml | Employer profile and management |
| job-service | 8084 | job-service.yml | Job posting and search service |
| application-service | 8085 | application-service.yml | Job application tracking service |
| notification-service | 8086 | notification-service.yml | Notification and messaging service |

## Common Configuration Properties

All services share the following common configuration structure:

### Database Configuration
- **Database**: MySQL 8.0+
- **Database Name**: revhire_db
- **Connection URL**: jdbc:mysql://localhost:3306/revhire_db
- **Default Username**: root
- **Default Password**: root
- **Auto-create Database**: true
- **SSL**: disabled
- **Timezone**: UTC

### JPA/Hibernate Settings
- **DDL Auto**: update
- **Show SQL**: true
- **Dialect**: MySQL8Dialect
- **Format SQL**: true

### JWT Configuration
- **Secret Key**: RevHireJwtSecretKeyForHS256MustBeAtLeast32CharsLong123
- **Token Expiration**: 86400000 ms (24 hours)

### Eureka Client Configuration
- **Eureka Server URL**: http://localhost:8761/eureka/
- **Register with Eureka**: true
- **Fetch Registry**: true
- **Hostname**: localhost
- **Prefer IP Address**: true
- **Lease Renewal Interval**: 30 seconds
- **Lease Expiration Duration**: 90 seconds

### Management Endpoints
All services expose the following actuator endpoints:
- health
- info
- metrics
- prometheus

## Service-Specific Details

### 1. Auth Service (Port 8081)
**Purpose**: Handles user authentication, JWT token generation, and authorization.

**Key Responsibilities**:
- User registration and login
- JWT token generation and validation
- Password encryption and verification
- Role-based access control

### 2. JobSeeker Service (Port 8082)
**Purpose**: Manages job seeker profiles, resumes, and related data.

**Key Responsibilities**:
- Job seeker profile management
- Resume upload and storage
- Skills and experience tracking
- Application history

### 3. Employer Service (Port 8083)
**Purpose**: Manages employer profiles and company information.

**Key Responsibilities**:
- Employer profile management
- Company information management
- Employer verification
- Hiring team management

### 4. Job Service (Port 8084)
**Purpose**: Handles job postings, job search, and job recommendations.

**Key Responsibilities**:
- Job posting creation and management
- Job search and filtering
- Job recommendations
- Job category management

### 5. Application Service (Port 8085)
**Purpose**: Manages job applications and application tracking.

**Key Responsibilities**:
- Application submission
- Application status tracking
- Application review workflow
- Interview scheduling

### 6. Notification Service (Port 8086)
**Purpose**: Handles all notifications and messaging within the platform.

**Key Responsibilities**:
- Email notifications
- In-app notifications
- Application status updates
- Interview reminders

## Configuration Access

### For Development
Services can access their configuration at:
```
http://localhost:8888/{service-name}/default
```

### Example URLs
- Auth Service: http://localhost:8888/auth-service/default
- JobSeeker Service: http://localhost:8888/jobseeker-service/default
- Employer Service: http://localhost:8888/employer-service/default
- Job Service: http://localhost:8888/job-service/default
- Application Service: http://localhost:8888/application-service/default
- Notification Service: http://localhost:8888/notification-service/default

## Security Considerations

### Production Deployment Checklist
- [ ] Change JWT secret key to a cryptographically secure random value
- [ ] Use environment variables for database credentials
- [ ] Enable SSL/TLS for database connections
- [ ] Implement Config Server authentication
- [ ] Encrypt sensitive configuration properties
- [ ] Use Spring Cloud Config encryption key
- [ ] Implement proper secret management (e.g., HashiCorp Vault)
- [ ] Change default database passwords
- [ ] Enable HTTPS for Config Server
- [ ] Implement network security groups/firewalls

## Environment-Specific Configuration

To support multiple environments (dev, test, prod), create profile-specific configuration files:

```
config/
├── auth-service.yml          # Default/common properties
├── auth-service-dev.yml      # Development overrides
├── auth-service-test.yml     # Testing overrides
└── auth-service-prod.yml     # Production overrides
```

## Configuration Refresh

Services can refresh their configuration without restart using the refresh endpoint:

```bash
curl -X POST http://localhost:{service-port}/actuator/refresh
```

## Troubleshooting

### Configuration Not Loading
1. Verify Config Server is running on port 8888
2. Check Eureka Server connectivity
3. Verify service name matches configuration file name
4. Check Config Server logs for errors

### Database Connection Issues
1. Verify MySQL is running on port 3306
2. Check database credentials
3. Ensure revhire_db exists or createDatabaseIfNotExist is true
4. Verify network connectivity

### Eureka Registration Issues
1. Ensure Eureka Server is running on port 8761
2. Check network connectivity
3. Verify service hostname configuration
4. Check Eureka dashboard for service status

## Monitoring and Health Checks

All services provide health check endpoints:
```
http://localhost:{service-port}/actuator/health
```

Health check includes:
- Database connectivity
- Disk space
- Application status

## Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | 2026-03-06 | Initial configuration for all six microservices |

## Support

For issues or questions regarding service configuration, contact the RevHire Development Team.
