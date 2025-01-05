package com.example.carcrashproject_v20_10112024.UI.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.location.LocationListener;

import android.Manifest;

import com.example.carcrashproject_v20_10112024.R;
import com.example.carcrashproject_v20_10112024.Data.db.models.Alarm;
import com.example.carcrashproject_v20_10112024.Data.db.provider.AlarmsTableHelper;
import com.example.carcrashproject_v20_10112024.domain.services.CrashDetectionService;
import com.example.carcrashproject_v20_10112024.domain.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private LocationManager locationManager;
    private AlarmsTableHelper alarmsTableHelper;

    private BroadcastReceiver crashReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.CRASH_DETECTED_ACTION.equals(intent.getAction())) {
                // Crash detected - handle it
                Toast.makeText(context, "Crash detected!", Toast.LENGTH_SHORT).show();
                moveToAccidentDetectedActivity();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize DB helper for alarms
        alarmsTableHelper = new AlarmsTableHelper(this);

        // Register the receiver for crash detection
        IntentFilter filter = new IntentFilter(Constants.CRASH_DETECTED_ACTION);
        registerReceiver(crashReceiver, filter, Context.RECEIVER_NOT_EXPORTED);



        // Initialize LocationManager for location tracking
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        checkAndRequestLocationPermissions();

        // Start the CrashDetectionService
        startCrashDetectionService();

        Button reportAccidentButton = findViewById(R.id.button);
        reportAccidentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToAccidentDetectedActivity();
            }
        });
    }

    private void checkAndRequestLocationPermissions() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1);
        } else {
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, location -> {
                });
            } catch (SecurityException e) {
                Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCrashDetectionService() {
        Intent serviceIntent = new Intent(this, CrashDetectionService.class);
        //startService(serviceIntent);
        ContextCompat.startForegroundService(this, serviceIntent);

        Log.d("CrashDetectionService", "Service started");

    }



    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, request location updates
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                }
            } else {
                // Permission denied
                Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Alarm handleAccidentAlarm() {
        Location location = getLastKnownLocation();
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem")); // Set timezone to Israel
            String currentDateTime = sdf.format(Calendar.getInstance().getTime());


            // Create and insert new alarm
            Alarm newAlarm = new Alarm();
            newAlarm.setLatitude(String.valueOf(latitude));
            newAlarm.setLongitude(String.valueOf(longitude));
            newAlarm.setDateTime(currentDateTime);
            alarmsTableHelper.insertNewAlarm(newAlarm);
            return  newAlarm;
        } else {
            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
        }
        return null;

    }

    // Helper method to get the last known location
    private Location getLastKnownLocation() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions if not granted
            return null;
        }
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    private void moveToAccidentDetectedActivity(){
        Alarm alarm = handleAccidentAlarm();
        Intent intent = new Intent(MainActivity.this, AccidentDetectedActivity.class);
        intent.putExtra(Constants.ALARM_ID_KEY,alarm.getId());
        startActivity(intent);
    }
}
