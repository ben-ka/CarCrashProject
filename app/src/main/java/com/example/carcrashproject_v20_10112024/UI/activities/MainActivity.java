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
import androidx.core.app.ActivityCompat;
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
import com.example.carcrashproject_v20_10112024.domain.services.LocationProvider;
import com.example.carcrashproject_v20_10112024.domain.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {



    private AlarmsTableHelper alarmsTableHelper;
    private LocationProvider locationProvider;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static final String GPS_LOGGING_CHANNEL_NAME = "GPS logging channel";

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

        //creates an instance of LocationProvider with MainActivity as it's context
        locationProvider = new LocationProvider(this);
        if (!locationProvider.hasLocationPermission()) {
            requestLocationPermissions();

        }



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

    private void startCrashDetectionService() {
        Intent serviceIntent = new Intent(this, CrashDetectionService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
        Log.d("CrashDetectionService", "Service started");
    }

    private void requestLocationPermissions() {
        requestPermissions(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, LOCATION_PERMISSION_REQUEST_CODE);
    }


    private void checkAndRequestLocationPermissions() {
        if (!locationProvider.hasLocationPermission()) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, request location updates
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Permission denied
                Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Alarm handleAccidentAlarm() {
        Location location = getLocation();
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Log.i(GPS_LOGGING_CHANNEL_NAME, String.format("the device latitude is: %f. the device longitude is: %f",latitude, longitude));
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

    private Location getLocation() {
        if (!locationProvider.hasLocationPermission()) {
            checkAndRequestLocationPermissions();
            return null;
        }
        try {
            return locationProvider.getCurrentLocation();
        } catch (SecurityException e) {
            Toast.makeText(this, "Failed to get location: Permissions are not granted.", Toast.LENGTH_SHORT).show();
            return null;
        }
    }


    private void moveToAccidentDetectedActivity(){
        //unregistering the CrashDetectionService so the second activity
        // will not be called repeatedly
        unregisterReceiver(crashReceiver);
        Alarm alarm = handleAccidentAlarm();
        if(alarm != null){
            Intent intent = new Intent(MainActivity.this, AccidentDetectedActivity.class);
            intent.putExtra(Constants.ALARM_ID_KEY,alarm.getId());
            startActivity(intent);
        }

    }

    //functions to reregister the service

    @Override
    protected void onStart() {
        super.onStart();
        // Register the receiver for crash detection
        IntentFilter filter = new IntentFilter(Constants.CRASH_DETECTED_ACTION);
        registerReceiver(crashReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the receiver for crash detection
        IntentFilter filter = new IntentFilter(Constants.CRASH_DETECTED_ACTION);
        registerReceiver(crashReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
    }
}
