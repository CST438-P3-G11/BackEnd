package com.planespotter.backend.auth;

import com.planespotter.backend.auth.dto.AuthResponse;
import com.planespotter.backend.auth.dto.GitHubAuthRequest;
import com.planespotter.backend.auth.dto.GoogleAuthRequest;
import com.planespotter.backend.entities.User;
import com.planespotter.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthControllerTest {
    @Mock private GoogleIdTokenService googleService;
    @Mock private GitHubOAuthService githubService;
    @Mock private JwtService jwt;
    @Mock private UserRepository users;

    @InjectMocks private AuthController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void google_success_newUser_returnsJwtAndCreatesUser() {
        when(googleService.verify("idtok"))
                .thenReturn(new GoogleIdTokenService.VerifiedGoogleUser("new@example.com", "google-sub-1"));
        when(users.findByEmail("new@example.com")).thenReturn(null);
        User saved = new User(7L, null, "new@example.com", false);
        when(users.save(any(User.class))).thenReturn(saved);
        when(jwt.issue("new@example.com", 7L)).thenReturn("fake-jwt");

        ResponseEntity<AuthResponse> res = controller.google(new GoogleAuthRequest("idtok"));

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertEquals("fake-jwt", res.getBody().token());
        assertEquals("new@example.com", res.getBody().user().email());
        verify(users).save(any(User.class));
    }

    @Test
    void google_success_existingUser_doesNotSave() {
        User existing = new User(3L, null, "old@example.com", false);
        when(googleService.verify("idtok"))
                .thenReturn(new GoogleIdTokenService.VerifiedGoogleUser("old@example.com", "google-sub-2"));
        when(users.findByEmail("old@example.com")).thenReturn(existing);
        when(jwt.issue("old@example.com", 3L)).thenReturn("fake-jwt");

        ResponseEntity<AuthResponse> res = controller.google(new GoogleAuthRequest("idtok"));

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertEquals("fake-jwt", res.getBody().token());
        verify(users, never()).save(any());
    }

    @Test
    void google_invalidToken_returns401() {
        when(googleService.verify("badtok"))
                .thenThrow(new GoogleIdTokenService.InvalidTokenException("bad token"));

        ResponseEntity<AuthResponse> res = controller.google(new GoogleAuthRequest("badtok"));

        assertEquals(HttpStatus.UNAUTHORIZED, res.getStatusCode());
        verify(users, never()).save(any());
        verify(jwt, never()).issue(any(), any());
    }

    @Test
    void github_success_newUser_returnsJwtAndCreatesUser() {
        when(githubService.exchangeCode("code123", "redir://"))
                .thenReturn(new GitHubOAuthService.VerifiedGitHubUser("gh@example.com", "12345"));
        when(users.findByEmail("gh@example.com")).thenReturn(null);
        User saved = new User(8L, null, "gh@example.com", false);
        when(users.save(any(User.class))).thenReturn(saved);
        when(jwt.issue("gh@example.com", 8L)).thenReturn("gh-jwt");

        ResponseEntity<AuthResponse> res = controller.github(new GitHubAuthRequest("code123", "redir://"));

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertEquals("gh-jwt", res.getBody().token());
        assertEquals("gh@example.com", res.getBody().user().email());
    }

    @Test
    void github_invalidCode_returns401() {
        when(githubService.exchangeCode("badcode", null))
                .thenThrow(new GitHubOAuthService.InvalidTokenException("invalid"));

        ResponseEntity<AuthResponse> res = controller.github(new GitHubAuthRequest("badcode", null));

        assertEquals(HttpStatus.UNAUTHORIZED, res.getStatusCode());
        verify(users, never()).save(any());
        verify(jwt, never()).issue(any(), any());
    }
}
