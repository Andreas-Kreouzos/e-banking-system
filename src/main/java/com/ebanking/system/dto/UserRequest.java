package com.ebanking.system.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Representation class that holds the details to create a new user
 */
public class UserRequest {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String afm;
    @NotBlank
    private String birthDate;
    @NotBlank
    private String mobileNumber;
    @NotBlank
    private String cardNumber;

    public UserRequest() {
    }

    public UserRequest(String firstName, String lastName, String email,
                       String password, String afm, String birthDate, String mobileNumber, String cardNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.afm = afm;
        this.birthDate = birthDate;
        this.mobileNumber = mobileNumber;
        this.cardNumber = cardNumber;
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
     * @return The afm
     */
    public String getAfm() {
        return afm;
    }

    /**
     * Sets the value of afm
     *
     * @param afm The new value to set
     */
    public void setAfm(String afm) {
        this.afm = afm;
    }

    /**
     * @return The birthDate
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the value of birthDate
     *
     * @param birthDate The new value to set
     */
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * @return The mobileNumber
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Sets the value of mobileNumber
     *
     * @param mobileNumber The new value to set
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    /**
     * @return The cardNumber
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Sets the value of cardNumber
     *
     * @param cardNumber The new value to set
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "UserRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", afm='" + afm + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                '}';
    }
}
