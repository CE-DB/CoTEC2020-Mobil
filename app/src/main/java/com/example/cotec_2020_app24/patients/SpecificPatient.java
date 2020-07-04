package com.example.cotec_2020_app24.patients;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PatientsQuery;
import com.example.cotec_2020_app24.DatabaseHandler;
import com.example.cotec_2020_app24.GlobalVars;
import com.example.cotec_2020_app24.R;
import com.example.cotec_2020_app24.models.PatientModel;
import com.example.cotec_2020_app24.models.PatientState;

import java.util.List;
import java.util.stream.Collectors;

public class SpecificPatient extends AppCompatActivity {

    private TextView patientId;
    private TextView patientName;
    private TextView patientLastname;
    private TextView patientAge;
    private TextView patientCountry;
    private TextView patientDate;
    private TextView patienthospitalized;
    private TextView patientICU;
    private TextView patientNationality;
    private TextView patientRegion;
    private TextView patientState;
    private TextView patientPathologies;
    private TextView patientMedications;
    private Button patientUpdate;
    private Button patientDelete;
    private Button patientPathologiesButton;
    private Button patientMedicationsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_patient);

        patientId = findViewById(R.id.detailPatientIDContent);
        patientName = findViewById(R.id.detailPatientNameContent);
        patientLastname = findViewById(R.id.detailPatientLastNameContent);
        patientAge = findViewById(R.id.detailPatientAgeContent);
        patientCountry = findViewById(R.id.detailPatientCountryContent);
        patientDate = findViewById(R.id.detailPatientDateOfAdmissionContent);
        patienthospitalized = findViewById(R.id.detailPatientHospitalizedContent);
        patientICU = findViewById(R.id.detailPatientICUContent);
        patientNationality = findViewById(R.id.detailPatientNationalityContent);
        patientRegion = findViewById(R.id.detailPatientRegionContent);
        patientState = findViewById(R.id.detailPatientStateContent);
        patientPathologies = findViewById(R.id.detailPatientPathologyContent);
        patientMedications = findViewById(R.id.detailPatientMedicationContent);
        patientPathologiesButton = findViewById(R.id.PatientPathologiesButton);
        patientMedicationsButton = findViewById(R.id.PatientMedicationsButton);
        patientDelete = findViewById(R.id.PatientDeleteButton);
        patientUpdate = findViewById(R.id.PatientUpdateButton);
        DatabaseHandler db = GlobalVars.getInstance().getDatabase();

        PatientModel p = GlobalVars.getInstance().getTempPatientHolderForView();

        if (p.getSyncState().equals(PatientState.DELETED)){
            patientDelete.setText("Undelete");
            patientUpdate.setEnabled(false);
        }

        patientDelete.setOnClickListener((v) -> {
            if (p.getSyncState().equals(PatientState.DELETED)){
                Integer deletedcount = db.deleteDeletedPatients(p.getIdentification());

                if (deletedcount > 0) {
                    patientDelete.setText("Delete");
                    patientUpdate.setEnabled(true);
                } else {
                    Toast.makeText(this, "Patient not restored, try again", Toast.LENGTH_SHORT).show();
                }

            } else {

                boolean inserted = db.insertDeletedPatients(p.getIdentification());

                if (inserted) {

                    db.deleteUpdatedPatientsMedication(p.getIdentification());
                    db.deleteUpdatedPatientsPathology(p.getIdentification());
                    db.deleteCreatedContactVisit(p.getIdentification());
                    db.deleteUpdatedContactVisit(p.getIdentification());
                    db.deleteDeletedContactVisit(p.getIdentification());
                    db.deleteUpdatedPatients(p.getIdentification());

                    patientDelete.setText("Undelete");
                    patientUpdate.setEnabled(false);
                } else {
                    Toast.makeText(this, "Patient not deleted, try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        patientUpdate.setOnClickListener(v -> {
            Intent intent= new Intent(SpecificPatient.this, PatientsUpdate.class);
            startActivity(intent);
        });




        if (p.isIdentificationModified()) {
            patientId.setText(p.getIdentification()
                    .concat("\n(")
                    .concat("New: ")
                    .concat(p.getNewPatient().getIdentification())
                    .concat(")"));
        } else {
            patientId.setText(p.getIdentification());
        }

        if (p.isFirstNameModified()) {
            patientName.setText(p.getFirstName()
                    .concat(" (")
                    .concat("New: ")
                    .concat(p.getNewPatient().getFirstName())
                    .concat(")"));
        } else {
            patientName.setText(p.getFirstName());
        }

        if (p.isLastNameModified()) {
            patientLastname.setText(p.getLastName()
                    .concat(" (")
                    .concat("New: ")
                    .concat(p.getNewPatient().getLastName())
                    .concat(")"));
        } else {
            patientLastname.setText(p.getLastName());
        }

        if (p.isAgeModified()) {
            patientAge.setText(String.valueOf(p.getAge())
                    .concat("(")
                    .concat("New: ")
                    .concat(String.valueOf(p.getNewPatient().getAge()))
                    .concat(")"));
        } else {
            patientAge.setText(String.valueOf(p.getAge()));
        }

        if (p.isCountryModified()) {
            patientCountry.setText(p.getCountry()
                    .concat("\n(")
                    .concat("New: ")
                    .concat(p.getNewPatient().getCountry())
                    .concat(")"));
        } else {
            patientCountry.setText(p.getCountry());
        }

        if (p.isHospitalizedModified()) {
            patienthospitalized.setText(p.getHospitalized()
                    .concat(" (")
                    .concat("New: ")
                    .concat(p.getNewPatient().getHospitalized())
                    .concat(")"));
        } else {
            patienthospitalized.setText(p.getHospitalized());
        }

        if (p.isIcuModified()) {
            patientICU.setText(p.getIcu()
                    .concat(" (")
                    .concat("New: ")
                    .concat(p.getNewPatient().getIcu())
                    .concat(")"));
        } else {
            patientICU.setText(p.getIcu());
        }

        if (p.isRegionModified()) {
            patientRegion.setText(p.getRegion()
                    .concat(" (")
                    .concat("New: ")
                    .concat(p.getNewPatient().getRegion())
                    .concat(")"));
        } else {
            patientRegion.setText(p.getRegion());
        }

        if (p.isStateModified()) {
            patientState.setText(p.getState()
                    .concat(" (")
                    .concat("New: ")
                    .concat(p.getNewPatient().getState())
                    .concat(")"));
        } else {
            patientState.setText(p.getState());
        }

        if (p.isDateEntranceModified()) {
            patientDate.setText(p.getDateEntrance()
                    .concat("\n(")
                    .concat("New: ")
                    .concat(p.getNewPatient().getDateEntrance())
                    .concat(")"));
        } else {
            patientDate.setText(p.getDateEntrance());
        }

        patientNationality.setText(p.getNationality());

        Button contactsButton = findViewById(R.id.PatientContactsButton);

        if (!p.getContacts().isEmpty()){
            contactsButton.setVisibility(Button.VISIBLE);

            contactsButton.setOnClickListener((v) -> {

                Intent i = new Intent(this, PatientContacts.class);
                this.startActivity(i);

            });
        }

        StringBuilder pathologies = new StringBuilder();

        if (!p.getPathologies().isEmpty()){
            patientPathologiesButton.setVisibility(Button.VISIBLE);

            patientPathologiesButton.setOnClickListener((v) -> {

                Intent i = new Intent(this, PatientPathologies.class);
                this.startActivity(i);

            });
        }

        for (PatientsQuery.Pathology path: p.getPathologies()) {
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

        if (p.isPatholoigesModified()) {
            StringBuilder pathDeleted = new StringBuilder();
            StringBuilder pathAdded = new StringBuilder();

            List<String> addedPath = p.getNewPatient().getPathologyNames()
                    .stream()
                    .filter(elm -> !p.getPathologyNames().contains(elm))
                    .collect(Collectors.toList());

            List<String> removedPath = p.getPathologyNames()
                    .stream()
                    .filter(elm -> !p.getNewPatient().getPathologyNames().contains(elm))
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

        patientPathologies.setText(pathologies.toString());




        StringBuilder medications = new StringBuilder();

        if (!p.getMedications().isEmpty()){
            patientMedicationsButton.setVisibility(Button.VISIBLE);

            patientMedicationsButton.setOnClickListener((v) -> {

                Intent i = new Intent(this, PatientMedications.class);
                this.startActivity(i);

            });
        }

        for (PatientsQuery.Medication path: p.getMedications()) {
            if(p.getMedications().indexOf(path) == 0){
                if(p.getMedications().size() == 1){
                    medications.append(path.name());
                } else {
                    medications.append(path.name()).append(", ");
                }
            } else if (p.getMedications().indexOf(path) == p.getMedications().size()-1){
                medications.append(path.name());
            } else {
                medications.append(path.name()).append(", ");
            }
        }

        if (p.isMedicationsModified()) {
            StringBuilder medicaDeleteStr = new StringBuilder();
            StringBuilder medicaAddedStr = new StringBuilder();

            List<String> medicaAddedList = p.getNewPatient().getMedicationNames()
                    .stream()
                    .filter(elm -> !p.getMedicationNames().contains(elm))
                    .collect(Collectors.toList());

            List<String> removedMedicaList = p.getMedicationNames()
                    .stream()
                    .filter(elm -> !p.getNewPatient().getMedicationNames().contains(elm))
                    .collect(Collectors.toList());

            for (String s: medicaAddedList) {
                if(medicaAddedList.indexOf(s) == 0){
                    if(medicaAddedList.size() == 1){
                        medicaAddedStr.append(s);
                    } else {
                        medicaAddedStr.append(s).append(", ");
                    }
                } else if (medicaAddedList.indexOf(s) == medicaAddedList.size()-1){
                    medicaAddedStr.append(s);
                } else {
                    medicaAddedStr.append(s).append(", ");
                }
            }

            for (String s: removedMedicaList) {
                if(removedMedicaList.indexOf(s) == 0){
                    if(removedMedicaList.size() == 1){
                        medicaDeleteStr.append(s);
                    } else {
                        medicaDeleteStr.append(s).append(", ");
                    }
                } else if (removedMedicaList.indexOf(s) == removedMedicaList.size()-1){
                    medicaDeleteStr.append(s);
                } else {
                    medicaDeleteStr.append(s).append(", ");
                }
            }

            medications.append("\n")
                    .append("(New: ")
                    .append(medicaAddedStr.toString())
                    .append(")\n")
                    .append("(Removed: ")
                    .append(medicaDeleteStr.toString())
                    .append(")");
        }

        patientMedications.setText(medications.toString());
    }
}