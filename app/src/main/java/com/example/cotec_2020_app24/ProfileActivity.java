package com.example.cotec_2020_app24;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    private Button patientsButton, contactsButton, visitsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        patientsButton = findViewById(R.id.patientsButton);
        contactsButton = findViewById(R.id.contactsButton);
        visitsButton = findViewById(R.id.visitsButton);
        patientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, PatientActivity.class));
                finish();
            }
        });
        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ContactActivity.class));
                finish();
            }
        });
        visitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ApolloActivity.class));
                finish();
            }
        });
    }
}