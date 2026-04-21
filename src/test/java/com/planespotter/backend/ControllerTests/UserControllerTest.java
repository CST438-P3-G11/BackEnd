package com.planespotter.backend.ControllerTests;


import com.planespotter.backend.controllers.UserController;
import com.planespotter.backend.entities.User;
import com.planespotter.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(1L, "userone@csumb.edu", true);
    }

    @Test
    public void testCurrentUser_isAuthenticated() {
        // 1. Mock authentication
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("userone@csumb.edu");

        // When calling findByEmail, it returns the user that was set up in @BeforeEach
        when(userRepository.findByEmail("userone@csumb.edu")).thenReturn(user);

        ResponseEntity<User> response = userController.me(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testCurrentUser_isNotAuthenticated() {
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(false);
        when(authentication.getName()).thenReturn("userone@csumb.edu");

        when(userRepository.findByEmail("userone@csumb.edu")).thenReturn(user);

        ResponseEntity<User> response = userController.me(authentication);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
