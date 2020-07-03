package com.example.cotec_2020_app24;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.apollographql.apollo.ApolloClient;
import okhttp3.OkHttpClient;

public class ApolloActivity extends AppCompatActivity {
    private ApolloClient apolloClient;
    private static final String ENDPOINT = "https://cotec-server.herokuapp.com/graphql/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apollo);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        apolloClient = ApolloClient.builder().serverUrl(ENDPOINT).okHttpClient(okHttpClient).build();
    }
}