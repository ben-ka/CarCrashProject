package com.example.carcrashproject_v20_10112024.UI.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.carcrashproject_v20_10112024.Data.db.models.AccidentDocument;
import com.example.carcrashproject_v20_10112024.Data.db.provider.AccidentDocumentsTableHelper;
import com.example.carcrashproject_v20_10112024.R;
import com.example.carcrashproject_v20_10112024.domain.managers.CameraManager;
import com.example.carcrashproject_v20_10112024.domain.utils.Constants;
import com.example.carcrashproject_v20_10112024.domain.utils.NavigationUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;

public class AlarmDocumentActivity extends AppCompatActivity {
    //TODO - check what information I need after a "property damage crash"
    private int accidentId;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private CameraManager cameraManager;
    private AccidentDocumentsTableHelper accidentDocumentsTableHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alarm_document);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        accidentDocumentsTableHelper = new AccidentDocumentsTableHelper(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavigationUtil navigationUtil = new NavigationUtil(bottomNavigationView, this  , AlarmDocumentActivity.this);
        navigationUtil.NavigateActivities();



        accidentId = Integer.parseInt(getIntent().getExtras().get(Constants.ACCIDENT_ID_KEY).toString());
        AccidentDocument document = new AccidentDocument();
        document.setAccidentId(accidentId);
        cameraManager = new CameraManager(AlarmDocumentActivity.this, accidentId, this, document);
        // Button to launch the camera
        Button btnAddImage = findViewById(R.id.addImageButton);
        btnAddImage.setOnClickListener(view -> {
            cameraManager.openCamera();
        });

        // questions
        CheckBox questionInjury = findViewById(R.id.cbInjury);
        CheckBox questionVehicleDamage = findViewById(R.id.cbVehicleDamage);
        CheckBox questionGuilty = findViewById(R.id.cbGuilt);
        EditText vehiclesCountET = findViewById(R.id.etVehiclesCount);
        ImageButton increaseButton = findViewById(R.id.btnIncreaseVehicles);
        ImageButton decreaseButton = findViewById(R.id.btnDecreaseVehicles);


        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentCount = Integer.parseInt(vehiclesCountET.getText().toString());
                currentCount++;
                vehiclesCountET.setText(String.valueOf(currentCount));
            }
        });
        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentCount = Integer.parseInt(vehiclesCountET.getText().toString());
                if (currentCount > 1) {
                    currentCount--;
                    vehiclesCountET.setText(String.valueOf(currentCount));
                }
            }
        });

        Button btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                document.setInjured(questionInjury.isChecked());
                document.setVehicleDamaged(questionVehicleDamage.isChecked());
                document.setGuilty(questionGuilty.isChecked());
                document.setNumberOfCarsInvolved(Integer.valueOf(String.valueOf(vehiclesCountET.getText())));

                accidentDocumentsTableHelper.insertAccidentDocument(document);

                if(document.getNumberOfCarsInvolved() == 0){
                    Intent intent = new Intent(AlarmDocumentActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                //Intent intent = new Intent(AlarmDocumentActivity.this, InvolvedDriversActivity.class);
                Intent intent = new Intent(AlarmDocumentActivity.this, MainActivity.class);
                intent.putExtra(Constants.ACCIDENT_ID_KEY, document.getAccidentId());
                intent.putExtra(Constants.VEHICLE_COUNT_KEY, document.getNumberOfCarsInvolved());
                intent.putExtra(Constants.DRIVER_INDEX_KEY, 1);
                startActivity(intent);
            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraManager.openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to take photos.", Toast.LENGTH_SHORT).show();
            }
        }
    }


}