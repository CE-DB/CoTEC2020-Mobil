package com.example.cotec_2020_app24.patients;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;

import com.example.cotec_2020_app24.GlobalVars;
import com.example.cotec_2020_app24.R;

public class PatientContacts extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_contacts);
        RecyclerView recyclerView = findViewById(R.id.contactsPatientRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        PatientContactsAdapter adapter = new PatientContactsAdapter(
                GlobalVars.getInstance().getTempPatientHolderForView().getContactVisits());
        recyclerView.setAdapter(adapter);

    }
}