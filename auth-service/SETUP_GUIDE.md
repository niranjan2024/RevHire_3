# Auth Service - Setup Guide

## Prerequisites

Before setting up the Auth Service, ensure you have the following installed:

1. **Java Development Kit (JDK) 17** or higher
   ```bash
   java -version
   ```

2. **Apache Maven 3.6+**
   ```bash
   mvn -version
   ```

3. **MySQL 8.0+**
   ```bash
   mysql --version
   ```

4. **Git** (for version control)
   ```bash
   git --version
   ```

5. **Postman** or **cURL** (for API testing)

## Step 1: Database Setup

### 1.1 Start MySQL Server

Make sure your MySQL server is running.

**macOS/Linux:**
```bash
sudo service mysql start
```

**Windows:**
```bash
net start MySQL80
```

### 1.2 Create Database

Login to MySQL:
```bash
mysql -u root -p
```

Create the database:
```sql
CREATE DATABASE revhire_auth_db;

-- Verify database creation
SHOW DATABASES;

-- Exit MySQL
EXIT;
```

### 1.3 Update Database Credentials

Edit `src/main/resources/application.yml` if your MySQL credentials differ:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/revhire_auth_db
    username: your_username
    password: your_password
```

## Step 2: Build the Project

### 2.1 Navigate to Project Directory
```bash
cd /Users/vimalkrishnan/Workspace/revature/2353/review/p2/repos/p3-auth-service
```

### 2.2 Build with Maven
```bash
mvn clean install
```

This will:
- Download all dependencies
- Compile the source code
- Run tests
- Create a JAR file in the `target/` directory

### 2.3 Skip Tests (Optional)
If you want to skip tests during build:
```bash
mvn clean install -DskipTests
```

## Step 3: Run the Service

### 3.1 Using Maven
```bash
mvn spring-boot:run
```

### 3.2 Using JAR file
```bash
java -jar target/auth-service-1.0.0.jar
```

### 3.3 Using Different Profiles

**Development Profile:**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**Production Profile:**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## Step 4: Verify Service is Running

### 4.1 Check Console Output

Look for:
```
Started AuthServiceApplication in X.XXX seconds
```

### 4.2 Test Health Endpoint

First, register and login to get a token, then:
```bash
curl -X GET http://localhost:8081/api/auth/health \
  -H "Authorization: Bearer {your-jwt-token}"
```

### 4.3 Check Eureka Dashboard (if Eureka is running)

Navigate to:
```
http://localhost:8761
```

You should see `AUTH-SERVICE` registered.

## Step 5: Test the APIs

### 5.1 Register a New User

```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "role": "JOB_SEEKER",
    "fullName": "Test User",
    "mobileNumber": "1234567890",
    "securityQuestion": "What is your favorite color?",
    "securityAnswer": "Blue"
  }'
```

### 5.2 Login

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

Save the JWT token from the response.

### 5.3 Test Protected Endpoint

```bash
curl -X GET http://localhost:8081/api/auth/user/1 \
  -H "Authorization: Bearer {your-jwt-token}"
```

## Step 6: Docker Setup (Optional)

### 6.1 Build Docker Image
```bash
mvn clean package
docker build -t auth-service:latest .
```

### 6.2 Run with Docker Compose
```bash
docker-compose up -d
```

This will start:
- MySQL database container
- Auth Service container

### 6.3 Stop Docker Services
```bash
docker-compose down
```

### 6.4 View Logs
```bash
docker-compose logs -f auth-service
```

## Step 7: IDE Setup

### IntelliJ IDEA

1. **Import Project**
   - File → Open → Select `pom.xml`
   - Import as Maven project

2. **Configure JDK**
   - File → Project Structure → Project SDK → Select JDK 17

3. **Enable Annotation Processing**
   - Settings → Build, Execution, Deployment → Compiler → Annotation Processors
   - Check "Enable annotation processing" (for Lombok)

4. **Install Lombok Plugin**
   - Settings → Plugins → Search "Lombok" → Install

5. **Run Configuration**
   - Run → Edit Configurations → Add New → Spring Boot
   - Main class: `com.revature.revhire.authservice.AuthServiceApplication`

### Eclipse

1. **Import Project**
   - File → Import → Existing Maven Projects
   - Select project root directory

2. **Install Lombok**
   - Download `lombok.jar`
   - Run: `java -jar lombok.jar`
   - Select Eclipse installation directory

3. **Run Configuration**
   - Run → Run Configurations → Java Application
   - Main class: `com.revature.revhire.authservice.AuthServiceApplication`

### VS Code

1. **Install Extensions**
   - Java Extension Pack
   - Spring Boot Extension Pack
   - Lombok Annotations Support

2. **Open Project**
   - File → Open Folder → Select project directory

3. **Run**
   - Press F5 or use Spring Boot Dashboard

## Step 8: Troubleshooting

### Database Connection Issues

**Problem:** `Communications link failure`

**Solution:**
- Check MySQL is running: `sudo service mysql status`
- Verify port 3306 is not blocked
- Check credentials in `application.yml`

### Port Already in Use

**Problem:** `Port 8081 is already in use`

**Solution:**
```bash
# Find process using port 8081
lsof -i :8081

# Kill the process
kill -9 {PID}

# Or change port in application.yml
server.port: 8082
```

### Eureka Connection Issues

**Problem:** Cannot connect to Eureka Server

**Solution:**
- Start Eureka Server first on port 8761
- Or disable Eureka in `application-dev.yml`:
  ```yaml
  eureka:
    client:
      enabled: false
  ```

### Maven Build Failures

**Problem:** Dependencies not downloading

**Solution:**
```bash
# Clear Maven cache
rm -rf ~/.m2/repository

# Rebuild
mvn clean install -U
```

### Lombok Not Working

**Problem:** Getters/Setters not found

**Solution:**
- Install Lombok plugin in IDE
- Enable annotation processing
- Rebuild project

## Step 9: Environment Variables (Production)

For production deployment, use environment variables instead of hardcoding values:

```bash
export DB_HOST=production-db-host
export DB_PORT=3306
export DB_NAME=revhire_auth_db
export DB_USERNAME=prod_user
export DB_PASSWORD=secure_password
export JWT_SECRET=your-256-bit-secret
export EUREKA_SERVER_URL=http://eureka-server:8761/eureka/

# Run with production profile
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## Step 10: Monitoring and Logs

### View Application Logs

Logs are configured in `application.yml`:

```yaml
logging:
  level:
    com.revature.revhire.authservice: DEBUG
```

### Change Log Level

Edit `application.yml` and restart:
```yaml
logging:
  level:
    com.revature.revhire.authservice: INFO  # INFO, DEBUG, WARN, ERROR
```

### Log Files

By default, logs print to console. To write to file, add:

```yaml
logging:
  file:
    name: logs/auth-service.log
  pattern:
    file: '%d{yyyy-MM-dd HH:mm:ss} - %msg%n'
```

## Next Steps

1. **Set up Eureka Server** for service discovery
2. **Set up API Gateway** for centralized routing
3. **Configure other microservices** to use Auth Service for authentication
4. **Set up monitoring** with Spring Boot Actuator
5. **Configure CI/CD pipeline** for automated deployment

## Support

For issues or questions:
- Check the main README.md
- Review API_DOCUMENTATION.md for endpoint details
- Contact: Niranjan (Module Owner)

## Quick Reference Commands

```bash
# Build
mvn clean install

# Run
mvn spring-boot:run

# Run with profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Test
mvn test

# Package
mvn clean package

# Docker build
docker build -t auth-service:latest .

# Docker run
docker-compose up -d
```
