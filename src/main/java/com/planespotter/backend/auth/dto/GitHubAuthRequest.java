package com.planespotter.backend.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record GitHubAuthRequest(@NotBlank String code, String redirectUri) {}
