package com.example.cotec_2020_app;

public class Contact {
    int id;
    String firstName;
    String lastName;
    String nationality;
    String region;
    String address;
    String email;
    int age;
    String pathology;
    String hospital;
    String patient;

    public Contact(int id, String firstName, String lastName, String nationality, String region, String address, String email, int age, String pathology, String patient, String hospital) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
        this.region = region;
        this.address = address;
        this.email = email;
        this.age = age;
        this.pathology = pathology;
        this.patient = patient;
        this.hospital = hospital;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPathology() {
        return pathology;
    }

    public void setPathology(String pathology) {
        this.pathology = pathology;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }
}
