package com.example.cotec_2020_app24.patients;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;

import com.example.cotec_2020_app24.GlobalVars;
import com.example.cotec_2020_app24.R;

public class PatientPathologies extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_pathologies);
        RecyclerView recyclerView = findViewById(R.id.pathologiesPatientRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

            PatientPathologyAdapter adapter = new PatientPathologyAdapter(
                    GlobalVars.getInstance().getTempPatientHolderForView().getPathologies());

            recyclerView.setAdapter(adapter);


    }
}