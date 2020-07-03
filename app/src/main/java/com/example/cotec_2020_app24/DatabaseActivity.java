package com.example.cotec_2020_app24;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DatabaseActivity extends AppCompatActivity {
    DatabaseHandler myDB;
    EditText editID, editFirstName, editLastName, editNationality, editRegion, editICU, editAge, editHospitalized, editMedication, editPathology, editState, editContact;
    Button selectPatientID, addPatient, selectAllPatients, selectPatientContact, updatePatients, deletePatients, backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        myDB = new DatabaseHandler(this);
        editID = findViewById(R.id.editID);
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editNationality = findViewById(R.id.editNationality);
        editRegion = findViewById(R.id.editRegion);
        editICU = findViewById(R.id.editICU);
        editAge = findViewById(R.id.editAge);
        editHospitalized = findViewById(R.id.editHospitalized);
        editMedication = findViewById(R.id.editMedication);
        editPathology = findViewById(R.id.editPathology);
        editState = findViewById(R.id.editState);
        editContact = findViewById(R.id.editContact);
        selectPatientContact = findViewById(R.id.selectButtonByContact);
        addPatient = findViewById(R.id.insertButton);
        selectAllPatients = findViewById(R.id.selectAllButton);
        selectPatientID = findViewById(R.id.selectButton);
        updatePatients = findViewById(R.id.updateButton);
        deletePatients = findViewById(R.id.deleteButton);
        backButton = findViewById(R.id.patientsBackButton);
        addPatient();
        viewPatients();
        viewPatientByID();
        viewPatientByContact();
        updatePatient();
        deletePatient();
        goToProfile();
    }
    public void addPatient() {
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDB.insertPatient(editID.getText().toString(), editFirstName.getText().toString(), editLastName.getText().toString(), editNationality.getText().toString(), editRegion.getText().toString(), editICU.getText().toString(), editAge.getText().toString(), editHospitalized.getText().toString(), editMedication.getText().toString(), editPathology.getText().toString(), editState.getText().toString(), editContact.getText().toString());
                if (isInserted) {
                    Toast.makeText(DatabaseActivity.this, "Data Inserted!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DatabaseActivity.this, "Data not Inserted!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void viewPatients() {
        selectAllPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDB.getAllPatients();
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
                Cursor res = myDB.getPatient(editID.getText().toString());
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
                Cursor res = myDB.getPatientByContact(editContact.getText().toString());
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
                boolean isUpdated = myDB.updatePatients(editID.getText().toString(), editFirstName.getText().toString(), editLastName.getText().toString(), editNationality.getText().toString(), editRegion.getText().toString(), editICU.getText().toString(), editAge.getText().toString(), editHospitalized.getText().toString(), editMedication.getText().toString(), editPathology.getText().toString(), editState.getText().toString(), editContact.getText().toString());
                if (isUpdated) {
                    Toast.makeText(DatabaseActivity.this, "Data Updated!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DatabaseActivity.this, "Data not Updated!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void deletePatient() {
        deletePatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer deletedRows = myDB.deletePatients(editID.getText().toString());
                if (deletedRows > 0) {
                    Toast.makeText(DatabaseActivity.this, "Data Deleted!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DatabaseActivity.this, "Data not Deleted!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void goToProfile() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DatabaseActivity.this, ProfileActivity.class));
                finish();
            }
        });
    }
}