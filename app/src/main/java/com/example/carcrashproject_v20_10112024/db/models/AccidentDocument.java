package com.example.carcrashproject_v20_10112024.db.models;


public class AccidentDocument {
    private int id;
    private int accidentId;
    private String fileName;

    // Constructors
    public AccidentDocument() { }

    public AccidentDocument(int id, int accidentId, String fileName) {
        this.id = id;
        this.accidentId = accidentId;
        this.fileName = fileName;
    }
    public AccidentDocument(int accidentId, String fileName) {
        this.accidentId = accidentId;
        this.fileName = fileName;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccidentId() {
        return accidentId;
    }

    public void setAccidentId(int accidentId) {
        this.accidentId = accidentId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}

