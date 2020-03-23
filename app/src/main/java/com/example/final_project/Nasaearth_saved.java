package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;


import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class Nasaearth_saved extends AppCompatActivity {
    //private MyListAdapter myAdapter;
    static ArrayList<NasaEarth> list = new ArrayList<>();
    ListView savedList;
    Bitmap image= null;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasaearth_saved);
        loadDataFromDatabase();
        View v1 = findViewById(R.id.savedList);
        Snackbar snackbar = Snackbar.make(v1, getString(R.string.earthdeleteIn), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.earthokay), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();

        savedList = findViewById(R.id.savedList);

        savedList.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.earthdelete))
                    //.setMessage(getString(R.string.select)+(position+1) + "\n"+getString(R.string.db)+id)
                    .setPositiveButton(getString(R.string.earthyes), (click,arg)-> {
                        //chatArray.remove(position);
                        //myAdapter.notifyDataSetChanged();

                    })
                    .setNegativeButton(getString(R.string.earthno), (click,arg) -> {

                    }).create().show();

            return true ;
        });

    }
    public void loadDataFromDatabase(){
        list.clear();
        NasaEarthMyOpener earthDB = new NasaEarthMyOpener(this);
        db = earthDB.getWritableDatabase();
        String[] columns = {NasaEarthMyOpener.COL_ID, NasaEarthMyOpener.COL_LONGITUDE, NasaEarthMyOpener.COL_LATITUDE, NasaEarthMyOpener.COL_DATE};
        Cursor results = db.query(false, NasaEarthMyOpener.TABLE_NAME, columns, null,null,null,null,null,null);
        int lonColIndex = results.getColumnIndex(NasaEarthMyOpener.COL_LONGITUDE);
        int latColIndex = results.getColumnIndex(NasaEarthMyOpener.COL_LATITUDE);
        int dateColIndex = results.getColumnIndex(NasaEarthMyOpener.COL_DATE);
        int idColIndex = results.getColumnIndex(NasaEarthMyOpener.COL_ID);

        while(results.moveToNext())
        {
            String lon = results.getString(lonColIndex);
            String lat = results.getString(latColIndex);
            String date = results.getString(dateColIndex);
            long id = results.getLong(idColIndex);

            list.add((new NasaEarth(id, lat, lon, date)));
        }

    }
}
