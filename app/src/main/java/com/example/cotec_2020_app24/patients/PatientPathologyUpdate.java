package com.example.cotec_2020_app24.patients;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.cotec_2020_app24.GlobalVars;
import com.example.cotec_2020_app24.R;

import java.util.List;

public class PatientPathologyUpdate extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_pathology_update);
        RecyclerView recyclerView = findViewById(R.id.patientPathologiesUpdatedRecylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        PatientPathologyUpdateAdapter adapter = new PatientPathologyUpdateAdapter(
                GlobalVars.getInstance()
                        .getTempPatientHolderForView()
                        .getNewPatient()
                        .getPathologyNames(),
                GlobalVars.getInstance()
                        .getTempPatientHolderForView());

        recyclerView.setAdapter(adapter);

        Button add = findViewById(R.id.patientPathologiesUpdateAddButton);

        add.setOnClickListener((v) -> {
            GlobalVars.getInstance()
                    .getTempPatientHolderForView()
                    .getNewPatient()
                    .getPathologyNames().add(0, "");

            recyclerView.getAdapter().notifyDataSetChanged();

            recyclerView.smoothScrollToPosition(0);
        });

        Button close = findViewById(R.id.patientPathologiesUpdateCloseButton);

        close.setOnClickListener((v) -> {

            List<String> path = GlobalVars.getInstance()
                    .getTempPatientHolderForView()
                    .getNewPatient()
                    .getPathologyNames();

            for (String s: path) {
                if (s.length() == 0) {
                    Toast.makeText(this, "All spaces have to be filled.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            finish();

        });
    }

    @Override
    public void onBackPressed() {
        List<String> path = GlobalVars.getInstance()
                .getTempPatientHolderForView()
                .getNewPatient()
                .getPathologyNames();

        for (String s: path) {
            if (s.length() == 0) {
                Toast.makeText(this, "All spaces have to be filled.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        finish();

        super.onBackPressed();
    }
}