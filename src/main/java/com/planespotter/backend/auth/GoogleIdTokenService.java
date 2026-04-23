package com.planespotter.backend.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GoogleIdTokenService {
    private final GoogleIdTokenVerifier verifier;

    public GoogleIdTokenService(
            @Value("${app.auth.google.web-client-id}") String webClientId,
            @Value("${app.auth.google.ios-client-id:}") String iosClientId,
            @Value("${app.auth.google.android-client-id:}") String androidClientId) {
        List<String> audiences = new ArrayList<>();
        audiences.add(webClientId);
        if (iosClientId != null && !iosClientId.isBlank()) audiences.add(iosClientId);
        if (androidClientId != null && !androidClientId.isBlank()) audiences.add(androidClientId);

        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(audiences)
                .setIssuers(Arrays.asList("accounts.google.com", "https://accounts.google.com"))
                .build();
    }

    public VerifiedGoogleUser verify(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new InvalidTokenException("Google ID token failed verification");
            }
            GoogleIdToken.Payload payload = idToken.getPayload();
            if (!Boolean.TRUE.equals(payload.getEmailVerified())) {
                throw new InvalidTokenException("Google email is not verified");
            }
            return new VerifiedGoogleUser(payload.getEmail(), payload.getSubject());
        } catch (GeneralSecurityException | IOException e) {
            throw new InvalidTokenException("Error verifying Google ID token: " + e.getMessage());
        }
    }

    public record VerifiedGoogleUser(String email, String googleSubject) {}

    public static class InvalidTokenException extends RuntimeException {
        public InvalidTokenException(String msg) { super(msg); }
    }
}
