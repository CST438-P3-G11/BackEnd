package com.planespotter.backend.controllers;

import com.planespotter.backend.entities.Photo;
import com.planespotter.backend.repositories.PhotoRepository;
import com.planespotter.backend.repositories.PlaneRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/photos")
public class PhotoController {

    private final PhotoRepository photoRepository;

    PhotoController(PhotoRepository photoRepository, PlaneRepository planeRepository) {
        this.photoRepository = photoRepository;
    }

    /**
     * GET endpoint that returns all Photos in the database.
     * @return All Photos currently in the database, or 404 if the database is empty.
     */
    @GetMapping("/getAllPhotos")
    public ResponseEntity<List<Photo>> getAllPhotos() {
        List<Photo> photos = photoRepository.getAllPhotos();
        if (photos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(photos);
    }

    /**
     * GET endpoint to retrieve a Photo with the specified ID.
     * @param id ID of the photo to get.
     * @return Photo object with the given ID, or 404 if no photo with that ID is found.
     */
    @GetMapping("/getById")
    public ResponseEntity<Photo> getPhotoById(@RequestParam("id") long id) {
        Photo photo = photoRepository.getPhotoById(id);
        if (photo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(photo);
    }

    /**
     * GET endpoint to retrieve all photos associated with a specified plane ID.
     * @param planeId ID of the plane to get photos for.
     * @return List of photos with the given plane ID, or 404 if no photos with that plane ID exist.
     */
    @GetMapping("/getByPlaneId")
    public ResponseEntity<List<Photo>> getPhotosByPlaneId(@RequestParam("plane_id") long planeId) {
        List<Photo> photos = photoRepository.getPhotosByPlane_id(planeId);
        if (photos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(photos);
    }

    /**
     * GET endpoint to retrieve all photos associated with a specified user ID.
     * @param userId ID of the user to get all photos from.
     * @return List of photos associated with the provided user ID, orr 404 if no photos associated with it exist.
     */
    @GetMapping("/getByUserId")
    public ResponseEntity<List<Photo>> getPhotosByUserId(@RequestParam("user_id") long userId) {
        List<Photo> photos = photoRepository.getPhotosByUser_id(userId);
        if (photos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(photos);
    }

    /**
     * GET endpoint to get a random photo from the database.
     * @return One random photo from the database, or 404 if no photo is returned (likely due to an empty DB).
     */
    @GetMapping("/getRandomPhoto")
    public ResponseEntity<Photo> getRandomPhoto() {
        Photo photo = photoRepository.getRandomPhoto();
        if (photo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(photo);
    }

    /**
     * POST endpoint to add a new photo into the database.
     * @param photo Photo object to insert into the database.
     * @return HTTP 201 after creation.
     */
    @PostMapping("/addPhoto")
    public ResponseEntity<Photo> insertPhoto(@RequestBody Photo photo) {
        photoRepository.insertPhoto(photo);
        return ResponseEntity.status(HttpStatus.CREATED).body(photo);
    }

    /**
     * PATCH mapping to update a photo.
     * @param photo Photo object to update.
     * @return HTTP 200 after a successful update, or 404 if no photo with the given ID exists.
     */
    @PatchMapping("/updatePhoto/{id}")
    public ResponseEntity<Photo> updatePhoto(@RequestBody Photo photo) {
        if (photoRepository.getPhotoById(photo.getPhoto_id()) == null) {
            return ResponseEntity.notFound().build();
        }
        photoRepository.updatePhoto(photo);
        return ResponseEntity.ok(photo);
    }

    /**
     * DELETE mapping to delete a photo.
     * @param id ID of the photo to delete.
     * @return HTTP 204 on successful deletion, or 404 if no photo with the given ID exists.
     */
    @DeleteMapping("/deletePhoto/{id}")
    public ResponseEntity<Void> deletePhoto(@RequestParam("id") long id) {
        if (photoRepository.getPhotoById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        photoRepository.deletePhotoById(id);
        return ResponseEntity.noContent().build();
    }

}
