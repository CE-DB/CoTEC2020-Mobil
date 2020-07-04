package com.example.cotec_2020_app24.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ContactsQuery;
import com.example.cotec_2020_app24.DatabaseHandler;
import com.example.cotec_2020_app24.GlobalVars;
import com.example.cotec_2020_app24.R;
import com.example.cotec_2020_app24.models.ContactModel;
import com.example.cotec_2020_app24.models.PatientState;

import java.util.List;
import java.util.stream.Collectors;

public class SpecificContact extends AppCompatActivity {

    private TextView ContactId;
    private TextView ContactName;
    private TextView ContactLastname;
    private TextView ContactAge;
    private TextView ContactCountry;
    private TextView ContactNationality;
    private TextView ContactRegion;
    private TextView ContactPathologies;
    private TextView ContactEmail;
    private TextView ContactAddress;
    private Button ContactUpdate;
    private Button ContactDelete;
    private Button ContactPathologiesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_contact);

        ContactId = findViewById(R.id.detailContactIDContent);
        ContactName = findViewById(R.id.detailContactNameContent);
        ContactLastname = findViewById(R.id.detailContactLastNameContent);
        ContactAge = findViewById(R.id.detailContactAgeContent);
        ContactCountry = findViewById(R.id.detailContactCountryContent);
        ContactNationality = findViewById(R.id.detailContactNationalityContent);
        ContactRegion = findViewById(R.id.detailContactRegionContent);
        ContactPathologies = findViewById(R.id.detailContactPathologyContent);
        ContactPathologiesButton = findViewById(R.id.ContactPathologiesButton);
        ContactDelete = findViewById(R.id.ContactDeleteButton);
        ContactUpdate = findViewById(R.id.ContactUpdateButton);
        ContactEmail = findViewById(R.id.detailContactEmailContent);
        ContactAddress = findViewById(R.id.detailContactAddressContent);
        DatabaseHandler db = GlobalVars.getInstance().getDatabase();

        ContactModel p = GlobalVars.getInstance().getTempContactHolderForView();

        if (p.getSyncState().equals(PatientState.DELETED)){
            ContactDelete.setText("Undelete");
            ContactUpdate.setEnabled(false);
        }

        ContactDelete.setOnClickListener((v) -> {
            if (p.getSyncState().equals(PatientState.DELETED)){
                Integer deletedcount = db.deleteDeletedContacts(p.getIdentification());

                if (deletedcount > 0) {
                    ContactDelete.setText("Delete");
                    ContactUpdate.setEnabled(true);
                } else {
                    Toast.makeText(this, "Contact not restored, try again", Toast.LENGTH_SHORT).show();
                }

            } else {

                boolean inserted = db.insertDeletedContacts(p.getIdentification());

                if (inserted) {

                    db.deleteUpdatedContactPathology(p.getIdentification());
                    db.deleteUpdatedContacts(p.getIdentification());

                    ContactDelete.setText("Undelete");
                    ContactUpdate.setEnabled(false);
                } else {
                    Toast.makeText(this, "Contact not deleted, try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ContactUpdate.setOnClickListener(v -> {
            Intent intent= new Intent(SpecificContact.this, ContactsUpdate.class);
            startActivity(intent);
        });

        if (p.isIdentificationModified()) {
            ContactId.setText(p.getIdentification()
                    .concat("\n(")
                    .concat("New: ")
                    .concat(p.getNewContact().getIdentification())
                    .concat(")"));
        } else {
            ContactId.setText(p.getIdentification());
        }

        if (p.isFirstNameModified()) {
            ContactName.setText(p.getFirstName()
                    .concat(" (")
                    .concat("New: ")
                    .concat(p.getNewContact().getFirstName())
                    .concat(")"));
        } else {
            ContactName.setText(p.getFirstName());
        }

        if (p.isLastNameModified()) {
            ContactLastname.setText(p.getLastName()
                    .concat(" (")
                    .concat("New: ")
                    .concat(p.getNewContact().getLastName())
                    .concat(")"));
        } else {
            ContactLastname.setText(p.getLastName());
        }

        if (p.isAgeModified()) {
            ContactAge.setText(String.valueOf(p.getAge())
                    .concat("(")
                    .concat("New: ")
                    .concat(String.valueOf(p.getNewContact().getAge()))
                    .concat(")"));
        } else {
            ContactAge.setText(String.valueOf(p.getAge()));
        }

        if (p.isCountryModified()) {
            ContactCountry.setText(p.getCountry()
                    .concat("\n(")
                    .concat("New: ")
                    .concat(p.getNewContact().getCountry())
                    .concat(")"));
        } else {
            ContactCountry.setText(p.getCountry());
        }

        if (p.isRegionModified()) {
            ContactRegion.setText(p.getRegion()
                    .concat(" (")
                    .concat("New: ")
                    .concat(p.getNewContact().getRegion())
                    .concat(")"));
        } else {
            ContactRegion.setText(p.getRegion());
        }

        if (p.isEmailModified()) {
            ContactEmail.setText(p.getEmail()
                    .concat(" (")
                    .concat("New: ")
                    .concat(p.getNewContact().getEmail())
                    .concat(")"));
        } else {
            ContactEmail.setText(p.getEmail());
        }

        if (p.isAddressModified()) {
            ContactAddress.setText(p.getAddress()
                    .concat(" (")
                    .concat("New: ")
                    .concat(p.getNewContact().getAddress())
                    .concat(")"));
        } else {
            ContactAddress.setText(p.getAddress());
        }

        ContactNationality.setText(p.getNationality());

        if (!p.getPathologies().isEmpty()){
            ContactPathologiesButton.setVisibility(Button.VISIBLE);

            ContactPathologiesButton.setOnClickListener((v) -> {

                Intent i = new Intent(this, ContactPathologies.class);
                this.startActivity(i);

            });
        }

        StringBuilder pathologies = new StringBuilder();

        for (ContactsQuery.Pathology path: p.getPathologies()) {
            if(p.getPathologies().indexOf(path) == 0){
                if(p.getPathologies().size() == 1){
                    pathologies.append(path.name());
                } else {
                    pathologies.append(path.name()).append(", ");
                }
            } else if (p.getPathologies().indexOf(path) == p.getPathologies().size()-1){
                pathologies.append(path.name());
            } else {
                pathologies.append(path.name()).append(", ");
            }
        }

        if (p.isPathologyModified()) {
            StringBuilder pathDeleted = new StringBuilder();
            StringBuilder pathAdded = new StringBuilder();

            List<String> addedPath = p.getNewContact().getPathologyNames()
                    .stream()
                    .filter(elm -> !p.getPathologyNames().contains(elm))
                    .collect(Collectors.toList());

            List<String> removedPath = p.getPathologyNames()
                    .stream()
                    .filter(elm -> !p.getNewContact().getPathologyNames().contains(elm))
                    .collect(Collectors.toList());

            for (String s: addedPath) {
                if(addedPath.indexOf(s) == 0){
                    if(addedPath.size() == 1){
                        pathAdded.append(s);
                    } else {
                        pathAdded.append(s).append(", ");
                    }
                } else if (addedPath.indexOf(s) == addedPath.size()-1){
                    pathAdded.append(s);
                } else {
                    pathAdded.append(s).append(", ");
                }
            }

            for (String s: removedPath) {
                if(removedPath.indexOf(s) == 0){
                    if(removedPath.size() == 1){
                        pathDeleted.append(s);
                    } else {
                        pathDeleted.append(s).append(", ");
                    }
                } else if (removedPath.indexOf(s) == removedPath.size()-1){
                    pathDeleted.append(s);
                } else {
                    pathDeleted.append(s).append(", ");
                }
            }

            pathologies.append("\n")
                    .append("(New: ")
                    .append(pathAdded.toString())
                    .append(")\n")
                    .append("(Removed: ")
                    .append(pathDeleted.toString())
                    .append(")");
        }

        ContactPathologies.setText(pathologies.toString());

    }
}