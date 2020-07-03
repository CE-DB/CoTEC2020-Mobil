package com.example.cotec_2020_app;

public class Patient {
    int id;
    String firstName;
    String lastName;
    String nationality;
    String region;
    String icu;
    int age;
    String hospitalized;
    String medication;
    String pathology;
    String state;
    String contact;

    public Patient(int id, String firstName, String lastName, String nationality, String region, String icu, int age, String hospitalized, String medication, String pathology, String state, String contact) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
        this.region = region;
        this.icu = icu;
        this.age = age;
        this.hospitalized = hospitalized;
        this.medication = medication;
        this.pathology = pathology;
        this.state = state;
        this.contact = contact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getPathology() {
        return pathology;
    }

    public void setPathology(String pathology) {
        this.pathology = pathology;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
