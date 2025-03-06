package com.example.carcrashproject_v20_10112024.Data.db.models;


public class AccidentDocument {
    private int id;
    private int accidentId;
    private boolean isInjured;
    private boolean isVehicleDamaged;
    private boolean isGuilty;
    private int numberOfCarsInvolved;
    private byte[] fileData;

    // Constructors
    public AccidentDocument() { }

    public AccidentDocument(int id, int accidentId, byte[] fileData, boolean isInjured, boolean isVehicleDamaged, boolean isGuilty, int numberOfCarsInvolved) {
        this.id = id;
        this.accidentId = accidentId;
        this.fileData = fileData;
        this.isInjured = isInjured;
        this.isVehicleDamaged = isVehicleDamaged;
        this.isGuilty = isGuilty;
        this.numberOfCarsInvolved = numberOfCarsInvolved;

    }
    public AccidentDocument(int accidentId, byte[] fileData, boolean isInjured, boolean isVehicleDamaged, boolean isGuilty, int numberOfCarsInvolved) {
        this.accidentId = accidentId;
        this.fileData = fileData;
        this.isInjured = isInjured;
        this.isVehicleDamaged = isVehicleDamaged;
        this.numberOfCarsInvolved = numberOfCarsInvolved;
        this.isGuilty = isGuilty;
    }

    public AccidentDocument(int accidentId, boolean isInjured, boolean isVehicleDamaged, boolean isGuilty, int numberOfCarsInvolved){
        this.accidentId = accidentId;
        this.isInjured = isInjured;
        this.isVehicleDamaged = isVehicleDamaged;
        this.isGuilty = isGuilty;
        this.numberOfCarsInvolved = numberOfCarsInvolved;

    }
    public AccidentDocument(int id, int accidentId, byte[] fileData) {
        this.id = id;
        this.accidentId = accidentId;
        this.fileData = fileData;

    }

    public boolean getInjured() {
        return isInjured;
    }

    public void setInjured(boolean injured) {
        isInjured = injured;
    }

    public boolean getVehicleDamaged() {
        return isVehicleDamaged;
    }

    public void setVehicleDamaged(boolean vehicleDamaged) {
        isVehicleDamaged = vehicleDamaged;
    }

    public boolean getGuilty() {
        return isGuilty;
    }

    public void setGuilty(boolean guilty) {
        isGuilty = guilty;
    }

    public int getNumberOfCarsInvolved() {
        return numberOfCarsInvolved;
    }

    public void setNumberOfCarsInvolved(int numberOfCarsInvolved) {
        this.numberOfCarsInvolved = numberOfCarsInvolved;
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

