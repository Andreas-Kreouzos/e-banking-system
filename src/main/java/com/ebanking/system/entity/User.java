package com.ebanking.system.entity;

/**
 * Represents the user entity of the application that gets stored in the database
 */
public class User {

    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;

    public User() {
    }

    public User(Long id, String username, String password, String firstName, String lastName, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * @return The id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the value of id
     *
     * @param id The new value to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of username
     *
     * @param username The new value to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of password
     *
     * @param password The new value to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return The firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of firstName
     *
     * @param firstName The new value to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return The lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of lastName
     *
     * @param lastName The new value to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of email
     *
     * @param email The new value to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
