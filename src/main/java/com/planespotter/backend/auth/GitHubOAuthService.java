package com.planespotter.backend.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class GitHubOAuthService {
    private static final String TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String API_BASE = "https://api.github.com";

    private final String clientId;
    private final String clientSecret;
    private final RestClient http = RestClient.create();

    public GitHubOAuthService(
            @Value("${app.auth.github.client-id}") String clientId,
            @Value("${app.auth.github.client-secret}") String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public VerifiedGitHubUser exchangeCode(String code, String redirectUri) {
        Map<String, Object> tokenResp = http.post()
                .uri(TOKEN_URL)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(buildForm(code, redirectUri))
                .retrieve()
                .body(Map.class);

        if (tokenResp == null || tokenResp.get("access_token") == null) {
            Object error = tokenResp == null ? "no response" : tokenResp.get("error_description");
            throw new InvalidTokenException("GitHub code exchange failed: " + error);
        }
        String accessToken = tokenResp.get("access_token").toString();
        return fetchUser(accessToken);
    }

    private VerifiedGitHubUser fetchUser(String accessToken) {
        Map<String, Object> userResp = http.get()
                .uri(API_BASE + "/user")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .retrieve()
                .body(Map.class);
        if (userResp == null) throw new InvalidTokenException("GitHub /user returned empty");

        String email = userResp.get("email") != null ? userResp.get("email").toString() : null;
        if (email == null || email.isBlank()) {
            email = fetchPrimaryEmail(accessToken);
        }
        if (email == null) throw new InvalidTokenException("Could not resolve a verified GitHub email");

        Object idObj = userResp.get("id");
        String githubId = idObj == null ? null : idObj.toString();
        return new VerifiedGitHubUser(email, githubId);
    }

    private String fetchPrimaryEmail(String accessToken) {
        List<Map<String, Object>> emails = http.get()
                .uri(API_BASE + "/user/emails")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .retrieve()
                .body(List.class);
        if (emails == null) return null;
        for (Map<String, Object> e : emails) {
            if (Boolean.TRUE.equals(e.get("primary")) && Boolean.TRUE.equals(e.get("verified"))) {
                return e.get("email") == null ? null : e.get("email").toString();
            }
        }
        return null;
    }

    private String buildForm(String code, String redirectUri) {
        StringBuilder sb = new StringBuilder();
        sb.append("client_id=").append(urlEncode(clientId));
        sb.append("&client_secret=").append(urlEncode(clientSecret));
        sb.append("&code=").append(urlEncode(code));
        if (redirectUri != null && !redirectUri.isBlank()) {
            sb.append("&redirect_uri=").append(urlEncode(redirectUri));
        }
        return sb.toString();
    }

    private static String urlEncode(String s) {
        return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8);
    }

    public record VerifiedGitHubUser(String email, String githubId) {}

    public static class InvalidTokenException extends RuntimeException {
        public InvalidTokenException(String msg) { super(msg); }
    }
}
