package com.example.cotec_2020_app24;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

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
    public static final String COL_13_1 = "Country";
    public static final String COL_15_1 = "Date_Entrance";
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
    public static final String COL_12_2 = "Country";
    public static final String TABLE_NAME_3 = "Patient_Updated";
    public static final String COL_14_1 = "Old_ID";
    public static final String TABLE_NAME_4 = "Contact_Updated";
    public static final String COL_13_2 = "Old_ID";
    public static final String TABLE_NAME_5 = "Patient_Contact";
    public static final String COL_1_5 = "Patient_ID";
    public static final String COL_2_5 = "Contact_ID";
    public static final String COL_3_5 = "Last_visit";
    public static final String TABLE_NAME_6 = "Patient_Pathology";
    public static final String COL_1_6 = "Pathology_name";
    public static final String COL_2_6 = "Patient_ID";
    public static final String TABLE_NAME_7 = "Contact_Pathology";
    public static final String COL_1_7 = "Pathology_name";
    public static final String COL_2_7 = "Contact_ID";
    public static final String TABLE_NAME_8 = "Patient_Medication";
    public static final String COL_1_8 = "Medication";
    public static final String COL_2_8 = "Patient_ID";
    public static final String TABLE_NAME_9 = "Patient_Deleted";
    public static final String COL_1_9 = "Patient_ID";
    public static final String TABLE_NAME_10 = "Contact_Deleted";
    public static final String COL_1_10 = "Contact_ID";
    public static final String TABLE_NAME_11 = "Patient_Contact_Updated";
    public static final String COL_1_11 = "Old_Patient_ID";
    public static final String COL_2_11 = "Old_Contact_ID";
    public static final String COL_3_11 = "Old_Last_visit";
    public static final String TABLE_NAME_12 = "Patient_Contact_Deleted";
    public static final String TABLE_NAME_13 = "Updated_Patient_Pathology";
    public static final String COL_1_13 = "Pathology_name";
    public static final String COL_2_13 = "Patient_ID";
    public static final String TABLE_NAME_14 = "Updated_Contact_Pathology";
    public static final String COL_1_14 = "Pathology_name";
    public static final String COL_2_14 = "Contact_ID";
    public static final String TABLE_NAME_15 = "Updated_Patient_Medication";
    public static final String COL_1_15 = "Medication";
    public static final String COL_2_15 = "Patient_ID";

    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Created patients stuff
        db.execSQL("CREATE TABLE " + TABLE_NAME_1 + "(" +
                "ID TEXT PRIMARY KEY, " +
                "First_name TEXT, " +
                "Last_name TEXT, " +
                "Nationality TEXT, " +
                "Region TEXT, " +
                "ICU TEXT, " +
                "Age INTEGER, " +
                "Hospitalized TEXT, " +
                "State TEXT, " +
                "Country TEXT, " +
                "Date_Entrance TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_NAME_6 + "(" +
                "Pathology_name TEXT, " +
                "Patient_ID TEXT, " +
                "PRIMARY KEY(Patient_ID,Pathology_name), " +
                "FOREIGN KEY (Patient_ID) REFERENCES " + TABLE_NAME_1 + " (ID))");
        db.execSQL("CREATE TABLE " + TABLE_NAME_8 + "(" +
                "Medication TEXT, " +
                "Patient_ID TEXT, " +
                "PRIMARY KEY(Patient_ID,Medication), " +
                "FOREIGN KEY (Patient_ID) REFERENCES " + TABLE_NAME_1 + " (ID))");
        //Updated patients stuff
        db.execSQL("CREATE TABLE " + TABLE_NAME_3 + "(" +
                "Old_ID TEXT PRIMARY KEY, " +
                "ID TEXT, " +
                "First_name TEXT, " +
                "Last_name TEXT, " +
                "Nationality TEXT, " +
                "Region TEXT, " +
                "ICU TEXT, " +
                "Age INTEGER, " +
                "Hospitalized TEXT, " +
                "State TEXT, " +
                "Country TEXT, " +
                "Date_Entrance TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_NAME_13 + "(" +
                "Pathology_name TEXT, " +
                "Patient_ID TEXT, " +
                "PRIMARY KEY(Patient_ID,Pathology_name), " +
                "FOREIGN KEY (Patient_ID) REFERENCES " + TABLE_NAME_3 + " (ID))");
        db.execSQL("CREATE TABLE " + TABLE_NAME_15 + "(" +
                "Medication TEXT, " +
                "Patient_ID TEXT, " +
                "PRIMARY KEY(Patient_ID,Medication), " +
                "FOREIGN KEY (Patient_ID) REFERENCES " + TABLE_NAME_3 + " (ID))");
        //Deleted patients stuff
        db.execSQL("CREATE TABLE " + TABLE_NAME_9 + "(Patient_ID TEXT PRIMARY KEY)");
        //Created contacts stuff
        db.execSQL("CREATE TABLE " + TABLE_NAME_2 + "(" +
                "ID TEXT PRIMARY KEY, " +
                "First_name TEXT, " +
                "Last_name TEXT, " +
                "Nationality TEXT, " +
                "Region TEXT, " +
                "Address TEXT, " +
                "Email TEXT, " +
                "Age INTEGER, " +
                "Country TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_NAME_7 + "(" +
                "Pathology_name TEXT, " +
                "Contact_ID TEXT, " +
                "PRIMARY KEY(Contact_ID,Pathology_name), " +
                "FOREIGN KEY (Contact_ID) REFERENCES " + TABLE_NAME_2 +" (ID))");
        //Updated contacts stuff
        db.execSQL("CREATE TABLE " + TABLE_NAME_4 + "(" +
                "Old_ID TEXT PRIMARY KEY, " +
                "ID TEXT, " +
                "First_name TEXT, " +
                "Last_name TEXT, " +
                "Nationality TEXT, " +
                "Region TEXT, " +
                "Address TEXT, " +
                "Email TEXT, " +
                "Age INTEGER, " +
                "Country TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_NAME_14 + "(" +
                "Pathology_name TEXT, " +
                "Contact_ID TEXT, " +
                "PRIMARY KEY(Contact_ID,Pathology_name), " +
                "FOREIGN KEY (Contact_ID) REFERENCES " + TABLE_NAME_4 +" (ID))");
        //Deleted contacts stuff
        db.execSQL("CREATE TABLE " + TABLE_NAME_10 + "(Contact_ID TEXT PRIMARY KEY)");
        //Created contact visit
        db.execSQL("CREATE TABLE " + TABLE_NAME_5 + " (" +
                "Patient_ID TEXT, " +
                "Contact_ID TEXT, " +
                "Last_visit TEXT, " +
                "PRIMARY KEY(Patient_ID,Contact_ID,Last_visit))");
        //Updated contact visit
        db.execSQL("CREATE TABLE " + TABLE_NAME_11 + "(" +
                "Old_Patient_ID TEXT, " +
                "Old_Contact_ID TEXT, " +
                "Old_Last_visit TEXT, " +
                "Patient_ID TEXT, " +
                "Contact_ID TEXT, " +
                "Last_visit TEXT, " +
                "PRIMARY KEY(Old_Patient_ID, Old_Contact_ID, Old_Last_visit))");
        //Deleted contact visit
        db.execSQL("CREATE TABLE " + TABLE_NAME_12 + "(" +
                "Patient_ID TEXT, " +
                "Contact_ID TEXT, " +
                "Last_visit TEXT, " +
                "PRIMARY KEY(Patient_ID, Contact_ID, Last_visit))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_4);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_5);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_6);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_7);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_8);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_9);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_10);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_11);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_12);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_13);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_14);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_15);
        onCreate(db);
    }

    // CREATED PATIENTS STUFF

    public boolean insertCreatedPatient(String id, String first_name, String last_name, String nationality, String region, String intensiveCare, int age, String hospitalized, String state, String Date_Entrance, String country) {
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
        contentValues.put(COL_11_1, state);
        contentValues.put(COL_13_1, country);
        contentValues.put(COL_15_1, Date_Entrance);
        long result = db.insert(TABLE_NAME_1, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getAllCreatedPatients() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_1;
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }
    public Cursor getCreatedPatient(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_1 + " WHERE " + COL_1_1 + " = " + new String("'").concat(id).concat("'");
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }
    public Integer deleteCreatedPatients(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_1, COL_1_1 + " = ?", new String[] {id});
    }

    public boolean insertCreatedPatientPathology(String Pathology_Name, String patient_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_6, Pathology_Name);
        contentValues.put(COL_2_6, patient_id);
        long result = db.insert(TABLE_NAME_6, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getAllCreatedPatientsPathology() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_6;
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }
    public Integer deleteCreatedPatientsPathology(String Pathology_Name, String patient_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_6, COL_1_6 + " = ?" + " AND " + COL_2_6 + " = ?", new String[] {Pathology_Name, patient_id});
    }

    public boolean insertCreatedPatientMedication(String Medication, String patient_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_8, Medication);
        contentValues.put(COL_2_8, patient_id);
        long result = db.insert(TABLE_NAME_6, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getAllCreatedPatientsMedication() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_8;
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }
    public Integer deleteCreatedPatientsMedication(String Medication, String patient_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_8, COL_1_8 + " = ?" + " AND " + COL_2_8 + " = ?", new String[] {Medication, patient_id});
    }

    //UPDATED PATIENTS STUFF

    public boolean insertUpdatedPatients(String old_id, String id, String first_name, String last_name, String nationality, String region, String intensiveCare, Integer age, String hospitalized, String state, String country, String date_entrance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_14_1, old_id);
        contentValues.put(COL_1_1, id);
        contentValues.put(COL_2_1, first_name);
        contentValues.put(COL_3_1, last_name);
        contentValues.put(COL_4_1, nationality);
        contentValues.put(COL_5_1, region);
        contentValues.put(COL_6_1, intensiveCare);
        contentValues.put(COL_7_1, age);
        contentValues.put(COL_8_1, hospitalized);
        contentValues.put(COL_11_1, state);
        contentValues.put(COL_13_1, country);
        contentValues.put(COL_15_1, date_entrance);
        long result = db.insert(TABLE_NAME_3, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getAllUpdatedPatients() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_3;
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }

    public Cursor getUpdatedPatient(String oldIdentification) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_3 + " WHERE " + COL_14_1 + " = " + new String("'").concat(oldIdentification).concat("'");
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }

    public Integer deleteUpdatedPatients(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_3, "Old_ID = ?", new String[] {id});
    }

    public boolean insertUpdatedPatientPathology(String Pathology_Name, String patient_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_13, Pathology_Name);
        contentValues.put(COL_2_13, patient_id);
        long result = db.insert(TABLE_NAME_13, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getAllUpdatedPatientsPathology() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_13;
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }

    public Cursor getUpdatedPatientsPathology(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_13 + " WHERE " + COL_2_13 + " = " + new String("'").concat(id).concat("'");
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }

    public Integer deleteUpdatedPatientsPathology(String Pathology_Name, String patient_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_13, COL_1_13 + " = ?" + " AND " + COL_2_13 + " = ?", new String[] {Pathology_Name, patient_id});
    }

    public Integer deleteUpdatedPatientsPathology(String patient_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_13, COL_2_13 + " = ?", new String[] {patient_id});
    }

    public boolean insertUpdatedPatientMedication(String Medication, String patient_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_15, Medication);
        contentValues.put(COL_2_15, patient_id);
        long result = db.insert(TABLE_NAME_15, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getAllUpdatedPatientsMedication() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_15;
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }

    public Cursor getUpdatedPatientsMedication(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_15 + " WHERE " + COL_2_15 + " = " + new String("'").concat(id).concat("'");
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }

    public Integer deleteUpdatedPatientsMedication(String Medication, String patient_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_15, COL_1_15 + " = ?" + " AND " + COL_2_15 + " = ?", new String[] {Medication, patient_id});
    }

    public Integer deleteUpdatedPatientsMedication(String patient_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_15, COL_2_15 + " = ?", new String[] {patient_id});
    }

    //DELETED PATIENTS STUFF

    public boolean insertDeletedPatients(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_9, id);
        long result = db.insert(TABLE_NAME_9, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getAllDeletedPatients() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_9;
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }
    public Cursor getDeletedPatient(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_9 + " WHERE " + COL_1_9 + " = " + new String("'").concat(id).concat("'");
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }
    public Integer deleteDeletedPatients(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_9, "Patient_ID = ?", new String[] {id});
    }


    //CREATED CONTACTS STUFF

    public boolean insertCreatedContact(String id, String first_name, String last_name, String nationality, String region, String address, String email, String age, String country) {
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
        contentValues.put(COL_12_2, country);
        long result = db.insert(TABLE_NAME_2, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getAllCreatedContacts() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_2;
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }
    public Cursor getCreatedContact(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_2 + " WHERE " + COL_1_2 + " = " + new String("'").concat(id).concat("'");
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }
    public Integer deleteCreatedContacts(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_2, "ID = ?", new String[] {id});
    }

    public boolean insertCreatedContactPathology(String Pathology_Name, String contact_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_7, Pathology_Name);
        contentValues.put(COL_2_7, contact_id);
        long result = db.insert(TABLE_NAME_7, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getAllCreatedContactPathology() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_7;
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }
    public Integer deleteCreatedContactPathology(String Pathology_Name, String contact_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_7, COL_1_7 + " = ?" + " AND " + COL_2_7 + " = ?", new String[] {Pathology_Name, contact_id});
    }

    //UPDATED CONTACTS STUFF

    public boolean insertUpdatedContacts(String old_id, String id, String first_name, String last_name, String nationality, String region, String address, String email, String age, String country) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_13_2, old_id);
        contentValues.put(COL_1_1, id);
        contentValues.put(COL_2_2, first_name);
        contentValues.put(COL_3_2, last_name);
        contentValues.put(COL_4_2, nationality);
        contentValues.put(COL_5_2, region);
        contentValues.put(COL_6_2, address);
        contentValues.put(COL_7_2, email);
        contentValues.put(COL_8_2, age);
        contentValues.put(COL_12_2, country);
        long result = db.insert(TABLE_NAME_4, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getAllUpdatedContacts() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_4;
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }
    public Cursor getUpdatedContact(String Old_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_4 + " WHERE " + COL_13_2 + " = " + new String("'").concat(Old_id).concat("'");
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }
    public Integer deleteUpdatedContacts(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_4, "Old_ID = ?", new String[] {id});
    }

    public boolean insertUpdatedContactPathology(String Pathology_Name, String contact_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_14, Pathology_Name);
        contentValues.put(COL_2_14, contact_id);
        long result = db.insert(TABLE_NAME_14, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getAllUpdatedContactPathology() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_14;
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }

    public Cursor getUpdatedContactPathology(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_14 + " WHERE " + COL_2_14 + " = " + new String("'").concat(id).concat("'");
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }

    public Integer deleteUpdatedContactPathology(String Pathology_Name, String contact_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_14, COL_1_14 + " = ?" + " AND " + COL_2_14 + " = ?", new String[] {Pathology_Name, contact_id});
    }

    public Integer deleteUpdatedContactPathology(String contact_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_14, COL_2_14 + " = ?", new String[] {contact_id});
    }

    //DELETED CONTACTS STUFF

    public boolean insertDeletedContacts(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_10, id);
        long result = db.insert(TABLE_NAME_10, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getAllDeletedContacts() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_10;
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }
    public Cursor getDeletedContact(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_10 + " WHERE " + COL_1_10 + " = " + new String("'").concat(id).concat("'");
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }
    public Integer deleteDeletedContacts(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_10, "Contact_ID = ?", new String[] {id});
    }

    //CREATED CONTACT VISIT STUFF

    public boolean insertCreatedContactVisit(String Patient_ID, String Contact_ID, String Last_visit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_5, Patient_ID);
        contentValues.put(COL_2_5, Contact_ID);
        contentValues.put(COL_3_5, Last_visit);
        long result = db.insert(TABLE_NAME_5, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getAllCreatedContactVisit() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_5;
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }
    public Cursor getCreatedContactVisit(String Patient_ID, String Contact_ID, String Last_visit) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_5 + " WHERE " + COL_1_5 + " = " + new String("'").concat(Patient_ID).concat("'")
                + " AND " + COL_2_5 + " = " + new String("'").concat(Contact_ID).concat("'") + " AND " + COL_3_5 + " = " + new String("'").concat(Last_visit).concat("'");
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }

    public Cursor getCreatedContactVisit(String Patient_ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_5 + " WHERE " + COL_1_5 + " = " + new String("'").concat(Patient_ID).concat("'");
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }

    public Integer deleteCreatedContactVisit(String Patient_ID, String Contact_ID, String Last_visit) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_5, "Patient_ID = ? AND Contact_ID = ? AND Last_visit = ?", new String[] {Patient_ID, Contact_ID, Last_visit});
    }

    public Integer deleteCreatedContactVisit(String Patient_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_5, "Patient_ID = ?", new String[] {Patient_ID});
    }

    //UPDATED CONTACT VISIT STUFF

    public boolean insertUpdatedContactVisit(String Old_Patient_ID, String Old_Contact_ID, String Old_Last_visit, String Patient_ID, String Contact_ID, String Last_visit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_11, Old_Patient_ID);
        contentValues.put(COL_2_11, Old_Contact_ID);
        contentValues.put(COL_3_11, Old_Last_visit);
        contentValues.put(COL_1_5, Patient_ID);
        contentValues.put(COL_2_5, Contact_ID);
        contentValues.put(COL_3_5, Last_visit);
        long result = db.insert(TABLE_NAME_11, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getAllUpdatedContactVisit() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_11;
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }
    public Cursor getUpdatedContactVisit(String Old_Patient_ID, String Old_Contact_ID, String Old_Last_visit) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_11 + " WHERE " + COL_1_11 + " = " + new String("'").concat(Old_Patient_ID).concat("'")
                + " AND " + COL_2_11 + " = " + new String("'").concat(Old_Contact_ID).concat("'") + " AND " + COL_3_11 + " = " + new String("'").concat(Old_Last_visit).concat("'");
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }

    public Cursor getUpdatedContactVisitByPatient(String Old_Patient_ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_11 + " WHERE " + COL_1_11 + " = " + new String("'").concat(Old_Patient_ID).concat("'");
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }

    public Integer deleteUpdatedContactVisit(String Patient_ID, String Contact_ID, String Last_visit) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_11, "Old_Patient_ID = ? AND Old_Contact_ID = ? AND Old_Last_visit = ?", new String[] {Patient_ID, Contact_ID, Last_visit});
    }

    public Integer deleteUpdatedContactVisit(String Patient_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_11, "Old_Patient_ID = ?", new String[] {Patient_ID});
    }

    //DELETED CONTACT VISIT STUFF

    public boolean insertDeletedContactVisit(String Patient_ID, String Contact_ID, String Last_visit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_5, Patient_ID);
        contentValues.put(COL_2_5, Contact_ID);
        contentValues.put(COL_3_5, Last_visit);
        long result = db.insert(TABLE_NAME_12, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getAllDeletedContactVisit() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_12;
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }
    public Cursor getDeletedContactVisit(String Patient_ID, String Contact_ID, String Last_visit) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_12 + " WHERE " + COL_1_5 + " = " + new String("'").concat(Patient_ID).concat("'")
                + " AND " + COL_2_5 + " = " + new String("'").concat(Contact_ID).concat("'") + " AND " + COL_3_5 + " = " + new String("'").concat(Last_visit).concat("'");
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }

    public Cursor getDeletedContactVisit(String Patient_ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_12 + " WHERE " + COL_1_5 + " = " + new String("'").concat(Patient_ID).concat("'");
        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }

    public Integer deleteDeletedContactVisit(String Patient_ID, String Contact_ID, String Last_visit) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_12, "Patient_ID = ? AND Contact_ID = ? AND Last_visit = ?", new String[] {Patient_ID, Contact_ID, Last_visit});
    }

    public Integer deleteDeletedContactVisit(String Patient_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_12, "Patient_ID = ?", new String[] {Patient_ID});
    }
}
