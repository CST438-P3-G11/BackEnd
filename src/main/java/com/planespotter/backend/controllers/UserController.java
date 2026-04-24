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

    /**
     * Updates the name of the user in their User profile
     * @param new_name, a String
     * @param authentication, a Authentication
     * @return a User object
     */
    @PatchMapping("/updateName")
    public ResponseEntity<User> updateName(@RequestBody String new_name, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        // Gets the User
        User user = userRepository.findByEmail(authentication.getName());
        // Checks user is not null
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        int result = userRepository.updateName(user.getUser_id(), new_name);

        if (result > 0) {
            user.setName(new_name);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(401).build();
    }







}
