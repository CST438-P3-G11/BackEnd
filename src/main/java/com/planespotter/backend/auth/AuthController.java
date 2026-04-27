package com.planespotter.backend.auth;

import com.planespotter.backend.auth.dto.AuthResponse;
import com.planespotter.backend.auth.dto.GitHubAuthRequest;
import com.planespotter.backend.auth.dto.GoogleAuthRequest;
import com.planespotter.backend.entities.User;
import com.planespotter.backend.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final GoogleIdTokenService googleService;
    private final GitHubOAuthService githubService;
    private final JwtService jwt;
    private final UserRepository users;

    public AuthController(GoogleIdTokenService googleService, GitHubOAuthService githubService,
                          JwtService jwt, UserRepository users) {
        this.googleService = googleService;
        this.githubService = githubService;
        this.jwt = jwt;
        this.users = users;
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> google(@Valid @RequestBody GoogleAuthRequest req) {
        try {
            var verified = googleService.verify(req.idToken());
            User user = upsertByEmail(verified.email());
            return ResponseEntity.ok(AuthResponse.of(jwt.issue(user.getEmail(), user.getUser_id()), user));
        } catch (GoogleIdTokenService.InvalidTokenException e) {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/github")
    public ResponseEntity<AuthResponse> github(@Valid @RequestBody GitHubAuthRequest req) {
        try {
            var verified = githubService.exchangeCode(req.code(), req.redirectUri());
            User user = upsertByEmail(verified.email());
            return ResponseEntity.ok(AuthResponse.of(jwt.issue(user.getEmail(), user.getUser_id()), user));
        } catch (GitHubOAuthService.InvalidTokenException e) {
            return ResponseEntity.status(401).build();
        }
    }

    private User upsertByEmail(String email) {
        User existing = users.findByEmail(email);
        if (existing != null) return existing;
        return users.save(new User(null, null, email, Boolean.FALSE));
    }
}
