package com.example.cotec_2020_app24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editUser, editPassword;
        Button signInButton;
        editUser = findViewById(R.id.editUser);
        editPassword = findViewById(R.id.editPassword);
        signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editUser.getText().toString() == "Usuario" && editPassword.getText().toString() == "Contraseña") {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "An Error Occurred!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}