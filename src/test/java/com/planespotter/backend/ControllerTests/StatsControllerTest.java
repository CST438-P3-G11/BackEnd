package com.planespotter.backend.ControllerTests;

import com.planespotter.backend.controllers.StatsController;
import com.planespotter.backend.entities.Stats;
import com.planespotter.backend.entities.User;
import com.planespotter.backend.repositories.StatsRepository;
import com.planespotter.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class StatsControllerTest {

    @Mock
    private StatsRepository statsRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StatsController statsController;

    private User user;
    private Stats stats;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(Long.valueOf(1L), "test@email.com", Boolean.valueOf(false));
        stats = new Stats(user, 10, 3, 5, 7);
    }

    @Test
    void getStats_returnsStats() {
        when(userRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(user));
        when(statsRepository.findByUser(user)).thenReturn(stats);

        ResponseEntity<Stats> response = statsController.getStats(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(stats, response.getBody());
        verify(userRepository, times(1)).findById(Long.valueOf(1L));
        verify(statsRepository, times(1)).findByUser(user);
    }

    @Test
    void getStats_404WhenUserNotFound() {
        when(userRepository.findById(Long.valueOf(1L))).thenReturn(Optional.empty());

        ResponseEntity<Stats> response = statsController.getStats(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getStats_404WhenStatsNotFound() {
        when(userRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(user));
        when(statsRepository.findByUser(user)).thenReturn(null);

        ResponseEntity<Stats> response = statsController.getStats(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}