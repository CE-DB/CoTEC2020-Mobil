package com.example.cotec_2020_app24.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.apollographql.apollo.exception.ApolloNetworkException;
import com.example.ContactsQuery;
import com.example.PatientsQuery;
import com.example.cotec_2020_app24.DatabaseHandler;
import com.example.cotec_2020_app24.GlobalVars;
import com.example.cotec_2020_app24.R;
import com.example.cotec_2020_app24.SyncService;
import com.example.cotec_2020_app24.models.ContactModel;
import com.example.cotec_2020_app24.models.PatientModel;
import com.example.cotec_2020_app24.patients.PatientAdapter;
import com.example.cotec_2020_app24.patients.PatientsView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ContactsView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button syncButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_view);
        recyclerView = findViewById(R.id.contactRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        syncButton = findViewById(R.id.refreshContactsButton);
        GlobalVars.getInstance().setDatabase(new DatabaseHandler(getApplicationContext()));
        DatabaseHandler myDB = GlobalVars.getInstance().getDatabase();
        createNotificationChannel();

        Thread r = new Thread(new SyncService(getApplicationContext()));
        r.start();

        myDB.getWritableDatabase().delete("Patient", null, null);
        myDB.getWritableDatabase().delete("Patient_Pathology", null, null);
        myDB.getWritableDatabase().delete("Patient_Medication", null, null);
        myDB.getWritableDatabase().delete("Patient_Updated", null, null);
        myDB.getWritableDatabase().delete("Updated_Patient_Pathology", null, null);
        myDB.getWritableDatabase().delete("Updated_Patient_Medication", null, null);
        myDB.getWritableDatabase().delete("Patient_Deleted", null, null);
        myDB.getWritableDatabase().delete("Contact", null, null);
        myDB.getWritableDatabase().delete("Contact_Pathology", null, null);
        myDB.getWritableDatabase().delete("Contact_Updated", null, null);
        myDB.getWritableDatabase().delete("Updated_Contact_Pathology", null, null);
        myDB.getWritableDatabase().delete("Contact_Deleted", null, null);
        myDB.getWritableDatabase().delete("Patient_Contact", null, null);
        myDB.getWritableDatabase().delete("Patient_Contact_Updated", null, null);
        myDB.getWritableDatabase().delete("Patient_Contact_Deleted", null, null);

        syncButton.setOnClickListener(v -> updateListView());

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
        List<ContactModel> models = new ArrayList<>();

        GlobalVars.getInstance().getApolloClient().query(ContactsQuery.builder().build())
                .enqueue(new ApolloCall.Callback<ContactsQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<ContactsQuery.Data> response) {

                        if (!response.hasErrors() && response.getData() != null){
                            for (ContactsQuery.Contact p: response.getData().contacts()) {
                                models.add(new ContactModel(p));
                            }

                            runOnUiThread(() -> {
                                ContactAdapter adapter = new ContactAdapter(models);
                                recyclerView.setAdapter(adapter);
                            });
                        } else {

                            runOnUiThread(() -> Toast.makeText(ContactsView.this, "Error retrieving the contacts, please try again.", Toast.LENGTH_SHORT).show());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        runOnUiThread(() -> Toast.makeText(ContactsView.this, "Error retrieving the contacts, please try again.", Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onNetworkError(@NotNull ApolloNetworkException e) {
                        runOnUiThread(() -> Toast.makeText(ContactsView.this, "No network available.", Toast.LENGTH_SHORT).show());
                    }
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