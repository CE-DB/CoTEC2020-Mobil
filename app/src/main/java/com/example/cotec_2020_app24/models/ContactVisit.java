package com.example.cotec_2020_app24.models;

public class ContactVisit {

    private String patientID;
    private String contactID;
    private String date;
    private String state;
    private String oldPatientID;
    private String oldContactID;
    private String oldDate;
    private String fullName;
    private boolean contactIDModified;
    private boolean contactVisitDateModified;

    public ContactVisit(String patientID, String contactID, String date, String state, String oldPatientID, String oldContactID, String oldDate, String fullName) {
        this.patientID = patientID;
        this.contactID = contactID;
        this.date = date;
        this.state = state;
        this.oldPatientID = oldPatientID;
        this.oldContactID = oldContactID;
        this.oldDate = oldDate;
        this.fullName = fullName;
    }

    public ContactVisit(ContactVisit source) {
        this.patientID = source.patientID;
        this.contactID = source.contactID;
        this.date = source.date;
        this.state = source.state;
        this.oldPatientID = source.oldPatientID;
        this.oldContactID = source.oldContactID;
        this.oldDate = source.oldDate;
        this.fullName = source.fullName;
        this.contactVisitDateModified = source.contactVisitDateModified;
        this.contactIDModified = source.contactIDModified;
    }

    public ContactVisit(String patientID) {
        this.contactIDModified = false;
        this.contactVisitDateModified = false;
        this.state = "NEW";
        this.contactID = "";
        this.date = "";
        this.patientID = patientID;
        this.oldContactID = "";
        this.oldPatientID = patientID;
        this.oldDate = "";
    }

    public boolean isAnyEmpty() {
        return this.contactID.isEmpty() || this.date.isEmpty();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isContactIDModified() {
        return contactIDModified;
    }

    public void setContactIDModified(boolean contactIDModified) {
        this.contactIDModified = contactIDModified;
    }

    public boolean isContactVisitDateModified() {
        return contactVisitDateModified;
    }

    public void setContactVisitDateModified(boolean contactVisitDateModified) {
        this.contactVisitDateModified = contactVisitDateModified;
    }

    public String getOldPatientID() {
        return oldPatientID;
    }

    public void setOldPatientID(String oldPatientID) {
        this.oldPatientID = oldPatientID;
    }

    public String getOldContactID() {
        return oldContactID;
    }

    public void setOldContactID(String oldContactID) {
        this.oldContactID = oldContactID;
    }

    public String getOldDate() {
        return oldDate;
    }

    public void setOldDate(String oldDate) {
        this.oldDate = oldDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
