package com.planespotter.backend.controllers;

import com.planespotter.backend.entities.Stats;
import com.planespotter.backend.entities.User;
import com.planespotter.backend.repositories.StatsRepository;
import com.planespotter.backend.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stats")
public class StatsController {
    private final StatsRepository statsRepository;
    private final UserRepository userRepository;

    StatsController(StatsRepository statsRepository, UserRepository userRepository) {
        this.statsRepository = statsRepository;
        this.userRepository = userRepository;
    }

    /**
     * GET endpoint to get the current user's stats
     *
     * @param userId the Id of the user whose stats to retrieve
     * @return Stats if found, otherwise 404
     */
    @GetMapping("/getStats")
    public ResponseEntity<Stats> getStats(@RequestParam("userId") long userId) {
        User user = userRepository.findById(Long.valueOf(userId)).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        Stats stats = statsRepository.findByUser(user);
        if (stats == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/getLeaderboard")
    public ResponseEntity<List<Stats>> getLeaderboard() {
        List<Stats> leaderboard = statsRepository.findTop10ByOrderByBestStreakDesc();
        return ResponseEntity.ok(leaderboard);
    }

    /**
     * POST mapping to insert a new stats entity into the DB.
     *
     * @param stats stats entity to insert.
     * @return HTTP 200, and the created object.
     */
    @PostMapping("/insertStats")
    public ResponseEntity<Stats> insertStats(@RequestBody Stats stats) {
        statsRepository.save(stats);
        return ResponseEntity.ok(stats);
    }


    @PatchMapping("/sendResult")
    public ResponseEntity<Stats> sendResult(@RequestParam long userId, @RequestParam boolean result) {
        Stats stats = statsRepository.findByUserId(userId);
        if (stats == null) {
            stats = new Stats(userRepository.getById(userId), 0, 0, 0, 0);
            statsRepository.save(stats);
        }
        if (result) {
            statsRepository.updateStats(stats.getGamesPlayed() + 1,
                    stats.getCorrectGuesses() + 1,
                    stats.getWinningStreak() + 1,
                    userId
            );
            if (stats.getWinningStreak() >= stats.getBestStreak()) {
                statsRepository.updateBestStreak(stats.getWinningStreak() + 1, userId);
            }
            return ResponseEntity.ok(statsRepository.findByUserId(userId));
        }
        statsRepository.updateStats(stats.getGamesPlayed() + 1, stats.getCorrectGuesses(), 0, userId);
        return ResponseEntity.ok(statsRepository.findByUserId(userId));
    }
}
