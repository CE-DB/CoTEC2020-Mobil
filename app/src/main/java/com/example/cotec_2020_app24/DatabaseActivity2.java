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

public class DatabaseActivity2 extends AppCompatActivity {
    DatabaseHandler myDB;
    EditText editID, editFirstName, editLastName, editNationality, editEmail, editAddress, editRegion, editAge, editPathology, editPatient, editHospital;
    Button selectContactID, addContact, selectAllContacts, selectContactPatient, updateContacts, deleteContacts, backButton;
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
        selectContactPatient = findViewById(R.id.selectButtonByPatient);
        addContact = findViewById(R.id.insertButton);
        selectAllContacts = findViewById(R.id.selectAllButton);
        selectContactID = findViewById(R.id.selectButton);
        updateContacts = findViewById(R.id.updateButton);
        deleteContacts = findViewById(R.id.deleteButton);
        backButton = findViewById(R.id.contactsBackButton);
        addContact();
        viewContacts();
        viewContactByID();
        viewContactByPatient();
        updateContact();
        deleteContact();
        goToProfile();
    }
    public void addContact() {
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDB.insertContact(editID.getText().toString(), editFirstName.getText().toString(), editLastName.getText().toString(), editNationality.getText().toString(), editRegion.getText().toString(), editAddress.getText().toString(), editEmail.getText().toString(), editAge.getText().toString(),editPathology.getText().toString(), editPatient.getText().toString(), editHospital.getText().toString());
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
                Cursor res = myDB.getContact(editID.getText().toString());
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
    public void viewContactByPatient() {
        selectContactPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDB.getContact(editPatient.getText().toString());
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
                boolean isUpdated = myDB.updateContacts(editID.getText().toString(), editFirstName.getText().toString(), editLastName.getText().toString(), editNationality.getText().toString(), editRegion.getText().toString(), editAddress.getText().toString(), editEmail.getText().toString(), editAge.getText().toString(),editPathology.getText().toString(), editPatient.getText().toString(), editHospital.getText().toString());
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
                Integer deletedRows = myDB.deleteContacts(editID.getText().toString());
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