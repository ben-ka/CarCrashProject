package com.example.carcrashproject_v20_10112024.domain.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.carcrashproject_v20_10112024.R;
import com.example.carcrashproject_v20_10112024.UI.activities.AccidentArchiveActivity;
import com.example.carcrashproject_v20_10112024.UI.activities.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationUtil {
    private  BottomNavigationView bottomNavigationView;
    private Activity activity;
    private Context context;

    public NavigationUtil(BottomNavigationView bottomNavigationView, Activity activity, Context context){
        this.bottomNavigationView = bottomNavigationView;
        this.activity = activity;
        this.context = context;
    }
    public void NavigateActivities(){
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent;
            if (item.getItemId() == R.id.nav_archive) {
                if (!(activity instanceof AccidentArchiveActivity)){
                    Log.i("Menu log", "archive pressed");
                    intent = new Intent(context, AccidentArchiveActivity.class);
                    context.startActivity(intent);

                }
                return true;
            }


            else if (item.getItemId() == R.id.nav_home) {
                // Handle the Home action
                if (!(activity instanceof MainActivity)){
                    intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                }
                return true;
            }
            else{
                return false;
            }
    });
}
}

