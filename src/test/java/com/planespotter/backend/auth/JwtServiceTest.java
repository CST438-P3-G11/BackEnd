package com.planespotter.backend.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {
    // 64 chars of base64 → 48 bytes after decode → exceeds 32-byte HS256 minimum.
    private static final String BASE64_SECRET =
            "dGVzdC1zZWNyZXQtZm9yLXVuaXQtdGVzdHMtdGVzdC1zZWNyZXQtZm9yLXVuaXR0";
    // 47 chars, contains '-' which is not valid standard base64.
    private static final String RAW_SECRET = "raw-test-secret-with-non-base64-chars-1234567890";

    @Test
    void issueAndParse_roundTripsSubjectAndUid() {
        JwtService jwt = new JwtService(BASE64_SECRET, 3600);
        String token = jwt.issue("user@example.com", 42L);

        Claims claims = jwt.parse(token);

        assertEquals("user@example.com", claims.getSubject());
        Number uid = claims.get("uid", Number.class);
        assertNotNull(uid);
        assertEquals(42L, uid.longValue());
    }

    @Test
    void parse_rejectsExpiredToken() {
        JwtService jwt = new JwtService(BASE64_SECRET, -1L); // already expired
        String token = jwt.issue("user@example.com", 1L);

        assertThrows(ExpiredJwtException.class, () -> jwt.parse(token));
    }

    @Test
    void parse_rejectsTamperedToken() {
        JwtService jwt = new JwtService(BASE64_SECRET, 3600);
        String token = jwt.issue("user@example.com", 1L);

        // Flip a character in the payload segment to invalidate the signature.
        String[] parts = token.split("\\.");
        char original = parts[1].charAt(5);
        char swapped = original == 'A' ? 'B' : 'A';
        String tamperedPayload = parts[1].substring(0, 5) + swapped + parts[1].substring(6);
        String tamperedToken = parts[0] + "." + tamperedPayload + "." + parts[2];

        assertThrows(JwtException.class, () -> jwt.parse(tamperedToken));
    }

    @Test
    void constructor_rejectsShortSecret() {
        assertThrows(IllegalStateException.class, () -> new JwtService("short", 3600));
    }

    @Test
    void constructor_acceptsRawNonBase64Secret() {
        // Regression test: secrets with hyphens (invalid base64 chars) should fall
        // through the base64 attempt to the raw-bytes path, not crash on DecodingException.
        JwtService jwt = new JwtService(RAW_SECRET, 3600);
        String token = jwt.issue("user@example.com", 1L);

        assertEquals("user@example.com", jwt.parse(token).getSubject());
    }
}
