package com.example.carcrashproject_v20_10112024.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.location.LocationListener;

import android.Manifest;
import com.example.carcrashproject_v20_10112024.R;
import com.example.carcrashproject_v20_10112024.db.models.Alarm;
import com.example.carcrashproject_v20_10112024.db.provider.AlarmsTableHelper;
import com.example.carcrashproject_v20_10112024.db.provider.LU_AlarmOptionsTableHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private LocationManager locationManager;
    private static final float CRASH_THRESHOLD = 10.0f; // Adjust this based on testing

    private AlarmsTableHelper alarmsTableHelper;


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


        // Initialize SensorManager and accelerometer
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        // Register the listener
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "Accelerometer not available", Toast.LENGTH_SHORT).show();
        }

        // Initialize LocationManager for location tracking
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Check location permissions and request if needed
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1);
        } else {
            // Permissions already granted, request location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }


        Button reportAccidentButton = findViewById(R.id.button);
        reportAccidentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToAccidentDetectedActivity();
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Get the x, y, z values of the accelerometer
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // Calculate the overall acceleration (magnitude of the vector)
        float acceleration = (float) Math.sqrt(x * x + y * y + z * z);

        // Check if the acceleration exceeds the crash threshold
        if (acceleration > CRASH_THRESHOLD) {
            moveToAccidentDetectedActivity();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

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
        intent.putExtra("AlarmId",alarm.getId());
        startActivity(intent);
    }
}
