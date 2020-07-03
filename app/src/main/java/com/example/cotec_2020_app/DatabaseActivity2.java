package com.example.cotec_2020_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DatabaseActivity2 extends AppCompatActivity {
    DatabaseHandler myDB;
    EditText editID, editFirstName, editLastName, editNationality, editEmail, editAddress, editRegion, editAge, editPathology, editPatient, editHospital;
    Button createContact, addContact, selectAllContacts, selectContact, updateContacts, deleteContacts, backButton;
    Contact myContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database2);
        myDB = new DatabaseHandler(this);
        editID = findViewById(R.id.editID);
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editNationality = findViewById(R.id.editNationality);
        editRegion = findViewById(R.id.editRegion);
        editAge = findViewById(R.id.editAge);
        editPathology = findViewById(R.id.editPathology);
        editAddress = findViewById(R.id.editAddress);
        editPatient = findViewById(R.id.editPatient);
        editEmail = findViewById(R.id.editEmail);
        editHospital = findViewById(R.id.editHospital);
        createContact = findViewById(R.id.createButton);
        addContact = findViewById(R.id.insertButton);
        selectAllContacts = findViewById(R.id.selectAllButton);
        selectContact = findViewById(R.id.selectButton);
        updateContacts = findViewById(R.id.updateButton);
        deleteContacts = findViewById(R.id.deleteButton);
        backButton = findViewById(R.id.contactsBackButton);
        createContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myContact = new Contact(Integer.parseInt(editID.getText().toString()),
                        editFirstName.getText().toString(),
                        editLastName.getText().toString(),
                        editNationality.getText().toString(),
                        editRegion.getText().toString(),
                        editAddress.getText().toString(),
                        editEmail.getText().toString(),
                        Integer.parseInt(editAge.getText().toString()),
                        editPathology.getText().toString(),
                        editPatient.getText().toString(),
                        editHospital.getText().toString());
            }
        });
        addContact();
        viewContacts();
        updateContact();
        deleteContact();
        goToProfile();
    }
    public void addContact() {
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDB.insertContact(myContact);
                if (isInserted) {
                    Toast.makeText(DatabaseActivity2.this, "Data Inserted!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DatabaseActivity2.this, "Data not Inserted!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void viewContacts() {
        selectAllContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDB.getAllContacts();
                if (res.getCount() == 0) {
                    //show message
                    Toast.makeText(DatabaseActivity2.this, "Error!", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(DatabaseActivity2.this, res.getString(0), Toast.LENGTH_LONG).show();
                Toast.makeText(DatabaseActivity2.this, res.getString(1), Toast.LENGTH_LONG).show();
                Toast.makeText(DatabaseActivity2.this, res.getString(2), Toast.LENGTH_LONG).show();
                Toast.makeText(DatabaseActivity2.this, res.getString(3), Toast.LENGTH_LONG).show();
                Toast.makeText(DatabaseActivity2.this, res.getString(4), Toast.LENGTH_LONG).show();
                Toast.makeText(DatabaseActivity2.this, res.getString(5), Toast.LENGTH_LONG).show();
                Toast.makeText(DatabaseActivity2.this, res.getString(6), Toast.LENGTH_LONG).show();
                Toast.makeText(DatabaseActivity2.this, res.getString(7), Toast.LENGTH_LONG).show();
                Toast.makeText(DatabaseActivity2.this, res.getString(8), Toast.LENGTH_LONG).show();
                Toast.makeText(DatabaseActivity2.this, res.getString(9), Toast.LENGTH_LONG).show();
                Toast.makeText(DatabaseActivity2.this, res.getString(10), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void updateContact() {
        updateContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUpdated = myDB.updateContacts(myContact);
                if (isUpdated) {
                    Toast.makeText(DatabaseActivity2.this, "Data Updated!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DatabaseActivity2.this, "Data not Updated!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void deleteContact() {
        deleteContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer deletedRows = myDB.deleteContacts(myContact.getId());
                if (deletedRows > 0) {
                    Toast.makeText(DatabaseActivity2.this, "Data Deleted!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DatabaseActivity2.this, "Data not Deleted!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void goToProfile() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DatabaseActivity2.this, ProfileActivity.class));
                finish();
            }
        });
    }
}