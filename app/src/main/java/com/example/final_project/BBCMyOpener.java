package com.example.final_project;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BBCMyOpener extends SQLiteOpenHelper {

    public static final String MY_OPENER = "PROFILE_ACTIVITY";
    protected final static String DATABASE_NAME = "ItemDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "ITEMS";
    public final static String COL_TITLE = "TITLE";
    public final static String COL_DESCRIPTION = "DESCRIPTION";
    public final static String COL_LINK = "LINK";
    public final static String COL_DATE = "DATE";
    public final static String COL_ID = "_id";

    public BBCMyOpener(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    //This function gets called if no database file exists.
    //Look on your device in the /data/data/package-name/database directory.
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " text,"
                + COL_DESCRIPTION + " text,"
                + COL_LINK + " text,"
                + COL_DATE  + " text);");  // add or remove columns
    }


    //this function gets called if the database version on your device is lower than VERSION_NUM
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    //this function gets called if the database version on your device is higher than VERSION_NUM
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    public int getVersion() { return VERSION_NUM; }

}
