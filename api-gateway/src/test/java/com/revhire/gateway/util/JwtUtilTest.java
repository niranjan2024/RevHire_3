package com.revhire.gateway.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    private String validToken;
    private String expiredToken;
    private final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    @BeforeEach
    void setUp() {
        // Set the secret key for testing
        ReflectionTestUtils.setField(jwtUtil, "SECRET_KEY", SECRET_KEY);

        // Create a valid token
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "JOBSEEKER");
        validToken = createToken(claims, "testuser@example.com", false);

        // Create an expired token
        expiredToken = createToken(claims, "testuser@example.com", true);
    }

    @Test
    void testValidateToken_ValidToken() {
        assertDoesNotThrow(() -> jwtUtil.validateToken(validToken));
    }

    @Test
    void testValidateToken_ExpiredToken() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            jwtUtil.validateToken(expiredToken);
        });
        assertTrue(exception.getMessage().contains("Invalid or expired JWT token"));
    }

    @Test
    void testExtractUsername() {
        String username = jwtUtil.extractUsername(validToken);
        assertEquals("testuser@example.com", username);
    }

    @Test
    void testExtractRole() {
        String role = jwtUtil.extractRole(validToken);
        assertEquals("JOBSEEKER", role);
    }

    @Test
    void testExtractExpiration() {
        Date expiration = jwtUtil.extractExpiration(validToken);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    private String createToken(Map<String, Object> claims, String subject, boolean expired) {
        long expirationTime = expired ? -1000 : 1000 * 60 * 60 * 10; // -1 second or 10 hours
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
