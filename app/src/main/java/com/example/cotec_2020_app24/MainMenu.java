package com.example.cotec_2020_app24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.cotec_2020_app24.contacts.ContactsView;
import com.example.cotec_2020_app24.patients.PatientsView;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Button pacientes = findViewById(R.id.pacientesboton);
        Button contactos = findViewById(R.id.contactosboton);

        pacientes.setOnClickListener(v -> {

            Intent i = new Intent(MainMenu.this, PatientsView.class);
            startActivity(i);

        });

        contactos.setOnClickListener(v -> {

            Intent i = new Intent(MainMenu.this, ContactsView.class);
            startActivity(i);

        });
    }
}