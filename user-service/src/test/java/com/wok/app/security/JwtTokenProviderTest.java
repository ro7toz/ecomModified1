package com.wok.app.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JWT Token Provider Tests")
class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;
    private static final String TEST_SECRET = "test-secret-key-minimum-32-characters-for-hs256";
    private static final String TEST_USER_ID = "user-123";
    private static final String TEST_EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(tokenProvider, "jwtSecret", TEST_SECRET);
        ReflectionTestUtils.setField(tokenProvider, "jwtExpirationMs", 86400000L);
    }

    @Test
    @DisplayName("Should generate valid JWT token")
    void testGenerateToken() {
        String token = tokenProvider.generateToken(TEST_USER_ID, TEST_EMAIL);
        
        assertNotNull(token);
        assertTrue(token.contains("."));
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    @DisplayName("Should extract user ID from token")
    void testGetUserIdFromToken() {
        String token = tokenProvider.generateToken(TEST_USER_ID, TEST_EMAIL);
        String extractedUserId = tokenProvider.getUserIdFromToken(token);
        
        assertEquals(TEST_USER_ID, extractedUserId);
    }

    @Test
    @DisplayName("Should extract email from token")
    void testGetEmailFromToken() {
        String token = tokenProvider.generateToken(TEST_USER_ID, TEST_EMAIL);
        String extractedEmail = tokenProvider.getEmailFromToken(token);
        
        assertEquals(TEST_EMAIL, extractedEmail);
    }

    @Test
    @DisplayName("Should validate valid token")
    void testValidateValidToken() {
        String token = tokenProvider.generateToken(TEST_USER_ID, TEST_EMAIL);
        boolean isValid = tokenProvider.validateToken(token);
        
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should reject invalid token")
    void testValidateInvalidToken() {
        boolean isValid = tokenProvider.validateToken("invalid-token");
        
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should reject malformed token")
    void testValidateMalformedToken() {
        boolean isValid = tokenProvider.validateToken("invalid.token.structure");
        
        assertFalse(isValid);
    }
}
