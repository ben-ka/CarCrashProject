package com.example.carcrashproject_v20_10112024.db.models;


public class Alarm {
    private int id;
    private int entityId;
    private String latitude;
    private String longitude;
    private String dateTime;
    private int alarmOptionId;

    // Constructors
    public Alarm() { }

    public Alarm(int id, int entityId, String latitude, String longitude, String dateTime, int alarmOptionId) {
        this.id = id;
        this.entityId = entityId;

        this.latitude = latitude;
        this.longitude = longitude;
        this.dateTime = dateTime;
        this.alarmOptionId = alarmOptionId;
    }

    public Alarm(int entityId, String latitude, String longitude, String dateTime, int alarmOptionId) {
        this.entityId = entityId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateTime = dateTime;
        this.alarmOptionId = alarmOptionId;
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



    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getAlarmOptionId() {
        return alarmOptionId;
    }

    public void setAlarmOptionId(int alarmOptionId) {
        this.alarmOptionId = alarmOptionId;
    }
}

