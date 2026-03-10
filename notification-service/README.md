# RevHire Notification Service

The Notification Service is a microservice in the RevHire recruitment platform that manages notifications for job applications, status updates, and system messages.

## Module Owner
**Niranjan**

## Technology Stack
- Java 17
- Spring Boot 3.3.2
- Spring Cloud 2023.0.2
- Spring Data JPA
- MySQL Database
- Netflix Eureka Client
- OpenFeign
- Lombok

## Port Configuration
- **Service Port**: 8086

## Database
- **Database Name**: revhire_notification_db
- **Default Connection**: mysql://localhost:3306/revhire_notification_db

## Features

### Notification Management
- Store and retrieve user notifications
- Track read/unread status
- Support multiple notification types

### Notification Types
- **APPLICATION_RECEIVED**: Notify employers of new applications
- **APPLICATION_UPDATE**: Notify job seekers of status changes
- **JOB_RECOMMENDATION**: Suggest relevant jobs to job seekers
- **SYSTEM**: General system notifications

### Integration
- **Feign Client**: Validates user existence via Auth Service
- **Eureka Client**: Service discovery and registration

## API Endpoints

### User Endpoints
- `GET /api/notifications/user/{userId}` - Get all notifications for a user
- `GET /api/notifications/unread-count` - Get unread notification count (requires X-User-Id header)
- `PATCH /api/notifications/{notificationId}/read` - Mark notification as read (requires X-User-Id header)

### Internal Service Endpoints
- `POST /api/notifications` - Create a custom notification
- `POST /api/notifications/application-received` - Create application received notification
- `POST /api/notifications/application-status-change` - Create application status change notification

## Building and Running

### Prerequisites
- Java 17
- Maven 3.6+
- MySQL 8.0+
- Eureka Server running on port 8761

### Database Setup
```sql
CREATE DATABASE revhire_notification_db;
```

### Build
```bash
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

Or run the JAR:
```bash
java -jar target/notification-service-1.0.0.jar
```

## Configuration
Key configuration can be modified in `src/main/resources/application.yml`:
- Server port
- Database connection
- Eureka server URL
- Feign client settings

## Entity Model

### Notification
- `id` - Unique identifier
- `recipientId` - ID of the user receiving the notification
- `jobId` - Associated job ID (nullable)
- `type` - Type of notification (enum)
- `message` - Notification message content
- `isRead` - Read status flag
- `createdAt` - Timestamp of creation

## Service Architecture
The service follows a layered architecture:
- **Controller Layer**: REST API endpoints
- **Service Layer**: Business logic
- **Repository Layer**: Data access
- **Client Layer**: External service communication (Feign)
- **Exception Layer**: Centralized error handling

## Error Handling
- Custom exceptions with appropriate HTTP status codes
- Global exception handler for consistent error responses
- Validation error handling with detailed messages

## Integration with Other Services
- **Auth Service**: User validation
- **Application Service**: Triggers notifications on application events
- **Job Service**: May trigger job recommendation notifications

## Development Notes
- Uses Lombok for boilerplate code reduction
- JPA for database operations
- Hibernate auto-DDL for schema management (update mode)
- Transaction management for data consistency
- Logging with SLF4J
