package com.example.carcrashproject_v20_10112024.domain.logic;

import android.hardware.SensorEvent;

public class CarCrashLogic {
    // this constant checks how sensitive the algorithm is.
    // Meaning the higher the constant the more extreme the movement
    // ought to be in order to be detected as a crash by the algorithm.
    //change the constant as you see fit.
    public static final float CRASH_THRESHOLD = 40.0f;

    public static boolean checkCrash(SensorEvent event)
    {
        // Get the x, y, z values of the accelerometer
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // Calculate the overall acceleration (magnitude of the vector)
        // by calculating the square root of the sum of squares of x,y,z.
        //algorithm should be tweaked in the future.
        float acceleration = (float) Math.sqrt(x * x + y * y + z * z);

        // Check if the acceleration exceeds the crash threshold
        if (acceleration > CRASH_THRESHOLD) {
            return true;
        }
        return false;
    }
}
