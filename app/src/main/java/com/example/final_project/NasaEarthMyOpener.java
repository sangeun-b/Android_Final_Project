package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.time.DateTimeException;

/**
 * @author Sangeun Baek
 * This activity is for creating the SQLite datbase.
 */

public class NasaEarthMyOpener extends SQLiteOpenHelper {
    /**
     * static variables for manipulating database
     */

    protected static final String DATABASE_NAME = "NasaEarthDB";
    protected static final int VERSION_NUM = 1;
    public static final String TABLE_NAME="NasaEarth";
    public static final String COL_ID = "_id";
    public static final String COL_LATITUDE = "Latitude";
    public static final String COL_LONGITUDE = "Longitude";
    public static final String COL_DATE = "Date";

    /**
     * constructor for SQLiteOpenHelper
     * @param ctx where the database is opened.
     */
    public NasaEarthMyOpener (Context ctx){super(ctx, DATABASE_NAME, null, VERSION_NUM);}

    /**
     * This function gets called if no database fils exist.
     * Create the table in the database.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_LATITUDE + " Latitude,"
                + COL_LONGITUDE + " Longitude,"
                + COL_DATE + " Date);");
    }

    /**
     * this function gets called if the database version on your device is lower than VERSION_NUM
     * @param db
     * @param oldVersion old database version
     * @param newVersion new database version
     */

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

        onCreate(db);
    }

    /**
     * this function gets called if the database version on your device is higer than VERSION_NUM
     * @param db
     * @param oldVersion old database version
     * @param newVersion new database version
     */
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}