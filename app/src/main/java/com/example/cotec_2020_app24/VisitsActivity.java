package com.example.cotec_2020_app24;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VisitsActivity extends AppCompatActivity {
    DatabaseHandler myDB;
    EditText editVisits, editPatientsID, editContactsID;
    Button addVisits, updateVisits, selectVisit, selectAllVisits, deleteVisits;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visits);
        myDB = new DatabaseHandler(this);
        editVisits = findViewById(R.id.editVisit);
        editPatientsID = findViewById(R.id.editPatientID);
        editContactsID = findViewById(R.id.editContactID);
        addVisits = findViewById(R.id.insertButton);
        updateVisits = findViewById(R.id.updateButton);
        selectAllVisits = findViewById(R.id.selectAllButton);
        selectVisit = findViewById(R.id.selectButton);
        deleteVisits = findViewById(R.id.deleteButton);
        addVisit();
        viewVisits();
        viewVisitByID();
        updateVisits();
        deleteVisits();
    }
    public void addVisit() {
        addVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDB.insertCreatedContactVisit(editPatientsID.getText().toString(), editContactsID.getText().toString(), editVisits.getText().toString());
                if (isInserted) {
                    Toast.makeText(VisitsActivity.this, "Data Inserted!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(VisitsActivity.this, "Data not Inserted!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void viewVisits() {
        selectAllVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDB.getAllCreatedContactVisit();
                StringBuffer buffer = new StringBuffer();
                if (res != null && res.moveToFirst()) {
                    //show message
                    buffer.append("Patient ID :" + res.getString(0) + "\n");
                    buffer.append("Contact ID :" + res.getString(1) + "\n");
                    buffer.append("Last Visit :" + res.getString(2) + "\n");
                } else {
                    showMessage("Error", "Nothing found!");
                    return;
                }
                res.close();
                showMessage("Data ", buffer.toString());
            }
        });
    }
    public void viewVisitByID () {
        selectVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDB.getCreatedContactVisit(editPatientsID.getText().toString(), editContactsID.getText().toString(), editVisits.getText().toString());
                StringBuffer buffer = new StringBuffer();
                if (res != null && res.moveToFirst()) {
                    //show message
                    buffer.append("Patient ID :" + res.getString(0) + "\n");
                    buffer.append("Contact ID :" + res.getString(1) + "\n");
                    buffer.append("Last Visit :" + res.getString(2) + "\n");
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
    public void updateVisits() {
        updateVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUpdated = true;
                if (isUpdated) {
                    Toast.makeText(VisitsActivity.this, "Data Updated!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(VisitsActivity.this, "Data not Updated!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void deleteVisits() {
        deleteVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer deletedRows = myDB.deleteCreatedContactVisit(editPatientsID.getText().toString(), editContactsID.getText().toString(), editVisits.getText().toString());
                if (deletedRows > 0) {
                    Toast.makeText(VisitsActivity.this, "Data Deleted!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(VisitsActivity.this, "Data not Deleted!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}