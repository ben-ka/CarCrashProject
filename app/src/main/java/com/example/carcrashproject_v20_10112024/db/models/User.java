package com.example.carcrashproject_v20_10112024.db.models;
public class User {
    private int id;
    private int entityId;
    private String password;

    // Constructors
    public User() { }

    public User(int id, int entityId, String password) {
        this.id = id;
        this.entityId = entityId;
        this.password = password;
    }
    public User(int entityId, String password) {
        this.entityId = entityId;
        this.password = password;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}