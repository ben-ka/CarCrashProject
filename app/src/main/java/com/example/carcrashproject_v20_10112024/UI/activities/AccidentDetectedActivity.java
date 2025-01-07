package com.example.carcrashproject_v20_10112024.UI.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.carcrashproject_v20_10112024.R;
import com.example.carcrashproject_v20_10112024.Data.db.models.Accident;


import android.util.Log;
import android.view.View;

import com.example.carcrashproject_v20_10112024.Data.db.provider.AccidentsTableHelper;
import com.example.carcrashproject_v20_10112024.Data.db.provider.AlarmsTableHelper;
import com.example.carcrashproject_v20_10112024.Data.db.provider.DBHelper;
import com.example.carcrashproject_v20_10112024.domain.utils.Constants;

import com.google.android.gms.location.LocationServices;


public class AccidentDetectedActivity extends AppCompatActivity implements View.OnClickListener {

    private AlarmsTableHelper alarmsTableHelper;
    private AccidentsTableHelper accidentsTableHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accident_detected);

        //set onClickLitseners for all Button Options (AlarmOption)
        Button needAnAmbulance = findViewById(R.id.Option1);
        needAnAmbulance.setOnClickListener(this);
        Button propertyDamage = findViewById(R.id.Option2);
        propertyDamage.setOnClickListener(this);
        Button nothingHappened = findViewById(R.id.Option3);
        nothingHappened.setOnClickListener(this);


        // Initialize AlarmsTableHelper and AccidentTableHelper
        alarmsTableHelper = new AlarmsTableHelper(this);
        accidentsTableHelper = new AccidentsTableHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        DBHelper dbHelper = new DBHelper(AccidentDetectedActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d("MainActivity", "Database accessed");

    }

    @Override
    public void onClick(View view) {
        int alarmId = Integer.parseInt(getIntent().getExtras().get(Constants.ALARM_ID_KEY).toString());

        if(view == findViewById(R.id.Option3)){
            changeAlarmOptionId(3, alarmId);
            Intent intent = new Intent(AccidentDetectedActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(view == findViewById(R.id.Option2)){
            changeAlarmOptionId(2, alarmId);
            // TODO - create an Accident row ✔️
            createNewAccident(alarmId);
        }
        else if(view == findViewById(R.id.Option1)){
            changeAlarmOptionId(1, alarmId);
            // TODO - create an Accident row ✔️
            createNewAccident(alarmId);
        }

    }
    private void createNewAccident(int alarmId){
        Accident accident = new Accident(alarmId);
        accidentsTableHelper.insertNewAccident(accident);
    }
    private void changeAlarmOptionId(int optionId, int alarmId){
        alarmsTableHelper.updateAlarmOption(optionId, alarmId);
    }

}