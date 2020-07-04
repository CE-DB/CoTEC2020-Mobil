package com.example.cotec_2020_app24;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactActivity extends AppCompatActivity {
    DatabaseHandler myDB;
    EditText editID, editFirstName, editLastName, editNationality, editEmail, editAddress, editRegion, editAge, editHospital;
    Button selectContactID, addContact, selectAllContacts, updateContacts, deleteContacts, backButton, insertPathology, insertPatient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        myDB = new DatabaseHandler(this);
        editID = findViewById(R.id.editTextContactIdCreate);
        editFirstName = findViewById(R.id.editTextContactNameCreate);
        editLastName = findViewById(R.id.editTextContactLastNameCreate);
        editRegion = findViewById(R.id.editTextContactRegionCreate);
        editAge = findViewById(R.id.editTextContactAgeCreate);
        editAddress = findViewById(R.id.editTextContactAddressCreate);
        editEmail = findViewById(R.id.editTextContactEmailCreate);
        editHospital = findViewById(R.id.editTextContactHospitalCreate);
        addContact = findViewById(R.id.ContactInsertCreateButton);
        selectAllContacts = findViewById(R.id.ContactSelectCreateButton);
        selectContactID = findViewById(R.id.ContactSelectIDCreateButton);
        updateContacts = findViewById(R.id.ContactUpdateCreateButton);
        deleteContacts = findViewById(R.id.ContactDeleteCreateButton);
        insertPathology = findViewById(R.id.ContactPathologiesCreateButton);
        insertPatient = findViewById(R.id.ContactPatientsCreateButton);
        backButton = findViewById(R.id.ContactEjectCreateButton);
        addContact();
        viewContacts();
        viewContactByID();
        updateContact();
        deleteContact();
        showPathologies();
        goToVisits();
        goToProfile();
    }
    public void addContact() {
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDB.insertCreatedContact(editID.getText().toString(), editFirstName.getText().toString(), editLastName.getText().toString(), editNationality.getText().toString(), editRegion.getText().toString(), editAddress.getText().toString(), editEmail.getText().toString(), editAge.getText().toString(), null);
                if (isInserted) {
                    Toast.makeText(ContactActivity.this, "Data Inserted!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ContactActivity.this, "Data not Inserted!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void viewContacts() {
        selectAllContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDB.getAllCreatedContacts();
                StringBuffer buffer = new StringBuffer();
                if (res != null) {
                    //show message
                    while (res.moveToNext()) {
                        buffer.append("Id :" + res.getString(0) + "\n");
                        buffer.append("First Name :" + res.getString(1) + "\n");
                        buffer.append("Last Name :" + res.getString(2) + "\n");
                    }
                } else {
                    showMessage("Error", "Nothing found!");
                    return;
                }
                res.close();
                showMessage("Data ", buffer.toString());
            }
        });
    }
    public void viewContactByID() {
        selectContactID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDB.getCreatedContact(editID.getText().toString());
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
    public void updateContact() {
        updateContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //boolean isUpdated = myDB.insertUpdatedContacts(null, editID.getText().toString(), editFirstName.getText().toString(), editLastName.getText().toString(), editNationality.getText().toString(), editRegion.getText().toString(), editAddress.getText().toString(), editEmail.getText().toString(), editAge.getText().toString(),editPathology.getText().toString(), null);
                boolean isUpdated = true;
                if (isUpdated) {
                    Toast.makeText(ContactActivity.this, "Data Updated!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ContactActivity.this, "Data not Updated!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void deleteContact() {
        deleteContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer deletedRows = myDB.deleteCreatedContacts(editID.getText().toString());
                if (deletedRows > 0) {
                    Toast.makeText(ContactActivity.this, "Data Deleted!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ContactActivity.this, "Data not Deleted!", Toast.LENGTH_LONG).show();
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
        editID = findViewById(R.id.editTextPathologyContactID);
        editPathology = findViewById(R.id.editTextContactPathologyName);
        Button addPathology, updatePathology, selectAllPathologies, deletePathology;
        addPathology = findViewById(R.id.ContactPathologyInsertCreateButton);
        updatePathology = findViewById(R.id.ContactPathologyUpdateCreateButton);
        selectAllPathologies = findViewById(R.id.ContactPathologySelectCreateButton);
        deletePathology = findViewById(R.id.ContactPathologyDeleteCreateButton);
        addPathology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDB.insertCreatedPatientPathology(editID.getText().toString(), editPathology.getText().toString());
                if (isInserted) {
                    Toast.makeText(ContactActivity.this, "Data Inserted!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ContactActivity.this, "Data not Inserted!", Toast.LENGTH_LONG).show();
                }
            }
        });
        updatePathology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUpdated = true; //myDB.updatePatients(editID.getText().toString(), editFirstName.getText().toString(), editLastName.getText().toString(), editNationality.getText().toString(), editRegion.getText().toString(), editICU.getText().toString(), editAge.getText().toString(), editHospitalized.getText().toString(), editMedication.getText().toString(), editPathology.getText().toString(), editState.getText().toString(), editContact.getText().toString());
                if (isUpdated) {
                    Toast.makeText(ContactActivity.this, "Data Updated!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ContactActivity.this, "Data not Updated!", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(ContactActivity.this, "Data Deleted!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ContactActivity.this, "Data not Deleted!", Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDialog.show();
    }
    public void goToVisits() {
        insertPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactActivity.this, VisitsActivity.class));
                finish();
            }
        });
    }
    public void goToProfile() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactActivity.this, ProfileActivity.class));
                finish();
            }
        });
    }
}