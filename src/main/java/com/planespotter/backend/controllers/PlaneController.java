package com.planespotter.backend.controllers;

import com.planespotter.backend.entities.Plane;
import com.planespotter.backend.repositories.PlaneRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planes")
public class PlaneController {

    private final PlaneRepository planeRepository;

    PlaneController(PlaneRepository planeRepository) {
        this.planeRepository = planeRepository;
    }

    @GetMapping("/getByName")
    public ResponseEntity<List<Plane>> getPlaneByName(@RequestParam("name") String name) {
        List<Plane> planes = planeRepository.getByName(name);
        if (planes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(planes);
    }

    @GetMapping("/getById")
    public ResponseEntity<Plane> getPlaneById(@RequestParam("plane_id") long plane_id) {
        Plane plane = planeRepository.getById(plane_id);
        if (plane == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(plane);
    }

    @PostMapping("/addPlane")
    public ResponseEntity<Plane> addPlane(@RequestBody Plane plane) {
        planeRepository.insertPlane(plane);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/updatePlane")
    public ResponseEntity<Plane> updatePlane(@RequestBody Plane plane) {
        if (!planeRepository.existsById(plane.getPlane_id())) {
            return ResponseEntity.notFound().build();
        }
        planeRepository.updatePlaneById(plane.getPlane_id(), plane.getName());
        return ResponseEntity.ok(plane);
    }
}
