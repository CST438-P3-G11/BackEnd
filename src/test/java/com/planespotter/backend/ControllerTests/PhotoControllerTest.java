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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        photo = new Photo(1L, 1L, "testing.test");
    }

    @Test
    void getAllPhotos_returnsListOfPhotos() {
        List<Photo> photos = List.of(new Photo(1L, 1L, "testing.test"),
                                    new Photo(2L, 1L, "testing.test"));
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

}
