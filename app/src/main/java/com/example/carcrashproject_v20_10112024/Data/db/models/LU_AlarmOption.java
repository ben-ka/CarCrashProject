package com.example.carcrashproject_v20_10112024.Data.db.models;



public class LU_AlarmOption {
    private int id;
    private String description;

    // Constructors
    public LU_AlarmOption() { }

    public LU_AlarmOption(int id, String description) {
        this.id = id;
        this.description = description;
    }
    public LU_AlarmOption(String description) {

        this.description = description;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

