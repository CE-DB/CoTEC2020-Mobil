package com.example.cotec_2020_app;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.Arrays;

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
    public static final String TABLE_NAME_3 = "Patient_Contact";
    public static final String COL_1_3 = "ID";
    public static final String COL_2_3 = "Patient_ID";
    public static final String COL_3_3 = "Contact_ID";
    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_1 + "(ID INTEGER PRIMARY KEY, First_name TEXT, Last_name TEXT, Nationality TEXT, Region TEXT, ICU TEXT, Age INTEGER, Hospitalized TEXT, Medication TEXT, Pathology TEXT, State TEXT, Contacts TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_NAME_2 + "(ID INTEGER PRIMARY KEY, First_name TEXT, Last_name TEXT, Nationality TEXT, Region TEXT, Address TEXT, Email TEXT, Age INTEGER, Pathology TEXT, Patient TEXT, Hospital TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_NAME_3 + "(ID INTEGER PRIMARY KEY, Patient_ID TEXT, Contact_ID TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_3);
        onCreate(db);
    }
    public boolean insertPatient(String id, String first_name, String last_name, String nationality, String region, String intensiveCare, String age, String hospitalized, String medication, String pathology, String state, String[] contacts) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_1, id);
        contentValues.put(COL_2_1, first_name);
        contentValues.put(COL_3_1, last_name);
        contentValues.put(COL_4_1, nationality);
        contentValues.put(COL_5_1, region);
        contentValues.put(COL_6_1, intensiveCare);
        contentValues.put(COL_7_1, age);
        contentValues.put(COL_8_1, hospitalized);
        contentValues.put(COL_9_1, medication);
        contentValues.put(COL_10_1, pathology);
        contentValues.put(COL_11_1, state);
        long result = db.insert(TABLE_NAME_1, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            for (String contact : contacts) {
                insertPatientContact(contact, id);
            }
            return true;
        }
    }
    public Cursor getAllPatients() {
        SQLiteDatabase db = this.getWritableDatabase();
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
    public Cursor getPatientByContact(String contact) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_1 + " pt, " + TABLE_NAME_2 + " ct, " + TABLE_NAME_3 + " cp + WHERE ct." + COL_2_2 + " = '" + contact + "'" + " AND ct." + COL_1_2 + " = " + "cp." + COL_3_3 + " AND pt." + COL_1_1 + " = " + "cp." + COL_2_3;
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }
    public boolean updatePatients(String id, String first_name, String last_name, String nationality, String region, String intensiveCare, String age, String hospitalized, String medication, String pathology, String state, String[] contacts) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_1, id);
        contentValues.put(COL_2_1, first_name);
        contentValues.put(COL_3_1, last_name);
        contentValues.put(COL_4_1, nationality);
        contentValues.put(COL_5_1, region);
        contentValues.put(COL_6_1, intensiveCare);
        contentValues.put(COL_7_1, age);
        contentValues.put(COL_8_1, hospitalized);
        contentValues.put(COL_9_1, medication);
        contentValues.put(COL_10_1, pathology);
        contentValues.put(COL_11_1, state);
        contentValues.put(COL_12_1, Arrays.toString(contacts));
        db.update(TABLE_NAME_1, contentValues, "id = ?", new String[] {id});
        return true;
    }
    public Integer deletePatients(String id, boolean deleteContacts, String[] contacts) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (deleteContacts) {
            for (String contact : contacts) {
                deleteContacts(id);
            }
        }
        return db.delete(TABLE_NAME_1, "ID = ?", new String[] {id});
    }
    public boolean insertContact(String id, String first_name, String last_name, String nationality, String region, String address, String email, String age, String pathology, String patient, String hospital) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_2, id);
        contentValues.put(COL_2_2, first_name);
        contentValues.put(COL_3_2, last_name);
        contentValues.put(COL_4_2, nationality);
        contentValues.put(COL_5_2, region);
        contentValues.put(COL_6_2, address);
        contentValues.put(COL_7_2, email);
        contentValues.put(COL_8_2, age);
        contentValues.put(COL_9_2, pathology);
        contentValues.put(COL_10_2, patient);
        contentValues.put(COL_11_2, hospital);
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
    public boolean updateContacts(String id, String first_name, String last_name, String nationality, String region, String address, String email, String age, String pathology, String patient, String hospital) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_2, id);
        contentValues.put(COL_2_2, first_name);
        contentValues.put(COL_3_2, last_name);
        contentValues.put(COL_4_2, nationality);
        contentValues.put(COL_5_2, region);
        contentValues.put(COL_6_2, address);
        contentValues.put(COL_7_2, email);
        contentValues.put(COL_8_2, age);
        contentValues.put(COL_9_2, pathology);
        contentValues.put(COL_10_2, patient);
        contentValues.put(COL_11_2, hospital);
        db.update(TABLE_NAME_2, contentValues, "id = ?", new String[] {id});
        return true;
    }
    public Integer deleteContacts(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_2, "ID = ?", new String[] {id});
    }
    public boolean insertPatientContact(String contactID, String patientID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_3, patientID);
        contentValues.put(COL_3_3, contactID);
        long result = db.insert(TABLE_NAME_3, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean updatePatientContacts(String id, String contactID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3_3, contactID);
        db.update(TABLE_NAME_2, contentValues, "id = ?", new String[] {id});
        return true;
    }
}
