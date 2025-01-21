package com.example.carcrashproject_v20_10112024.domain.managers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PhoneCallManager {
    private final Context context;
    private final Activity activity;
    private static final String PHONE_NUMBER = "0507741610";

    public PhoneCallManager(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + PHONE_NUMBER));
            context.startActivity(callIntent);
        } else {
            // Request the CALL_PHONE permission
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
    }


}
