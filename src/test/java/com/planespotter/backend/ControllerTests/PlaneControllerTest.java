package com.planespotter.backend.ControllerTests;

import com.planespotter.backend.controllers.PlaneController;
import com.planespotter.backend.entities.Plane;
import com.planespotter.backend.repositories.PlaneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PlaneControllerTest {

    @Mock
    private PlaneRepository planeRepository;

    @InjectMocks
    private PlaneController planeController;

    private Plane plane;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        plane = new Plane(0, "plane");
    }

    @Test
    void getPlaneByName_returnsListOfPlanes() {
        List<Plane> planes = Arrays.asList(new Plane(0, "plane 1"),
                                            new Plane(1, "plane 2"));
        when(planeRepository.getByName("plane")).thenReturn(planes);

        ResponseEntity<List<Plane>> response = planeController.getPlaneByName("plane");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(planes, response.getBody());
        verify(planeRepository, times(1)).getByName("plane");
    }

    @Test
    void getPlaneByName_404OnEmptyList() {
        when(planeRepository.getByName("plane")).thenReturn(List.of());

        ResponseEntity<List<Plane>> response = planeController.getPlaneByName("plane");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getPlaneById_returnsListPlane() {
        when(planeRepository.getById(anyLong())).thenReturn(plane);

        ResponseEntity<Plane> response = planeController.getPlaneById(anyLong());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(plane, response.getBody());
        verify(planeRepository, times(1)).getById(anyLong());
    }

    @Test
    void getPlaneById_404WhenIdDoesNotExist() {
        when(planeRepository.getById(anyLong())).thenReturn(null);

        ResponseEntity<Plane> response = planeController.getPlaneById(anyLong());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void insertPlane_successfullyInsertsPlane() {
        ResponseEntity<Plane> response = planeController.addPlane(plane);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void updatePlaneById_successfullyUpdatesPlane() {
        when(planeRepository.existsById(anyLong())).thenReturn(true);

        ResponseEntity<Plane> response = planeController.updatePlane(anyLong(), plane);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(plane, response.getBody());
    }

    @Test
    void updatePlaneById_404WhenIdDoesNotExist() {
        when(planeRepository.existsById(anyLong())).thenReturn(false);

        ResponseEntity<Plane> response = planeController.updatePlane(anyLong(), plane);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deletePlaneById_successfullyDeletesPlane() {
        when(planeRepository.getById(anyLong())).thenReturn(plane);

        ResponseEntity<Void> response = planeController.deletePlane(anyLong());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deletePlaneById_404WhenIdDoesNotExist() {
        when(planeRepository.getById(anyLong())).thenReturn(null);

        ResponseEntity<Void> response = planeController.deletePlane(anyLong());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
