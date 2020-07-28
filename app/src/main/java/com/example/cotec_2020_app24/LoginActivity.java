package com.example.cotec_2020_app24;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.AuthMutation;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private EditText password;
    private EditText id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.buttonLogIn);
        password = findViewById(R.id.editTextPassword);
        id = findViewById(R.id.editTextIdentification);
        GlobalVars.getInstance().setDatabase(new DatabaseHandler(getApplicationContext()));

        createNotificationChannel();


        Thread r = new Thread(new SyncService(getApplicationContext()));
        r.start();

        login.setOnClickListener(v -> {

            if (id.getText().toString().isEmpty()) {
                id.setError("You must provide an id");
                return;
            }

            if (password.getText().toString().isEmpty()) {
                password.setError("You must provide a password");
                return;
            }

            GlobalVars.getInstance().getApolloClient()
                    .mutate(AuthMutation.builder().id(id.getText().toString()).pass(password.getText().toString()).build())
                    .enqueue(new ApolloCall.Callback<AuthMutation.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<AuthMutation.Data> response) {
                            if (!response.hasErrors() && response.getData() != null) {
                                if(response.getData().authentication().role().equals("hospitalworker")){
                                    runOnUiThread(() -> {
                                        Toast.makeText(LoginActivity.this, "Access granted", Toast.LENGTH_SHORT).show();
                                    });
                                    GlobalVars.getInstance().setAccessKey(null);
                                    Log.d("auth", GlobalVars.getInstance().getAccessKey());

                                    Intent i = new Intent(LoginActivity.this, MainMenu.class);
                                    startActivity(i);


                                } else {
                                    runOnUiThread(() -> {
                                        Toast.makeText(LoginActivity.this, "Role incorrect, you must be a hospital worker.", Toast.LENGTH_SHORT).show();
                                    });
                                    Log.d("auth", response.getData().authentication().role());
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {
                            runOnUiThread(() -> {
                                Toast.makeText(LoginActivity.this, "Error, try again later.", Toast.LENGTH_SHORT).show();
                            });
                        }
                    });


        });


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("01", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}