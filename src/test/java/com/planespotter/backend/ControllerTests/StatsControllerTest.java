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
        user = new User(Long.valueOf(1L), "test", "test@email.com", Boolean.valueOf(false));
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

    @Test
    void insertStats_insertsStats() {
        ResponseEntity<Stats> response = statsController.insertStats(stats);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void sendResult_successfullyUpdatesCorrectAnswers() {
        Stats expectedStats = new Stats(stats.getUser(),
                stats.getGamesPlayed() + 1,
                stats.getWinningStreak() + 1,
                stats.getBestStreak() + 1,
                stats.getCorrectGuesses() + 1);
        when(statsRepository.findByUserId(Long.valueOf(1L))).thenReturn(stats);
        when(statsRepository.findByUserId(Long.valueOf(1L))).thenReturn(expectedStats);

        ResponseEntity<Stats> response = statsController.sendResult(1L, true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStats, response.getBody());
    }

    @Test
    void sendResult_successfullyUpdatesWrongAnswer() {
        Stats expectedStats = new Stats(stats.getUser(),
                stats.getGamesPlayed() + 1,
                0,
                stats.getBestStreak(),
                stats.getCorrectGuesses());
        when(statsRepository.findByUserId(Long.valueOf(1L))).thenReturn(stats);
        when(statsRepository.findByUserId(Long.valueOf(1L))).thenReturn(expectedStats);

        ResponseEntity<Stats> response = statsController.sendResult(1L, false);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStats, response.getBody());
    }
}