package com.planespotter.backend.repositories;


import com.planespotter.backend.entities.Photo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    @Query(value = "SELECT * FROM \"photo\"", nativeQuery = true)
    List<Photo> getAllPhotos();

    @Query(value = "SELECT * FROM \"photo\" WHERE photo_id = :id", nativeQuery = true)
    Photo getPhotoById(long id);

    @Query(value = "SELECT * FROM \"photo\" WHERE user_id = :id", nativeQuery = true)
    List<Photo> getPhotosByUser_id(long id);

    @Query(value = "SELECT * FROM \"photo\" WHERE plane_id = :id", nativeQuery = true)
    List<Photo> getPhotosByPlane_id(long id);

    @Query(value = "SELECT * FROM \"photo\" ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Photo getRandomPhoto();

    @Query(value = "SELECT * FROM \"photo\" WHERE plane_id = :id ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Photo getRandomPhotoByPlane_id(long id);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO \"photo\" (user_id, plane_id, url) VALUES " +
            "(:#{#photo.user_id}, :#{#photo.plane_id}, :#{#photo.url})", nativeQuery = true)
    void insertPhoto(Photo photo);

    @Transactional
    @Modifying
    @Query(value = "UPDATE \"photo\" SET user_id = :#{#photo.user_id}, " +
            "plane_id = :#{#photo.plane_id}, url = :#{#photo.url}", nativeQuery = true)
    void updatePhoto(Photo photo);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM \"photo\" WHERE photo_id = :id", nativeQuery = true)
    void deletePhotoById(long id);

}
