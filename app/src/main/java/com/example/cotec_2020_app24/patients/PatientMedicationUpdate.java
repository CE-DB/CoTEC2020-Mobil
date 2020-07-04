package com.example.cotec_2020_app24.patients;

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

public class PatientMedicationUpdate extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_medication_update);
        RecyclerView recyclerView = findViewById(R.id.patientMedicationsUpdatedRecylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        PatientMedicationUpdateAdapter adapter = new PatientMedicationUpdateAdapter(
                GlobalVars.getInstance()
                        .getTempPatientHolderForView()
                        .getNewPatient()
                        .getMedicationNames(),
                GlobalVars.getInstance()
                        .getTempPatientHolderForView());

        recyclerView.setAdapter(adapter);

        Button add = findViewById(R.id.patientMedicationsUpdateAddButton);

        add.setOnClickListener((v) -> {
            GlobalVars.getInstance()
                    .getTempPatientHolderForView()
                    .getNewPatient()
                    .getMedicationNames().add(0, "");

            recyclerView.getAdapter().notifyDataSetChanged();

            recyclerView.smoothScrollToPosition(0);
        });

        Button close = findViewById(R.id.patientMedicationsUpdateCloseButton);

        close.setOnClickListener((v) -> {

            List<String> path = GlobalVars.getInstance()
                    .getTempPatientHolderForView()
                    .getNewPatient()
                    .getMedicationNames();

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
                .getMedicationNames();

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