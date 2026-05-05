package com.planespotter.backend.auth;

import com.planespotter.backend.entities.User;
import com.planespotter.backend.repositories.UserRepository;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtAuthFilterTest {
    private static final String SECRET =
            "test-jwt-secret-at-least-32-bytes-long-padding-padding";

    private JwtService jwtService;
    private JwtAuthFilter filter;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET, 3600);
        userRepository = mock(UserRepository.class);
        filter = new JwtAuthFilter(jwtService, userRepository);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void missingHeader_leavesContextEmpty() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(req, res, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void validBearer_setsAuthenticationWithEmailAsPrincipal() throws ServletException, IOException {
        String token = jwtService.issue("alice@example.com", 7L);

        User mockUser = new User(1L,"testuser","alice@example.com", false);

        when(userRepository.findByEmail("alice@example.com"))
                .thenReturn(mockUser);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(req, res, chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals("alice@example.com", auth.getName());
        assertTrue(auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void invalidBearer_leavesContextEmpty() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Authorization", "Bearer not.a.valid.jwt");
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(req, res, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void nonBearerHeader_isIgnored() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Authorization", "Basic dXNlcjpwYXNz");
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(req, res, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
