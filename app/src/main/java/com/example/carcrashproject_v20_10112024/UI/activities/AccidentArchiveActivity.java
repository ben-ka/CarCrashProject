package com.example.carcrashproject_v20_10112024.UI.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carcrashproject_v20_10112024.Data.db.models.Accident;
import com.example.carcrashproject_v20_10112024.Data.db.provider.AccidentDocumentsTableHelper;
import com.example.carcrashproject_v20_10112024.Data.db.provider.AccidentsTableHelper;
import com.example.carcrashproject_v20_10112024.R;
import com.example.carcrashproject_v20_10112024.domain.managers.AccidentArchiveAdapter;

import java.util.ArrayList;
import java.util.List;

public class AccidentArchiveActivity extends AppCompatActivity {
    private RecyclerView rvAccidents;
    private AccidentArchiveAdapter adapter;
    private AccidentsTableHelper accidentsTableHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_archive);
        rvAccidents = findViewById(R.id.rvAccidents);
        accidentsTableHelper = new AccidentsTableHelper(this);
        ArrayList<Accident> accidentList = accidentsTableHelper.retrieveAllAccidents();
        adapter = new AccidentArchiveAdapter(this, accidentList);
        rvAccidents.setLayoutManager(new LinearLayoutManager(this));
        rvAccidents.setAdapter(adapter);
    }
}