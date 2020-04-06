package com.example.final_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The NasaDayImageMyOpen class implements SQLite database to store the image-related information from the NASA website.
 * The database name is NasaDayImageDB. The table name is NasaDayImage and it has five different columns which are id, date, title,
 * url and hdurl.
 *
 * @author Hsing-I Wang
 * @version 1.0
 */
public class NasaDayImageMyOpener extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME= "NasaDayImageDB";
    protected final static int VERSION_NUM= 1;
    public final static String TABLE_NAME= "NasaDayImage";
    public final static String COL_DATE ="DATE";
    public final static String COL_TITLE= "TITLE";
    public final static String COL_URL= "URL";
    public final static String COL_HDURL= "HDURL";
    public final static String COL_ID= "_id";

    public NasaDayImageMyOpener(Context context){
        super(context,DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * create the table: NasaDayImageDB that holds id, date, title, url and hdurl five different columns
     * @param db SQLiteDatabase
     */
    public void onCreate(SQLiteDatabase db){
        db.execSQL(" CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DATE + " TEXT,"
                + COL_TITLE + " TEXT,"
                + COL_URL + " TEXT,"
                + COL_HDURL  + " TEXT);");
    }

    /**
     * upgrade the version of the database
     * @param db SQLiteDatabase
     * @param oldVersion old version number
     * @param newVersion new version number
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * downgrade the version of the database
     * @param db SQliteDatabase
     * @param oldVersion old version number
     * @param newVersion new version number
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}

