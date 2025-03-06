package com.example.carcrashproject_v20_10112024.domain.managers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carcrashproject_v20_10112024.Data.db.models.AccidentDocument;
import com.example.carcrashproject_v20_10112024.Data.db.provider.AccidentDocumentsTableHelper;

import java.io.ByteArrayOutputStream;

public class CameraManager  {
    private final Context context;
    private final AppCompatActivity activity;
    private final ActivityResultLauncher<Intent> cameraLauncher;
    private int accidentId;
    private AccidentDocumentsTableHelper documentsTableHelper;
    private AccidentDocument document;

    public CameraManager(Context context, int accidentId, AppCompatActivity activity, AccidentDocument document) {
        this.context = context;
        this.accidentId = accidentId;
        this.activity = activity;
        this.document = document;
        this.cameraLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        handleCameraResult(result.getData());
                    } else {
                        Toast.makeText(activity, "Camera canceled or failed.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        documentsTableHelper = new AccidentDocumentsTableHelper(context);
    }
    public void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(context.getPackageManager()) != null) {
            cameraLauncher.launch(cameraIntent);
        } else {
            Toast.makeText(context, "Camera is not available", Toast.LENGTH_SHORT).show();
        }
    }
    private void handleCameraResult(Intent data) {
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        if (photo != null) {
            saveImageToDocument(photo);
        } else {
            Toast.makeText(context, "Failed to capture image.", Toast.LENGTH_SHORT).show();
        }
    }
    private void saveImageToDocument(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageBytes = stream.toByteArray();

        document.setFileData(imageBytes);
        Toast.makeText(context, "Image saved successfully!", Toast.LENGTH_SHORT).show();
    }
    public Bitmap displayImageFromDatabase(int documentId) {
        // Retrieve the document from the database
        AccidentDocument document = documentsTableHelper.getAccidentDocumentById(documentId);

        if (document != null) {
            byte[] fileData = document.getFileData();

            // Convert byte[] to Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(fileData, 0, fileData.length);
            return bitmap;

        } else {
            Toast.makeText(activity,"No image found for this document!", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
