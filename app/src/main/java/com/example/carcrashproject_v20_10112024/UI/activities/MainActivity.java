package com.example.carcrashproject_v20_10112024.UI.activities;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;
import com.example.carcrashproject_v20_10112024.R;
import com.example.carcrashproject_v20_10112024.Data.db.models.Alarm;
import com.example.carcrashproject_v20_10112024.domain.managers.AccidentAlarmManager;
import com.example.carcrashproject_v20_10112024.domain.services.CrashBroadcastReceiver;
import com.example.carcrashproject_v20_10112024.domain.services.CrashDetectionService;
import com.example.carcrashproject_v20_10112024.domain.managers.LocationProvider;
import com.example.carcrashproject_v20_10112024.domain.utils.Constants;

public class MainActivity extends AppCompatActivity {
    private LocationProvider locationProvider;
    private AccidentAlarmManager accidentAlarmManager;
    private CrashBroadcastReceiver crashReceiver;
/*
    public BroadcastReceiver crashReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.CRASH_DETECTED_ACTION.equals(intent.getAction())) {
                // Crash detected - handle it
                Toast.makeText(context, "Crash detected!", Toast.LENGTH_SHORT).show();
                moveToAccidentDetectedActivity();
            }
        }
    };

 */

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

        //creates an instance of AccidentAlarmManager with MainActivity as it's context
        // and checks the permissions
        accidentAlarmManager = new AccidentAlarmManager(this);
        if (!accidentAlarmManager.hasRequiredPermissions()) {
            accidentAlarmManager.requestLocationPermissions(this);
        }


        //creates an instance of LocationProvider with MainActivity as it's context
        // and checks the permissions
        locationProvider = new LocationProvider(this);
        if (!locationProvider.hasLocationPermission()) {
            accidentAlarmManager.requestLocationPermissions(this);
            Log.i("service logs", "service doesn't have permission");

        }

        // Register the receiver for crash detection
        crashReceiver = new CrashBroadcastReceiver(this, accidentAlarmManager);
        IntentFilter filter = new IntentFilter(Constants.CRASH_DETECTED_ACTION);
        registerReceiver(crashReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
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


    private void moveToAccidentDetectedActivity(){
        //unregistering the CrashDetectionService so the second activity
        // will not be called repeatedly
        //unregisterReceiver(crashReceiver);
        Alarm alarm = accidentAlarmManager.createAccidentAlarm();
        if(alarm != null){
            Intent intent = new Intent(MainActivity.this, AccidentDetectedActivity.class);
            intent.putExtra(Constants.ALARM_ID_KEY,alarm.getId());
            startActivity(intent);
        }
    }




/*
    //functions to re - register the service
    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter(Constants.CRASH_DETECTED_ACTION);
        registerReceiver(crashReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Constants.CRASH_DETECTED_ACTION);
        registerReceiver(crashReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
    }
*/

}
