package com.example.hp.selfsecurity;

/**
 * Created by hp on 10/26/2015.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginDataBaseAdapter1 {
    static final String DATABASE_NAME = "details.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;
    // TODO: Create public field for each column in your table.

    private static final String KEY_ID = "ID";
    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table " + "DETAILS" +
            "( " + "ID" + " integer primary key autoincrement," + "Name  text,Number text); ";
    private final ArrayList<Contact> contact_list = new ArrayList<Contact>();

    public final ArrayList<String> Mobile_Number = new ArrayList<String>();

    public SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DataBaseHelper1 dbHelper;

    public LoginDataBaseAdapter1(Context _context) {
        context = _context;
        dbHelper = new DataBaseHelper1(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public LoginDataBaseAdapter1 open() throws SQLException {
        db = dbHelper.getWritableDatabase();

        return this;

    }

    public LoginDataBaseAdapter1 read() throws SQLException {
        db = dbHelper.getReadableDatabase();
        return this;
    }


    public void printTableData() {

        // SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM DETAILS", null);

        if (cur.getCount() == 0)
            Toast.makeText(context, "Nothing to Display", Toast.LENGTH_LONG).show();

        if (cur.getCount() != 0) {
            cur.moveToFirst();


            do {
                String row_values = "";

                for (int i = 0; i < cur.getColumnCount(); i++) {
                    row_values = row_values + " || " + cur.getString(i);
                    Toast.makeText(context, row_values, Toast.LENGTH_LONG).show();
                }

                Log.d("LOG_TAG_HERE", row_values);

            } while (cur.moveToNext());
        }
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    //Insert records in database
    public void insertEntry(String Name, String Number) {
        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put("Name", Name);
        newValues.put("Number", Number);

        // Insert the row into your table
        db.insert("DETAILS", null, newValues);
        ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
    }


    // Getting All Contacts
    public ArrayList<Contact> Get_Contacts() {
        try {
            contact_list.clear();

            // Select All Query
            String selectQuery = "SELECT  * FROM DETAILS";
           // SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    contact.setID(Integer.parseInt(cursor.getString(0)));
                    contact.setName(cursor.getString(1));
                    contact.setPhoneNumber(cursor.getString(2));

                    // Adding contact to list
                    contact_list.add(contact);
                } while (cursor.moveToNext());
            }

            // return contact list
            cursor.close();
            db.close();
            return contact_list;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_contact", "" + e);
        }
        return contact_list;
    }

    // Deleting single contact
    public void Delete_Contact(int id) {


        db.delete("DETAILS", KEY_ID + " = " + id, null);
        db.close();

    }


    // Getting contacts Count
    public int Get_Total_Contacts() {
        String countQuery = "SELECT  * FROM DETAILS";
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public ArrayList<String> GetMobileNumber() {

        // SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT NUMBER FROM DETAILS", null);
        Mobile_Number.clear();

        if (cur.getCount() == 0)
            Toast.makeText(context, "Nothing to Display", Toast.LENGTH_LONG).show();

        if (cur.getCount() != 0) {
            cur.moveToFirst();


            do {
                String row_values = "";

                for (int i = 0; i < cur.getColumnCount(); i++) {
                    String number=cur.getString(i);
                    Mobile_Number.add(number);
                }

                Log.d("LOG_TAG_HERE", row_values);

            } while (cur.moveToNext());
            return Mobile_Number;
        }

        return null;
    }

}

