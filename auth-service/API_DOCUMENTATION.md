# Auth Service API Documentation

## Base URL
```
http://localhost:8081/api/auth
```

## Endpoints

### 1. Register User

**Endpoint:** `POST /register`

**Description:** Register a new user (Job Seeker or Employer)

**Request Body:**
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123",
  "role": "JOB_SEEKER",
  "fullName": "John Doe",
  "mobileNumber": "1234567890",
  "securityQuestion": "What is your favorite color?",
  "securityAnswer": "Blue"
}
```

**Response (201 Created):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "role": "JOB_SEEKER",
  "profileCompleted": false,
  "message": "Registration successful"
}
```

**Validation Rules:**
- Username: 3-50 characters, alphanumeric with underscores
- Email: Valid email format
- Password: Minimum 6 characters
- Role: JOB_SEEKER or EMPLOYER
- Security question and answer are required

---

### 2. Login

**Endpoint:** `POST /login`

**Description:** Authenticate user and receive JWT token

**Request Body:**
```json
{
  "username": "johndoe",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "role": "JOB_SEEKER",
  "profileCompleted": true,
  "message": "Login successful"
}
```

**Error Response (401 Unauthorized):**
```json
{
  "success": false,
  "message": "Invalid username or password",
  "timestamp": "2024-03-06T10:30:00"
}
```

---

### 3. Logout

**Endpoint:** `POST /logout`

**Authentication Required:** Yes

**Headers:**
```
Authorization: Bearer {jwt-token}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Logout successful",
  "timestamp": "2024-03-06T10:35:00"
}
```

---

### 4. Change Password

**Endpoint:** `POST /change-password`

**Authentication Required:** Yes

**Headers:**
```
Authorization: Bearer {jwt-token}
```

**Request Body:**
```json
{
  "currentPassword": "password123",
  "newPassword": "newpassword456"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Password changed successfully",
  "timestamp": "2024-03-06T10:40:00"
}
```

**Error Response (400 Bad Request):**
```json
{
  "success": false,
  "message": "Current password is incorrect",
  "timestamp": "2024-03-06T10:40:00"
}
```

---

### 5. Forgot Password

**Endpoint:** `POST /forgot-password`

**Description:** Generate password reset token using security answer

**Request Body:**
```json
{
  "email": "john@example.com",
  "securityAnswer": "Blue"
}
```

**Response (200 OK):**
```json
{
  "resetToken": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "message": "Password reset token generated successfully",
  "expiresInMinutes": 30
}
```

**Error Response (403 Forbidden):**
```json
{
  "success": false,
  "message": "Security answer is incorrect",
  "timestamp": "2024-03-06T10:45:00"
}
```

**Error Response (404 Not Found):**
```json
{
  "success": false,
  "message": "User not found with this email",
  "timestamp": "2024-03-06T10:45:00"
}
```

---

### 6. Reset Password

**Endpoint:** `POST /reset-password`

**Description:** Reset password using reset token

**Request Body:**
```json
{
  "resetToken": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "newPassword": "newpassword456"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Password reset successfully",
  "timestamp": "2024-03-06T10:50:00"
}
```

**Error Response (400 Bad Request):**
```json
{
  "success": false,
  "message": "Reset token has expired",
  "timestamp": "2024-03-06T10:50:00"
}
```

**Error Response (404 Not Found):**
```json
{
  "success": false,
  "message": "Invalid or expired reset token",
  "timestamp": "2024-03-06T10:50:00"
}
```

---

### 7. Update Profile Completion

**Endpoint:** `POST /profile-completion/{userId}`

**Authentication Required:** Yes

**Headers:**
```
Authorization: Bearer {jwt-token}
```

**Path Parameters:**
- `userId` - User ID (Long)

**Request Body:**
```json
{
  "fullName": "John Doe",
  "mobileNumber": "1234567890",
  "location": "New York",
  "employmentStatus": "EMPLOYED"
}
```

**Employment Status Options:**
- FRESHER
- EMPLOYED
- UNEMPLOYED

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Profile completed successfully",
  "timestamp": "2024-03-06T11:00:00"
}
```

---

### 8. Get User by ID

**Endpoint:** `GET /user/{userId}`

**Authentication Required:** Yes

**Headers:**
```
Authorization: Bearer {jwt-token}
```

**Path Parameters:**
- `userId` - User ID (Long)

**Response (200 OK):**
```json
{
  "id": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "role": "JOB_SEEKER",
  "fullName": "John Doe",
  "mobileNumber": "1234567890",
  "location": "New York",
  "employmentStatus": "EMPLOYED",
  "profileCompleted": true,
  "createdAt": "2024-03-01T09:00:00"
}
```

**Error Response (404 Not Found):**
```json
{
  "success": false,
  "message": "User not found",
  "timestamp": "2024-03-06T11:05:00"
}
```

---

### 9. Validate Token

**Endpoint:** `GET /validate`

**Description:** Validate JWT token (for inter-service communication)

**Query Parameters:**
- `token` - JWT token (String)

**Example:**
```
GET /validate?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response (200 OK) - Valid Token:**
```json
{
  "success": true,
  "message": "Token is valid",
  "data": "johndoe",
  "timestamp": "2024-03-06T11:10:00"
}
```

**Response (200 OK) - Invalid Token:**
```json
{
  "success": false,
  "message": "Token is invalid",
  "timestamp": "2024-03-06T11:10:00"
}
```

---

### 10. Health Check

**Endpoint:** `GET /health`

**Authentication Required:** Yes

**Headers:**
```
Authorization: Bearer {jwt-token}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Auth Service is running",
  "timestamp": "2024-03-06T11:15:00"
}
```

---

## Error Codes

| Status Code | Description |
|-------------|-------------|
| 200 | Success |
| 201 | Created (Registration) |
| 400 | Bad Request (Validation errors) |
| 401 | Unauthorized (Invalid credentials) |
| 403 | Forbidden (Access denied) |
| 404 | Not Found (User/Resource not found) |
| 409 | Conflict (Username/Email exists) |
| 500 | Internal Server Error |

---

## Common Error Response Format

**Validation Error (400):**
```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "username": "Username must be between 3 and 50 characters",
    "email": "Email should be valid"
  },
  "timestamp": "2024-03-06T11:20:00"
}
```

---

## Authentication

Most endpoints require JWT authentication. Include the token in the Authorization header:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Token Expiration:** 24 hours (86400000 ms)

---

## Example Usage with cURL

### Register
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "email": "john@example.com",
    "password": "password123",
    "role": "JOB_SEEKER",
    "fullName": "John Doe",
    "mobileNumber": "1234567890",
    "securityQuestion": "What is your favorite color?",
    "securityAnswer": "Blue"
  }'
```

### Login
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "password": "password123"
  }'
```

### Get User (with authentication)
```bash
curl -X GET http://localhost:8081/api/auth/user/1 \
  -H "Authorization: Bearer {your-jwt-token}"
```

---

## Testing with Postman

1. Import the endpoints into Postman
2. Create an environment variable for `baseUrl`: `http://localhost:8081/api/auth`
3. Create an environment variable for `token` to store JWT after login
4. Use `{{baseUrl}}` and `{{token}}` in requests
5. Set Authorization Type to "Bearer Token" for protected endpoints

---

## Notes

- All timestamps are in ISO-8601 format
- Passwords are encrypted using BCrypt
- Security answers are also encrypted for additional security
- Reset tokens expire after 30 minutes
- Input is sanitized to prevent XSS and SQL injection attacks
- Username and email lookups are case-insensitive
