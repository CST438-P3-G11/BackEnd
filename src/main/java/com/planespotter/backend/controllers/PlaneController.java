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

    /**
     * GET endpoint to get a list of planes with the matching name i.e. "Boeing" would return 737, 747, 757, etc...
     * @param name Part or whole name of the plane to match.
     * @return List of planes matching the provided name, or 404s if no matches are found.
     */
    @GetMapping("/getByName")
    public ResponseEntity<List<Plane>> getPlaneByName(@RequestParam("name") String name) {
        List<Plane> planes = planeRepository.getByName(name);
        if (planes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(planes);
    }

    /**
     * GET endpoint to get a single plane by its ID.
     * @param plane_id ID of the plane to get.
     * @return Plane with the given ID, or 404s if no such plane exists.
     */
    @GetMapping("/getById")
    public ResponseEntity<Plane> getPlaneById(@RequestParam("plane_id") long plane_id) {
        Plane plane = planeRepository.getById(plane_id);
        if (plane == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(plane);
    }

    /**
     * POST endpoint to add a plane to the database.
     * @param plane Plane object to add to the database.
     * @return HTTP.CREATED after the plane is added.
     */
    @PostMapping("/addPlane")
    public ResponseEntity<Plane> addPlane(@RequestBody Plane plane) {
        planeRepository.insertPlane(plane);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * PATCH endpoint to update the name of a plane.
     * @param id ID of the plane to update.
     * @param plane Plane object to update.
     * @return 404 if no plane with the given ID exists, or 200 if the plane is successfully updated.
     */
    @PatchMapping("/updatePlane/{id}")
    public ResponseEntity<Plane> updatePlane(@PathVariable("id") long id, @RequestBody Plane plane) {
        if (!planeRepository.existsById(plane.getPlane_id())) {
            return ResponseEntity.notFound().build();
        }
        planeRepository.updatePlaneById(plane.getPlane_id(), plane.getName());
        return ResponseEntity.ok(plane);
    }

    /**
     * DELETE endpoint to delete the plane with a given ID.
     * @param id ID of the plane to delete.
     * @return 404 if no plane with the given ID exists, or no content if the plane is successfully deleted.
     */
    @DeleteMapping("/deletePlane/{id}")
    public ResponseEntity<Void> deletePlane(@PathVariable("id") long id) {
        if (planeRepository.getById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        planeRepository.deletePlaneById(id);
        return ResponseEntity.noContent().build();
    }
}
