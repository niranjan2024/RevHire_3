# Application Service

**Developer:** Guru Prasadh
**Responsibility:** Job Application Management

## Overview

The Application Service is a microservice responsible for managing job applications in the RevHire recruitment platform. It handles the complete application lifecycle from submission to status updates, including validations, status transitions, and notifications

## Features

### Core Functionality
- Job application submission with validations
- Application withdrawal by job seekers
- Application status management by employers
- Application status transition validation
- Statistics and analytics for applications
- Event-driven notifications on status changes

### Business Rules
1. **Application Requirements:**
   - Job seekers must have an uploaded resume before applying
   - Jobs must be in OPEN status to receive applications
   - Duplicate applications are not allowed

2. **Status Transitions:**
   - APPLIED → UNDER_REVIEW, SHORTLISTED, REJECTED
   - UNDER_REVIEW → SHORTLISTED, REJECTED
   - SHORTLISTED → REJECTED
   - WITHDRAWN and REJECTED are terminal states

3. **Access Control:**
   - Job seekers can only manage their own applications
   - Employers can only manage applications for their own jobs

## Technology Stack

- **Framework:** Spring Boot 3.2.5
- **Language:** Java 17
- **Database:** MySQL
- **Service Discovery:** Eureka Client
- **Inter-service Communication:** OpenFeign
- **Validation:** Jakarta Validation

## Architecture

### Entities
- `JobApplication`: Core entity storing application data
- `ApplicationStatus`: Enum (APPLIED, UNDER_REVIEW, SHORTLISTED, REJECTED, WITHDRAWN)

### API Endpoints

#### Job Seeker Endpoints
- `POST /api/jobseeker/jobs/apply` - Submit a job application
- `POST /api/jobseeker/applications/{id}/withdraw` - Withdraw an application
- `GET /api/jobseeker/applications` - Get all applications for logged-in job seeker
- `GET /api/jobseeker/applications/{id}` - Get specific application details
- `GET /api/jobseeker/jobs/applied-ids` - Get list of applied job IDs

#### Employer Endpoints
- `GET /api/employer/jobs/{jobId}/applications` - Get all applications for a job
- `GET /api/employer/applications/{id}` - Get specific application details
- `PATCH /api/employer/applications/{id}/status` - Update application status
- `GET /api/employer/jobs/{jobId}/statistics` - Get application statistics

#### Internal Endpoints (Inter-service)
- `GET /internal/applications/job/{jobId}` - Get applications by job
- `GET /internal/applications/jobseeker/{jobSeekerId}` - Get applications by job seeker
- `GET /internal/applications/{id}` - Get specific application
- `GET /internal/applications/job/{jobId}/count` - Get application count
- `GET /internal/applications/job/{jobId}/statistics` - Get statistics
- `GET /internal/applications/jobseeker/{jobSeekerId}/applied-jobs` - Get applied job IDs

### Service Dependencies

The Application Service communicates with:
1. **Auth Service** - User validation
2. **JobSeeker Service** - Resume validation, job seeker validation
3. **Employer Service** - Job validation, employer verification
4. **Notification Service** - Event notifications

## Database Schema

```sql
CREATE TABLE job_applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_id BIGINT NOT NULL,
    job_seeker_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    notes VARCHAR(2000),
    cover_letter VARCHAR(1000),
    withdraw_reason VARCHAR(250),
    applied_at DATETIME NOT NULL,
    UNIQUE KEY uk_job_seeker (job_id, job_seeker_id)
);
```

## Configuration

### Application Properties
- Server runs on port `8084`
- Database: `revhire_application_db`
- Eureka server: `http://localhost:8761/eureka/`

### Environment Variables
Configure the following in your environment:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE`

## Running the Service

### Prerequisites
- JDK 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Eureka Server running

### Steps
1. Create database:
   ```sql
   CREATE DATABASE revhire_application_db;
   ```

2. Update database credentials in `application.properties`

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Run the service:
   ```bash
   mvn spring-boot:run
   ```

The service will start on port 8084 and register with Eureka.

## API Usage Examples

### Apply to a Job
```bash
curl -X POST http://localhost:8084/api/jobseeker/jobs/apply \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 123" \
  -d '{
    "jobId": 456,
    "coverLetter": "I am very interested in this position..."
  }'
```

### Withdraw Application
```bash
curl -X POST http://localhost:8084/api/jobseeker/applications/789/withdraw \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 123" \
  -d '{
    "reason": "Accepted another offer"
  }'
```

### Update Application Status (Employer)
```bash
curl -X PATCH http://localhost:8084/api/employer/applications/789/status \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 456" \
  -d '{
    "status": "SHORTLISTED",
    "notes": "Strong candidate, schedule interview"
  }'
```

## Error Handling

The service provides comprehensive error handling with appropriate HTTP status codes:
- `400 Bad Request` - Validation errors, invalid transitions
- `404 Not Found` - Resource not found
- `409 Conflict` - Duplicate applications
- `403 Forbidden` - Unauthorized access
- `500 Internal Server Error` - Unexpected errors

## Event Publishing

The service publishes events for:
- New application submitted → Notification to employer
- Application status changed → Notification to job seeker
- Application withdrawn → Notification to employer

## Testing

Run tests with:
```bash
mvn test
```

## Developer Notes

### Status Transition Logic
The service enforces strict status transition rules to maintain data integrity:
- Terminal states (WITHDRAWN, REJECTED) cannot be changed
- Forward-only progression through the application lifecycle
- Validation happens before any state change

### Performance Considerations
- Database indexes on `job_id`, `job_seeker_id` for faster queries
- Unique constraint prevents duplicate applications
- Feign client timeouts configured for resilience

## Future Enhancements
- Application scoring/ranking
- Bulk status updates
- Advanced filtering and search
- Application timeline/history
- Email notifications integration
- Document attachment support

## License
Copyright 2024 RevHire. All rights reserved.
