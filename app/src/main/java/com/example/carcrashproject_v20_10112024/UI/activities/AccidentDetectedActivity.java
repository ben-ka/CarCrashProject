package com.example.carcrashproject_v20_10112024.UI.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.carcrashproject_v20_10112024.R;
import com.example.carcrashproject_v20_10112024.Data.db.models.Accident;


import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.carcrashproject_v20_10112024.Data.db.provider.AccidentsTableHelper;
import com.example.carcrashproject_v20_10112024.Data.db.provider.AlarmsTableHelper;
import com.example.carcrashproject_v20_10112024.Data.db.provider.DBHelper;
import com.example.carcrashproject_v20_10112024.domain.services.CrashBroadcastReceiver;
import com.example.carcrashproject_v20_10112024.domain.utils.Constants;

public class AccidentDetectedActivity extends AppCompatActivity implements View.OnClickListener {

    private AlarmsTableHelper alarmsTableHelper;
    private AccidentsTableHelper accidentsTableHelper;
    private CrashBroadcastReceiver crashReceiver;
    public static boolean isActive = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isActive = true;

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accident_detected);

        //set onClickLitseners for all Button Options (AlarmOption)
        Button needAnAmbulance = findViewById(R.id.option1);
        needAnAmbulance.setOnClickListener(this);
        Button propertyDamage = findViewById(R.id.option2);
        propertyDamage.setOnClickListener(this);
        Button nothingHappened = findViewById(R.id.option3);
        nothingHappened.setOnClickListener(this);



        // Initialize AlarmsTableHelper and AccidentTableHelper
        alarmsTableHelper = new AlarmsTableHelper(this);
        accidentsTableHelper = new AccidentsTableHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onClick(View view) {
   //     int alarmId = Integer.parseInt(getIntent().getExtras().get(Constants.ALARM_ID_KEY).toString());
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(Constants.ALARM_ID_KEY)) {
            int alarmId = extras.getInt(Constants.ALARM_ID_KEY, -1); // Default to -1 if key is missing
            if (alarmId != -1) {
                // Use alarmId here
                // nothing happened option
                if(view == findViewById(R.id.option3)){
                    changeAlarmOptionId(3, alarmId);
                    Intent intent = new Intent(AccidentDetectedActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                // property damage option
                else if(view == findViewById(R.id.option2)){
                    changeAlarmOptionId(2, alarmId);
                    // TODO - create an Accident row ✔️
                    Accident accident = createNewAccident(alarmId);
                    moveToAlarmDocumentActivity(accident);

                }

                //call an ambulance option
                else if(view == findViewById(R.id.option1)){
                    changeAlarmOptionId(1, alarmId);
                    // TODO - create an Accident row ✔️
                    createNewAccident(alarmId);
                    makePhoneCall("0507741610");

                }
            } else {
                Log.e("AccidentDetectedActivity", "Alarm ID is invalid.");
            }
        } else {
            Log.e("AccidentDetectedActivity", "No extras or ALARM_ID_KEY not found.");
        }


    }
    private Accident createNewAccident(int alarmId){
        Accident accident = new Accident(alarmId);
        accidentsTableHelper.insertNewAccident(accident);
        return accident;
    }

    private void moveToAlarmDocumentActivity(Accident accident)
    {
        Intent intent = new Intent(AccidentDetectedActivity.this, AlarmDocumentActivity.class);
        intent.putExtra(Constants.ACCIDENT_ID_KEY, accident.getId());
        startActivity(intent);
    }

    private void changeAlarmOptionId(int optionId, int alarmId){
        alarmsTableHelper.updateAlarmOption(optionId, alarmId);
    }

    private void makePhoneCall(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        } else {
            // Request the CALL_PHONE permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted. Tap the button again to make a call.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied. Cannot make a call.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActive = false;
    }
}