package com.planespotter.backend.entities;

import jakarta.persistence.*;

@Entity
public class Plane {
    @Id
    @GeneratedValue
    private long plane_id;

    private long user_id;
    private String name;

    protected Plane() {}

    /**
     * Constructor for the Plane class
     * @param user_id user ID of the user that added the plane
     * @param name Name of the plane
     */
    public Plane(long user_id, String name) {
        this.user_id = user_id;
        this.name = name;
    }

    /**
     * Getter for the Plane's ID
     * @return Plane's ID in the DB
     */
    public long getPlane_id() {
        return plane_id;
    }

    /**
     * Getter for the user ID who added the plane
     * @return ID of the user who added the plane
     */
    public long getUser_id() {
        return user_id;
    }

    /**
     * Getter for the name of the plane
     * @return Name of the plane
     */
    public String getName() {
        return name;
    }

    /**
     * Setter to set the name of the plane
     * @param name New name of the plane
     */
    public void setName(String name) {
        this.name = name;
    }


}
