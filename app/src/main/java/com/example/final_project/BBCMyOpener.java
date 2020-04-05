package com.example.final_project;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class is for creating and opening databases called "BBCItemDB". It has a table called "BBCITEMS" with 6 columns to store all the BBC news downloaded form the website.
 * COL_ID: news id, primary key, autoincrement
 * COL_TITLE: news title, classified with <title> tag on the website
 * COL_DESCRIPTION: news description, classified with <description> tag on the website
 * COL_LINK: news link, classified with <link> tag on the website
 * COL_DATE: news publish date, classified with <pubDate> tag on the website
 * COL_ISFAVOURITE: whether the news is favourite; when downloaded from the website, it is "false" by default. When user adds to favourite list, change the value to "true".
 * @author Xin Guo
 * @version 1.0
 */
public class BBCMyOpener extends SQLiteOpenHelper {
    /**
     * Declare the database name as constant variables.
     */
    protected final static String DATABASE_NAME = "BBCItemDB";
    /**
     * Declare the database version as constant variables.
     */
    protected final static int VERSION_NUM = 1;
    /**
     * Declare the table name as constant variables.
     */
    public final static String TABLE_NAME = "BBCITEMS";
    /**
     * Declare the column id as constant variables.
     */
    public final static String COL_ID = "_id";
    /**
     * Declare the column title as constant variables.
     */
    public final static String COL_TITLE = "TITLE";
    /**
     * Declare the column description as constant variables.
     */
    public final static String COL_DESCRIPTION = "DESCRIPTION";
    /**
     * Declare the column link as constant variables.
     */
    public final static String COL_LINK = "LINK";
    /**
     * Declare the column date as constant variables.
     */
    public final static String COL_DATE = "DATE";
    /**
     * Declare the column isfavourite as constant variables.
     */
    public final static String COL_ISFAVOURITE = "ISFAVOURITE";
    /**
     * One argument constructor of BBCMyOpener which calls super constructor of SQLiteOpenHelper.
     * @param ctx The Activity where the database is being opened.
     */
    public BBCMyOpener(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }
    /**
     * This function gets called if no database file exists. It executes a table creation statement in SQL.
     * @param db A database object given by Android for running SQL commands.
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {

        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " text,"
                + COL_DESCRIPTION + " text,"
                + COL_LINK + " text,"
                + COL_DATE + " text,"
                + COL_ISFAVOURITE  + " text);");  // add or remove columns
    }
    /**
     * This function gets called if the database version on your device is lower than VERSION_NUM.
     * @param db A database object given by Android for running SQL commands.
     * @param oldVersion old database version
     * @param newVersion new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }
    /**
     * This function gets called if the database version on your device is higher than VERSION_NUM.
     *  @param db A database object given by Android for running SQL commands.
     *  @param oldVersion old database version
     *  @param newVersion new database version
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }
    /**
     * This getter returns the database version.
     * @return database version
     */
    public int getVersion() { return VERSION_NUM; }
}
