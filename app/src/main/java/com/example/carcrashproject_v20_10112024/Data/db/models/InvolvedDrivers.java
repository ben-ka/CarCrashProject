package com.example.carcrashproject_v20_10112024.Data.db.models;

public class InvolvedDrivers {
    private int id;
    private int accidentId;
    private String driverName;
    private String phoneNumber;
    private String licensePlate;

    public InvolvedDrivers(int id, int accidentId, String driverName, String phoneNumber, String licensePlate) {
        this.id = id;
        this.accidentId = accidentId;
        this.driverName = driverName;
        this.phoneNumber = phoneNumber;
        this.licensePlate = licensePlate;
    }

    public InvolvedDrivers(int accidentId, String driverName, String phoneNumber, String licensePlate) {
        this.accidentId = accidentId;
        this.driverName = driverName;
        this.phoneNumber = phoneNumber;
        this.licensePlate = licensePlate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public int getAccidentId() {
        return accidentId;
    }

    public void setAccidentId(int accidentId) {
        this.accidentId = accidentId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
