package com.planespotter.backend.controllers;

import com.planespotter.backend.entities.User;
import com.planespotter.backend.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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

    /**
     * Find's a single user in the database by email
     * @param email, User's email (String)
     * @param authentication, User making the request if they have the authentication to do it
     * @return User
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/findUser")
    public ResponseEntity<User> findUserByEmail(@RequestBody String email,Authentication authentication) {
        // Check if user is logged in
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    /**
     * Gets all the user in the database
     * @param authentication, User making the request if they have the authentication to do it
     * @return List<User>, A list of all the users
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<User>> findAllUser(Authentication authentication) {
        // Check if user is logged in
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String email = authentication.getName();
        List<User> users = userRepository.findAll();

        if (users == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(users);
    }

    /**
     * Update a user's privilege to an Admin Status
     * @param email, User's email to be promoted (String)
     * @param authentication, User making the request if they have the authentication to do it
     * @return User, with updated information
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/makeAdmin")
    public ResponseEntity<User> makeAdmin(@RequestBody String email, Authentication authentication) {
        // Check if user is logged in
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        User user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setIs_admin(true);
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    /**
     * Removes the Admin Status of a user
     * @param email, User's email to be demoted (String)
     * @param authentication, User making the request if they have the authentication to do it
     * @return User, with the updated information
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/removeAdmin")
    public ResponseEntity<User> removeAdmin(@RequestBody String email, Authentication authentication) {
        // Check if user is logged in
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        User user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setIs_admin(false);
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    /**
     * Delete's a user
     * @param email, User's email (String)
     * @param authentication, User making the request if they have the authentication to do it
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/deleteUser")
    public ResponseEntity<Void> deleteUser(@RequestBody String email, Authentication authentication) {
        // Check if user is logged in
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        User user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(user);

        return ResponseEntity.noContent().build();
    }




}
