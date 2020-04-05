package com.example.final_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * a subclass of SQLiteOpenHelper for creating and opening databases.
 * @author Qi Wang
 * @version April 01,2020
 */
public class GuardianMyOpener extends SQLiteOpenHelper {
    protected final static String DATABASE_NAME="GuardianNews";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "GUARDIAN";
    public final static String COL_TITLE = "TITLE";
    public final static String COL_URL= "URL";
    public final static String COL_SECTION= "SECTION";
    public final static String COL_ID = "_id";

    /**
     * You must pass certain important information to the super constructor of SQLiteOpenHelper
     * @param ctx-the Activity where the database is being opened
     */
    public GuardianMyOpener (Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * If the database file doesn’t exist yet, then onCreate gets called immediately by Android
     * @param db-a database object given by Android for running SQL commands.
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +COL_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " text,"
                + COL_URL + " text,"
                + COL_SECTION  + " text );");  //db.execSQL(  ) is a function that executes a string SQL statement
    }

    /**
     * If the database does exist on the device, and the version in the constructor is newer than the version
     * that exists on the device, then onUpgrade gets called.
     * @param db-a database object given by Android for running SQL commands.
     * @param oldVersion-The old database version.
     * @param newVersion-The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    /**
     * If the database does exist, and the version number in the constructor is lower than the version number that exists on the device,
     * then onDowngrade gets called.
     * @param db-a database object given by Android for running SQL commands.
     * @param oldVersion-The old database version.
     * @param newVersion-The new database version.
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }


}
