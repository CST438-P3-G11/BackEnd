package com.planespotter.backend.repositories;

import com.planespotter.backend.entities.Plane;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlaneRepository extends JpaRepository<Plane, Long> {

    @Query(value = "SELECT * FROM \"plane\" WHERE \"name\" LIKE CONCAT('%', :name, '%')", nativeQuery = true)
    List<Plane> getByName(String name);

    @Query(value = "SELECT * FROM \"plane\" WHERE plane_id = :id", nativeQuery = true)
    Plane getById(long id);

    @Query(value = "SELECT * FROM \"plane\" ORDER BY RANDOM() LIMIT 4", nativeQuery = true)
    List<Plane> getPlanesForGame();

    @Query(value = "SELECT * FROM \"plane\"", nativeQuery = true)
    List<Plane> getAllPlanes();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO \"plane\" (user_id, name) VALUES " +
            "(:#{#plane.user_id}, :#{#plane.name})", nativeQuery = true)
    void insertPlane(Plane plane);

    @Transactional
    @Modifying
    @Query(value = "UPDATE \"plane\" SET name = :name WHERE \"plane_id\"  = :id", nativeQuery = true)
    void updatePlaneById(long plane_id, String name);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM \"plane\" WHERE \"plane_id\" = :plane_id", nativeQuery = true)
    void deletePlaneById(long plane_id);
}
