# RevHire API Gateway - Route Documentation

## Base URL
```
http://localhost:8080
```

## Authentication

Most endpoints require JWT authentication. Include the token in the Authorization header:
```
Authorization: Bearer <your_jwt_token>
```

## Route Summary

| Service | Route Pattern | Auth Required | Target Service |
|---------|--------------|---------------|----------------|
| Authentication | `/api/auth/**` | No | AUTH-SERVICE |
| Jobseeker | `/api/jobseeker/**` | Yes | JOBSEEKER-SERVICE |
| Resume | `/api/resume/**` | Yes | JOBSEEKER-SERVICE |
| Employer | `/api/employer/**` | Yes | EMPLOYER-SERVICE |
| Jobs | `/api/jobs/**` | GET: No, Others: Yes | JOB-SERVICE |
| Applications | `/api/applications/**` | Yes | APPLICATION-SERVICE |
| Notifications | `/api/notifications/**` | Yes | NOTIFICATION-SERVICE |

---

## Authentication Service Routes

Base Path: `/api/auth`

### POST /api/auth/register
Register a new user account

**Authentication:** Not required

**Request Body:**
```json
{
  "username": "john.doe@example.com",
  "password": "SecurePassword123",
  "firstName": "John",
  "lastName": "Doe",
  "role": "JOBSEEKER"
}
```

**Response:**
```json
{
  "id": 1,
  "username": "john.doe@example.com",
  "role": "JOBSEEKER",
  "message": "User registered successfully"
}
```

### POST /api/auth/login
Authenticate user and receive JWT token

**Authentication:** Not required

**Request Body:**
```json
{
  "username": "john.doe@example.com",
  "password": "SecurePassword123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "john.doe@example.com",
  "role": "JOBSEEKER",
  "expiresIn": 86400
}
```

### POST /api/auth/refresh
Refresh JWT token

**Authentication:** Not required (but requires valid refresh token)

**Request Body:**
```json
{
  "refreshToken": "refresh_token_here"
}
```

**Response:**
```json
{
  "token": "new_jwt_token",
  "refreshToken": "new_refresh_token"
}
```

---

## Jobseeker Service Routes

Base Path: `/api/jobseeker`

**Authentication:** Required for all endpoints

### GET /api/jobseeker/profile
Get authenticated jobseeker's profile

**Headers:**
- `Authorization: Bearer <token>`

**Response:**
```json
{
  "id": 1,
  "username": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "123-456-7890",
  "skills": ["Java", "Spring Boot", "React"],
  "experience": 3
}
```

### PUT /api/jobseeker/profile
Update jobseeker profile

**Headers:**
- `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "phone": "123-456-7890",
  "skills": ["Java", "Spring Boot", "React", "AWS"],
  "experience": 4
}
```

### GET /api/jobseeker/applications
Get jobseeker's applications

**Headers:**
- `Authorization: Bearer <token>`

---

## Resume Service Routes

Base Path: `/api/resume`

**Authentication:** Required for all endpoints

### POST /api/resume/upload
Upload resume

**Headers:**
- `Authorization: Bearer <token>`
- `Content-Type: multipart/form-data`

**Request:**
```
file: resume.pdf
```

### GET /api/resume
Get resume

**Headers:**
- `Authorization: Bearer <token>`

### DELETE /api/resume
Delete resume

**Headers:**
- `Authorization: Bearer <token>`

---

## Employer Service Routes

Base Path: `/api/employer`

**Authentication:** Required for all endpoints

### GET /api/employer/profile
Get employer profile

**Headers:**
- `Authorization: Bearer <token>`

**Response:**
```json
{
  "id": 1,
  "companyName": "Tech Corp",
  "industry": "Technology",
  "website": "https://techcorp.com",
  "description": "Leading tech company"
}
```

### PUT /api/employer/profile
Update employer profile

**Headers:**
- `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "companyName": "Tech Corp",
  "industry": "Technology",
  "website": "https://techcorp.com",
  "description": "Leading tech company in innovation"
}
```

### GET /api/employer/jobs
Get employer's job postings

**Headers:**
- `Authorization: Bearer <token>`

---

## Job Service Routes

Base Path: `/api/jobs`

### GET /api/jobs
Get all job listings (public)

**Authentication:** Not required

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 10)
- `location` (optional): Filter by location
- `title` (optional): Search by job title

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "title": "Senior Java Developer",
      "description": "We are looking for...",
      "location": "New York, NY",
      "salary": "120000-150000",
      "employerId": 1,
      "companyName": "Tech Corp",
      "postedDate": "2024-03-01"
    }
  ],
  "totalElements": 50,
  "totalPages": 5,
  "currentPage": 0
}
```

### GET /api/jobs/{id}
Get job by ID (public)

**Authentication:** Not required

**Response:**
```json
{
  "id": 1,
  "title": "Senior Java Developer",
  "description": "Full job description...",
  "requirements": ["5+ years Java", "Spring Boot", "Microservices"],
  "location": "New York, NY",
  "salary": "120000-150000",
  "employerId": 1,
  "companyName": "Tech Corp",
  "postedDate": "2024-03-01"
}
```

### POST /api/jobs
Create new job posting

**Authentication:** Required (Employer only)

**Headers:**
- `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "title": "Senior Java Developer",
  "description": "Full job description...",
  "requirements": ["5+ years Java", "Spring Boot", "Microservices"],
  "location": "New York, NY",
  "salary": "120000-150000"
}
```

### PUT /api/jobs/{id}
Update job posting

**Authentication:** Required (Employer only)

**Headers:**
- `Authorization: Bearer <token>`

### DELETE /api/jobs/{id}
Delete job posting

**Authentication:** Required (Employer only)

**Headers:**
- `Authorization: Bearer <token>`

---

## Application Service Routes

Base Path: `/api/applications`

**Authentication:** Required for all endpoints

### POST /api/applications
Submit job application

**Headers:**
- `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "jobId": 1,
  "coverLetter": "I am very interested in this position..."
}
```

**Response:**
```json
{
  "id": 1,
  "jobId": 1,
  "jobseekerId": 1,
  "status": "SUBMITTED",
  "submittedDate": "2024-03-06T10:30:00",
  "coverLetter": "I am very interested..."
}
```

### GET /api/applications
Get user's applications

**Headers:**
- `Authorization: Bearer <token>`

**Response:**
```json
[
  {
    "id": 1,
    "jobId": 1,
    "jobTitle": "Senior Java Developer",
    "companyName": "Tech Corp",
    "status": "SUBMITTED",
    "submittedDate": "2024-03-06T10:30:00"
  }
]
```

### GET /api/applications/{id}
Get application details

**Headers:**
- `Authorization: Bearer <token>`

### PUT /api/applications/{id}
Update application (withdraw, update cover letter)

**Headers:**
- `Authorization: Bearer <token>`

### GET /api/applications/job/{jobId}
Get applications for a job (Employer only)

**Headers:**
- `Authorization: Bearer <token>`

---

## Notification Service Routes

Base Path: `/api/notifications`

**Authentication:** Required for all endpoints

### GET /api/notifications
Get user's notifications

**Headers:**
- `Authorization: Bearer <token>`

**Response:**
```json
[
  {
    "id": 1,
    "userId": 1,
    "message": "Your application for Senior Java Developer has been viewed",
    "type": "APPLICATION_UPDATE",
    "read": false,
    "createdDate": "2024-03-06T10:30:00"
  }
]
```

### PUT /api/notifications/{id}/read
Mark notification as read

**Headers:**
- `Authorization: Bearer <token>`

### DELETE /api/notifications/{id}
Delete notification

**Headers:**
- `Authorization: Bearer <token>`

---

## Gateway Management Endpoints

### GET /actuator/health
Check gateway health

**Authentication:** Not required

**Response:**
```json
{
  "status": "UP",
  "components": {
    "gateway": {
      "status": "UP"
    },
    "diskSpace": {
      "status": "UP"
    }
  }
}
```

### GET /actuator/gateway/routes
View configured routes

**Authentication:** Not required

**Response:**
```json
[
  {
    "route_id": "auth-service",
    "uri": "lb://AUTH-SERVICE",
    "order": 0,
    "predicate": "Paths: [/api/auth/**]"
  }
]
```

---

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2024-03-06T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request parameters"
}
```

### 401 Unauthorized
```json
{
  "timestamp": "2024-03-06T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Missing or invalid JWT token"
}
```

### 403 Forbidden
```json
{
  "timestamp": "2024-03-06T10:30:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Insufficient permissions"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-03-06T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Resource not found"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "2024-03-06T10:30:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred"
}
```

### 503 Service Unavailable
```json
{
  "timestamp": "2024-03-06T10:30:00",
  "status": 503,
  "error": "Service Unavailable",
  "message": "Target service is currently unavailable"
}
```

---

## Rate Limiting (Future Feature)

Rate limits will be implemented in a future version:
- 100 requests per minute for authenticated users
- 20 requests per minute for unauthenticated requests
- Custom limits for premium employer accounts

---

## Versioning

The API Gateway currently supports v1 of all services. Future versions will support multiple API versions:
- `/api/v1/jobs/**`
- `/api/v2/jobs/**`

---

## Contact

For API support or questions, contact the RevHire development team.
