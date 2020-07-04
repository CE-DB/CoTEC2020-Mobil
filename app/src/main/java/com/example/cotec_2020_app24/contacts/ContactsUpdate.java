package com.example.cotec_2020_app24.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.CountriesListQuery;
import com.example.cotec_2020_app24.DatabaseHandler;
import com.example.cotec_2020_app24.GlobalVars;
import com.example.cotec_2020_app24.R;
import com.example.cotec_2020_app24.models.ContactModel;
import com.example.cotec_2020_app24.models.PatientModel;
import com.example.cotec_2020_app24.patients.PatientPathologyUpdate;
import com.example.cotec_2020_app24.patients.PatientsUpdate;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ContactsUpdate extends AppCompatActivity {

    private EditText identification;
    private EditText firstName;
    private EditText lastName;
    private EditText age;
    private EditText region;
    private EditText email;
    private EditText address;
    private Button pathologies;
    private Button cancel;
    private Button save;
    private Spinner country;
    String countrySelected = "";
    ArrayAdapter<String> dataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_update);

        identification = findViewById(R.id.editTextContactIdUpdate);
        firstName = findViewById(R.id.editTextContactNameUpdated);
        lastName = findViewById(R.id.editTextContactLastNameUpdated);
        age = findViewById(R.id.editTextContactAgeUpdated);
        region = findViewById(R.id.editTextContactRegionUpdated);
        email = findViewById(R.id.editTextContactEmailUpdated);
        address = findViewById(R.id.editTextContactAddressUpdated);
        cancel = findViewById(R.id.ContactCancelUpdatedButton);
        save = findViewById(R.id.ContactSaveUpdatedButton);
        pathologies = findViewById(R.id.ContactPathologiesUpdatedButton);
        country= findViewById(R.id.SpinnerContactCountryUpdated);
        List<String> countries = new ArrayList<>();
        ContactModel p = GlobalVars.getInstance().getTempContactHolderForView().getNewContact();
        ContactModel oldP = GlobalVars.getInstance().getTempContactHolderForView();

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countrySelected = parent.getItemAtPosition(position).toString();
                if (!countrySelected.equals(oldP.getCountry())) {
                    oldP.setCountryModified(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        GlobalVars.getInstance().getApolloClient().query(CountriesListQuery.builder().build())
                .enqueue(new ApolloCall.Callback<CountriesListQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<CountriesListQuery.Data> response) {
                        if (response.hasErrors()){
                            Toast.makeText(ContactsUpdate.this, "Error getting countries, try again later", Toast.LENGTH_SHORT).show();
                        } else {
                            for (CountriesListQuery.Country c: response.getData().countries() ) {
                                countries.add(c.name());
                            }

                            dataAdapter = new ArrayAdapter<>(getApplicationContext(),
                                    android.R.layout.simple_spinner_item, countries);

                            runOnUiThread(() -> {

                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                country.setAdapter(dataAdapter);

                                ContactModel p = GlobalVars.getInstance().getTempContactHolderForView().getNewContact();
                                country.setSelection(dataAdapter.getPosition(p.getCountry()));
                                oldP.setCountryModified(false);

                            });
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Toast.makeText(ContactsUpdate.this, "Error getting countries, try again later", Toast.LENGTH_SHORT).show();
                    }
                });

        identification.setText(p.getIdentification());
        firstName.setText(p.getFirstName());
        lastName.setText(p.getLastName());
        age.setText(String.valueOf(p.getAge()));
        region.setText(p.getRegion());
        email.setText(p.getEmail());
        address.setText(p.getAddress());

        identification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                oldP.setIdentificationModified(true);
            }
        });

        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                oldP.setFirstNameModified(true);
            }
        });

        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                oldP.setLastNameModified(true);
            }
        });

        age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                oldP.setAgeModified(true);
            }
        });

        region.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                oldP.setRegionModified(true);
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                oldP.setEmailModified(true);
            }
        });

        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                oldP.setAddressModified(true);
            }
        });

        cancel.setOnClickListener((v) -> finish());


        pathologies.setOnClickListener((v) -> {
            Intent i = new Intent(ContactsUpdate.this, ContactPathologyUpdate.class);
            startActivity(i);
        });

        save.setOnClickListener((v) -> {

            if(identification.getText().toString().length() == 0){
                identification.setError("You must enter a identification code");
                return;
            }

            if(firstName.getText().toString().length() == 0){
                firstName.setError("You must enter a name");
                return;
            }

            if(lastName.getText().toString().length() == 0){
                lastName.setError("You must enter a last name");
                return;
            }

            if(age.getText().toString().length() == 0){
                age.setError("You must enter an age");
                return;
            }

            if(email.getText().toString().length() == 0){
                email.setError("You must enter a email");
                return;
            }

            if(address.getText().toString().length() == 0){
                address.setError("You must enter an address");
                return;
            }

            if(region.getText().toString().length() == 0){
                region.setError("You must enter a region");
                return;
            }

            DatabaseHandler db = GlobalVars.getInstance().getDatabase();

            if (!oldP.isIdentificationModified() &&
                    !oldP.isFirstNameModified() &&
                    !oldP.isAgeModified() &&
                    !oldP.isAddressModified() &&
                    !oldP.isCountryModified() &&
                    !oldP.isRegionModified() &&
                    !oldP.isEmailModified() &&
                    !oldP.isPathologyModified() &&
                    !oldP.isLastNameModified()) {
                Toast.makeText(this, "Nothing to change", Toast.LENGTH_SHORT).show();
                return;
            }

            Integer updateClean = db.deleteUpdatedContacts(oldP.getIdentification());
            Integer pathologyClean = db.deleteUpdatedContactPathology(oldP.getIdentification());

            StringBuilder clean = new StringBuilder();

            if (updateClean > 0) {
                clean.append("Contacts DB cleaned\n");
            } else {
                clean.append("Contacts DB not cleaned or empty\n");
            }

            if (pathologyClean > 0) {
                clean.append("Pathologies DB cleaned\n");
            } else {
                clean.append("Pathologies DB not cleaned or empty\n");
            }

            Toast.makeText(this, clean.toString(), Toast.LENGTH_SHORT).show();



            boolean inserted = db.insertUpdatedContacts(oldP.getIdentification(),
                    oldP.isIdentificationModified() ? identification.getText().toString() : null,
                    oldP.isFirstNameModified() ? firstName.getText().toString() : null,
                    oldP.isLastNameModified() ? lastName.getText().toString() : null,
                    null,
                    oldP.isRegionModified() ? region.getText().toString() : null,
                    oldP.isAddressModified() ? address.getText().toString() : null,
                    oldP.isEmailModified() ? email.getText().toString() : null,
                    oldP.isAgeModified() ? age.getText().toString() : null,
                    oldP.isCountryModified() ? countrySelected : null);

            StringBuilder insertData = new StringBuilder();

            if (inserted) {
                insertData.append("Contact saved\n");
            } else {
                insertData.append("Contact not saved\n");
            }

            boolean pathoUpdatedInserted = true;

            for (String s: p.getPathologyNames()) {
                boolean ins = db.insertUpdatedContactPathology(s, oldP.getIdentification());

                if (!ins) {
                    pathoUpdatedInserted = false;
                }
            }

            if (pathoUpdatedInserted) {
                insertData.append("Pathologies saved\n");
            } else {
                insertData.append("Some pathologies were not saved\n");
            }

            Toast.makeText(this, insertData.toString(), Toast.LENGTH_LONG).show();

        });


    }
}