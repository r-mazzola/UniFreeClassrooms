package com.project.rm.unifreeclassrooms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marco on 24/05/2017.
 */

public class Messaggi_DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "MessageBoard";
    // Contacts table name
    private static final String TABLE_MESS = "Messaggio";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_CORSO = "id_corso";
    private static final String KEY_MESSAGGIO = "Mex";
    private static final String KEY_DATA = "Date";

    public Messaggi_DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MESS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CORSO + " TEXT," + KEY_MESSAGGIO + " TEXT,"+ KEY_DATA + " TEXT"+ ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESS);
// Creating tables again
        onCreate(db);
    }
    public void addMsg(Messaggio messaggio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CORSO, messaggio.getCorso()); //Corso di cui il messaggio parla
        values.put(KEY_MESSAGGIO, messaggio.getTestoMessaggio()); // Contenuto del messaggio
        values.put(KEY_DATA, messaggio.getTimeStamp());//ora e data del messaggio
// Inserting Row
        db.insert(TABLE_MESS, null, values);
        db.close(); // Closing database connection
    }

    public Messaggio getMess(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MESS, new String[] { KEY_ID, KEY_CORSO, KEY_MESSAGGIO,KEY_DATA}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Messaggio messaggio = new Messaggio(cursor.getString(1),cursor.getString(2),cursor.getString(3));
// return Corso
        return messaggio;
    }

    public List<Messaggio> getAllMsg() {
        List<Messaggio> MsgList = new ArrayList<Messaggio>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MESS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Messaggio msg = new Messaggio();
                msg.setId(cursor.getString(0));//ID
                msg.setCorso(cursor.getString(1));//Nome del corso a cui il msg riferisce
                msg.setTestoMessaggio(cursor.getString(2));//Testo del Msg
                msg.setTimeStamp(cursor.getString(3));//data e ora
// Adding contact to list
                MsgList.add(msg);
            } while (cursor.moveToNext());
        }
// return contact list
        return MsgList;
    }


    public int getMsgCount() {
        String countQuery = "SELECT * FROM " + TABLE_MESS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
// return count
        return cursor.getCount();
    }

//    // Updating a Corso
//    public int updateCorsi(Messaggio messaggio) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(KEY_MESSAGGIO, messaggio.getDescription());
//        values.put(KEY_DATA, messaggio.getMsg_time());

    //// updating row
//        return db.update(TABLE_MESS, values, KEY_ID + " = ?",
//                new String[]{String.valueOf(messaggio.getNumero_Corso())});
//    }
//
    // Deleting a msg
    public void deleteMsg(Messaggio msg) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MESS, KEY_ID + " = ?",
                new String[] { String.valueOf(msg.getCorso()) });
        db.close();
    }

    public void deletAllMsg() {
// Select All Query
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MESS,null,null);
        // db.execSQL("delete * from "+ TABLE_MESS);
        // db.close();
    }

}
