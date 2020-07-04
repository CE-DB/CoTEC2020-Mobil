package com.example.cotec_2020_app24.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;

import com.example.cotec_2020_app24.GlobalVars;
import com.example.cotec_2020_app24.R;
import com.example.cotec_2020_app24.patients.PatientPathologyAdapter;

public class ContactPathologies extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_pathologies);
        RecyclerView recyclerView = findViewById(R.id.pathologiesPatientRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        ContactPathologiesAdapter adapter = new ContactPathologiesAdapter(
                GlobalVars.getInstance().getTempContactHolderForView().getPathologies());

        recyclerView.setAdapter(adapter);
    }
}