package com.example.cotec_2020_app24.models;

import android.database.Cursor;

import com.example.ContactsQuery;
import com.example.PatientsQuery;
import com.example.cotec_2020_app24.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContactModel {

    private String identification;
    private String firstName;
    private String lastName;
    private String nationality;
    private String region;
    private String country;
    private int age;
    private String address;
    private String email;
    private PatientState syncState;
    private ContactModel newContact;
    private List<String> pathologyNames;
    private List<ContactsQuery.Pathology> pathologies;

    private boolean identificationModified;
    private boolean firstNameModified;
    private boolean lastNameModified;
    private boolean nationalityModified;
    private boolean regionModified;
    private boolean countryModified;
    private boolean ageModified;
    private boolean addressModified;
    private boolean emailModified;
    private boolean pathologyModified;

    public ContactModel(ContactsQuery.Contact p) {
        this.identification = p.identification();
        this.firstName = p.firstName();
        this.lastName = p.lastName();
        this.nationality = p.nationality();
        this.region = Objects.requireNonNull(p.origin()).name();
        this.country = Objects.requireNonNull(p.origin().country()).name();
        this.age = Integer.parseInt(Objects.requireNonNull(p.age()).toString());
        this.email = p.email();
        this.address = p.address();
        this.syncState = PatientState.ONLINE;
        this.newContact = null;
        this.pathologies = p.pathologies();

        this.pathologyNames = new ArrayList<>();
        for (ContactsQuery.Pathology pa: pathologies) {
            this.pathologyNames.add(pa.name());
        }

    }

    public ContactModel() {
    }

    public void checkSyncState(DatabaseHandler db) {
        Cursor deleted = db.getDeletedContact(this.identification);

        if (deleted != null && deleted.getCount() > 0) {
            this.syncState = PatientState.DELETED;
        } else {
            this.syncState = PatientState.ONLINE;
        }

        Cursor updated = db.getUpdatedContact(this.identification);

        if (updated != null && updated.getCount() > 0) {

            this.syncState = PatientState.UPDATED;
            this.newContact = new ContactModel();

            while (updated.moveToNext()){
                for (int i = 1; i < updated.getColumnCount(); i++){

                    String data = updated.getString(i);

                    if(updated.getColumnIndex(DatabaseHandler.COL_1_2) == i){
                        if (data == null) {
                            newContact.setIdentification(identification);
                            identificationModified = false;
                        } else {
                            newContact.setIdentification(data);
                            identificationModified = true;
                        }
                    } else if (updated.getColumnIndex(DatabaseHandler.COL_2_2) == i ){
                        if (data == null) {
                            newContact.setFirstName(firstName);
                            firstNameModified = false;
                        } else {
                            newContact.setFirstName(data);
                            firstNameModified = true;
                        }
                    } else if (updated.getColumnIndex(DatabaseHandler.COL_3_2) == i ){
                        if (data == null) {
                            newContact.setLastName(lastName);
                            lastNameModified = false;
                        } else {
                            newContact.setLastName(data);
                            lastNameModified = true;
                        }
                    } else if (updated.getColumnIndex(DatabaseHandler.COL_4_2) == i ){
                        if (data == null) {
                            newContact.setNationality(nationality);
                            nationalityModified = false;
                        } else {
                            newContact.setNationality(data);
                            nationalityModified = true;
                        }
                    } else if (updated.getColumnIndex(DatabaseHandler.COL_5_2) == i ){
                        if (data == null) {
                            newContact.setRegion(region);
                            regionModified = false;
                        } else {
                            newContact.setRegion(data);
                            regionModified = true;
                        }
                    } else if (updated.getColumnIndex(DatabaseHandler.COL_8_2) == i ){
                        if (data == null) {
                            newContact.setAge(age);
                            ageModified = false;
                        } else {
                            newContact.setAge(Integer.parseInt(data));
                            ageModified = true;
                        }
                    } else if (updated.getColumnIndex(DatabaseHandler.COL_12_2) == i ){
                        if (data == null) {
                            newContact.setCountry(country);
                            countryModified = false;
                        } else {
                            newContact.setCountry(data);
                            countryModified = true;
                        }
                    } else if (updated.getColumnIndex(DatabaseHandler.COL_6_2) == i ){
                        if (data == null) {
                            newContact.setAddress(address);
                            addressModified = false;
                        } else {
                            newContact.setAddress(data);
                            addressModified = true;
                        }
                    } else if (updated.getColumnIndex(DatabaseHandler.COL_7_2) == i ){
                        if (data == null) {
                            newContact.setEmail(email);
                            emailModified = false;
                        } else {
                            newContact.setEmail(data);
                            emailModified = true;
                        }
                    }
                }
            }

            Cursor pathUpdated = db.getUpdatedContactPathology(this.identification);

            this.newContact.pathologyNames = new ArrayList<>();

            if (pathUpdated != null && pathUpdated.getCount() > 0){
                pathologyModified = true;
                while (pathUpdated.moveToNext()){
                    newContact.pathologyNames.add(pathUpdated.getString(0));
                }
            } else {
                pathologyModified = false;
                this.newContact.setPathologyNames(new ArrayList<>());
                for (ContactsQuery.Pathology p: pathologies) {
                    this.newContact.pathologyNames.add(p.name());
                }
            }

        } else {
            this.newContact = new ContactModel();
            this.newContact.setIdentification(identification);
            this.newContact.setFirstName(firstName);
            this.newContact.setLastName(lastName);
            this.newContact.setNationality(nationality);
            this.newContact.setRegion(region);
            this.newContact.setCountry(country);
            this.newContact.setAge(age);
            this.newContact.setAddress(address);
            this.newContact.setEmail(email);

            this.newContact.setPathologyNames(new ArrayList<>());
            for (ContactsQuery.Pathology p: pathologies) {
                this.newContact.pathologyNames.add(p.name());
            }

            identificationModified = false;
            firstNameModified = false;
            lastNameModified = false;
            nationalityModified = false;
            regionModified = false;
            countryModified = false;
            ageModified = false;
            pathologyModified = false;
            emailModified = false;
            addressModified = false;
        }
    }

    public void setNewContact(ContactModel newContact) {
        this.newContact = newContact;
    }

    public List<String> getPathologyNames() {
        return pathologyNames;
    }

    public void setPathologyNames(List<String> pathologyNames) {
        this.pathologyNames = pathologyNames;
    }

    public List<ContactsQuery.Pathology> getPathologies() {
        return pathologies;
    }

    public void setPathologies(List<ContactsQuery.Pathology> pathologies) {
        this.pathologies = pathologies;
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

    public boolean isAgeModified() {
        return ageModified;
    }

    public void setAgeModified(boolean ageModified) {
        this.ageModified = ageModified;
    }

    public boolean isAddressModified() {
        return addressModified;
    }

    public void setAddressModified(boolean addressModified) {
        this.addressModified = addressModified;
    }

    public boolean isEmailModified() {
        return emailModified;
    }

    public void setEmailModified(boolean emailModified) {
        this.emailModified = emailModified;
    }

    public boolean isPathologyModified() {
        return pathologyModified;
    }

    public void setPathologyModified(boolean pathologyModified) {
        this.pathologyModified = pathologyModified;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PatientState getSyncState() {
        return syncState;
    }

    public void setSyncState(PatientState syncState) {
        this.syncState = syncState;
    }

    public ContactModel getNewContact() {
        return newContact;
    }
}
