package com.example.cotec_2020_app24;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Error;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloCanceledException;
import com.apollographql.apollo.exception.ApolloException;
import com.apollographql.apollo.exception.ApolloHttpException;
import com.apollographql.apollo.exception.ApolloNetworkException;
import com.apollographql.apollo.exception.ApolloParseException;
import com.example.AddContactVisitMutation;
import com.example.AddPatientMutation;
import com.example.CreateContactsMutation;
import com.example.DeleteContactVisitMutation;
import com.example.DeleteContactsMutation;
import com.example.DeletePatientsMutation;
import com.example.UpdateContactVisitMutation;
import com.example.UpdateContactsMutation;
import com.example.UpdatePatientsMutation;
import com.example.type.CreateContactInput;
import com.example.type.CreatePatientInput;
import com.example.type.UpdateContactInput;
import com.example.type.UpdateContactVisitInput;
import com.example.type.UpdatePatientInput;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SyncService implements Runnable {

    private Context context;
    DatabaseHandler myDB;
    ApolloClient apolloClient;
    NotificationManagerCompat notificationManager;
    NotificationCompat.Builder builder;
    private final int TAG = 54389;
    private int totalData;
    private int progressData;

    public SyncService(Context context) {
        this.context = context;
        this.apolloClient = GlobalVars.getInstance().getApolloClient();
        this.myDB = GlobalVars.getInstance().getDatabase();
        this.notificationManager = NotificationManagerCompat.from(context);
        this.builder = new NotificationCompat.Builder(context, "01");
        this.totalData = 0;
        this.progressData = 0;
    }

    @Override
    public void run() {

        while(!Thread.currentThread().isInterrupted()) {
            builder.setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("CoTEC App")
                .setContentText("Syncing records")
                    .setProgress(0, 0, true)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(TAG, builder.build());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (GlobalVars.getInstance().getAccessKey() == null){

            ConnectivityManager cm =
                    (ConnectivityManager) context
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkCapabilities cp = cm.getNetworkCapabilities(cm.getActiveNetwork());
            boolean connected = false;

            Cursor createdPatientsCur = myDB.getAllCreatedPatients();
            Cursor updatedPatientsCur = myDB.getAllUpdatedPatients();
            Cursor deletedPatientsCur = myDB.getAllDeletedPatients();
            Cursor createdContactsCur = myDB.getAllCreatedContacts();
            Cursor updatedContactsCur = myDB.getAllUpdatedContacts();
            Cursor deletedContactsCur = myDB.getAllDeletedContacts();
            Cursor createdContactVisitCur = myDB.getAllCreatedContactVisit();
            Cursor updatedContactVisitCur = myDB.getAllUpdatedContactVisit();
            Cursor deletedContactVisitCur = myDB.getAllDeletedContactVisit();

            totalData = createdPatientsCur.getCount() +
                    updatedPatientsCur.getCount() +
                    deletedPatientsCur.getCount() +
                    createdContactsCur.getCount() +
                    updatedContactsCur.getCount() +
                    deletedContactsCur.getCount() +
                    createdContactVisitCur.getCount() +
                    updatedContactVisitCur.getCount() +
                    deletedContactVisitCur.getCount();

            if (cp != null) {
                cp.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
                connected = true;
            }

            if (connected) {
                boolean nothingToSync = true;


                if (createdPatientsCur != null && createdPatientsCur.getCount() > 0){

                    syncCreatedPatients(createdPatientsCur);
                    nothingToSync = false;
                }

                if (updatedPatientsCur != null && updatedPatientsCur.getCount() > 0){
                    syncUpdatedPatients(updatedPatientsCur);
                    nothingToSync = false;
                }

                if (deletedPatientsCur != null && deletedPatientsCur.getCount() > 0){
                    syncDeletedPatients(deletedPatientsCur);
                    nothingToSync = false;
                }

                if (createdContactsCur != null && createdContactsCur.getCount() > 0){
                    syncCreatedContacts(createdContactsCur);
                    nothingToSync = false;
                }

                if (updatedContactsCur != null && updatedContactsCur.getCount() > 0){
                    syncUpdatedContacts(updatedContactsCur);
                    nothingToSync = false;
                }

                if (deletedContactsCur != null && deletedContactsCur.getCount() > 0){
                    syncDeletedContacts(deletedContactsCur);
                    nothingToSync = false;
                }

                if (createdContactVisitCur != null && createdContactVisitCur.getCount() > 0){
                    syncCreatedContactVisit(createdContactVisitCur);
                    nothingToSync = false;
                }

                if (updatedContactVisitCur != null && updatedContactVisitCur.getCount() > 0){
                    syncUpdatedContactVisit(updatedContactVisitCur);
                    nothingToSync = false;
                }

                if (deletedContactVisitCur != null && deletedContactVisitCur.getCount() > 0){
                    syncDeletedContactVisit(deletedContactVisitCur);
                    nothingToSync = false;
                }

                if (nothingToSync){

                    builder.setContentText("Nothing to sync")
                        .setProgress(0, 0, false);
                    notificationManager.notify(TAG, builder.build());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                builder.setContentText("The phone is not connected to network.")
                        .setProgress(0, 0, false);;
                notificationManager.notify(TAG, builder.build());

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } else {

            builder.setContentText("The app needs a logged account to sync with server, please sign in")
                    .setProgress(0, 0, false);;
            notificationManager.notify(TAG, builder.build());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

            notificationManager.cancel(TAG);

            this.notificationManager = NotificationManagerCompat.from(context);
            this.builder = new NotificationCompat.Builder(context, "01");
            this.totalData = 0;
            this.progressData = 0;


            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void syncCreatedPatients(Cursor createdPatientsCur) {

        while (createdPatientsCur.moveToNext()){

            progressData += 1;

            String id = String.valueOf(createdPatientsCur.getString(createdPatientsCur.getColumnIndex(DatabaseHandler.COL_1_1)));


            CreatePatientInput p = CreatePatientInput.builder()
                    .age(createdPatientsCur.getInt(createdPatientsCur.getColumnIndex(DatabaseHandler.COL_7_1)))
                    .country(createdPatientsCur.getString(createdPatientsCur.getColumnIndex(DatabaseHandler.COL_13_1)))
                    .dateEntrance(createdPatientsCur.getString(createdPatientsCur.getColumnIndex(DatabaseHandler.COL_15_1)))
                    .firstName(createdPatientsCur.getString(createdPatientsCur.getColumnIndex(DatabaseHandler.COL_2_1)))
                    .hospitalized(createdPatientsCur.getString(createdPatientsCur.getColumnIndex(DatabaseHandler.COL_8_1)).equals("true"))
                    .identification(id)
                    .intensiveCareUnite(createdPatientsCur.getString(createdPatientsCur.getColumnIndex(DatabaseHandler.COL_6_1)).equals("true"))
                    .lastName(createdPatientsCur.getString(createdPatientsCur.getColumnIndex(DatabaseHandler.COL_3_1)))
                    .nationality(createdPatientsCur.getString(createdPatientsCur.getColumnIndex(DatabaseHandler.COL_4_1)))
                    .region(createdPatientsCur.getString(createdPatientsCur.getColumnIndex(DatabaseHandler.COL_5_1)))
                    .state(createdPatientsCur.getString(createdPatientsCur.getColumnIndex(DatabaseHandler.COL_11_1)))
                    .build();

            String s = String.format("Syncing created patients (%s de %s)", createdPatientsCur.getPosition()+1, createdPatientsCur.getCount());

            builder.setContentText(s)
                    .setProgress(totalData, progressData, false)
                    .setOngoing(true)
                    .build();
            notificationManager.notify(TAG, builder.build());

            apolloClient.mutate(AddPatientMutation.builder().p(p).build())
                    .enqueue(new ApolloCall.Callback<AddPatientMutation.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<AddPatientMutation.Data> response) {

                            if (response.hasErrors()){

                                HashMap<String, String> defaultError = new HashMap<>();
                                defaultError.put("code", "Unknown");
                                defaultError.put("DatabaseMessage", "Unknown");

                                for (Error e: Objects.requireNonNull(response.getErrors())) {

                                    HashMap<String, String> extensions = (HashMap<String, String>) e.getCustomAttributes().getOrDefault("extensions", defaultError);

                                    String title = String
                                            .format("Sync error (Code: %s)", extensions.getOrDefault("code", "Unknown"));

                                    String bigMessage = "Server: " + e.getMessage() + "\n" +
                                            "Database: " +
                                            extensions
                                                    .getOrDefault("DatabaseMessage",
                                                            "Database has no message");

                                    builder.setContentTitle(title)
                                            .setContentText(String.format("Patient: %s", id))
                                            .setProgress(0, 0, false)
                                            .setOngoing(false)
                                            .setStyle(new NotificationCompat.BigTextStyle()
                                                    .setBigContentTitle(String.format("Patient: %s", id))
                                                    .bigText(bigMessage));

                                    notificationManager.notify(p.hashCode(), builder.build());

                                    if (extensions.getOrDefault("code", "Unknown").equals("50006")){
                                        Integer deleted = myDB.deleteCreatedPatients(id);

                                        if (deleted > 0) {
                                            Log.d("BACKGROUND_HILO", String.format("Patient %s deleted locally after error.", id));
                                        } else {
                                            Log.d("BACKGROUND_HILO", String.format("Patient %s not deleted locally after error.", id));
                                        }
                                    }
                                }
                            } else {
                                String idResult = response.getData().addPatient().identification();

                                List<String> pathologies = new ArrayList<>();

                                Cursor pathCur = myDB.getAllCreatedPatientsPathology();

                                while (pathCur.moveToNext()){
                                    String patient = pathCur.getString(1);

                                    if (patient.equals(idResult)){
                                        pathologies.add(pathCur.getString(0));
                                    }
                                }

                                if (pathologies.size() > 0) {

                                    for (String pathology : pathologies) {

                                        UpdatePatientInput input = UpdatePatientInput.builder()
                                                .pathologies(new ArrayList<>(Collections.singleton(pathology)))
                                                .build();

                                        apolloClient.mutate(UpdatePatientsMutation.builder().idToUpdate(idResult).input(input).build())
                                                .enqueue(new ApolloCall.Callback<UpdatePatientsMutation.Data>() {
                                                    @Override
                                                    public void onResponse(@NotNull Response<UpdatePatientsMutation.Data> response) {
                                                        if (response.hasErrors()){
                                                            HashMap<String, String> defaultError = new HashMap<>();
                                                            defaultError.put("code", "Unknown");
                                                            defaultError.put("DatabaseMessage", "Unknown");

                                                            for (Error e: Objects.requireNonNull(response.getErrors())) {

                                                                HashMap<String, String> extensions = (HashMap<String, String>) e.getCustomAttributes().getOrDefault("extensions", defaultError);

                                                                String title = String
                                                                        .format("Sync error (Code: %s)", extensions.getOrDefault("code", "Unknown"));

                                                                String bigMessage = "Server: " + e.getMessage() + "\n" +
                                                                        "Database: " +
                                                                        extensions
                                                                                .getOrDefault("DatabaseMessage",
                                                                                        "Database has no message");

                                                                builder.setContentTitle(title)
                                                                        .setContentText(String.format("Patient created: %s", id))
                                                                        .setProgress(0, 0, false)
                                                                        .setOngoing(false)
                                                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                                                .setBigContentTitle(String.format("Pathology %s of new patient: %s", pathology, id))
                                                                                .bigText(bigMessage));

                                                                notificationManager.notify(input.hashCode(), builder.build());

                                                                if (extensions.getOrDefault("code", "Unknown").equals("2627")){
                                                                    Integer deleted = myDB.deleteCreatedPatientsPathology(pathology, idResult);

                                                                    if (deleted > 0) {
                                                                        Log.d("BACKGROUND_HILO", String.format("Patient %s created pathology %s deleted locally after error.", idResult, pathology));
                                                                    } else {
                                                                        Log.d("BACKGROUND_HILO", String.format("Patient %s created pathology %s not deleted locally after error.", idResult, pathology));
                                                                    }
                                                                } else {
                                                                    boolean patient = myDB.insertUpdatedPatients(idResult, null, null, null,null,null,null,null,null,null, null, null);
                                                                    boolean p = myDB.insertUpdatedPatientPathology(pathology, idResult);

                                                                    Log.d("BACKGROUND_HILO_passed_CREATEDPAITENTPATHOLOGY", String.valueOf(patient));
                                                                    Log.d("BACKGROUND_HILO_passed_CREATEDPAITENTPATHOLOGY", String.valueOf(p));

                                                                    Integer deleted = myDB.deleteCreatedPatientsPathology(pathology, idResult);

                                                                }
                                                            }
                                                        } else {
                                                            Integer deleted = myDB.deleteCreatedPatientsPathology(pathology, idResult);

                                                            if (deleted > 0) {
                                                                Log.d("BACKGROUND_HILO", String.format("Patient %s created pathology %s deleted locally after error.", idResult, pathology));
                                                            } else {
                                                                Log.d("BACKGROUND_HILO", String.format("Patient %s created pathology %s not deleted locally after error.", idResult, pathology));
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(@NotNull ApolloException e) {
                                                        builder.setContentTitle("Sync Error")
                                                                .setContentText(String.format("Error for created patient : %s", id))
                                                                .setProgress(0, 0, false)
                                                                .setOngoing(false)
                                                                .setStyle(new NotificationCompat.BigTextStyle()
                                                                        .setBigContentTitle(String.format("Error in pathology: %s", pathology))
                                                                        .bigText(e.getMessage()));

                                                        notificationManager.notify(pathology.hashCode(), builder.build());
                                                    }

                                                    @Override
                                                    public void onHttpError(@NotNull ApolloHttpException e) {
                                                        Log.d("BACKGROUND_HILO_HttpFailure", e.message());

                                                        builder.setContentTitle("Sync Error")
                                                                .setContentText("Error connecting to server")
                                                                .setProgress(0, 0, false)
                                                                .setOngoing(false)
                                                                .setStyle(new NotificationCompat.BigTextStyle()
                                                                        .setBigContentTitle("Error connecting to server")
                                                                        .bigText(String.format("Error %s: %s", e.code(), e.message())));

                                                        notificationManager.notify(e.code(), builder.build());
                                                        e.rawResponse().close();
                                                    }

                                                    @Override
                                                    public void onNetworkError(@NotNull ApolloNetworkException e) {
                                                        //Log.d("BACKGROUND_HILO_NetworkFailure", e.getMessage());

                                                        builder.setContentTitle("Sync Error")
                                                                .setContentText("Failed to connect to server.")
                                                                .setProgress(0, 0, false)
                                                                .setOngoing(false)
                                                                .setStyle(new NotificationCompat.BigTextStyle()
                                                                        .setBigContentTitle("Failed to connect to server.")
                                                                        .bigText(e.getMessage()));

                                                        notificationManager.notify(239487562, builder.build());
                                                    }

                                                    @Override
                                                    public void onParseError(@NotNull ApolloParseException e) {
                                                        Log.d("BACKGROUND_HILO_ParseFailure", e.getMessage());

                                                    }

                                                    @Override
                                                    public void onCanceledError(@NotNull ApolloCanceledException e) {
                                                        Log.d("BACKGROUND_HILO_CanceledEvent", e.getMessage());
                                                    }
                                                });
                                    }
                                }



                                List<String> medications = new ArrayList<>();

                                Cursor medicationCur = myDB.getAllCreatedPatientsMedication();

                                while (medicationCur.moveToNext()){
                                    String patient = medicationCur.getString(1);

                                    if (patient.equals(idResult)){
                                        medications.add(medicationCur.getString(0));
                                    }
                                }

                                if (medications.size() > 0) {

                                    for (String medication : medications) {

                                        UpdatePatientInput input = UpdatePatientInput.builder()
                                                .medication(new ArrayList<>(Collections.singleton(medication)))
                                                .build();

                                        apolloClient.mutate(UpdatePatientsMutation.builder().idToUpdate(idResult).input(input).build())
                                                .enqueue(new ApolloCall.Callback<UpdatePatientsMutation.Data>() {
                                                    @Override
                                                    public void onResponse(@NotNull Response<UpdatePatientsMutation.Data> response) {
                                                        if (response.hasErrors()){
                                                            HashMap<String, String> defaultError = new HashMap<>();
                                                            defaultError.put("code", "Unknown");
                                                            defaultError.put("DatabaseMessage", "Unknown");

                                                            for (Error e: Objects.requireNonNull(response.getErrors())) {

                                                                HashMap<String, String> extensions = (HashMap<String, String>) e.getCustomAttributes().getOrDefault("extensions", defaultError);

                                                                String title = String
                                                                        .format("Sync error (Code: %s)", extensions.getOrDefault("code", "Unknown"));

                                                                String bigMessage = "Server: " + e.getMessage() + "\n" +
                                                                        "Database: " +
                                                                        extensions
                                                                                .getOrDefault("DatabaseMessage",
                                                                                        "Database has no message");

                                                                builder.setContentTitle(title)
                                                                        .setContentText(String.format("Patient created: %s", id))
                                                                        .setProgress(0, 0, false)
                                                                        .setOngoing(false)
                                                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                                                .setBigContentTitle(String.format("Medication %s of new patient: %s", medication, id))
                                                                                .bigText(bigMessage));

                                                                notificationManager.notify(input.hashCode(), builder.build());

                                                                if (extensions.getOrDefault("code", "Unknown").equals("2627")){
                                                                    Integer deleted = myDB.deleteCreatedPatientsMedication(medication, idResult);

                                                                    if (deleted > 0) {
                                                                        Log.d("BACKGROUND_HILO", String.format("Patient %s created medication %s deleted locally after error.", idResult, medication));
                                                                    } else {
                                                                        Log.d("BACKGROUND_HILO", String.format("Patient %s created medication %s not deleted locally after error.", idResult, medication));
                                                                    }
                                                                } else {
                                                                    boolean patient = myDB.insertUpdatedPatients(idResult, null, null, null,null,null,null,null,null,null, null, null);
                                                                    boolean p = myDB.insertUpdatedPatientMedication(medication, idResult);

                                                                    Log.d("BACKGROUND_HILO_passed_CREATEDPAITENTPATHOLOGY", String.valueOf(patient));
                                                                    Log.d("BACKGROUND_HILO_passed_CREATEDPAITENTPATHOLOGY", String.valueOf(p));

                                                                    Integer deleted = myDB.deleteCreatedPatientsMedication(medication, idResult);

                                                                }
                                                            }
                                                        } else {
                                                            Integer deleted = myDB.deleteCreatedPatientsMedication(medication, idResult);

                                                            if (deleted > 0) {
                                                                Log.d("BACKGROUND_HILO", String.format("Patient %s created medication %s deleted locally after error.", idResult, medication));
                                                            } else {
                                                                Log.d("BACKGROUND_HILO", String.format("Patient %s created medication %s not deleted locally after error.", idResult, medication));
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(@NotNull ApolloException e) {
                                                        builder.setContentTitle("Sync Error")
                                                                .setContentText(String.format("Error for created patient : %s", id))
                                                                .setProgress(0, 0, false)
                                                                .setOngoing(false)
                                                                .setStyle(new NotificationCompat.BigTextStyle()
                                                                        .setBigContentTitle(String.format("Error in medication: %s", medication))
                                                                        .bigText(e.getMessage()));

                                                        notificationManager.notify(medication.hashCode(), builder.build());
                                                    }

                                                    @Override
                                                    public void onHttpError(@NotNull ApolloHttpException e) {
                                                        Log.d("BACKGROUND_HILO_HttpFailure", e.message());

                                                        builder.setContentTitle("Sync Error")
                                                                .setContentText("Error connecting to server")
                                                                .setProgress(0, 0, false)
                                                                .setOngoing(false)
                                                                .setStyle(new NotificationCompat.BigTextStyle()
                                                                        .setBigContentTitle("Error connecting to server")
                                                                        .bigText(String.format("Error %s: %s", e.code(), e.message())));

                                                        notificationManager.notify(e.code(), builder.build());
                                                        e.rawResponse().close();
                                                    }

                                                    @Override
                                                    public void onNetworkError(@NotNull ApolloNetworkException e) {
                                                        //Log.d("BACKGROUND_HILO_NetworkFailure", e.getMessage());

                                                        builder.setContentTitle("Sync Error")
                                                                .setContentText("Failed to connect to server.")
                                                                .setProgress(0, 0, false)
                                                                .setOngoing(false)
                                                                .setStyle(new NotificationCompat.BigTextStyle()
                                                                        .setBigContentTitle("Failed to connect to server.")
                                                                        .bigText(e.getMessage()));

                                                        notificationManager.notify(239487562, builder.build());
                                                    }

                                                    @Override
                                                    public void onParseError(@NotNull ApolloParseException e) {
                                                        Log.d("BACKGROUND_HILO_ParseFailure", e.getMessage());

                                                    }

                                                    @Override
                                                    public void onCanceledError(@NotNull ApolloCanceledException e) {
                                                        Log.d("BACKGROUND_HILO_CanceledEvent", e.getMessage());
                                                    }
                                                });
                                    }
                                }



                                Integer deleted = myDB.deleteCreatedPatients(idResult);

                                if (deleted > 0) {
                                    Log.d("BACKGROUND_HILO", String.format("Patient %s deleted locally", idResult));
                                } else {
                                    Log.d("BACKGROUND_HILO", String.format("Patient %s not deleted locally", idResult));
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {
                            //Log.d("BACKGROUND_HILO_Failure", e.getMessage());

                            builder.setContentTitle("Sync Error")
                                    .setContentText(String.format("Error for created patient: %s", id))
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle(String.format("Error for created patient: %s", id))
                                            .bigText(e.getMessage()));

                            notificationManager.notify(p.hashCode(), builder.build());

                        }

                        @Override
                        public void onHttpError(@NotNull ApolloHttpException e) {
                            Log.d("BACKGROUND_HILO_HttpFailure", e.message());

                            builder.setContentTitle("Sync Error")
                                    .setContentText("Error connecting to server")
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle("Error connecting to server")
                                            .bigText(String.format("Error %s: %s", e.code(), e.message())));

                            notificationManager.notify(e.code(), builder.build());
                            e.rawResponse().close();
                        }

                        @Override
                        public void onNetworkError(@NotNull ApolloNetworkException e) {
                            //Log.d("BACKGROUND_HILO_NetworkFailure", e.getMessage());

                            builder.setContentTitle("Sync Error")
                                    .setContentText("Failed to connect to server.")
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle("Failed to connect to server.")
                                            .bigText(e.getMessage()));

                            notificationManager.notify(239487562, builder.build());
                        }

                        @Override
                        public void onParseError(@NotNull ApolloParseException e) {
                            Log.d("BACKGROUND_HILO_ParseFailure", e.getMessage());

                        }

                        @Override
                        public void onCanceledError(@NotNull ApolloCanceledException e) {
                            Log.d("BACKGROUND_HILO_CanceledEvent", e.getMessage());
                        }
                    });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void syncUpdatedPatients(Cursor updatedPatientsCur) {
        while (updatedPatientsCur.moveToNext()){

            progressData += 1;

            String s = String.format("Syncing updated patients (%s de %s)", updatedPatientsCur.getPosition()+1, updatedPatientsCur.getCount());

            builder.setContentText(s)
                    .setProgress(totalData, progressData, false)
                    .setOngoing(true)
                    .build();
            notificationManager.notify(TAG, builder.build());

            String id = String.valueOf(updatedPatientsCur.getString(updatedPatientsCur.getColumnIndex(DatabaseHandler.COL_14_1)));

            UpdatePatientInput.Builder p = UpdatePatientInput.builder()
                    .age(updatedPatientsCur.isNull(updatedPatientsCur.getColumnIndex(DatabaseHandler.COL_7_1)) ?
                            null :
                            updatedPatientsCur.getInt(updatedPatientsCur.getColumnIndex(DatabaseHandler.COL_7_1)))
                    .country(updatedPatientsCur.getString(updatedPatientsCur.getColumnIndex(DatabaseHandler.COL_13_1)))
                    .dateEntrance(updatedPatientsCur.getString(updatedPatientsCur.getColumnIndex(DatabaseHandler.COL_15_1)))
                    .firstName(updatedPatientsCur.getString(updatedPatientsCur.getColumnIndex(DatabaseHandler.COL_2_1)))
                    .hospitalized(updatedPatientsCur.getString(updatedPatientsCur.getColumnIndex(DatabaseHandler.COL_8_1)) == null ? null : updatedPatientsCur.getString(updatedPatientsCur.getColumnIndex(DatabaseHandler.COL_8_1)).equals("true"))
                    .identification(updatedPatientsCur.getString(updatedPatientsCur.getColumnIndex(DatabaseHandler.COL_1_1)))
                    .intensiveCareUnite(updatedPatientsCur.getString(updatedPatientsCur.getColumnIndex(DatabaseHandler.COL_6_1)) == null ? null : updatedPatientsCur.getString(updatedPatientsCur.getColumnIndex(DatabaseHandler.COL_6_1)).equals("true"))
                    .lastName(updatedPatientsCur.getString(updatedPatientsCur.getColumnIndex(DatabaseHandler.COL_3_1)))
                    .nationality(updatedPatientsCur.getString(updatedPatientsCur.getColumnIndex(DatabaseHandler.COL_4_1)))
                    .region(updatedPatientsCur.getString(updatedPatientsCur.getColumnIndex(DatabaseHandler.COL_5_1)))
                    .state(updatedPatientsCur.getString(updatedPatientsCur.getColumnIndex(DatabaseHandler.COL_11_1)));

            List<String> pathologies = new ArrayList<>();

            Cursor pathCur = myDB.getAllUpdatedPatientsPathology();

            if (pathCur != null && pathCur.getCount() > 0){
                while (pathCur.moveToNext()){
                    String patient = pathCur.getString(1);

                    if (patient.equals(id)){
                        pathologies.add(pathCur.getString(0));
                    }
                }
            }

            p.pathologies(pathologies);

            List<String> medications = new ArrayList<>();

            Cursor medicationCur = myDB.getAllUpdatedPatientsMedication();

            if (medicationCur != null && medicationCur.getCount() > 0) {
                while (medicationCur.moveToNext()){
                    String patient = medicationCur.getString(1);

                    if (patient.equals(id)){
                        medications.add(medicationCur.getString(0));
                    }
                }
            }

            p.medication(medications);

            apolloClient.mutate(UpdatePatientsMutation.builder().input(p.build()).idToUpdate(id).build())
                    .enqueue(new ApolloCall.Callback<UpdatePatientsMutation.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<UpdatePatientsMutation.Data> response) {

                            if (response.hasErrors()){

                                HashMap<String, String> defaultError = new HashMap<>();
                                defaultError.put("code", "Unknown");
                                defaultError.put("DatabaseMessage", "Unknown");

                                for (Error e: Objects.requireNonNull(response.getErrors())) {

                                    HashMap<String, String> extensions = (HashMap<String, String>) e.getCustomAttributes().getOrDefault("extensions", defaultError);

                                    String title = String
                                            .format("Sync error (Code: %s)", extensions.getOrDefault("code", "Unknown"));

                                    String bigMessage = "Server: " + e.getMessage() + "\n" +
                                            "Database: " +
                                            extensions
                                                    .getOrDefault("DatabaseMessage",
                                                            "Database has no message");


                                    builder.setContentTitle(title)
                                            .setContentText(String.format("Patient: %s", id))
                                            .setProgress(0, 0, false)
                                            .setOngoing(false)
                                            .setStyle(new NotificationCompat.BigTextStyle()
                                                    .setBigContentTitle(String.format("Patient: %s", id))
                                                    .bigText(bigMessage));

                                    //NotificationManager m = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                                    //m.notify(e.hashCode(), builder.build());

                                    notificationManager.notify(e.hashCode(), builder.build());

                                }
                            } else {

                                Integer deleted = myDB.deleteUpdatedPatients(id);

                                if (deleted > 0) {
                                    Log.d("BACKGROUND_HILO", "Updated Patient deleted locally.");
                                } else {
                                    Log.d("BACKGROUND_HILO", "Updated Patient not deleted locally.");
                                }

                                if (!pathologies.isEmpty()){
                                    for (String path: pathologies) {
                                        myDB.deleteUpdatedPatientsPathology(path, id);
                                    }
                                }

                                if (!medications.isEmpty()){
                                    for (String med: medications) {
                                        myDB.deleteUpdatedPatientsMedication(med, id);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {
                            //Log.d("BACKGROUND_HILO_Failure", e.getMessage());

                            builder.setContentTitle("Sync Error")
                                    .setContentText(String.format("Error while trying to update the patient: %s", id))
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle(String.format("Error while trying to update the patient: %s", id))
                                            .bigText(e.getMessage()));

                            notificationManager.notify(p.hashCode(), builder.build());

                        }

                        @Override
                        public void onHttpError(@NotNull ApolloHttpException e) {
                            Log.d("BACKGROUND_HILO_HttpFailure", e.message());

                            builder.setContentTitle("Sync Error")
                                    .setContentText("Error connecting to server")
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle("Error connecting to server")
                                            .bigText(String.format("Error %s: %s", e.code(), e.message())));

                            notificationManager.notify(e.code(), builder.build());
                            e.rawResponse().close();
                        }

                        @Override
                        public void onNetworkError(@NotNull ApolloNetworkException e) {
                            //Log.d("BACKGROUND_HILO_NetworkFailure", e.getMessage());

                            builder.setContentTitle("Sync Error")
                                    .setContentText("Failed to connect to server.")
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle("Failed to connect to server.")
                                            .bigText(e.getMessage()));

                            notificationManager.notify(239487562, builder.build());
                        }

                        @Override
                        public void onParseError(@NotNull ApolloParseException e) {
                            Log.d("BACKGROUND_HILO_ParseFailure", e.getMessage());

                        }

                        @Override
                        public void onCanceledError(@NotNull ApolloCanceledException e) {
                            Log.d("BACKGROUND_HILO_CanceledEvent", e.getMessage());
                        }
                    });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void syncDeletedPatients(Cursor deletedPatientsCur) {
        while (deletedPatientsCur.moveToNext()){

            progressData += 1;

            String id = String.valueOf(deletedPatientsCur.getString(0));

            String s = String.format("Syncing deleted patients (%s de %s)", deletedPatientsCur.getPosition()+1, deletedPatientsCur.getCount());

            builder.setContentText(s)
                    .setProgress(totalData, progressData, false)
                    .setOngoing(true)
                    .build();
            notificationManager.notify(TAG, builder.build());

            apolloClient.mutate(DeletePatientsMutation.builder().idToDelete(id).build())
                    .enqueue(new ApolloCall.Callback<DeletePatientsMutation.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<DeletePatientsMutation.Data> response) {

                            if (response.hasErrors()){

                                HashMap<String, String> defaultError = new HashMap<>();
                                defaultError.put("code", "Unknown");
                                defaultError.put("DatabaseMessage", "Unknown");

                                for (Error e: Objects.requireNonNull(response.getErrors())) {

                                    HashMap<String, String> extensions = (HashMap<String, String>) e.getCustomAttributes().getOrDefault("extensions", defaultError);

                                    String title = String
                                            .format("Sync error (Code: %s)", extensions.getOrDefault("code", "Unknown"));

                                    String bigMessage = "Server: " + e.getMessage() + "\n" +
                                            "Database: " +
                                            extensions
                                                    .getOrDefault("DatabaseMessage",
                                                            "Database has no message");

                                    builder.setContentTitle(title)
                                            .setContentText(String.format("Patient: %s", id))
                                            .setProgress(0, 0, false)
                                            .setOngoing(false)
                                            .setStyle(new NotificationCompat.BigTextStyle()
                                                    .setBigContentTitle(String.format("Patient: %s", id))
                                                    .bigText(bigMessage));

                                    notificationManager.notify(id.hashCode(), builder.build());

                                    if (extensions.getOrDefault("code", "Unknown").equals("PATIENT_NOT_FOUND")){
                                        Integer deleted = myDB.deleteDeletedPatients(id);

                                        if (deleted > 0) {
                                            Log.d("BACKGROUND_HILO", String.format("Patient %s deleted locally after error.", id));
                                        } else {
                                            Log.d("BACKGROUND_HILO", String.format("Patient %s not deleted locally after error.", id));
                                        }
                                    }
                                }
                            } else {
                                String idResult = response.getData().deletePatient().identification();

                                Integer deleted = myDB.deleteDeletedPatients(idResult);

                                if (deleted > 0) {
                                    Log.d("BACKGROUND_HILO", String.format("Patient %s deleted locally", idResult));
                                } else {
                                    Log.d("BACKGROUND_HILO", String.format("Patient %s not deleted locally", idResult));
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {
                            //Log.d("BACKGROUND_HILO_Failure", e.getMessage());

                            builder.setContentTitle("Sync Error")
                                    .setContentText(String.format("Error for deleted patient: %s", id))
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle(String.format("Error for deleted patient: %s", id))
                                            .bigText(e.getMessage()));

                            notificationManager.notify(id.hashCode(), builder.build());

                        }

                        @Override
                        public void onHttpError(@NotNull ApolloHttpException e) {
                            Log.d("BACKGROUND_HILO_HttpFailure", e.message());

                            builder.setContentTitle("Sync Error")
                                    .setContentText("Error connecting to server")
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle("Error connecting to server")
                                            .bigText(String.format("Error %s: %s", e.code(), e.message())));

                            notificationManager.notify(e.code(), builder.build());
                            e.rawResponse().close();
                        }

                        @Override
                        public void onNetworkError(@NotNull ApolloNetworkException e) {
                            //Log.d("BACKGROUND_HILO_NetworkFailure", e.getMessage());

                            builder.setContentTitle("Sync Error")
                                    .setContentText("Failed to connect to server.")
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle("Failed to connect to server.")
                                            .bigText(e.getMessage()));

                            notificationManager.notify(239487562, builder.build());
                        }

                        @Override
                        public void onParseError(@NotNull ApolloParseException e) {
                            Log.d("BACKGROUND_HILO_ParseFailure", e.getMessage());

                        }

                        @Override
                        public void onCanceledError(@NotNull ApolloCanceledException e) {
                            Log.d("BACKGROUND_HILO_CanceledEvent", e.getMessage());
                        }
                    });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void syncCreatedContacts(Cursor createdContactsCur) {

        while (createdContactsCur.moveToNext()){

            progressData += 1;

            String id = String.valueOf(createdContactsCur.getString(createdContactsCur.getColumnIndex(DatabaseHandler.COL_1_2)));


            CreateContactInput p = CreateContactInput.builder()
                    .age(createdContactsCur.getInt(createdContactsCur.getColumnIndex(DatabaseHandler.COL_8_2)))
                    .country(createdContactsCur.getString(createdContactsCur.getColumnIndex(DatabaseHandler.COL_12_2)))
                    .firstName(createdContactsCur.getString(createdContactsCur.getColumnIndex(DatabaseHandler.COL_2_2)))
                    .identification(id)
                    .lastName(createdContactsCur.getString(createdContactsCur.getColumnIndex(DatabaseHandler.COL_3_2)))
                    .nationality(createdContactsCur.getString(createdContactsCur.getColumnIndex(DatabaseHandler.COL_4_2)))
                    .region(createdContactsCur.getString(createdContactsCur.getColumnIndex(DatabaseHandler.COL_5_2)))
                    .address(createdContactsCur.getString(createdContactsCur.getColumnIndex(DatabaseHandler.COL_6_2)))
                    .email(createdContactsCur.getString(createdContactsCur.getColumnIndex(DatabaseHandler.COL_7_2)))
                    .build();

            String s = String.format("Syncing created contacts (%s de %s)", createdContactsCur.getPosition()+1, createdContactsCur.getCount());

            builder.setContentText(s)
                    .setProgress(totalData, progressData, false)
                    .setOngoing(true)
                    .build();
            notificationManager.notify(TAG, builder.build());

            apolloClient.mutate(CreateContactsMutation.builder().input(p).build())
                    .enqueue(new ApolloCall.Callback<CreateContactsMutation.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<CreateContactsMutation.Data> response) {

                            if (response.hasErrors()){

                                HashMap<String, String> defaultError = new HashMap<>();
                                defaultError.put("code", "Unknown");
                                defaultError.put("DatabaseMessage", "Unknown");

                                for (Error e: Objects.requireNonNull(response.getErrors())) {

                                    HashMap<String, String> extensions = (HashMap<String, String>) e.getCustomAttributes().getOrDefault("extensions", defaultError);

                                    String title = String
                                            .format("Sync error (Code: %s)", extensions.getOrDefault("code", "Unknown"));

                                    String bigMessage = "Server: " + e.getMessage() + "\n" +
                                            "Database: " +
                                            extensions
                                                    .getOrDefault("DatabaseMessage",
                                                            "Database has no message");

                                    builder.setContentTitle(title)
                                            .setContentText(String.format("Contact: %s", id))
                                            .setProgress(0, 0, false)
                                            .setOngoing(false)
                                            .setStyle(new NotificationCompat.BigTextStyle()
                                                    .setBigContentTitle(String.format("Contact: %s", id))
                                                    .bigText(bigMessage));

                                    notificationManager.notify(p.hashCode(), builder.build());

                                    if (extensions.getOrDefault("code", "Unknown").equals("50006")){
                                        Integer deleted = myDB.deleteCreatedContacts(id);

                                        if (deleted > 0) {
                                            Log.d("BACKGROUND_HILO", String.format("Contact %s deleted locally after error.", id));
                                        } else {
                                            Log.d("BACKGROUND_HILO", String.format("Contact %s not deleted locally after error.", id));
                                        }
                                    }
                                }
                            } else {
                                String idResult = response.getData().addContact().identification();

                                List<String> pathologies = new ArrayList<>();

                                Cursor pathCur = myDB.getAllCreatedContactPathology();

                                while (pathCur.moveToNext()){
                                    String patient = pathCur.getString(1);

                                    if (patient.equals(idResult)){
                                        pathologies.add(pathCur.getString(0));
                                    }
                                }

                                if (pathologies.size() > 0) {

                                    for (String pathology : pathologies) {

                                        UpdateContactInput input = UpdateContactInput.builder()
                                                .pathologies(new ArrayList<>(Collections.singleton(pathology)))
                                                .build();

                                        apolloClient.mutate(UpdateContactsMutation.builder().toUpdate(idResult).input(input).build())
                                                .enqueue(new ApolloCall.Callback<UpdateContactsMutation.Data>() {
                                                    @Override
                                                    public void onResponse(@NotNull Response<UpdateContactsMutation.Data> response) {
                                                        if (response.hasErrors()){
                                                            HashMap<String, String> defaultError = new HashMap<>();
                                                            defaultError.put("code", "Unknown");
                                                            defaultError.put("DatabaseMessage", "Unknown");

                                                            for (Error e: Objects.requireNonNull(response.getErrors())) {

                                                                HashMap<String, String> extensions = (HashMap<String, String>) e.getCustomAttributes().getOrDefault("extensions", defaultError);

                                                                String title = String
                                                                        .format("Sync error (Code: %s)", extensions.getOrDefault("code", "Unknown"));

                                                                String bigMessage = "Server: " + e.getMessage() + "\n" +
                                                                        "Database: " +
                                                                        extensions
                                                                                .getOrDefault("DatabaseMessage",
                                                                                        "Database has no message");

                                                                builder.setContentTitle(title)
                                                                        .setContentText(String.format("Contact created: %s", id))
                                                                        .setProgress(0, 0, false)
                                                                        .setOngoing(false)
                                                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                                                .setBigContentTitle(String.format("Pathology %s of new contact: %s", pathology, id))
                                                                                .bigText(bigMessage));

                                                                notificationManager.notify(input.hashCode(), builder.build());

                                                                if (extensions.getOrDefault("code", "Unknown").equals("2627")){
                                                                    Integer deleted = myDB.deleteCreatedContactPathology(pathology, idResult);

                                                                    if (deleted > 0) {
                                                                        Log.d("BACKGROUND_HILO", String.format("Contact %s created pathology %s deleted locally after error.", idResult, pathology));
                                                                    } else {
                                                                        Log.d("BACKGROUND_HILO", String.format("Contact %s created pathology %s not deleted locally after error.", idResult, pathology));
                                                                    }
                                                                } else {
                                                                    boolean patient = myDB.insertUpdatedContacts(idResult, null, null, null,null,null,null,null,null,null);
                                                                    boolean p = myDB.insertUpdatedContactPathology(pathology, idResult);

                                                                    Log.d("BACKGROUND_HILO_passed_CREATEDPAITENTPATHOLOGY", String.valueOf(patient));
                                                                    Log.d("BACKGROUND_HILO_passed_CREATEDPAITENTPATHOLOGY", String.valueOf(p));

                                                                    Integer deleted = myDB.deleteCreatedContactPathology(pathology, idResult);

                                                                }
                                                            }
                                                        } else {
                                                            Integer deleted = myDB.deleteCreatedContactPathology(pathology, idResult);

                                                            if (deleted > 0) {
                                                                Log.d("BACKGROUND_HILO", String.format("Patient %s created pathology %s deleted locally after error.", idResult, pathology));
                                                            } else {
                                                                Log.d("BACKGROUND_HILO", String.format("Patient %s created pathology %s not deleted locally after error.", idResult, pathology));
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(@NotNull ApolloException e) {
                                                        builder.setContentTitle("Sync Error")
                                                                .setContentText(String.format("Error for created contact : %s", id))
                                                                .setProgress(0, 0, false)
                                                                .setOngoing(false)
                                                                .setStyle(new NotificationCompat.BigTextStyle()
                                                                        .setBigContentTitle(String.format("Error in pathology: %s", pathology))
                                                                        .bigText(e.getMessage()));

                                                        notificationManager.notify(pathology.hashCode(), builder.build());
                                                    }

                                                    @Override
                                                    public void onHttpError(@NotNull ApolloHttpException e) {
                                                        Log.d("BACKGROUND_HILO_HttpFailure", e.message());

                                                        builder.setContentTitle("Sync Error")
                                                                .setContentText("Error connecting to server")
                                                                .setProgress(0, 0, false)
                                                                .setOngoing(false)
                                                                .setStyle(new NotificationCompat.BigTextStyle()
                                                                        .setBigContentTitle("Error connecting to server")
                                                                        .bigText(String.format("Error %s: %s", e.code(), e.message())));

                                                        notificationManager.notify(e.code(), builder.build());
                                                        e.rawResponse().close();
                                                    }

                                                    @Override
                                                    public void onNetworkError(@NotNull ApolloNetworkException e) {
                                                        //Log.d("BACKGROUND_HILO_NetworkFailure", e.getMessage());

                                                        builder.setContentTitle("Sync Error")
                                                                .setContentText("Failed to connect to server.")
                                                                .setProgress(0, 0, false)
                                                                .setOngoing(false)
                                                                .setStyle(new NotificationCompat.BigTextStyle()
                                                                        .setBigContentTitle("Failed to connect to server.")
                                                                        .bigText(e.getMessage()));

                                                        notificationManager.notify(239487562, builder.build());
                                                    }

                                                    @Override
                                                    public void onParseError(@NotNull ApolloParseException e) {
                                                        Log.d("BACKGROUND_HILO_ParseFailure", e.getMessage());

                                                    }

                                                    @Override
                                                    public void onCanceledError(@NotNull ApolloCanceledException e) {
                                                        Log.d("BACKGROUND_HILO_CanceledEvent", e.getMessage());
                                                    }
                                                });
                                    }
                                }


                                Integer deleted = myDB.deleteCreatedContacts(idResult);

                                if (deleted > 0) {
                                    Log.d("BACKGROUND_HILO", String.format("Contact %s deleted locally", idResult));
                                } else {
                                    Log.d("BACKGROUND_HILO", String.format("Contact %s not deleted locally", idResult));
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {
                            //Log.d("BACKGROUND_HILO_Failure", e.getMessage());

                            builder.setContentTitle("Sync Error")
                                    .setContentText(String.format("Error for created contact: %s", id))
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle(String.format("Error for created contact: %s", id))
                                            .bigText(e.getMessage()));

                            notificationManager.notify(p.hashCode(), builder.build());

                        }

                        @Override
                        public void onHttpError(@NotNull ApolloHttpException e) {
                            Log.d("BACKGROUND_HILO_HttpFailure", e.message());

                            builder.setContentTitle("Sync Error")
                                    .setContentText("Error connecting to server")
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle("Error connecting to server")
                                            .bigText(String.format("Error %s: %s", e.code(), e.message())));

                            notificationManager.notify(e.code(), builder.build());
                            e.rawResponse().close();
                        }

                        @Override
                        public void onNetworkError(@NotNull ApolloNetworkException e) {
                            //Log.d("BACKGROUND_HILO_NetworkFailure", e.getMessage());

                            builder.setContentTitle("Sync Error")
                                    .setContentText("Failed to connect to server.")
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle("Failed to connect to server.")
                                            .bigText(e.getMessage()));

                            notificationManager.notify(239487562, builder.build());
                        }

                        @Override
                        public void onParseError(@NotNull ApolloParseException e) {
                            Log.d("BACKGROUND_HILO_ParseFailure", e.getMessage());

                        }

                        @Override
                        public void onCanceledError(@NotNull ApolloCanceledException e) {
                            Log.d("BACKGROUND_HILO_CanceledEvent", e.getMessage());
                        }
                    });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void syncUpdatedContacts(Cursor updatedContactsCur) {
        while (updatedContactsCur.moveToNext()){

            progressData += 1;

            String s = String.format("Syncing updated contacts (%s de %s)", updatedContactsCur.getPosition()+1, updatedContactsCur.getCount());

            builder.setContentText(s)
                    .setProgress(totalData, progressData, false)
                    .setOngoing(true)
                    .build();
            notificationManager.notify(TAG, builder.build());

            String id = String.valueOf(updatedContactsCur.getString(updatedContactsCur.getColumnIndex(DatabaseHandler.COL_13_2)));

            UpdateContactInput.Builder p = UpdateContactInput.builder()
                    .age(updatedContactsCur.isNull(updatedContactsCur.getColumnIndex(DatabaseHandler.COL_8_2)) ?
                            null :
                            updatedContactsCur.getInt(updatedContactsCur.getColumnIndex(DatabaseHandler.COL_8_2)))
                    .country(updatedContactsCur.getString(updatedContactsCur.getColumnIndex(DatabaseHandler.COL_12_2)))
                    .firstName(updatedContactsCur.getString(updatedContactsCur.getColumnIndex(DatabaseHandler.COL_2_2)))
                    .identification(updatedContactsCur.getString(updatedContactsCur.getColumnIndex(DatabaseHandler.COL_1_2)))
                    .lastName(updatedContactsCur.getString(updatedContactsCur.getColumnIndex(DatabaseHandler.COL_3_2)))
                    .nationality(updatedContactsCur.getString(updatedContactsCur.getColumnIndex(DatabaseHandler.COL_4_2)))
                    .region(updatedContactsCur.getString(updatedContactsCur.getColumnIndex(DatabaseHandler.COL_5_2)))
                    .address(updatedContactsCur.getString(updatedContactsCur.getColumnIndex(DatabaseHandler.COL_6_2)))
                    .email(updatedContactsCur.getString(updatedContactsCur.getColumnIndex(DatabaseHandler.COL_7_2)));

            List<String> pathologies = new ArrayList<>();

            Cursor pathCur = myDB.getAllUpdatedContactPathology();

            while (pathCur.moveToNext()){
                String patient = pathCur.getString(1);

                if (patient.equals(id)){
                    pathologies.add(pathCur.getString(0));
                }
            }

            p.pathologies(pathologies);

            apolloClient.mutate(UpdateContactsMutation.builder().input(p.build()).toUpdate(id).build())
                    .enqueue(new ApolloCall.Callback<UpdateContactsMutation.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<UpdateContactsMutation.Data> response) {

                            if (response.hasErrors()){

                                HashMap<String, String> defaultError = new HashMap<>();
                                defaultError.put("code", "Unknown");
                                defaultError.put("DatabaseMessage", "Unknown");

                                for (Error e: Objects.requireNonNull(response.getErrors())) {

                                    HashMap<String, String> extensions = (HashMap<String, String>) e.getCustomAttributes().getOrDefault("extensions", defaultError);

                                    String title = String
                                            .format("Sync error (Code: %s)", extensions.getOrDefault("code", "Unknown"));

                                    String bigMessage = "Server: " + e.getMessage() + "\n" +
                                            "Database: " +
                                            extensions
                                                    .getOrDefault("DatabaseMessage",
                                                            "Database has no message");


                                    builder.setContentTitle(title)
                                            .setContentText(String.format("Contact: %s", id))
                                            .setProgress(0, 0, false)
                                            .setOngoing(false)
                                            .setStyle(new NotificationCompat.BigTextStyle()
                                                    .setBigContentTitle(String.format("Contact: %s", id))
                                                    .bigText(bigMessage));

                                    notificationManager.notify(e.hashCode(), builder.build());

                                }
                            } else {

                                Integer deleted = myDB.deleteUpdatedContacts(id);

                                if (deleted > 0) {
                                    Log.d("BACKGROUND_HILO", "Updated Patient deleted locally.");
                                } else {
                                    Log.d("BACKGROUND_HILO", "Updated Patient not deleted locally.");
                                }

                                if (!pathologies.isEmpty()){
                                    for (String path: pathologies) {
                                        myDB.deleteUpdatedContactPathology(path, id);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {
                            //Log.d("BACKGROUND_HILO_Failure", e.getMessage());

                            builder.setContentTitle("Sync Error")
                                    .setContentText(String.format("Error on contact update: %s", id))
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle(String.format("Error on contact update: %s", id))
                                            .bigText(e.getMessage()));

                            notificationManager.notify(p.hashCode(), builder.build());

                        }

                        @Override
                        public void onHttpError(@NotNull ApolloHttpException e) {
                            Log.d("BACKGROUND_HILO_HttpFailure", e.message());

                            builder.setContentTitle("Sync Error")
                                    .setContentText("Error connecting to server")
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle("Error connecting to server")
                                            .bigText(String.format("Error %s: %s", e.code(), e.message())));

                            notificationManager.notify(e.code(), builder.build());
                            e.rawResponse().close();
                        }

                        @Override
                        public void onNetworkError(@NotNull ApolloNetworkException e) {
                            //Log.d("BACKGROUND_HILO_NetworkFailure", e.getMessage());

                            builder.setContentTitle("Sync Error")
                                    .setContentText("Failed to connect to server.")
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle("Failed to connect to server.")
                                            .bigText(e.getMessage()));

                            notificationManager.notify(239487562, builder.build());
                        }

                        @Override
                        public void onParseError(@NotNull ApolloParseException e) {
                            Log.d("BACKGROUND_HILO_ParseFailure", e.getMessage());

                        }

                        @Override
                        public void onCanceledError(@NotNull ApolloCanceledException e) {
                            Log.d("BACKGROUND_HILO_CanceledEvent", e.getMessage());
                        }
                    });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void syncDeletedContacts(Cursor deletedContactsCur) {
        while (deletedContactsCur.moveToNext()){

            progressData += 1;

            String id = String.valueOf(deletedContactsCur.getString(0));

            String s = String.format("Syncing deleted contacts (%s de %s)", deletedContactsCur.getPosition()+1, deletedContactsCur.getCount());

            builder.setContentText(s)
                    .setProgress(totalData, progressData, false)
                    .setOngoing(true)
                    .build();
            notificationManager.notify(TAG, builder.build());

            apolloClient.mutate(DeleteContactsMutation.builder().toDelete(id).build())
                    .enqueue(new ApolloCall.Callback<DeleteContactsMutation.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<DeleteContactsMutation.Data> response) {

                            if (response.hasErrors()){

                                HashMap<String, String> defaultError = new HashMap<>();
                                defaultError.put("code", "Unknown");
                                defaultError.put("DatabaseMessage", "Unknown");

                                for (Error e: Objects.requireNonNull(response.getErrors())) {

                                    HashMap<String, String> extensions = (HashMap<String, String>) e.getCustomAttributes().getOrDefault("extensions", defaultError);

                                    String title = String
                                            .format("Sync error (Code: %s)", extensions.getOrDefault("code", "Unknown"));

                                    String bigMessage = "Server: " + e.getMessage() + "\n" +
                                            "Database: " +
                                            extensions
                                                    .getOrDefault("DatabaseMessage",
                                                            "Database has no message");

                                    builder.setContentTitle(title)
                                            .setContentText(String.format("Contact: %s", id))
                                            .setProgress(0, 0, false)
                                            .setOngoing(false)
                                            .setStyle(new NotificationCompat.BigTextStyle()
                                                    .setBigContentTitle(String.format("Contact: %s", id))
                                                    .bigText(bigMessage));

                                    notificationManager.notify(id.hashCode(), builder.build());

                                    if (extensions.getOrDefault("code", "Unknown").equals("CONTACT_NOT_FOUND")){
                                        Integer deleted = myDB.deleteDeletedContacts(id);

                                        if (deleted > 0) {
                                            Log.d("BACKGROUND_HILO", String.format("Contact %s deleted locally after error.", id));
                                        } else {
                                            Log.d("BACKGROUND_HILO", String.format("Contact %s not deleted locally after error.", id));
                                        }
                                    }
                                }
                            } else {
                                String idResult = response.getData().deleteContact().identification();

                                Integer deleted = myDB.deleteDeletedContacts(idResult);

                                if (deleted > 0) {
                                    Log.d("BACKGROUND_HILO", String.format("Contact %s deleted locally", idResult));
                                } else {
                                    Log.d("BACKGROUND_HILO", String.format("Contact %s not deleted locally", idResult));
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {
                            //Log.d("BACKGROUND_HILO_Failure", e.getMessage());

                            builder.setContentTitle("Sync Error")
                                    .setContentText(String.format("Error for deleted patient: %s", id))
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle(String.format("Error for deleted patient: %s", id))
                                            .bigText(e.getMessage()));

                            notificationManager.notify(id.hashCode(), builder.build());

                        }

                        @Override
                        public void onHttpError(@NotNull ApolloHttpException e) {
                            Log.d("BACKGROUND_HILO_HttpFailure", e.message());

                            builder.setContentTitle("Sync Error")
                                    .setContentText("Error connecting to server")
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle("Error connecting to server")
                                            .bigText(String.format("Error %s: %s", e.code(), e.message())));

                            notificationManager.notify(e.code(), builder.build());
                            e.rawResponse().close();
                        }

                        @Override
                        public void onNetworkError(@NotNull ApolloNetworkException e) {
                            //Log.d("BACKGROUND_HILO_NetworkFailure", e.getMessage());

                            builder.setContentTitle("Sync Error")
                                    .setContentText("Failed to connect to server.")
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle("Failed to connect to server.")
                                            .bigText(e.getMessage()));

                            notificationManager.notify(239487562, builder.build());
                        }

                        @Override
                        public void onParseError(@NotNull ApolloParseException e) {
                            Log.d("BACKGROUND_HILO_ParseFailure", e.getMessage());

                        }

                        @Override
                        public void onCanceledError(@NotNull ApolloCanceledException e) {
                            Log.d("BACKGROUND_HILO_CanceledEvent", e.getMessage());
                        }
                    });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void syncCreatedContactVisit(Cursor createdContactVisitCur) {

        while (createdContactVisitCur.moveToNext()){

            progressData += 1;

            String contactID = String.valueOf(createdContactVisitCur.getString(createdContactVisitCur.getColumnIndex(DatabaseHandler.COL_2_5)));
            String patientID = String.valueOf(createdContactVisitCur.getString(createdContactVisitCur.getColumnIndex(DatabaseHandler.COL_1_5)));
            String visitDate = String.valueOf(createdContactVisitCur.getString(createdContactVisitCur.getColumnIndex(DatabaseHandler.COL_3_5)));

            String s = String.format("Syncing created contact visits (%s de %s)", createdContactVisitCur.getPosition()+1, createdContactVisitCur.getCount());

            builder.setContentText(s)
                    .setProgress(totalData, progressData, false)
                    .setOngoing(true)
                    .build();
            notificationManager.notify(TAG, builder.build());

            apolloClient.mutate(AddContactVisitMutation.builder().contactID(contactID).patientID(patientID).date(visitDate).build())
                    .enqueue(new ApolloCall.Callback<AddContactVisitMutation.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<AddContactVisitMutation.Data> response) {

                            if (response.hasErrors()){

                                HashMap<String, String> defaultError = new HashMap<>();
                                defaultError.put("code", "Unknown");
                                defaultError.put("DatabaseMessage", "Unknown");

                                for (Error e: Objects.requireNonNull(response.getErrors())) {

                                    HashMap<String, String> extensions = (HashMap<String, String>) e.getCustomAttributes().getOrDefault("extensions", defaultError);

                                    String title = String
                                            .format("Sync error (Code: %s)", extensions.getOrDefault("code", "Unknown"));

                                    String bigMessage = "Server: " + e.getMessage() + "\n" +
                                            "Database: " +
                                            extensions
                                                    .getOrDefault("DatabaseMessage",
                                                            "Database has no message");

                                    builder.setContentTitle(title)
                                            .setContentText(String.format("Visit of contact %s to Patient %s", contactID, patientID))
                                            .setProgress(0, 0, false)
                                            .setOngoing(false)
                                            .setStyle(new NotificationCompat.BigTextStyle()
                                                    .setBigContentTitle(String.format("Visit of contact %s to Patient %s", contactID, patientID))
                                                    .bigText(bigMessage));

                                    notificationManager.notify(contactID.hashCode() + patientID.hashCode() + visitDate.hashCode(), builder.build());

                                    if (extensions.getOrDefault("code", "Unknown").equals("2627")){
                                        Integer deleted = myDB.deleteCreatedContactVisit(patientID, contactID, visitDate);

                                        if (deleted > 0) {
                                            Log.d("BACKGROUND_HILO", String.format("Contact %s deleted locally after error.", contactID));
                                        } else {
                                            Log.d("BACKGROUND_HILO", String.format("Contact %s not deleted locally after error.", contactID));
                                        }
                                    }
                                }
                            } else {
                                String contactResultID = response.getData().addContactVisit().contact().identification();
                                String patientResultID = response.getData().addContactVisit().patient().identification();
                                String visitDateResultID = response.getData().addContactVisit().visitDate().toString();


                                Integer deleted = myDB.deleteCreatedContactVisit(patientResultID, contactResultID, visitDateResultID);

                                if (deleted > 0) {
                                    Log.d("BACKGROUND_HILO", String.format("Contact %s, %s, %s deleted locally", contactResultID, patientResultID, visitDateResultID));
                                } else {
                                    Log.d("BACKGROUND_HILO", String.format("Contact %s, %s, %s not deleted locally", contactResultID, patientResultID, visitDateResultID));
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {
                            //Log.d("BACKGROUND_HILO_Failure", e.getMessage());

                            builder.setContentTitle("Sync Error")
                                    .setContentText(String.format("Error on visit: p:%s, c:%s, d:%s", patientID, contactID, visitDate))
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle(String.format("Error on visit: p:%s, c:%s, d:%s", patientID, contactID, visitDate))
                                            .bigText(e.getMessage()));

                            notificationManager.notify(contactID.hashCode() + patientID.hashCode() + visitDate.hashCode(), builder.build());

                        }

                        @Override
                        public void onHttpError(@NotNull ApolloHttpException e) {
                            Log.d("BACKGROUND_HILO_HttpFailure", e.message());

                            builder.setContentTitle("Sync Error")
                                    .setContentText("Error connecting to server")
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle("Error connecting to server")
                                            .bigText(String.format("Error %s: %s", e.code(), e.message())));

                            notificationManager.notify(e.code(), builder.build());
                            e.rawResponse().close();
                        }

                        @Override
                        public void onNetworkError(@NotNull ApolloNetworkException e) {
                            //Log.d("BACKGROUND_HILO_NetworkFailure", e.getMessage());

                            builder.setContentTitle("Sync Error")
                                    .setContentText("Failed to connect to server.")
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle("Failed to connect to server.")
                                            .bigText(e.getMessage()));

                            notificationManager.notify(239487562, builder.build());
                        }

                        @Override
                        public void onParseError(@NotNull ApolloParseException e) {
                            Log.d("BACKGROUND_HILO_ParseFailure", e.getMessage());

                        }

                        @Override
                        public void onCanceledError(@NotNull ApolloCanceledException e) {
                            Log.d("BACKGROUND_HILO_CanceledEvent", e.getMessage());
                        }
                    });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void syncUpdatedContactVisit(Cursor updatedContactVisitCur) {
        while (updatedContactVisitCur.moveToNext()){

            progressData += 1;

            String s = String.format("Syncing updated contact visit (%s de %s)", updatedContactVisitCur.getPosition()+1, updatedContactVisitCur.getCount());

            builder.setContentText(s)
                    .setProgress(totalData, progressData, false)
                    .setOngoing(true)
                    .build();
            notificationManager.notify(TAG, builder.build());

            String patientID = String.valueOf(updatedContactVisitCur.getString(updatedContactVisitCur.getColumnIndex(DatabaseHandler.COL_1_11)));
            String contactID = String.valueOf(updatedContactVisitCur.getString(updatedContactVisitCur.getColumnIndex(DatabaseHandler.COL_2_11)));
            String visitDate = String.valueOf(updatedContactVisitCur.getString(updatedContactVisitCur.getColumnIndex(DatabaseHandler.COL_3_11)));

            UpdateContactVisitInput p = UpdateContactVisitInput.builder()
                    .contactId(updatedContactVisitCur.getString(updatedContactVisitCur.getColumnIndex(DatabaseHandler.COL_2_5)))
                    .patientId(updatedContactVisitCur.getString(updatedContactVisitCur.getColumnIndex(DatabaseHandler.COL_1_5)))
                    .visitDate(updatedContactVisitCur.getString(updatedContactVisitCur.getColumnIndex(DatabaseHandler.COL_3_5)))
                    .build();

            apolloClient.mutate(UpdateContactVisitMutation.builder().input(p).contactID(contactID).patientID(patientID).date(visitDate).build())
                    .enqueue(new ApolloCall.Callback<UpdateContactVisitMutation.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<UpdateContactVisitMutation.Data> response) {

                            if (response.hasErrors()){

                                HashMap<String, String> defaultError = new HashMap<>();
                                defaultError.put("code", "Unknown");
                                defaultError.put("DatabaseMessage", "Unknown");

                                for (Error e: Objects.requireNonNull(response.getErrors())) {

                                    HashMap<String, String> extensions = (HashMap<String, String>) e.getCustomAttributes().getOrDefault("extensions", defaultError);

                                    String title = String
                                            .format("Sync error (Code: %s)", extensions.getOrDefault("code", "Unknown"));

                                    String bigMessage = "Server: " + e.getMessage() + "\n" +
                                            "Database: " +
                                            extensions
                                                    .getOrDefault("DatabaseMessage",
                                                            "Database has no message");


                                    builder.setContentTitle(title)
                                            .setContentText(String.format("Visit of contact %s to Patient %s", contactID, patientID))
                                            .setProgress(0, 0, false)
                                            .setOngoing(false)
                                            .setStyle(new NotificationCompat.BigTextStyle()
                                                    .setBigContentTitle(String.format("Visit of contact %s to Patient %s", contactID, patientID))
                                                    .bigText(bigMessage));

                                    notificationManager.notify(patientID.hashCode() + contactID.hashCode() + visitDate.hashCode(), builder.build());

                                }
                            } else {

                                Integer deleted = myDB.deleteUpdatedContactVisit(patientID, contactID, visitDate);

                                if (deleted > 0) {
                                    Log.d("BACKGROUND_HILO", "Updated Patient deleted locally.");
                                } else {
                                    Log.d("BACKGROUND_HILO", "Updated Patient not deleted locally.");
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {
                            //Log.d("BACKGROUND_HILO_Failure", e.getMessage());

                            builder.setContentTitle("Sync Error")
                                    .setContentText(String.format("Error on visit: p:%s, c:%s, d:%s", patientID, contactID, visitDate))
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle(String.format("Error on visit: p:%s, c:%s, d:%s", patientID, contactID, visitDate))
                                            .bigText(e.getMessage()));

                            notificationManager.notify(contactID.hashCode() + patientID.hashCode() + visitDate.hashCode(), builder.build());

                        }

                        @Override
                        public void onHttpError(@NotNull ApolloHttpException e) {
                            Log.d("BACKGROUND_HILO_HttpFailure", e.message());

                            builder.setContentTitle("Sync Error")
                                    .setContentText("Error connecting to server")
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle("Error connecting to server")
                                            .bigText(String.format("Error %s: %s", e.code(), e.message())));

                            notificationManager.notify(e.code(), builder.build());
                            e.rawResponse().close();
                        }

                        @Override
                        public void onNetworkError(@NotNull ApolloNetworkException e) {
                            //Log.d("BACKGROUND_HILO_NetworkFailure", e.getMessage());

                            builder.setContentTitle("Sync Error")
                                    .setContentText("Failed to connect to server.")
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle("Failed to connect to server.")
                                            .bigText(e.getMessage()));

                            notificationManager.notify(239487562, builder.build());
                        }

                        @Override
                        public void onParseError(@NotNull ApolloParseException e) {
                            Log.d("BACKGROUND_HILO_ParseFailure", e.getMessage());

                        }

                        @Override
                        public void onCanceledError(@NotNull ApolloCanceledException e) {
                            Log.d("BACKGROUND_HILO_CanceledEvent", e.getMessage());
                        }
                    });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void syncDeletedContactVisit(Cursor deletedContactVisitCur) {
        while (deletedContactVisitCur.moveToNext()){

            progressData += 1;

            String patientID = String.valueOf(deletedContactVisitCur.getString(
                    deletedContactVisitCur.getColumnIndex(DatabaseHandler.COL_1_5)));
            String contactID = String.valueOf(deletedContactVisitCur.getString(
                    deletedContactVisitCur.getColumnIndex(DatabaseHandler.COL_2_5)));
            String visitDate = String.valueOf(deletedContactVisitCur.getString(
                    deletedContactVisitCur.getColumnIndex(DatabaseHandler.COL_3_5)));

            String s = String.format("Syncing deleted contact visit (%s de %s)", deletedContactVisitCur.getPosition()+1, deletedContactVisitCur.getCount());

            builder.setContentText(s)
                    .setProgress(totalData, progressData, false)
                    .setOngoing(true)
                    .build();
            notificationManager.notify(TAG, builder.build());

            apolloClient.mutate(DeleteContactVisitMutation.builder().contactID(contactID).patientID(patientID).date(visitDate).build())
                    .enqueue(new ApolloCall.Callback<DeleteContactVisitMutation.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<DeleteContactVisitMutation.Data> response) {

                            if (response.hasErrors()){

                                HashMap<String, String> defaultError = new HashMap<>();
                                defaultError.put("code", "Unknown");
                                defaultError.put("DatabaseMessage", "Unknown");

                                for (Error e: Objects.requireNonNull(response.getErrors())) {

                                    HashMap<String, String> extensions = (HashMap<String, String>) e.getCustomAttributes().getOrDefault("extensions", defaultError);

                                    String title = String
                                            .format("Sync error (Code: %s)", extensions.getOrDefault("code", "Unknown"));

                                    String bigMessage = "Server: " + e.getMessage() + "\n" +
                                            "Database: " +
                                            extensions
                                                    .getOrDefault("DatabaseMessage",
                                                            "Database has no message");

                                    builder.setContentTitle(title)
                                            .setContentText(String.format("Visit of contact %s to Patient %s", contactID, patientID))
                                            .setProgress(0, 0, false)
                                            .setOngoing(false)
                                            .setStyle(new NotificationCompat.BigTextStyle()
                                                    .setBigContentTitle(String.format("Visit of contact %s to Patient %s", contactID, patientID))
                                                    .bigText(bigMessage));

                                    notificationManager.notify(patientID.hashCode() + contactID.hashCode() + visitDate.hashCode(), builder.build());

                                    if (extensions.getOrDefault("code", "Unknown").equals("CONTACT_NOT_FOUND")){
                                        Integer deleted = myDB.deleteDeletedContactVisit(patientID, contactID, visitDate);

                                        if (deleted > 0) {
                                            Log.d("BACKGROUND_HILO", String.format("Contact %s deleted locally after error.", patientID));
                                        } else {
                                            Log.d("BACKGROUND_HILO", String.format("Contact %s not deleted locally after error.", patientID));
                                        }
                                    }
                                }
                            } else {
                                String patientidResult = response.getData().deleteContactVisit().patient().identification();
                                String contactidResult = response.getData().deleteContactVisit().contact().identification();
                                String visitdateResult = response.getData().deleteContactVisit().visitDate().toString();

                                Integer deleted = myDB.deleteDeletedContactVisit(patientidResult, contactidResult, visitdateResult);

                                if (deleted > 0) {
                                    Log.d("BACKGROUND_HILO", String.format("Contact %s deleted locally", patientidResult));
                                } else {
                                    Log.d("BACKGROUND_HILO", String.format("Contact %s not deleted locally", patientidResult));
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {
                            //Log.d("BACKGROUND_HILO_Failure", e.getMessage());

                            builder.setContentTitle("Sync Error")
                                    .setContentText(String.format("Error on visit: p:%s, c:%s, d:%s", patientID, contactID, visitDate))
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle(String.format("Error on visit: p:%s, c:%s, d:%s", patientID, contactID, visitDate))
                                            .bigText(e.getMessage()));

                            notificationManager.notify(contactID.hashCode() + patientID.hashCode() + visitDate.hashCode(), builder.build());

                        }

                        @Override
                        public void onHttpError(@NotNull ApolloHttpException e) {
                            Log.d("BACKGROUND_HILO_HttpFailure", e.message());

                            builder.setContentTitle("Sync Error")
                                    .setContentText("Error connecting to server")
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle("Error connecting to server")
                                            .bigText(String.format("Error %s: %s", e.code(), e.message())));

                            notificationManager.notify(e.code(), builder.build());
                            e.rawResponse().close();
                        }

                        @Override
                        public void onNetworkError(@NotNull ApolloNetworkException e) {
                            //Log.d("BACKGROUND_HILO_NetworkFailure", e.getMessage());

                            builder.setContentTitle("Sync Error")
                                    .setContentText("Failed to connect to server.")
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle("Failed to connect to server.")
                                            .bigText(e.getMessage()));

                            notificationManager.notify(239487562, builder.build());
                        }

                        @Override
                        public void onParseError(@NotNull ApolloParseException e) {
                            Log.d("BACKGROUND_HILO_ParseFailure", e.getMessage());

                        }

                        @Override
                        public void onCanceledError(@NotNull ApolloCanceledException e) {
                            Log.d("BACKGROUND_HILO_CanceledEvent", e.getMessage());
                        }
                    });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
