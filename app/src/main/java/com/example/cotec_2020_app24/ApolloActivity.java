package com.example.cotec_2020_app24;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.apollographql.apollo.ApolloClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class ApolloActivity extends AppCompatActivity {
    private ApolloClient apolloClient;
    DatabaseHandler myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apollo);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        apolloClient = GlobalVars.getInstance().getApolloClient();
        myDB = new DatabaseHandler(this);

        //myDB.onUpgrade(myDB.getWritableDatabase(), 0, 0);
        //myDB.onCreate(myDB.getWritableDatabase());


        //myDB.getWritableDatabase().delete("Patient", null, null);

        /*List<String> patientsId = new ArrayList<>();

        patientsId.add("1234567");



        for (String id: patientsId) {
            boolean inserted = myDB.insertCreatedPatient(id,
                    "Alejandro",
                    "Diaz",
                    "CR",
                    "San Jose",
                    "1",
                    43,
                    "1",
                    "Recovered",
                    "2020-06-07",
                    "Costa Rica, Republic of");

            myDB.insertCreatedPatientPathology("Sarapion", id);
            myDB.insertCreatedPatientPathology("Feer", id);

            if (inserted){
                //Toast.makeText(this, "Patient with id 7534890 inserted", Toast.LENGTH_SHORT).show();
                Log.d("BACKGROUND_HILO", String.format("Patient with id %s inserted", id));
            } else {
                Log.d("BACKGROUND_HILO", String.format("Patient with id %s not inserted", id));
                //Toast.makeText(this, "Patient with id 7534890 not inserted", Toast.LENGTH_SHORT).show();
            }
        }*/

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

        List<String> patientsId = new ArrayList<>();

        patientsId.add("423678923");

        myDB.insertCreatedPatient("543890H",
                "Alejandro",
                "Diaz",
                "CR",
                "San Jose",
                "1",
                43,
                "1",
                "Infected",
                "2020-05-06",
                "Costa Rica, Republic of");

        myDB.insertCreatedPatientPathology("Fever", "543890H");
        myDB.insertCreatedPatientPathology("Safa", "543890H");

        /*boolean inserted = myDB.insertCreatedPatient("523468976r",
                "Alejandro",
                "Diaz",
                "CR",
                "San Jose",
                "1",
                43,
                "1",
                "Infected",
                "2020-05-06",
                "Costa Rica, Republic of");

        myDB.insertCreatedPatientPathology("Fever", "523468976r");

        myDB.insertUpdatedContacts("423678923", null, null, "Gutierrez", null, null,null, null, null, null);

        myDB.insertDeletedContactVisit("123454321", "11111110", "1804-07-11");*/

        //myDB.insertDeletedPatients("523468976r");

        //myDB.insertUpdatedPatients("523468976r", null, null, null, null, null, null, null, null, null, null, null);


        /*for (String id: patientsId) {
            boolean inserted = myDB.insertDeletedContactVisit("1234", id, "2020-05-04");
            *//*boolean inserted = myDB.insertUpdatedContactVisit("1234", id, "2020-05-04",
                    "1234", null, null);*//*

            if (inserted){
                //Toast.makeText(this, "Patient with id 7534890 inserted", Toast.LENGTH_SHORT).show();
                Log.d("BACKGROUND_HILO", String.format("Contact with id %s inserted", id));
            } else {
                Log.d("BACKGROUND_HILO", String.format("Contact with id %s not inserted", id));
                //Toast.makeText(this, "Patient with id 7534890 not inserted", Toast.LENGTH_SHORT).show();
            }*/

            /*myDB.insertUpdatedContactPathology("Fever", id);
            myDB.insertUpdatedContactPathology("Sarampion", id);*/

        /*myDB.getWritableDatabase().delete("Contact", null, null);
        myDB.getWritableDatabase().delete("Patient", null, null);




        List<String> patientsId = new ArrayList<>();

        patientsId.add("4579079");
        patientsId.add("65452343");



        for (String id: patientsId) {
            boolean inserted = myDB.insertPatient(id,
                    "Alejandro",
                    "Diaz",
                    "CR",
                    "San Jose",
                    "1",
                    "43",
                    "1",
                    null,
                    null,
                    "Infected",
                    null);

            if (inserted){
                //Toast.makeText(this, "Patient with id 7534890 inserted", Toast.LENGTH_SHORT).show();
                Log.d("BACKGROUND_HILO", String.format("Patient with id %s inserted", id));
            } else {
                Log.d("BACKGROUND_HILO", String.format("Patient with id %s inserted", id));
                //Toast.makeText(this, "Patient with id 7534890 not inserted", Toast.LENGTH_SHORT).show();
            }
        }

        List<String> ids = new ArrayList<>();

        ids.add("6546345");
        ids.add("76574467");


        for (String id: ids) {
            boolean inserted = myDB.insertCreatedContact(id,
                    "Alejandro",
                    "Diaz",
                    "CR",
                    "San Jose",
                    "Calle",
                    "loco@lol.com",
                    "15",
                    null,
                    null,
                    null);

            if (inserted){
                //Toast.makeText(this, "Patient with id 7534890 inserted", Toast.LENGTH_SHORT).show();
                Log.d("BACKGROUND_HILO", String.format("Contact with id %s inserted", id));
            } else {
                Log.d("BACKGROUND_HILO", String.format("Contact with id %s not inserted", id));
                //Toast.makeText(this, "Patient with id 7534890 not inserted", Toast.LENGTH_SHORT).show();
            }
        }*/

    }


}