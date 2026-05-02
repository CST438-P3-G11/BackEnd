package com.planespotter.backend.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.planespotter.backend.entities.User;
import com.planespotter.backend.entities.Stats;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {
    Stats findByUser(User user);
    List<Stats> findTop10ByOrderByBestStreakDesc();

    @Query(value = "SELECT * FROM \"stats\" WHERE user_id = :userId", nativeQuery = true)
    Stats findByUserId(long userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE \"stats\" SET games_played = :games, correct_guesses = :guesses, winning_streak = :streak" +
            " WHERE user_id = :id", nativeQuery = true)
    void updateStats(int games, int guesses, int streak, long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE \"stats\" SET best_streak = :streak WHERE user_id = :id", nativeQuery = true)
    void updateBestStreak(int streak, long id);

    long user(User user);
}
