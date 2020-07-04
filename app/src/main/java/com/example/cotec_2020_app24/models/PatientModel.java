package com.example.cotec_2020_app24.models;

import android.database.Cursor;
import android.util.Log;

import com.example.PatientsQuery;
import com.example.cotec_2020_app24.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PatientModel {

    private String identification;
    private String firstName;
    private String lastName;
    private String nationality;
    private String region;
    private String country;
    private String icu;
    private int age;
    private String hospitalized;
    private String state;
    private String dateEntrance;
    private PatientState syncState;
    private PatientModel newPatient;
    private List<String> pathologyNames;
    private List<PatientsQuery.Pathology> pathologies;
    private List<String> medicationNames;
    private List<PatientsQuery.Medication> medications;
    private List<PatientsQuery.Contact> contacts;
    private List<ContactVisit> contactVisits;

    private boolean identificationModified;
    private boolean firstNameModified;
    private boolean lastNameModified;
    private boolean nationalityModified;
    private boolean regionModified;
    private boolean countryModified;
    private boolean icuModified;
    private boolean ageModified;
    private boolean hospitalizedModified;
    private boolean stateModified;
    private boolean dateEntranceModified;
    private boolean patholoigesModified;
    private boolean medicationsModified;
    private boolean contactsModified;


    public PatientModel(PatientsQuery.Patient p){
        this.identification = p.identification();
        this.firstName = p.firstName();
        this.lastName = p.lastName();
        this.nationality = p.nationality();
        this.region = Objects.requireNonNull(p.region()).name();
        this.country = Objects.requireNonNull(p.country()).name();
        this.icu = String.valueOf(p.intensiveCareUnite());
        this.age = Integer.parseInt(p.age().toString());
        this.hospitalized = String.valueOf(p.hospitalized());
        this.syncState = PatientState.ONLINE;
        this.state = p.state();
        this.newPatient = null;
        this.dateEntrance = p.dateEntrance().toString();
        this.pathologies = p.pathologies();
        this.medications = p.medication();
        this.contacts = p.contacts();

        this.setPathologyNames(new ArrayList<>());
        for (PatientsQuery.Pathology pa: pathologies) {
            this.pathologyNames.add(pa.name());
        }

        this.setMedicationNames(new ArrayList<>());
        for (PatientsQuery.Medication pa: medications) {
            this.medicationNames.add(pa.name());
        }

    }

    public PatientModel() {
    }

    public void checkSyncState(DatabaseHandler db){

        Cursor deleted = db.getDeletedPatient(this.identification);

        if (deleted != null && deleted.getCount() > 0) {
            this.syncState = PatientState.DELETED;
        } else {
            this.syncState = PatientState.ONLINE;
        }

        Cursor updated = db.getUpdatedPatient(this.identification);

        if (updated != null && updated.getCount() > 0) {

            this.syncState = PatientState.UPDATED;
            this.newPatient = new PatientModel();

            while (updated.moveToNext()){
                for (int i = 1; i < updated.getColumnCount(); i++){

                    String data = updated.getString(i);

                    if(updated.getColumnIndex(DatabaseHandler.COL_1_1) == i){
                        if (data == null) {
                            newPatient.setIdentification(identification);
                            identificationModified = false;
                        } else {
                            newPatient.setIdentification(data);
                            identificationModified = true;
                        }
                    } else if (updated.getColumnIndex(DatabaseHandler.COL_2_1) == i ){
                        if (data == null) {
                            newPatient.setFirstName(firstName);
                            firstNameModified = false;
                        } else {
                            newPatient.setFirstName(data);
                            firstNameModified = true;
                        }
                    } else if (updated.getColumnIndex(DatabaseHandler.COL_3_1) == i ){
                        if (data == null) {
                            newPatient.setLastName(lastName);
                            lastNameModified = false;
                        } else {
                            newPatient.setLastName(data);
                            lastNameModified = true;
                        }
                    } else if (updated.getColumnIndex(DatabaseHandler.COL_4_1) == i ){
                        if (data == null) {
                            newPatient.setNationality(nationality);
                            nationalityModified = false;
                        } else {
                            newPatient.setNationality(data);
                            nationalityModified = true;
                        }
                    } else if (updated.getColumnIndex(DatabaseHandler.COL_5_1) == i ){
                        if (data == null) {
                            newPatient.setRegion(region);
                            regionModified = false;
                        } else {
                            newPatient.setRegion(data);
                            regionModified = true;
                        }
                    } else if (updated.getColumnIndex(DatabaseHandler.COL_6_1) == i ){
                        if (data == null) {
                            newPatient.setIcu(icu);
                            icuModified = false;
                        } else {
                            newPatient.setIcu(data);
                            icuModified = true;
                        }
                    } else if (updated.getColumnIndex(DatabaseHandler.COL_7_1) == i ){
                        if (data == null) {
                            newPatient.setAge(age);
                            ageModified = false;
                        } else {
                            newPatient.setAge(Integer.parseInt(data));
                            ageModified = true;
                        }
                    } else if (updated.getColumnIndex(DatabaseHandler.COL_8_1) == i ){
                        if (data == null) {
                            newPatient.setHospitalized(hospitalized);
                            hospitalizedModified = false;
                        } else {
                            newPatient.setHospitalized(data);
                            hospitalizedModified = true;
                        }
                    } else if (updated.getColumnIndex(DatabaseHandler.COL_11_1) == i ){
                        if (data == null) {
                            newPatient.setState(state);
                            stateModified = false;
                        } else {
                            newPatient.setState(data);
                            stateModified = true;
                        }
                    } else if (updated.getColumnIndex(DatabaseHandler.COL_13_1) == i ){
                        if (data == null) {
                            newPatient.setCountry(country);
                            countryModified = false;
                        } else {
                            newPatient.setCountry(data);
                            countryModified = true;
                        }
                    } else if (updated.getColumnIndex(DatabaseHandler.COL_15_1) == i ){
                        if (data == null) {
                            newPatient.setDateEntrance(dateEntrance);
                            dateEntranceModified = false;
                        } else {
                            newPatient.setDateEntrance(data);
                            dateEntranceModified = true;
                        }
                    }
                }
            }

            Cursor pathUpdated = db.getUpdatedPatientsPathology(this.identification);

            this.newPatient.pathologyNames = new ArrayList<>();

            if (pathUpdated != null && pathUpdated.getCount() > 0){
                patholoigesModified = true;
                while (pathUpdated.moveToNext()){
                    newPatient.pathologyNames.add(pathUpdated.getString(0));
                }
            } else {
                patholoigesModified = false;
                this.newPatient.setPathologyNames(new ArrayList<>());
                for (PatientsQuery.Pathology p: pathologies) {
                    this.newPatient.getPathologyNames().add(p.name());
                }
            }

            Cursor medicaUpdated = db.getUpdatedPatientsMedication(this.identification);

            this.newPatient.medicationNames = new ArrayList<>();

            if (medicaUpdated != null && medicaUpdated.getCount() > 0){
                medicationsModified = true;
                while (medicaUpdated.moveToNext()){
                    newPatient.medicationNames.add(medicaUpdated.getString(0));
                }
            } else {
                medicationsModified = false;
                this.newPatient.setMedicationNames(new ArrayList<>());
                for (PatientsQuery.Medication p: medications) {
                    this.newPatient.getMedicationNames().add(p.name());
                }
            }

        } else {
            this.newPatient = new PatientModel();
            this.newPatient.setIdentification(identification);
            this.newPatient.setFirstName(firstName);
            this.newPatient.setLastName(lastName);
            this.newPatient.setNationality(nationality);
            this.newPatient.setRegion(region);
            this.newPatient.setCountry(country);
            this.newPatient.setIcu(icu);
            this.newPatient.setAge(age);
            this.newPatient.setHospitalized(hospitalized);
            this.newPatient.setState(state);
            this.newPatient.setDateEntrance(dateEntrance);

            this.newPatient.setPathologyNames(new ArrayList<>());
            for (PatientsQuery.Pathology p: pathologies) {
                this.newPatient.getPathologyNames().add(p.name());
            }

            this.newPatient.setMedicationNames(new ArrayList<>());
            for (PatientsQuery.Medication p: medications) {
                this.newPatient.getMedicationNames().add(p.name());
            }

            identificationModified = false;
            firstNameModified = false;
            lastNameModified = false;
            nationalityModified = false;
            regionModified = false;
            countryModified = false;
            icuModified = false;
            ageModified = false;
            hospitalizedModified = false;
            stateModified = false;
            dateEntranceModified = false;
            patholoigesModified = false;
            medicationsModified = false;
        }

        this.newPatient.setContactVisits(new ArrayList<>());
        for (PatientsQuery.Contact p: contacts) {
            Cursor contactVisitUpdated = db.getUpdatedContactVisit(identification,
                    p.contact().identification(),
                    p.visitDate().toString());
            Cursor contactVisitDeleted = db.getDeletedContactVisit(identification,
                    p.contact().identification(),
                    p.visitDate().toString());

            String name = p.contact().firstName().concat(" ").concat(p.contact().lastName());

            if (contactVisitUpdated != null && contactVisitUpdated.getCount() == 1){
                this.syncState = PatientState.UPDATED;
                while (contactVisitUpdated.moveToNext()){
                    ContactVisit visit = new ContactVisit(
                            identification,
                            contactVisitUpdated.isNull(contactVisitUpdated.getColumnIndex(DatabaseHandler.COL_2_5))
                                    ? p.contact().identification() :
                                    contactVisitUpdated.getString(contactVisitUpdated.getColumnIndex(DatabaseHandler.COL_2_5)),
                            contactVisitUpdated.isNull(contactVisitUpdated.getColumnIndex(DatabaseHandler.COL_3_5))
                                    ? p.visitDate().toString() :
                                    contactVisitUpdated.getString(contactVisitUpdated.getColumnIndex(DatabaseHandler.COL_3_5)),
                            "UPDATED",
                            identification,
                            p.contact().identification(),
                            p.visitDate().toString(),
                            name);

                    if (contactVisitUpdated.isNull(contactVisitUpdated.getColumnIndex(DatabaseHandler.COL_2_5))) {
                        visit.setContactIDModified(false);
                    } else {
                        visit.setContactIDModified(true);
                    }

                    if (contactVisitUpdated.isNull(contactVisitUpdated.getColumnIndex(DatabaseHandler.COL_3_5))) {
                        visit.setContactVisitDateModified(false);
                    } else {
                        visit.setContactVisitDateModified(true);
                    }

                    newPatient.contactVisits.add(visit);
                }
            } else if (contactVisitDeleted != null && contactVisitDeleted.getCount() == 1) {
                this.syncState = PatientState.UPDATED;
                while (contactVisitDeleted.moveToNext()){
                    newPatient.contactVisits.add(
                            new ContactVisit(
                                    identification,
                                    contactVisitDeleted.getString(contactVisitDeleted.getColumnIndex(DatabaseHandler.COL_2_5)),
                                    contactVisitDeleted.getString(contactVisitDeleted.getColumnIndex(DatabaseHandler.COL_3_5)),
                                    "DELETED",
                                    identification,
                                    p.contact().identification(),
                                    p.visitDate().toString(),
                                    name));
                }
            } else {
                ContactVisit visit = new ContactVisit(
                        identification,
                        p.contact().identification(),
                        p.visitDate().toString(),
                        "ONLINE",
                        identification,
                        p.contact().identification(),
                        p.visitDate().toString(),
                        name);

                visit.setContactVisitDateModified(false);
                visit.setContactIDModified(false);

                newPatient.contactVisits.add(visit);
            }
        }

        Cursor contactVisitNew = db.getCreatedContactVisit(identification);

        if (contactVisitNew != null && contactVisitNew.getCount() > 0){
            this.syncState = PatientState.UPDATED;
            while (contactVisitNew.moveToNext()){
                ContactVisit visit = new ContactVisit(
                        contactVisitNew.getString(contactVisitNew.getColumnIndex(DatabaseHandler.COL_1_5)),
                        contactVisitNew.getString(contactVisitNew.getColumnIndex(DatabaseHandler.COL_2_5)),
                        contactVisitNew.getString(contactVisitNew.getColumnIndex(DatabaseHandler.COL_3_5)),
                        "NEW",
                        contactVisitNew.getString(contactVisitNew.getColumnIndex(DatabaseHandler.COL_1_5)),
                        contactVisitNew.getString(contactVisitNew.getColumnIndex(DatabaseHandler.COL_2_5)),
                        contactVisitNew.getString(contactVisitNew.getColumnIndex(DatabaseHandler.COL_3_5)),
                        "UNKNOWN");

                visit.setContactIDModified(false);
                visit.setContactVisitDateModified(false);

                newPatient.contactVisits.add(visit);
            }
        }

        this.contactVisits = new ArrayList<>();

        for (ContactVisit c: newPatient.getContactVisits()) {
            this.contactVisits.add(new ContactVisit(c));
        }

    }

    public boolean isContactsModified() {
        return contactsModified;
    }

    public void setContactsModified(boolean contactsModified) {
        this.contactsModified = contactsModified;
    }

    public boolean isIdentificationModified() {
        return identificationModified;
    }

    public void setIdentificationModified(boolean identificationModified) {
        this.identificationModified = identificationModified;
    }

    public boolean isFirstNameModified() {
        return firstNameModified;
    }

    public void setFirstNameModified(boolean firstNameModified) {
        this.firstNameModified = firstNameModified;
    }

    public boolean isLastNameModified() {
        return lastNameModified;
    }

    public void setLastNameModified(boolean lastNameModified) {
        this.lastNameModified = lastNameModified;
    }

    public boolean isNationalityModified() {
        return nationalityModified;
    }

    public void setNationalityModified(boolean nationalityModified) {
        this.nationalityModified = nationalityModified;
    }

    public boolean isRegionModified() {
        return regionModified;
    }

    public void setRegionModified(boolean regionModified) {
        this.regionModified = regionModified;
    }

    public boolean isCountryModified() {
        return countryModified;
    }

    public void setCountryModified(boolean countryModified) {
        this.countryModified = countryModified;
    }

    public boolean isIcuModified() {
        return icuModified;
    }

    public void setIcuModified(boolean icuModified) {
        this.icuModified = icuModified;
    }

    public boolean isAgeModified() {
        return ageModified;
    }

    public void setAgeModified(boolean ageModified) {
        this.ageModified = ageModified;
    }

    public boolean isHospitalizedModified() {
        return hospitalizedModified;
    }

    public void setHospitalizedModified(boolean hospitalizedModified) {
        this.hospitalizedModified = hospitalizedModified;
    }

    public boolean isStateModified() {
        return stateModified;
    }

    public void setStateModified(boolean stateModified) {
        this.stateModified = stateModified;
    }

    public boolean isDateEntranceModified() {
        return dateEntranceModified;
    }

    public void setDateEntranceModified(boolean dateEntranceModified) {
        this.dateEntranceModified = dateEntranceModified;
    }

    public boolean isPatholoigesModified() {
        return patholoigesModified;
    }

    public void setPatholoigesModified(boolean patholoigesModified) {
        this.patholoigesModified = patholoigesModified;
    }

    public boolean isMedicationsModified() {
        return medicationsModified;
    }

    public void setMedicationsModified(boolean medicationsModified) {
        this.medicationsModified = medicationsModified;
    }

    public List<ContactVisit> getContactVisits() {
        return contactVisits;
    }

    public void setContactVisits(List<ContactVisit> contactVisits) {
        this.contactVisits = contactVisits;
    }

    public List<String> getPathologyNames() {
        return pathologyNames;
    }

    public void setPathologyNames(List<String> pathologyNames) {
        this.pathologyNames = pathologyNames;
    }

    public List<String> getMedicationNames() {
        return medicationNames;
    }

    public void setMedicationNames(List<String> medicationNames) {
        this.medicationNames = medicationNames;
    }

    public List<PatientsQuery.Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<PatientsQuery.Contact> contacts) {
        this.contacts = contacts;
    }

    public List<PatientsQuery.Pathology> getPathologies() {
        return pathologies;
    }

    public void setPathologies(List<PatientsQuery.Pathology> pathologies) {
        this.pathologies = pathologies;
    }

    public List<PatientsQuery.Medication> getMedications() {
        return medications;
    }

    public void setMedications(List<PatientsQuery.Medication> medications) {
        this.medications = medications;
    }

    public String getDateEntrance() {
        return dateEntrance;
    }

    public void setDateEntrance(String dateEntrance) {
        this.dateEntrance = dateEntrance;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public PatientModel getNewPatient() {
        return newPatient;
    }

    public void setNewPatient(PatientModel newPatient) {
        this.newPatient = newPatient;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIcu() {
        return icu;
    }

    public void setIcu(String icu) {
        this.icu = icu;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHospitalized() {
        return hospitalized;
    }

    public void setHospitalized(String hospitalized) {
        this.hospitalized = hospitalized;
    }

    public PatientState getSyncState() {
        return syncState;
    }

    public void setSyncState(PatientState syncState) {
        this.syncState = syncState;
    }
}
