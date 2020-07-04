package com.example.cotec_2020_app24.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.cotec_2020_app24.GlobalVars;
import com.example.cotec_2020_app24.R;
import com.example.cotec_2020_app24.patients.PatientPathologyUpdateAdapter;

import java.util.List;

public class ContactPathologyUpdate extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_pathology_update);
        RecyclerView recyclerView = findViewById(R.id.patientPathologiesUpdatedRecylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        ContactPathologyUpdateAdapter adapter = new ContactPathologyUpdateAdapter(
                GlobalVars.getInstance()
                        .getTempContactHolderForView()
                        .getNewContact()
                        .getPathologyNames(),
                GlobalVars.getInstance()
                        .getTempContactHolderForView());

        recyclerView.setAdapter(adapter);

        Button add = findViewById(R.id.patientPathologiesUpdateAddButton);

        add.setOnClickListener((v) -> {
            GlobalVars.getInstance()
                    .getTempContactHolderForView()
                    .getNewContact()
                    .getPathologyNames().add(0, "");

            recyclerView.getAdapter().notifyDataSetChanged();

            recyclerView.smoothScrollToPosition(0);
        });

        Button close = findViewById(R.id.patientPathologiesUpdateCloseButton);

        close.setOnClickListener((v) -> {

            List<String> path = GlobalVars.getInstance()
                    .getTempContactHolderForView()
                    .getNewContact()
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
                .getTempContactHolderForView()
                .getNewContact()
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