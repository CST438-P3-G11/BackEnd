package com.planespotter.backend.controllers;

import com.planespotter.backend.entities.User;
import com.planespotter.backend.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * GET endpoint to get the current logged-in user
     * @return User if found, otherwise 404
     */
    @GetMapping("/currentUser")
    public ResponseEntity<User> me(Authentication authentication) {
        // Check if user is logged in
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }







}
