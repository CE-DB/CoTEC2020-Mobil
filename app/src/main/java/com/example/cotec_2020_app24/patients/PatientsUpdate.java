package com.example.cotec_2020_app24.patients;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.CountriesListQuery;
import com.example.cotec_2020_app24.DatabaseHandler;
import com.example.cotec_2020_app24.GlobalVars;
import com.example.cotec_2020_app24.R;
import com.example.cotec_2020_app24.models.ContactVisit;
import com.example.cotec_2020_app24.models.PatientModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PatientsUpdate extends AppCompatActivity {

    private DatePickerDialog picker;
    private EditText datePicker;
    private EditText identification;
    private EditText firstName;
    private EditText lastName;
    private EditText age;
    private EditText state;
    private EditText region;
    private CheckBox hospitalized;
    private CheckBox icu;
    private Button contacts;
    private Button medications;
    private Button pathologies;
    private Button cancel;
    private Button save;
    private Spinner country;
    String countrySelected = "";
    String date = "";
    ArrayAdapter<String> dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_update);

        datePicker = findViewById(R.id.editTextPatientEntranceDateUpdated);
        identification = findViewById(R.id.editTextPatientIdUpdate);
        firstName = findViewById(R.id.editTextPatientNameUpdated);
        lastName = findViewById(R.id.editTextPatientLastNameUpdated);
        age = findViewById(R.id.editTextPatientAgeUpdated);
        state = findViewById(R.id.editTextPatientStateUpdated);
        region = findViewById(R.id.editTextPatientRegionUpdated);
        hospitalized = findViewById(R.id.checkBoxPatientHospitalizedUpdated);
        icu = findViewById(R.id.checkBoxPatientICUUpdated);
        contacts = findViewById(R.id.PatientContactsUpdatedButton);
        pathologies = findViewById(R.id.PatientPathologiesUpdatedButton);
        medications = findViewById(R.id.PatientMedicationsUpdatedButton);
        cancel = findViewById(R.id.PatientCancelUpdatedButton);
        save = findViewById(R.id.PatientSaveUpdatedButton);
        country= findViewById(R.id.SpinnerPatientCountryUpdated);
        List<String> countries = new ArrayList<>();
        PatientModel p = GlobalVars.getInstance().getTempPatientHolderForView().getNewPatient();
        PatientModel oldP = GlobalVars.getInstance().getTempPatientHolderForView();

        contacts.setOnClickListener(v -> {

            Intent i = new Intent(PatientsUpdate.this, PatientContactUpdate.class);
            startActivity(i);

        });

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
                            Toast.makeText(PatientsUpdate.this, "Error getting countries, try again later", Toast.LENGTH_SHORT).show();
                        } else {
                            for (CountriesListQuery.Country c: response.getData().countries() ) {
                                countries.add(c.name());
                            }

                            dataAdapter = new ArrayAdapter<>(getApplicationContext(),
                                    android.R.layout.simple_spinner_item, countries);

                            runOnUiThread(() -> {

                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                country.setAdapter(dataAdapter);

                                PatientModel p = GlobalVars.getInstance().getTempPatientHolderForView().getNewPatient();
                                country.setSelection(dataAdapter.getPosition(p.getCountry()));
                                oldP.setCountryModified(false);

                            });
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Toast.makeText(PatientsUpdate.this, "Error getting countries, try again later", Toast.LENGTH_SHORT).show();
                    }
                });

        identification.setText(p.getIdentification());
        firstName.setText(p.getFirstName());
        lastName.setText(p.getLastName());
        age.setText(String.valueOf(p.getAge()));
        region.setText(p.getRegion());
        state.setText(p.getState());
        hospitalized.setChecked(p.getHospitalized().equals("true"));
        icu.setChecked(p.getIcu().equals("true"));
        date = p.getDateEntrance();

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

        state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                oldP.setStateModified(true);
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

        datePicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                oldP.setDateEntranceModified(true);
            }
        });

        hospitalized.setOnCheckedChangeListener((buttonView, isChecked) -> {
            oldP.setHospitalizedModified(true);
        });

        icu.setOnCheckedChangeListener((buttonView, isChecked) -> {
            oldP.setIcuModified(true);
        });

        datePicker.setText(date);
        oldP.setDateEntranceModified(false);
        datePicker.setOnClickListener(v -> {

            int day = Integer.parseInt(((EditText) v).getText().toString().split("-")[2]);
            int month = Integer.parseInt(((EditText) v).getText().toString().split("-")[1]);
            int year = Integer.parseInt(((EditText) v).getText().toString().split("-")[0]);

            picker = new DatePickerDialog(PatientsUpdate.this,
                    (view, year1, month1, dayOfMonth) -> {

                        datePicker.setText(year1 + "-" + month1 + "-" + dayOfMonth);

                    }, year, month, day);

            picker.show();

        });


        cancel.setOnClickListener((v) -> finish());

        save.setOnClickListener((v) -> {

            if (!checkFieldsFilled()) {
                return;
            }

            DatabaseHandler db = GlobalVars.getInstance().getDatabase();

            if (!oldP.isIdentificationModified() &&
            !oldP.isFirstNameModified() &&
            !oldP.isLastNameModified() &&
            !oldP.isAgeModified() &&
            !oldP.isStateModified() &&
            !oldP.isCountryModified() &&
            !oldP.isRegionModified() &&
            !oldP.isDateEntranceModified() &&
            !oldP.isHospitalizedModified() &&
            !oldP.isIcuModified() &&
            !oldP.isPatholoigesModified() &&
            !oldP.isMedicationsModified() &&
            !oldP.isContactsModified()) {
                Toast.makeText(this, "Nothing to change", Toast.LENGTH_SHORT).show();
                return;
            }

            if (oldP.isIdentificationModified() ||
                    oldP.isFirstNameModified() ||
                    oldP.isLastNameModified() ||
                    oldP.isAgeModified() ||
                    oldP.isStateModified() ||
                    oldP.isCountryModified() ||
                    oldP.isRegionModified() ||
                    oldP.isDateEntranceModified() ||
                    oldP.isHospitalizedModified() ||
                    oldP.isIcuModified() ||
                    oldP.isPatholoigesModified() ||
                    oldP.isMedicationsModified()) {


                Integer updateClean = db.deleteUpdatedPatients(oldP.getIdentification());
                Integer pathologyClean = db.deleteUpdatedPatientsPathology(oldP.getIdentification());
                Integer medicationClean = db.deleteUpdatedPatientsMedication(oldP.getIdentification());

                StringBuilder clean = new StringBuilder();

                if (updateClean > 0) {
                    clean.append("Patients DB cleaned\n");
                } else {
                    clean.append("Patients DB not cleaned or empty\n");
                }

                if (pathologyClean > 0) {
                    clean.append("Pathologies DB cleaned\n");
                } else {
                    clean.append("Pathologies DB not cleaned or empty\n");
                }

                if (medicationClean > 0) {
                    clean.append("Medications DB cleaned");
                } else {
                    clean.append("Medications DB not cleaned or empty");
                }

                Toast.makeText(this, clean.toString(), Toast.LENGTH_SHORT).show();



                boolean inserted = db.insertUpdatedPatients(oldP.getIdentification(),
                        oldP.isIdentificationModified() ? identification.getText().toString() : null,
                        oldP.isFirstNameModified() ? firstName.getText().toString() : null,
                        oldP.isLastNameModified() ? lastName.getText().toString() : null,
                        null,
                        oldP.isRegionModified() ? region.getText().toString() : null,
                        oldP.isIcuModified() ? String.valueOf(icu.isChecked()) : null,
                        oldP.isAgeModified() ? Integer.parseInt(age.getText().toString()) : null,
                        oldP.isHospitalizedModified() ? String.valueOf(hospitalized.isChecked()) : null,
                        oldP.isStateModified() ? state.getText().toString() : null,
                        oldP.isCountryModified() ? countrySelected : null,
                        oldP.isDateEntranceModified() ? datePicker.getText().toString() : null);

                StringBuilder insertData = new StringBuilder();

                if (inserted) {
                    insertData.append("Patient saved\n");
                } else {
                    insertData.append("Patient not saved\n");
                }

                boolean pathoUpdatedInserted = true;

                for (String s: p.getPathologyNames()) {
                    boolean ins = db.insertUpdatedPatientPathology(s, oldP.getIdentification());

                    if (!ins) {
                        pathoUpdatedInserted = false;
                    }
                }

                if (pathoUpdatedInserted) {
                    insertData.append("Pathologies saved\n");
                } else {
                    insertData.append("Some pathologies were not saved\n");
                }

                boolean medicaionsInserted = true;

                for (String s: p.getMedicationNames()) {
                    boolean ins = db.insertUpdatedPatientMedication(s, oldP.getIdentification());

                    if (!ins) {
                        medicaionsInserted = false;
                    }
                }

                if (medicaionsInserted) {
                    insertData.append("Medications saved\n");
                } else {
                    insertData.append("Some medications were not saved\n");
                }

                Toast.makeText(this, insertData.toString(), Toast.LENGTH_LONG).show();

            }



            db.deleteCreatedContactVisit(oldP.getIdentification());
            db.deleteUpdatedContactVisit(oldP.getIdentification());
            db.deleteDeletedContactVisit(oldP.getIdentification());

            for (ContactVisit c: p.getContactVisits()) {

                if (c.getState().equals("NEW") || c.getState().equals("NEWUPDATED")) {
                    db.insertCreatedContactVisit(c.getPatientID(), c.getContactID(), c.getDate());
                } else if (c.getState().equals("UPDATED")) {
                    db.insertUpdatedContactVisit(c.getOldPatientID(),
                            c.getOldContactID(),
                            c.getOldDate(),
                            c.getPatientID(),
                            c.isContactIDModified() ? c.getContactID() : null,
                            c.isContactVisitDateModified() ? c.getDate() : null);
                } else if (c.getState().equals("DELETED")) {
                    db.insertDeletedContactVisit(c.getPatientID(),
                            c.getContactID(),
                            c.getDate());
                }

            }

        });

        pathologies.setOnClickListener((v) -> {
            Intent i = new Intent(PatientsUpdate.this, PatientPathologyUpdate.class);
            startActivity(i);
        });

        medications.setOnClickListener((v) -> {
            Intent i = new Intent(PatientsUpdate.this, PatientMedicationUpdate.class);
            startActivity(i);
        });
    }

    private boolean checkFieldsFilled() {
        if(identification.getText().toString().length() == 0){
            identification.setError("You must enter a identification code");
            return false;
        }

        if(firstName.getText().toString().length() == 0){
            firstName.setError("You must enter a name");
            return false;
        }

        if(lastName.getText().toString().length() == 0){
            lastName.setError("You must enter a last name");
            return false;
        }

        if(age.getText().toString().length() == 0){
            age.setError("You must enter an age");
            return false;
        }

        if(state.getText().toString().length() == 0){
            state.setError("You must enter an state");
            return false;
        }

        if(region.getText().toString().length() == 0){
            region.setError("You must enter a region");
            return false;
        }

        return true;
    }
}