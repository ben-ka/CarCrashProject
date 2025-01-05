package com.example.carcrashproject_v20_10112024.db.models;


public class Entity {
    private int id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String carNumber;
    private String identification;

    // Constructors
    public Entity() { }

    public Entity(int id, String firstName, String lastName, String phoneNumber, String email, String carNumber, String identification) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.carNumber = carNumber;
        this.identification = identification;
    }
    public Entity(String firstName, String lastName, String phoneNumber, String email, String carNumber, String identification) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.carNumber = carNumber;
        this.identification = identification;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }
}
