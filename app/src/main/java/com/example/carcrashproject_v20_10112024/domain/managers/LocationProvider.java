package com.example.carcrashproject_v20_10112024.domain.managers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.content.ContextCompat;

public class LocationProvider {
    private final Context context;

    public LocationProvider(Context context) {
        this.context = context;
    }

    /**
     * Checks if location permissions are granted.
     *
     * @return true if both fine and coarse location permissions are granted, false otherwise.
     */
    public boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Retrieves the last known location if permissions are granted.
     *
     * @return The last known location, or null if permissions are missing or the location is unavailable.
     */
    public Location getCurrentLocation() {
        if (!hasLocationPermission()) {
            throw new SecurityException("Location permissions are not granted.");
        }

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            try {
                // Try to retrieve the last known location from both GPS and Network providers
                Location gpsLocation = null;
                Location networkLocation = null;

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                // Return the more accurate and recent location, if available
                if (gpsLocation != null && networkLocation != null) {
                    return (gpsLocation.getTime() > networkLocation.getTime()) ? gpsLocation : networkLocation;
                }
                return (gpsLocation != null) ? gpsLocation : networkLocation;
            } catch (SecurityException e) {
                // Log or handle the exception (permissions revoked during runtime)
                e.printStackTrace();
            }
        }
        return null;
    }
}
