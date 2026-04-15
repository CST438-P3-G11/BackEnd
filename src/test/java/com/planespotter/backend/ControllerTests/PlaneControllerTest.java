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
}
