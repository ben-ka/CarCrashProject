package com.example.carcrashproject_v20_10112024.Data.db.models;


public class AccidentDocument {
    private int id;
    private int accidentId;
    private byte[] fileData;

    // Constructors
    public AccidentDocument() { }

    public AccidentDocument(int id, int accidentId, byte[] fileData) {
        this.id = id;
        this.accidentId = accidentId;
        this.fileData = fileData;
    }
    public AccidentDocument(int accidentId, byte[] fileData) {
        this.accidentId = accidentId;
        this.fileData = fileData;
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

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}

