package com.example.carcrashproject_v20_10112024.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.carcrashproject_v20_10112024.R;
import com.example.carcrashproject_v20_10112024.logic.CarCrashLogic;

public class CrashDetectionService extends Service implements SensorEventListener{

    private static final String CHANNEL_ID = "CrashDetectionChannel";
    private SensorManager sensorManager;
    private Sensor accelerometer;


    @Override
    public void onCreate() {
        super.onCreate();

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
            Intent crashIntent = new Intent("com.example.CRASH_DETECTED");
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
