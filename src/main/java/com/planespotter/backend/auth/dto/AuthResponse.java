package com.planespotter.backend.auth.dto;

import com.planespotter.backend.entities.User;

public record AuthResponse(String token, UserDto user) {
    public static AuthResponse of(String token, User user) {
        return new AuthResponse(token, new UserDto(user.getUser_id(), user.getEmail(), user.getIs_admin()));
    }

    public record UserDto(Long userId, String email, Boolean isAdmin) {}
}
