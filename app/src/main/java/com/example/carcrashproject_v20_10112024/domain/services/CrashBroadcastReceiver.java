package com.example.carcrashproject_v20_10112024.domain.services;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.carcrashproject_v20_10112024.Data.db.models.Alarm;
import com.example.carcrashproject_v20_10112024.domain.managers.AccidentAlarmManager;
import com.example.carcrashproject_v20_10112024.domain.utils.Constants;
import com.example.carcrashproject_v20_10112024.UI.activities.AccidentDetectedActivity;


public class CrashBroadcastReceiver extends BroadcastReceiver {
    private final Context context;
    private final AccidentAlarmManager accidentAlarmManager;

    public CrashBroadcastReceiver(Context context, AccidentAlarmManager accidentAlarmManager) {
        this.context = context;
        this.accidentAlarmManager = accidentAlarmManager;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Constants.CRASH_DETECTED_ACTION.equals(intent.getAction())) {
            if (!isAccidentDetectedActivityActive()) {
                moveToAccidentDetectedActivity();
            } else {
                // Optionally, handle the broadcast differently
                Log.d("CrashReceiver", "AccidentDetectedActivity is already active.");
            }
        }
    }
    private void moveToAccidentDetectedActivity() {
        // Create an alarm
        Alarm alarm = accidentAlarmManager.createAccidentAlarm();
        if (alarm != null) {
            Intent intent = new Intent(context, com.example.carcrashproject_v20_10112024.UI.activities.AccidentDetectedActivity.class);
            intent.putExtra(Constants.ALARM_ID_KEY, alarm.getId());

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


            context.startActivity(intent);
        } else {
            Log.e("moveToAccidentDetected", "Alarm creation failed.");
        }
    }
    private boolean isAccidentDetectedActivityActive() {
        // Use an appropriate method to check if the activity is active.
        // This could be a shared preference, a static flag, or ActivityManager.
        return AccidentDetectedActivity.isActive;
    }
}
