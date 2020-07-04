package com.example.cotec_2020_app24.patients;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.apollographql.apollo.exception.ApolloNetworkException;
import com.example.PatientsQuery;
import com.example.cotec_2020_app24.DatabaseHandler;
import com.example.cotec_2020_app24.GlobalVars;
import com.example.cotec_2020_app24.PatientActivity;
import com.example.cotec_2020_app24.R;
import com.example.cotec_2020_app24.SyncService;
import com.example.cotec_2020_app24.models.PatientModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PatientsView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button syncButton;
    private Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_view);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        syncButton = findViewById(R.id.syncPatientsButton);
        DatabaseHandler myDB = GlobalVars.getInstance().getDatabase();
        create = findViewById(R.id.createPatientsButton);

        create.setOnClickListener(v -> {

            Intent i = new Intent(PatientsView.this, PatientActivity.class);
            startActivity(i);

        });

        syncButton.setOnClickListener(v -> {
          updateListView();
        });

        updateListView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerView.getAdapter() != null){
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    private void updateListView(){

        recyclerView.removeAllViews();
        List<PatientModel> models = new ArrayList<>();

        GlobalVars.getInstance().getApolloClient().query(PatientsQuery.builder().build())
                .enqueue(new ApolloCall.Callback<PatientsQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<PatientsQuery.Data> response) {

                        if (!response.hasErrors() && response.getData() != null){
                            for (PatientsQuery.Patient p: response.getData().patients()) {

                                models.add(new PatientModel(p));
                            }

                            runOnUiThread(() -> {
                                PatientAdapter adapter = new PatientAdapter(models, getApplicationContext());
                                recyclerView.setAdapter(adapter);
                            });
                        } else {

                            runOnUiThread(() -> Toast.makeText(PatientsView.this, "Error retrieving the patients, please try again.", Toast.LENGTH_SHORT).show());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        runOnUiThread(() -> Toast.makeText(PatientsView.this, "Error retrieving the patients, please try again.", Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onNetworkError(@NotNull ApolloNetworkException e) {
                        runOnUiThread(() -> Toast.makeText(PatientsView.this, "No network available.", Toast.LENGTH_SHORT).show());
                    }
                });
    }


}