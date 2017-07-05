package com.project.rm.unifreeclassrooms;
/**
 * Created by Marco on 19/05/2017.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Corsi_DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "CorsiInfo";
    // Contacts table name
    private static final String TABLE_CORSI = "corsi";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_NAME = "name";
   // private static final String KEY_SH_ADDR = "shop_address";
    public Corsi_DBHandler(Context  context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CORSI + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NUMBER + " TEXT," + KEY_NAME + " TEXT"+ ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CORSI);
// Creating tables again
        onCreate(db);
    }


    public void addCorso(Corso corso) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NUMBER, corso.getNumero_Corso()); //Numero Corso
        values.put(KEY_NAME, corso.getNome_Corso()); // Nome Corso
// Inserting Row
        db.insert(TABLE_CORSI, null, values);
        db.close(); // Closing database connection
    }

    /**
     * ATTENZIONE!
     * Questo è un attimo da modificare se lo vogliamo utilizzare
     * Perchè così ora chiedi l'id che assegna lui per cercare il corso nella tabella
     * e restituirlo. Ma dovremmo mettere il nostro numero_Corso ad esempio come parametro
     * però non so poi come funziona il pezzo dove crea la nuova String
     */
    public Corso getCorso(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CORSI, new String[] { KEY_ID, KEY_NUMBER, KEY_NAME}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Corso corso = new Corso(cursor.getString(1),cursor.getString(2));
// return Corso
        return corso;
    }


    public List<Corso> getAllCorsi() {
        List<Corso> corsiList = new ArrayList<Corso>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CORSI;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Corso corso = new Corso();
                corso.setNumero_Corso(cursor.getString(1));
                corso.setNome_Corso(cursor.getString(2));
// Adding contact to list
                corsiList.add(corso);
            } while (cursor.moveToNext());
        }
// return contact list
        return corsiList;
    }

    public int getCorsiCount() {
        String countQuery = "SELECT * FROM " + TABLE_CORSI;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
// return count
        return cursor.getCount();
    }

    // Deleting a Corso
    public void deleteCorso(String titolo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CORSI, KEY_NAME + " = ?",
                new String[] { titolo });
        db.close();
    }
}
