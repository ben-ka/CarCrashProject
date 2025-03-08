package com.example.carcrashproject_v20_10112024.UI.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.carcrashproject_v20_10112024.Data.db.models.InvolvedDriver;
import com.example.carcrashproject_v20_10112024.Data.db.provider.InvolvedDriversTableHelper;
import com.example.carcrashproject_v20_10112024.R;
import com.example.carcrashproject_v20_10112024.domain.utils.Constants;

public class InvolvedDriversActivity extends AppCompatActivity {
    private InvolvedDriversTableHelper involvedDriversTableHelper;
    private EditText nameEditText, phoneEditText, carNumberEditText;
    private Button btnContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_involved_drivers);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        involvedDriversTableHelper = new InvolvedDriversTableHelper(this);
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        carNumberEditText = findViewById(R.id.carNumberEditText);
        btnContinue = findViewById(R.id.btnContinue);

        btnContinue.setEnabled(false); // Initially disabled

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                checkFields();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        };

        nameEditText.addTextChangedListener(textWatcher);
        phoneEditText.addTextChangedListener(textWatcher);
        carNumberEditText.addTextChangedListener(textWatcher);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int accidentId = Integer.parseInt(getIntent().getExtras().get(Constants.ACCIDENT_ID_KEY).toString());
                String name = nameEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();
                String carNumber = carNumberEditText.getText().toString().trim();
                InvolvedDriver driver = new InvolvedDriver(accidentId, name, phone, carNumber);
                involvedDriversTableHelper.insertInvolvedDriver(driver);
                handleNavigationToNextActivity();
            }
        });
    }

    private void handleNavigationToNextActivity() {
        int vehicleCount = Integer.parseInt(getIntent().getExtras().get(Constants.VEHICLE_COUNT_KEY).toString());
        int currentDriverIndex = Integer.parseInt(getIntent().getExtras().get(Constants.DRIVER_INDEX_KEY).toString());
        if (vehicleCount == currentDriverIndex) {
            Intent intent = new Intent(InvolvedDriversActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(InvolvedDriversActivity.this, InvolvedDriversActivity.class);
            intent.putExtra(Constants.ACCIDENT_ID_KEY, Integer.parseInt(getIntent().getExtras().get(Constants.ACCIDENT_ID_KEY).toString()));
            intent.putExtra(Constants.VEHICLE_COUNT_KEY, vehicleCount);
            intent.putExtra(Constants.DRIVER_INDEX_KEY, currentDriverIndex + 1);
            startActivity(intent);
        }
    }

    private void checkFields() {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String carNumber = carNumberEditText.getText().toString().trim();

        // Enable the button only if all fields are filled
        btnContinue.setEnabled(!name.isEmpty() && !phone.isEmpty() && !carNumber.isEmpty());
    }

}