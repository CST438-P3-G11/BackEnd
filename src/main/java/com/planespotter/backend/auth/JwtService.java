package com.planespotter.backend.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key;
    private final long ttlSeconds;

    public JwtService(
            @Value("${app.auth.jwt.secret}") String secret,
            @Value("${app.auth.jwt.ttl-seconds}") long ttlSeconds) {
        byte[] keyBytes = decodeSecret(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.ttlSeconds = ttlSeconds;
    }

    public String issue(String email, Long userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(email)
                .claim("uid", userId)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(ttlSeconds)))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public Claims parse(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private static byte[] decodeSecret(String secret) {
        try {
            byte[] decoded = Decoders.BASE64.decode(secret);
            if (decoded.length >= 32) return decoded;
        } catch (IllegalArgumentException | io.jsonwebtoken.io.DecodingException ignored) {
        }
        byte[] raw = secret.getBytes();
        if (raw.length < 32) {
            throw new IllegalStateException(
                    "JWT_SECRET must be at least 32 bytes (or a base64-encoded 32+ byte value). Got " + raw.length + " bytes.");
        }
        return raw;
    }
}
