# Contributing to Auth Service

Thank you for your interest in contributing to the RevHire Auth Service! This document provides guidelines and instructions for contributing.

## Code of Conduct

- Be respectful and inclusive
- Provide constructive feedback
- Follow coding standards and best practices
- Write clear commit messages

## Development Setup

1. Fork the repository
2. Clone your fork
3. Follow the SETUP_GUIDE.md for environment setup
4. Create a new branch for your feature/fix

## Branch Naming Convention

Use descriptive branch names:
- `feature/feature-name` - For new features
- `bugfix/bug-description` - For bug fixes
- `hotfix/critical-fix` - For critical production fixes
- `refactor/what-is-refactored` - For code refactoring
- `docs/documentation-update` - For documentation changes

Examples:
```bash
git checkout -b feature/add-oauth-support
git checkout -b bugfix/fix-token-validation
git checkout -b refactor/improve-error-handling
```

## Coding Standards

### Java Style Guide

1. **Follow Google Java Style Guide**
   - Use 4 spaces for indentation (not tabs)
   - Maximum line length: 120 characters
   - Use meaningful variable names

2. **Naming Conventions**
   - Classes: PascalCase (e.g., `AuthService`)
   - Methods: camelCase (e.g., `validateToken`)
   - Constants: UPPER_SNAKE_CASE (e.g., `MAX_LOGIN_ATTEMPTS`)
   - Packages: lowercase (e.g., `com.revature.revhire.authservice`)

3. **Documentation**
   - Add Javadoc for public methods and classes
   - Include parameter descriptions and return values
   - Document exceptions that can be thrown

Example:
```java
/**
 * Validates a JWT token and returns user information
 *
 * @param token the JWT token to validate
 * @return ApiResponse containing validation result
 * @throws BadRequestException if token is malformed
 */
public ApiResponse validateToken(String token) {
    // implementation
}
```

### Spring Boot Best Practices

1. **Use Constructor Injection**
   ```java
   @RequiredArgsConstructor
   public class AuthService {
       private final UserRepository userRepository;
   }
   ```

2. **Use DTOs for Request/Response**
   - Never expose entity classes directly
   - Use separate DTOs for different operations

3. **Exception Handling**
   - Use custom exceptions
   - Handle exceptions in GlobalExceptionHandler
   - Return meaningful error messages

4. **Logging**
   - Use SLF4J with Lombok's `@Slf4j`
   - Log at appropriate levels (DEBUG, INFO, WARN, ERROR)
   - Include context in log messages

### Security Best Practices

1. **Input Validation**
   - Always validate user input
   - Use Bean Validation annotations
   - Sanitize inputs to prevent XSS

2. **Password Management**
   - Never log passwords
   - Use BCrypt for password encoding
   - Enforce password strength requirements

3. **Token Management**
   - Set appropriate token expiration times
   - Validate tokens on every request
   - Use secure secret keys (never hardcode in production)

## Testing

### Unit Tests

Write unit tests for:
- Service layer methods
- Utility classes
- Custom validators

Example:
```java
@Test
void testRegisterUser_Success() {
    // Arrange
    RegisterRequest request = new RegisterRequest();
    // ... setup request

    // Act
    AuthResponse response = authService.register(request);

    // Assert
    assertNotNull(response.getToken());
    assertEquals("johndoe", response.getUsername());
}
```

### Integration Tests

Write integration tests for:
- API endpoints
- Database operations
- Security configurations

### Test Coverage

- Aim for at least 80% code coverage
- Focus on critical business logic
- Test both success and failure scenarios

Run tests:
```bash
mvn test
mvn verify  # Includes integration tests
```

## Pull Request Process

### Before Submitting

1. **Update your branch**
   ```bash
   git pull origin main
   git rebase main
   ```

2. **Run tests**
   ```bash
   mvn clean test
   ```

3. **Check code style**
   ```bash
   mvn checkstyle:check
   ```

4. **Build the project**
   ```bash
   mvn clean install
   ```

### Creating Pull Request

1. **Write a clear title**
   - Format: `[Type] Brief description`
   - Examples:
     - `[Feature] Add OAuth2 authentication support`
     - `[Bugfix] Fix token expiration validation`
     - `[Refactor] Improve error handling in AuthService`

2. **Provide detailed description**
   ```markdown
   ## Description
   Brief description of changes

   ## Changes Made
   - Change 1
   - Change 2

   ## Testing
   - How to test these changes

   ## Related Issues
   Fixes #123
   ```

3. **Screenshots/Logs (if applicable)**
   - Include before/after screenshots
   - Add relevant log outputs

### Review Process

1. At least one reviewer must approve
2. All CI/CD checks must pass
3. No merge conflicts
4. Code follows style guidelines
5. Tests are passing

## Commit Messages

### Format
```
[Type] Short description (max 50 chars)

Detailed description (if needed)
- Point 1
- Point 2

Fixes #issue-number
```

### Types
- `[Feature]` - New feature
- `[Bugfix]` - Bug fix
- `[Refactor]` - Code refactoring
- `[Docs]` - Documentation changes
- `[Test]` - Adding or updating tests
- `[Build]` - Build system changes
- `[CI]` - CI/CD changes

### Examples
```
[Feature] Add JWT token refresh endpoint

Implemented token refresh functionality to allow users to
refresh expired tokens without re-login.

- Added RefreshTokenRequest DTO
- Updated JwtService with refresh logic
- Added endpoint in AuthController
- Updated security configuration

Fixes #45
```

## Documentation

### Update Documentation

When making changes, update:
- `README.md` - If functionality changes
- `API_DOCUMENTATION.md` - If endpoints change
- `SETUP_GUIDE.md` - If setup process changes
- Javadoc comments - For code changes

### API Documentation

When adding/modifying endpoints:
1. Update Swagger annotations
2. Add examples to API_DOCUMENTATION.md
3. Update Postman collection

## Database Changes

### Migration Strategy

1. **Never delete columns directly**
   - First mark as deprecated
   - Remove in next version

2. **Use Liquibase/Flyway for migrations**
   - Create versioned migration scripts
   - Test migrations on sample data

3. **Backward Compatibility**
   - Ensure changes don't break existing data
   - Provide data migration scripts if needed

## Security Considerations

### Reporting Security Issues

**DO NOT** create public issues for security vulnerabilities.

Instead:
1. Email security concerns to project maintainers
2. Provide detailed description
3. Include steps to reproduce
4. Wait for response before disclosure

### Security Checklist

Before submitting PR, verify:
- [ ] No hardcoded secrets or credentials
- [ ] Input validation is implemented
- [ ] Sensitive data is not logged
- [ ] SQL injection prevention
- [ ] XSS prevention
- [ ] CSRF protection (if applicable)

## Performance Considerations

1. **Database Queries**
   - Use indexes appropriately
   - Avoid N+1 queries
   - Use pagination for large datasets

2. **Caching**
   - Cache frequently accessed data
   - Set appropriate TTL
   - Clear cache when data changes

3. **Async Operations**
   - Use `@Async` for long-running tasks
   - Don't block request threads

## Getting Help

- Review existing code for examples
- Check documentation in `/docs` folder
- Ask questions in team chat
- Contact module owner: Niranjan

## Recognition

Contributors will be recognized in:
- CONTRIBUTORS.md file
- Release notes
- Project documentation

Thank you for contributing to RevHire Auth Service!
