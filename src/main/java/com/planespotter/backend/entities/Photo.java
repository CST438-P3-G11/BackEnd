package com.planespotter.backend.entities;

import jakarta.persistence.*;

@Entity
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long photo_id;

    private long user_id;
    private long plane_id;

    private String url;

    protected Photo() {}

    /**
     * Public constructor for the Photo entity.
     * @param user_id ID of the User who added the photo.
     * @param plane_id ID of the Plane that the photo is of.
     * @param url URL of the photo.
     */
    public Photo(long user_id, long plane_id, String url) {
        this.user_id = user_id;
        this.plane_id = plane_id;
        this.url = url;
    }

    /**
     * Getter for the photo's ID.
     * @return ID of the Photo.
     */
    public long getPhoto_id() {
        return photo_id;
    }

    /**
     * Getter for the ID of the User who added the Photo.
     * @return User ID of submitting User.
     */
    public long getUser_id() {
        return user_id;
    }

    /**
     * Getter for the ID of the plane the photo is of.
     * @return ID of the plane in the photo.
     */
    public long getPlane_id() {
        return plane_id;
    }

    /**
     * Gets the URL of the photo.
     * @return  URL of the photo.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter for the ID of the plane the photo is of.
     * @param plane_id New ID of the plane.
     */
    public void setPlane_id(long plane_id) {
        this.plane_id = plane_id;
    }

    /**
     * Setter for the URL of the photo.
     * @param url URL to update.
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
