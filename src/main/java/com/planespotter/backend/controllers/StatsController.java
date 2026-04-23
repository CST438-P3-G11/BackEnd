package com.planespotter.backend.controllers;

import com.planespotter.backend.entities.Stats;
import com.planespotter.backend.entities.User;
import com.planespotter.backend.repositories.StatsRepository;
import com.planespotter.backend.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * @param userId the Id of the user whose stats to retrieve
     * @return Stats if found, otherwise 404
     */
    @GetMapping("/getStats")
    public ResponseEntity<Stats> getStats(@RequestParam("userId") long userId){
        User user = userRepository.findById(Long.valueOf(userId)).orElse(null);
        if (user == null){
            return ResponseEntity.notFound().build();
        }
        Stats stats = statsRepository.findByUser(user);
        if (stats == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stats);
    }
}
