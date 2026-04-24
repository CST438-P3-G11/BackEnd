package com.planespotter.backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    private String name;
    private String email;
    private Boolean is_admin;

    protected User() {}

    /**
     * Gets the name of the user
     * @return A String, name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the name of the user
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Constructor for the User class
     * @param user_id user ID of the User
     * @param email email of the User
     * @param is_admin admin status of User
     */
    public User(Long user_id, String name, String email, Boolean is_admin) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.is_admin = is_admin;
    }

    /**
     * Getter for User's email
     * @return email of User, a String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the User's email
     * @param email a String of given email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get Admin Status of a User
     * @return a boolean, true if admin, else false
     */
    public Boolean getIs_admin() {
        return is_admin;
    }

    /**
     * Set a User's Admin Status
     * @param is_admin true for admin access, false for regular users
     */
    public void setIs_admin(Boolean is_admin) {
        this.is_admin = is_admin;
    }

    /**
     * Gets the User's ID
     * @return a Long, ID of the User
     */
    public Long getUser_id() {
        return user_id;
    }

}
