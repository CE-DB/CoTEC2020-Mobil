package com.example.cotec_2020_app24.patients;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cotec_2020_app24.GlobalVars;
import com.example.cotec_2020_app24.R;
import com.example.cotec_2020_app24.models.ContactVisit;

import java.util.List;

public class PatientContactUpdate extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_contact_update);
        RecyclerView recyclerView = findViewById(R.id.patientContactsUpdateRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        PatientContactsUpdateAdapter adapter = new PatientContactsUpdateAdapter(
                GlobalVars.getInstance()
                        .getTempPatientHolderForView()
                        .getNewPatient()
                        .getContactVisits(),
                this);

        recyclerView.setAdapter(adapter);

        Button cancel = findViewById(R.id.patientContactsUpdateCancelButton);

        cancel.setOnClickListener((v) -> {


            GlobalVars.getInstance().getTempPatientHolderForView()
                    .getNewPatient()
                    .setContactVisits(GlobalVars.getInstance()
                            .getTempPatientHolderForView().getContactVisits());

            finish();
        });

        Button ok = findViewById(R.id.patientContactsUpdateOkButton);

        ok.setOnClickListener((v) -> {

            for(ContactVisit c: GlobalVars.getInstance()
                    .getTempPatientHolderForView()
                    .getNewPatient()
                    .getContactVisits()) {

                if (c.isAnyEmpty()) {
                    Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            finish();
        });

        Button add = findViewById(R.id.patientContactsUpdateAddButton);

        add.setOnClickListener((v) -> {
            GlobalVars.getInstance()
                    .getTempPatientHolderForView()
                    .getNewPatient()
                    .getContactVisits().add(0,
                        new ContactVisit(GlobalVars.getInstance().getTempPatientHolderForView()
                                .getIdentification()));

            recyclerView.getAdapter().notifyDataSetChanged();

            recyclerView.smoothScrollToPosition(0);
        });



    }
}