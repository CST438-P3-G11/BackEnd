package com.planespotter.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.planespotter.backend.entities.User;
import com.planespotter.backend.entities.Stats;
import org.springframework.stereotype.Repository;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {
    Stats findByUser(User user);
}
