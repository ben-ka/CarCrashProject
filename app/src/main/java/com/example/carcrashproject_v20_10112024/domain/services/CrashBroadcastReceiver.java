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
                accidentAlarmManager.moveToAccidentDetectedActivity();
            } else {
                // Optionally, handle the broadcast differently
                Log.d("CrashReceiver", "AccidentDetectedActivity is already active.");
            }
        }
    }

    private boolean isAccidentDetectedActivityActive() {
        // Use an appropriate method to check if the activity is active.
        // This could be a shared preference, a static flag, or ActivityManager.
        return AccidentDetectedActivity.isActive;
    }
}
