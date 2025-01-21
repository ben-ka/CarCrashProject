package com.example.carcrashproject_v20_10112024.UI.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;

public class AlarmDocumentActivity extends AppCompatActivity {
    //TODO - check what information I need after a "property damage crash"
    private int accidentId;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private CameraManager cameraManager;
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
        accidentId = Integer.parseInt(getIntent().getExtras().get(Constants.ACCIDENT_ID_KEY).toString());
        cameraManager = new CameraManager(AlarmDocumentActivity.this, accidentId, this);
        // Button to launch the camera
        Button btnAddImage = findViewById(R.id.add_image_button);
        btnAddImage.setOnClickListener(view -> {
            cameraManager.openCamera();
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