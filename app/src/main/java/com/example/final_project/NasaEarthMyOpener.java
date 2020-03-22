package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.time.DateTimeException;

public class NasaEarthMyOpener extends SQLiteOpenHelper {

    protected static final String DATABASE_NAME = "NasaEarthDB";
    protected static final int VERSION_NUM = 1;
    public static final String TABLE_NAME="NasaEarth";
    public static final String COL_ID = "_id";
    public static final String COL_LATITUDE = "Latitude";
    public static final String COL_LONGITUDE = "Longitude";
    public static final String COL_DATE = "Date";

    public NasaEarthMyOpener (Context ctx){super(ctx, DATABASE_NAME, null, VERSION_NUM);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_LATITUDE + " Latitude,"
                + COL_LONGITUDE + " Longitude,"
                + COL_DATE + " Date);");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}