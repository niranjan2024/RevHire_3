# Auth Service - Quick Start Guide

Get the Auth Service up and running in 5 minutes!

## Prerequisites Check

Verify you have these installed:

```bash
java -version    # Should be 17+
mvn -version     # Should be 3.6+
mysql --version  # Should be 8.0+
```

## Quick Setup (5 Steps)

### Step 1: Create Database (30 seconds)

```bash
mysql -u root -p
```

```sql
CREATE DATABASE revhire_auth_db;
EXIT;
```

### Step 2: Update Configuration (Optional)

Edit `src/main/resources/application.yml` if needed:

```yaml
spring:
  datasource:
    username: your_username
    password: your_password
```

### Step 3: Build Project (1-2 minutes)

```bash
cd /Users/vimalkrishnan/Workspace/revature/2353/review/p2/repos/p3-auth-service
mvn clean install
```

### Step 4: Run Service (10 seconds)

```bash
mvn spring-boot:run
```

Wait for: `Started AuthServiceApplication in X.XXX seconds`

### Step 5: Test API (30 seconds)

Open a new terminal and register a user:

```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "role": "JOB_SEEKER",
    "securityQuestion": "Favorite color?",
    "securityAnswer": "Blue"
  }'
```

You should get a response with a JWT token!

## Quick Test Sequence

### 1. Register
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "email": "john@example.com",
    "password": "password123",
    "role": "JOB_SEEKER",
    "securityQuestion": "Pet name?",
    "securityAnswer": "Max"
  }'
```

Copy the `token` from the response.

### 2. Login
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "password": "password123"
  }'
```

### 3. Get User Info (use your token)
```bash
curl -X GET http://localhost:8081/api/auth/user/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## Using Postman (Recommended)

### Import Collection

1. Open Postman
2. Click **Import**
3. Select `postman_collection.json` from project root
4. Collection imported!

### Set Environment

1. Create new environment: "Auth Service Local"
2. Add variable:
   - `baseUrl`: `http://localhost:8081/api/auth`
3. Save and select the environment

### Test Flow

1. **Register Job Seeker** → Run request
2. **Login** → Token auto-saved to environment
3. **Get User By ID** → Uses saved token
4. **Update Profile** → Complete profile

## Common Issues & Solutions

### Issue: "Port 8081 already in use"

**Solution:**
```bash
# Find process
lsof -i :8081

# Kill it
kill -9 {PID}
```

### Issue: "Access denied for user"

**Solution:** Update database credentials in `application.yml`

### Issue: "Cannot create database"

**Solution:**
```bash
mysql -u root -p
```
```sql
CREATE DATABASE revhire_auth_db;
GRANT ALL PRIVILEGES ON revhire_auth_db.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

### Issue: Lombok errors in IDE

**Solution:**
- IntelliJ: Install Lombok plugin + Enable annotation processing
- Eclipse: Run `java -jar lombok.jar`
- VS Code: Install Lombok extension

## Development Mode

Run with auto-restart on code changes:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Docker Quick Start

### Build and Run
```bash
# Build JAR
mvn clean package -DskipTests

# Build Docker image
docker build -t auth-service:latest .

# Run with Docker Compose (includes MySQL)
docker-compose up -d

# View logs
docker-compose logs -f auth-service

# Stop
docker-compose down
```

## API Endpoints Quick Reference

| Method | Endpoint | Auth Required | Description |
|--------|----------|---------------|-------------|
| POST | `/api/auth/register` | No | Register new user |
| POST | `/api/auth/login` | No | Login user |
| POST | `/api/auth/logout` | Yes | Logout user |
| POST | `/api/auth/change-password` | Yes | Change password |
| POST | `/api/auth/forgot-password` | No | Get reset token |
| POST | `/api/auth/reset-password` | No | Reset password |
| POST | `/api/auth/profile-completion/{id}` | Yes | Complete profile |
| GET | `/api/auth/user/{id}` | Yes | Get user details |
| GET | `/api/auth/validate?token={token}` | No | Validate token |
| GET | `/api/auth/health` | Yes | Health check |

## Sample Data for Testing

### Job Seeker Registration
```json
{
  "username": "jobseeker1",
  "email": "jobseeker@example.com",
  "password": "password123",
  "role": "JOB_SEEKER",
  "fullName": "John Seeker",
  "mobileNumber": "1234567890",
  "securityQuestion": "What is your favorite color?",
  "securityAnswer": "Blue"
}
```

### Employer Registration
```json
{
  "username": "employer1",
  "email": "employer@example.com",
  "password": "password123",
  "role": "EMPLOYER",
  "fullName": "ABC Corporation",
  "mobileNumber": "0987654321",
  "securityQuestion": "What year was company founded?",
  "securityAnswer": "2020"
}
```

### Profile Completion
```json
{
  "fullName": "John Doe Updated",
  "mobileNumber": "1234567890",
  "location": "New York, NY",
  "employmentStatus": "EMPLOYED"
}
```

## Next Steps

After getting the service running:

1. ✅ Test all API endpoints with Postman
2. ✅ Review API documentation: `API_DOCUMENTATION.md`
3. ✅ Understand architecture: `ARCHITECTURE.md`
4. ✅ Set up Eureka Server for service discovery
5. ✅ Configure API Gateway
6. ✅ Deploy other microservices

## Useful Commands

```bash
# Build without tests
mvn clean install -DskipTests

# Run tests only
mvn test

# Package JAR
mvn clean package

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Check application logs
tail -f logs/auth-service.log

# Database backup
mysqldump -u root -p revhire_auth_db > backup.sql

# Database restore
mysql -u root -p revhire_auth_db < backup.sql
```

## Monitoring

### Application Status
```bash
# Check if running
curl http://localhost:8081/actuator/health

# View application info
curl http://localhost:8081/actuator/info
```

### Database Status
```bash
mysql -u root -p -e "USE revhire_auth_db; SHOW TABLES;"
```

## Getting Help

- 📖 Full documentation: `README.md`
- 🏗️ Architecture details: `ARCHITECTURE.md`
- 🔧 Setup guide: `SETUP_GUIDE.md`
- 📡 API reference: `API_DOCUMENTATION.md`
- 🤝 Contributing: `CONTRIBUTING.md`

## Support

Module Owner: **Niranjan**

---

**That's it! Your Auth Service should be running on http://localhost:8081**

Happy coding! 🚀
