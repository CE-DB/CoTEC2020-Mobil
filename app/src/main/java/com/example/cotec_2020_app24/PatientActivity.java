package com.example.cotec_2020_app24;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class PatientActivity extends AppCompatActivity {
    DatabaseHandler myDB;
    EditText editID, editFirstName, editLastName, editRegion, editNationality, editAge, editState, editDateEntrance;
    Spinner editCountry;
    CheckBox editICU, editHospitalized;
    Button addPatient, selectAllPatients, selectPatientID, selectPatientContact, updatePatients, deletePatients, backButton, insertPathology, insertMedication, insertContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);
        myDB = GlobalVars.getInstance().getDatabase();
        editID = findViewById(R.id.editTextPatientIdCreate);
        editFirstName = findViewById(R.id.editTextPatientNameCreate);
        editLastName = findViewById(R.id.editTextPatientLastNameCreate);
        editRegion = findViewById(R.id.editTextPatientRegionCreate);
        editAge = findViewById(R.id.editTextPatientAgeCreate);
        editState = findViewById(R.id.editTextPatientStateCreate);
        editDateEntrance = findViewById(R.id.editTextPatientEntranceDateCreate);
        editCountry = findViewById(R.id.SpinnerPatientCountryCreate);
        editICU = findViewById(R.id.checkBoxPatientICUCreate);
        editHospitalized = findViewById(R.id.checkBoxPatientHospitalizedCreate);
        addPatient = findViewById(R.id.PatientInsertCreateButton);
        selectAllPatients = findViewById(R.id.PatientSelectCreateButton);
        selectPatientID = findViewById(R.id.PatientSelectIDCreateButton);
        selectPatientContact = findViewById(R.id.PatientSelectContactCreateButton);
        updatePatients = findViewById(R.id.PatientUpdateCreateButton);
        deletePatients = findViewById(R.id.PatientDeleteCreateButton);
        insertPathology = findViewById(R.id.PatientPathologiesCreateButton);
        insertMedication = findViewById(R.id.PatientMedicationsCreateButton);
        insertContact = findViewById(R.id.PatientContactsCreateButton);
        backButton = findViewById(R.id.PatientEjectCreateButton);
        addPatient();
        viewPatients();
        viewPatientByID();
        viewPatientByContact();
        updatePatient();
        deletePatient();
        goToProfile();
        goToVisits();
        showPathologies();
        showMedications();



        
    }
    public void addPatient() {
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDB.insertCreatedPatient(editID.getText().toString(), editFirstName.getText().toString(), editLastName.getText().toString(), editNationality.getText().toString(), editRegion.getText().toString(), String.valueOf(editICU.isChecked()), Integer.parseInt(editAge.getText().toString()), String.valueOf(editHospitalized.isChecked()), editState.getText().toString(), null, null);
                if (isInserted) {
                    Toast.makeText(PatientActivity.this, "Data Inserted!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PatientActivity.this, "Data not Inserted!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void viewPatients() {
        selectAllPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDB.getAllCreatedPatients();
                StringBuffer buffer = new StringBuffer();
                if (res != null) {
                    //show message
                    while (res.moveToNext()) {
                        buffer.append("Id :" + res.getString(0) + "\n");
                        buffer.append("First Name :" + res.getString(1) + "\n");
                        buffer.append("Last Name :" + res.getString(2) + "\n");
                    }
                } else  {
                    showMessage("Error", "Nothing found!");
                    return;
                }
                res.close();
                //show all data
                showMessage("Data ", buffer.toString());
            }
        });
    }
    public void viewPatientByID() {
        selectPatientID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDB.getCreatedPatient(editID.getText().toString());
                StringBuffer buffer = new StringBuffer();
                if (res != null && res.moveToFirst()) {
                    //show message
                    buffer.append("Id :" + res.getString(0) + "\n");
                    buffer.append("First Name :" + res.getString(1) + "\n");
                    buffer.append("Last Name :" + res.getString(2) + "\n");
                } else {
                    showMessage("Error", "Nothing found!");
                    return;
                }
                res.close();
                showMessage("Data ", buffer.toString());
            }
        });
    }
    public void viewPatientByContact() {
        selectPatientContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDB.getCreatedPatient(editDateEntrance.getText().toString());
                StringBuffer buffer = new StringBuffer();
                if (res != null && res.moveToFirst()) {
                    //show message
                    buffer.append("Id :" + res.getString(0) + "\n");
                    buffer.append("First Name :" + res.getString(1) + "\n");
                    buffer.append("Last Name :" + res.getString(2) + "\n");
                } else {
                    showMessage("Error", "Nothing found!");
                    return;
                }
                res.close();
                showMessage("Data ", buffer.toString());
            }
        });
    }
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void updatePatient() {
        updatePatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUpdated = true; //myDB.updatePatients(editID.getText().toString(), editFirstName.getText().toString(), editLastName.getText().toString(), editNationality.getText().toString(), editRegion.getText().toString(), editICU.getText().toString(), editAge.getText().toString(), editHospitalized.getText().toString(), editMedication.getText().toString(), editPathology.getText().toString(), editState.getText().toString(), editContact.getText().toString());
                if (isUpdated) {
                    Toast.makeText(PatientActivity.this, "Data Updated!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PatientActivity.this, "Data not Updated!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void deletePatient() {
        deletePatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer deletedRows = myDB.deleteCreatedPatients(editID.getText().toString());
                if (deletedRows > 0) {
                    Toast.makeText(PatientActivity.this, "Data Deleted!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PatientActivity.this, "Data not Deleted!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void showPathologies() {
        insertPathology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPatientDialog();
            }
        });
    }
    private void showPatientDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.patient_pathology_alert_dialog, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        EditText editID, editPathology;
        editID = findViewById(R.id.editTextPathologyPatientID);
        editPathology = findViewById(R.id.editTextPatientPathologyName);
        Button addPathology, updatePathology, selectAllPathologies, deletePathology;
        addPathology = findViewById(R.id.PatientPathologyInsertCreateButton);
        updatePathology = findViewById(R.id.PatientPathologyUpdateCreateButton);
        selectAllPathologies = findViewById(R.id.PatientPathologySelectCreateButton);
        deletePathology = findViewById(R.id.PatientPathologyDeleteCreateButton);
        addPathology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDB.insertCreatedPatientPathology(editID.getText().toString(), editPathology.getText().toString());
                if (isInserted) {
                    Toast.makeText(PatientActivity.this, "Data Inserted!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PatientActivity.this, "Data not Inserted!", Toast.LENGTH_LONG).show();
                }
            }
        });
        updatePathology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUpdated = true; //myDB.updatePatients(editID.getText().toString(), editFirstName.getText().toString(), editLastName.getText().toString(), editNationality.getText().toString(), editRegion.getText().toString(), editICU.getText().toString(), editAge.getText().toString(), editHospitalized.getText().toString(), editMedication.getText().toString(), editPathology.getText().toString(), editState.getText().toString(), editContact.getText().toString());
                if (isUpdated) {
                    Toast.makeText(PatientActivity.this, "Data Updated!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PatientActivity.this, "Data not Updated!", Toast.LENGTH_LONG).show();
                }
            }
        });
        selectAllPathologies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDB.getAllCreatedPatientsPathology();
                StringBuffer buffer = new StringBuffer();
                if (res != null) {
                    //show message
                    while (res.moveToNext()) {
                        buffer.append("Id :" + res.getString(0) + "\n");
                        buffer.append("Pathology :" + res.getString(1) + "\n");
                    }
                } else  {
                    showMessage("Error", "Nothing found!");
                    return;
                }
                res.close();
                //show all data
                showMessage("Data ", buffer.toString());
            }
        });
        deletePathology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer deletedRows = myDB.deleteCreatedPatientsPathology(editPathology.getText().toString(), editID.getText().toString());
                if (deletedRows > 0) {
                    Toast.makeText(PatientActivity.this, "Data Deleted!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PatientActivity.this, "Data not Deleted!", Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDialog.show();
    }
    public void showMedications() {
        insertMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMedicDialog();
            }
        });
    }
    private void showMedicDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.patient_medication_alert_dialog, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        EditText editID, editMedication;
        editID = findViewById(R.id.editTextMedicationPatientID);
        editMedication = findViewById(R.id.editTextMedicationName);
        Button addMedication, updateMedication, selectAllMedications, deleteMedication;
        addMedication = findViewById(R.id.PatientMedicationInsertCreateButton);
        updateMedication = findViewById(R.id.PatientMedicationUpdateCreateButton);
        selectAllMedications = findViewById(R.id.PatientMedicationSelectCreateButton);
        deleteMedication = findViewById(R.id.PatientMedicationDeleteCreateButton);
        addMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDB.insertCreatedPatientPathology(editID.getText().toString(), editMedication.getText().toString());
                if (isInserted) {
                    Toast.makeText(PatientActivity.this, "Data Inserted!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PatientActivity.this, "Data not Inserted!", Toast.LENGTH_LONG).show();
                }
            }
        });
        updateMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUpdated = true; //myDB.updatePatients(editID.getText().toString(), editFirstName.getText().toString(), editLastName.getText().toString(), editNationality.getText().toString(), editRegion.getText().toString(), editICU.getText().toString(), editAge.getText().toString(), editHospitalized.getText().toString(), editMedication.getText().toString(), editPathology.getText().toString(), editState.getText().toString(), editContact.getText().toString());
                if (isUpdated) {
                    Toast.makeText(PatientActivity.this, "Data Updated!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PatientActivity.this, "Data not Updated!", Toast.LENGTH_LONG).show();
                }
            }
        });
        selectAllMedications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDB.getAllCreatedPatientsPathology();
                StringBuffer buffer = new StringBuffer();
                if (res != null) {
                    //show message
                    while (res.moveToNext()) {
                        buffer.append("Id :" + res.getString(0) + "\n");
                        buffer.append("Medication :" + res.getString(1) + "\n");
                    }
                } else  {
                    showMessage("Error", "Nothing found!");
                    return;
                }
                res.close();
                //show all data
                showMessage("Data ", buffer.toString());
            }
        });
        deleteMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer deletedRows = myDB.deleteCreatedPatientsPathology(editMedication.getText().toString(), editID.getText().toString());
                if (deletedRows > 0) {
                    Toast.makeText(PatientActivity.this, "Data Deleted!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PatientActivity.this, "Data not Deleted!", Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDialog.show();
    }
    public void goToVisits() {
        insertContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientActivity.this, VisitsActivity.class));
                finish();
            }
        });
    }
    public void goToProfile() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientActivity.this, ProfileActivity.class));
                finish();
            }
        });
    }
}