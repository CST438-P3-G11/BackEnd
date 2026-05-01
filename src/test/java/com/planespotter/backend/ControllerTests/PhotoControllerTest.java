package com.planespotter.backend.ControllerTests;

import com.planespotter.backend.controllers.PhotoController;
import com.planespotter.backend.entities.Photo;
import com.planespotter.backend.repositories.PhotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class PhotoControllerTest {

    @Mock
    private PhotoRepository photoRepository;

    @InjectMocks
    private PhotoController photoController;

    private Photo photo;
    private List<Photo> photos;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        photo = new Photo(1L, 1L, "testing.test");
        photos = List.of(new Photo(1L, 1L, "testing.test"),
                        new Photo(2L, 1L, "testing.test"));
    }

    @Test
    void getAllPhotos_returnsListOfPhotos() {
        when(photoRepository.getAllPhotos()).thenReturn(photos);

        ResponseEntity<List<Photo>> response = photoController.getAllPhotos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(photos, response.getBody());
    }

    @Test
    void getAllPhotos_404WhenEmptyList() {
        when(photoRepository.getAllPhotos()).thenReturn(List.of());

        ResponseEntity<List<Photo>> response = photoController.getAllPhotos();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getPhotoById_returnsPhoto() {
        when(photoRepository.getPhotoById(anyLong())).thenReturn(photo);

        ResponseEntity<Photo> response = photoController.getPhotoById(anyLong());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(photo, response.getBody());
    }

    @Test
    void getPhotoById_404WhenIdNotExists() {
        when(photoRepository.getPhotoById(anyLong())).thenReturn(null);

        ResponseEntity<Photo> response = photoController.getPhotoById(anyLong());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getPhotoByPlaneId_returnsListOfPhotos() {
        when(photoRepository.getPhotosByPlane_id(1L)).thenReturn(photos);

        ResponseEntity<List<Photo>> response = photoController.getPhotosByPlaneId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(photos, response.getBody());
    }

    @Test
    void getPhotoByPlaneId_404WhenNoPhotosExist() {
        when(photoRepository.getPhotosByPlane_id(anyLong())).thenReturn(List.of());

        ResponseEntity<List<Photo>> response = photoController.getPhotosByPlaneId(anyLong());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getPhotoByUserId_returnsListOfPhotos() {
        when(photoRepository.getPhotosByUser_id(anyLong())).thenReturn(photos);

        ResponseEntity<List<Photo>> response = photoController.getPhotosByUserId(anyLong());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(photos, response.getBody());
    }

    @Test
    void getPhotosByUserId_404WhenNoPhotosWithId() {
        when(photoRepository.getPhotosByUser_id(anyLong())).thenReturn(List.of());

        ResponseEntity<List<Photo>> response = photoController.getPhotosByUserId(anyLong());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getRandomPhoto_returnsRandomPhoto() {
        when(photoRepository.getRandomPhoto()).thenReturn(photo);

        ResponseEntity<Photo> response = photoController.getRandomPhoto();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(photo, response.getBody());
    }

    @Test
    void getRandomPhoto_404WhenNullReturnFromRepo() {
        when(photoRepository.getRandomPhoto()).thenReturn(null);

        ResponseEntity<Photo> response = photoController.getRandomPhoto();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getRandomPhotoByPlaneId_returnsRandomPhoto() {
        when(photoRepository.getRandomPhotoByPlane_id(1L)).thenReturn(photo);

        ResponseEntity<Photo> response = photoController.getRandomByPlaneId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(photo, response.getBody());
    }

    @Test
    void GetRandomPhotoByPlaneId_404WhenNoPhotosExist() {
        when(photoRepository.getRandomPhotoByPlane_id(anyLong())).thenReturn(null);

        ResponseEntity<Photo> response = photoController.getRandomByPlaneId(anyLong());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void insertPhoto_insertsPhoto() {
        ResponseEntity<Photo> response = photoController.insertPhoto(photo);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(photo, response.getBody());
    }

    @Test
    void updatePhoto_updatesPhoto() {
        when(photoRepository.getPhotoById(photo.getPhoto_id())).thenReturn(photo);

        ResponseEntity<Photo> response = photoController.updatePhoto(photo);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(photo, response.getBody());
    }

    @Test
    void updatePhoto_404WhenIdNotExists() {
        when(photoRepository.getPhotoById(anyLong())).thenReturn(null);

        ResponseEntity<Photo> response = photoController.updatePhoto(photo);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deletePhoto_deletesPhoto() {
        when(photoRepository.getPhotoById(anyLong())).thenReturn(photo);

        ResponseEntity<Void> response = photoController.deletePhoto(photo.getPhoto_id());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deletePhoto_404WhenIdNotExists() {
        when(photoRepository.getPhotoById(anyLong())).thenReturn(null);

        ResponseEntity<Void> response = photoController.deletePhoto(photo.getPhoto_id());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
