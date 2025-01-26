package com.example.carcrashproject_v20_10112024.domain.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.carcrashproject_v20_10112024.R;
import com.example.carcrashproject_v20_10112024.UI.activities.AccidentDetectedActivity;
import com.example.carcrashproject_v20_10112024.UI.activities.MainActivity;
import com.example.carcrashproject_v20_10112024.domain.logic.CarCrashLogic;
import com.example.carcrashproject_v20_10112024.domain.managers.AccidentAlarmManager;
import com.example.carcrashproject_v20_10112024.domain.utils.Constants;

public class CrashDetectionService extends Service implements SensorEventListener{

    private static final String CHANNEL_ID = "CrashDetectionChannel";
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private AccidentAlarmManager accidentAlarmManager;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Your crash detection logic here
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        Log.i("service logs", "service onStart");

        return START_STICKY;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("service logs", "service started");
        accidentAlarmManager = new AccidentAlarmManager(this);

        // Create notification for foreground service
        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Crash Detection Active")
                .setContentText("Monitoring for potential crashes.")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();
        startForeground(1, notification);

        // Initialize accelerometer
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Crash Detection",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void showCrashNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Create the notification channel (for Android O and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Crash Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }
        /*
        // Create an intent to open AccidentDetectedActivity
        Intent intent = new Intent(this, AccidentDetectedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


         */
        if(!AccidentDetectedActivity.isActive){
            Intent intent = accidentAlarmManager.moveToAccidentDetectedActivity();
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            // Build the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_background) // Replace with your app's icon
                    .setContentTitle("Accident Detected")
                    .setContentText("Tap to respond to the accident.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            // Show the notification
            notificationManager.notify(1, builder.build());
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // checks if the algorithm in te logic deems the sensorEvent as a crash
        if(CarCrashLogic.checkCrash(sensorEvent)){
            // Notify the app about the crash

            showCrashNotification();
            Intent crashIntent = new Intent(Constants.CRASH_DETECTED_ACTION);
            sendBroadcast(crashIntent);

        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // TODO - change the logic sensitivity based on the accuracy of the sensors
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }




}
