package com.planespotter.backend.repositories;

import com.planespotter.backend.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Transactional
    @Modifying
    @Query (value = "UPDATE \"user\" SET name = :name WHERE user_id = :id", nativeQuery = true)
    void updateName(@Param("id")Long id, @Param("name")String name);

}
