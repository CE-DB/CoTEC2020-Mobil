package com.example.cotec_2020_app24;

import com.apollographql.apollo.ApolloClient;
import com.example.cotec_2020_app24.models.ContactModel;
import com.example.cotec_2020_app24.models.PatientModel;


import okhttp3.OkHttpClient;

public class GlobalVars {

    private static GlobalVars instance = null;
    private String accessKey = null;
    private ApolloClient apolloClient;
    private static final String ENDPOINT = "https://cotec-server.herokuapp.com/graphql/";
    private DatabaseHandler database;

    private PatientModel TempPatientHolderForView;
    private ContactModel TempContactHolderForView;

    public ContactModel getTempContactHolderForView() {
        return TempContactHolderForView;
    }

    public void setTempContactHolderForView(ContactModel tempContactHolderForView) {
        TempContactHolderForView = tempContactHolderForView;
    }

    public PatientModel getTempPatientHolderForView() {
        return TempPatientHolderForView;
    }

    public void setTempPatientHolderForView(PatientModel tempPatientHolderForView) {
        TempPatientHolderForView = tempPatientHolderForView;
    }


    private GlobalVars() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder(). build();
        apolloClient = ApolloClient.builder().serverUrl(ENDPOINT).okHttpClient(okHttpClient).build();
    }

    public static GlobalVars getInstance(){
        if (instance == null){
            instance = new GlobalVars();
        }
        return instance;
    }

    public DatabaseHandler getDatabase() {
        return database;
    }

    public void setDatabase(DatabaseHandler database) {
        this.database = database;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public ApolloClient getApolloClient() {
        return apolloClient;
    }
}
