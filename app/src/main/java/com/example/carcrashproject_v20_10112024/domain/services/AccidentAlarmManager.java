package com.example.carcrashproject_v20_10112024.domain.services;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.carcrashproject_v20_10112024.Data.db.models.Alarm;
import com.example.carcrashproject_v20_10112024.Data.db.provider.AlarmsTableHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class AccidentAlarmManager {
    private static final String GPS_LOGGING_CHANNEL_NAME = "GPS logging channel";
    private final Context context;
    private final LocationProvider locationProvider;
    private final AlarmsTableHelper alarmsTableHelper;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public AccidentAlarmManager(Context context) {
        this.context = context;
        this.locationProvider = new LocationProvider(context);
        this.alarmsTableHelper = new AlarmsTableHelper(context);
    }

    public Alarm createAccidentAlarm() {
        try {
            Location location = locationProvider.getCurrentLocation();
            if (location != null) {
                return createAndSaveAlarm(location);
            } else {
                Toast.makeText(context, "Location not available", Toast.LENGTH_SHORT).show();
                return null;
            }
        } catch (SecurityException e) {
            Toast.makeText(context, "Failed to get location: Permissions are not granted.", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private Alarm createAndSaveAlarm(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Log.i(GPS_LOGGING_CHANNEL_NAME,
                String.format("Device location - latitude: %f, longitude: %f",
                        latitude, longitude));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
        String currentDateTime = sdf.format(Calendar.getInstance().getTime());

        Alarm newAlarm = new Alarm();
        newAlarm.setLatitude(String.valueOf(latitude));
        newAlarm.setLongitude(String.valueOf(longitude));
        newAlarm.setDateTime(currentDateTime);

        alarmsTableHelper.insertNewAlarm(newAlarm);
        return newAlarm;
    }

    public boolean hasRequiredPermissions() {
        return locationProvider.hasLocationPermission();
    }

    public void requestLocationPermissions(Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                LOCATION_PERMISSION_REQUEST_CODE
        );
    }
}
