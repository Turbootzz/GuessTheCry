package com.guessthecry.utils;

import com.guessthecry.utils.JwtUtil;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        jwtUtil = new JwtUtil();
        // inject a secret because we cant use @Value
        String secret = "c2VjdXJlLWp3dC1zZWNyZXQtZm9yLWd1ZXNzLXRoZS1jcnktYXBwLXRlc3Rpbmc=";
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", secret);
    }

    @Test
    void testGenerateTokenShouldCreateValidToken() {
        String username = "testuser";
        String role = "User";

        String token = jwtUtil.generateToken(username, role);

        assertNotNull(token);
        assertEquals(username, jwtUtil.extractUsername(token));
        assertEquals(role, jwtUtil.extractRole(token));
        assertTrue(jwtUtil.validateToken(token, username));
    }

    @Test
    void testValidateTokenWithInvalidSignatureShouldFail() {
        // Token generated with different secret
        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInJvbGUiOiJVc2VyIiwiaWF0IjoxNjE2NDk2ODQ1LCJleHAiOjE2MTY1MzI4NDV9.B-invalid-signature";
        assertThrows(SignatureException.class, () -> jwtUtil.extractUsername(invalidToken));
    }

    @Test
    void testValidateTokenWithExpiredTokenShouldReturnFalse() throws InterruptedException {
        // make experation really short (1ms)
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", 1L);
        String token = jwtUtil.generateToken("expiredUser", "User");

        // wait for token to expire
        Thread.sleep(5);

        assertFalse(jwtUtil.validateToken(token, "expiredUser"));
    }
}