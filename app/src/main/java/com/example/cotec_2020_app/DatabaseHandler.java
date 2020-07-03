package com.example.cotec_2020_app;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "CoTEC-2020-Lite.db";
    public static final String TABLE_NAME_1 = "Patient";
    public static final String COL_1_1 = "ID";
    public static final String COL_2_1 = "First_name";
    public static final String COL_3_1 = "Last_name";
    public static final String COL_4_1 = "Nationality";
    public static final String COL_5_1 = "Region";
    public static final String COL_6_1 = "ICU";
    public static final String COL_7_1 = "Age";
    public static final String COL_8_1 = "Hospitalized";
    public static final String COL_9_1 = "Medication";
    public static final String COL_10_1 = "Pathology";
    public static final String COL_11_1 = "State";
    public static final String COL_12_1 = "Contacts";
    public static final String TABLE_NAME_2 = "Contact";
    public static final String COL_1_2 = "ID";
    public static final String COL_2_2 = "First_name";
    public static final String COL_3_2 = "Last_name";
    public static final String COL_4_2 = "Nationality";
    public static final String COL_5_2 = "Region";
    public static final String COL_6_2 = "Address";
    public static final String COL_7_2 = "Email";
    public static final String COL_8_2 = "Age";
    public static final String COL_9_2 = "Pathology";
    public static final String COL_10_2 = "Patient";
    public static final String COL_11_2 = "Hospital";
    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_1 + "(ID INTEGER PRIMARY KEY, First_name TEXT, Last_name TEXT, Nationality TEXT, Region TEXT, ICU TEXT, Age INTEGER, Hospitalized TEXT, Medication TEXT, Pathology TEXT, State TEXT, Contacts TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_NAME_2 + "(ID INTEGER PRIMARY KEY, First_name TEXT, Last_name TEXT, Nationality TEXT, Region TEXT, Address TEXT, Email TEXT, Age INTEGER, Pathology TEXT, Patient TEXT, Hospital TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        onCreate(db);
    }
    public boolean insertPatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_1, patient.getId());
        contentValues.put(COL_2_1, patient.getFirstName());
        contentValues.put(COL_3_1, patient.getLastName());
        contentValues.put(COL_4_1, patient.getNationality());
        contentValues.put(COL_5_1, patient.getRegion());
        contentValues.put(COL_6_1, patient.getIcu());
        contentValues.put(COL_7_1, patient.getAge());
        contentValues.put(COL_8_1, patient.getHospitalized());
        contentValues.put(COL_9_1, patient.getMedication());
        contentValues.put(COL_10_1, patient.getPathology());
        contentValues.put(COL_11_1, patient.getState());
        long result = db.insert(TABLE_NAME_1, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getAllPatients() {
        List<Patient> patients = new ArrayList<Patient>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_1;
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }
    public Cursor getPatient(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_1 + " WHERE " + COL_1_1 + " = " + id;
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }
    public boolean updatePatients(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_1, patient.getId());
        contentValues.put(COL_2_1, patient.getFirstName());
        contentValues.put(COL_3_1, patient.getLastName());
        contentValues.put(COL_4_1, patient.getNationality());
        contentValues.put(COL_5_1, patient.getRegion());
        contentValues.put(COL_6_1, patient.getIcu());
        contentValues.put(COL_7_1, patient.getAge());
        contentValues.put(COL_8_1, patient.getHospitalized());
        contentValues.put(COL_9_1, patient.getMedication());
        contentValues.put(COL_10_1, patient.getMedication());
        contentValues.put(COL_11_1, patient.getState());
        contentValues.put(COL_12_1, patient.getContact());
        db.update(TABLE_NAME_1, contentValues, "id = ?", new String[] {String.valueOf(patient.getId())});
        return true;
    }
    public Integer deletePatients(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_1, "ID = ?", new String[] {String.valueOf(id)});
    }
    public boolean insertContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_2, contact.getId());
        contentValues.put(COL_2_2, contact.getFirstName());
        contentValues.put(COL_3_2, contact.getLastName());
        contentValues.put(COL_4_2, contact.getNationality());
        contentValues.put(COL_5_2, contact.getRegion());
        contentValues.put(COL_6_2, contact.getAddress());
        contentValues.put(COL_7_2, contact.getEmail());
        contentValues.put(COL_8_2, contact.getAge());
        contentValues.put(COL_9_2, contact.getPathology());
        contentValues.put(COL_10_2, contact.getPatient());
        contentValues.put(COL_11_2, contact.getHospital());
        long result = db.insert(TABLE_NAME_1, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getAllContacts() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_2;
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }
    public boolean updateContacts(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_2, contact.getId());
        contentValues.put(COL_2_2, contact.getFirstName());
        contentValues.put(COL_3_2, contact.getLastName());
        contentValues.put(COL_4_2, contact.getNationality());
        contentValues.put(COL_5_2, contact.getRegion());
        contentValues.put(COL_6_2, contact.getAddress());
        contentValues.put(COL_7_2, contact.getEmail());
        contentValues.put(COL_8_2, contact.getAge());
        contentValues.put(COL_9_2, contact.getPathology());
        contentValues.put(COL_10_2, contact.getPatient());
        contentValues.put(COL_11_2, contact.getHospital());
        db.update(TABLE_NAME_2, contentValues, "id = ?", new String[] {String.valueOf(contact.getId())});
        return true;
    }
    public Integer deleteContacts(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_2, "ID = ?", new String[] {String.valueOf(id)});
    }
}
