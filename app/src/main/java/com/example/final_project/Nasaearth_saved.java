package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;


import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static com.example.final_project.NasaEarthMyOpener.COL_ID;
import static com.example.final_project.NasaEarthMyOpener.TABLE_NAME;

public class Nasaearth_saved extends AppCompatActivity {
    //private NasaEarthAdapter myAdapter;
    static ArrayList<NasaEarth> earthArray = new ArrayList<>();
    ListView savedList;
    Bitmap image= null;
    SQLiteDatabase db;
    public static final String EARTH_ID="id";
    public static final String EARTH_DATE = "date";
    public static final String EARTH_LATITUDE="Latitude";
    public static final String EARTH_LONGITUDE = "Longitude";
    NasaEarthDetailsFragment eFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasaearth_saved);
        //loadDataFromDatabase();
        View v1 = findViewById(R.id.savedList);
        Snackbar snackbar = Snackbar.make(v1, getString(R.string.earthdeleteIn), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.earthokay), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
        boolean isTablet = findViewById(R.id.earthFragment) != null;
        savedList = findViewById(R.id.savedList);

        savedList.setOnItemClickListener((list,view,pos,id)->{
            Bundle dataToPass = new Bundle();
            dataToPass.putLong(EARTH_ID, earthArray.get(pos).getId());
            dataToPass.putString(EARTH_LATITUDE, earthArray.get(pos).getLatitude());
            dataToPass.putString(EARTH_LONGITUDE, earthArray.get(pos).getLongitude());
            dataToPass.putString(EARTH_DATE, earthArray.get(pos).getDate());

            if(isTablet) {
                eFragment = new NasaEarthDetailsFragment();//add a DetailFragment
                eFragment.setArguments(dataToPass);//pass it a bundle for information
                eFragment.setTablet(true);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.earthFragment, eFragment) //Add the fragment in FrameLayout
                        .commit();
            }else{
                Intent nextActivity = new Intent(Nasaearth_saved.this, NasaEarthEmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }
        });

        savedList.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.earthdelete))
                    //.setMessage(getString(R.string.select)+(position+1) + "\n"+getString(R.string.db)+id)
                    .setPositiveButton(getString(R.string.earthyes), (click,arg)-> {
                        db.delete(TABLE_NAME, COL_ID + "=?", new String[]{Long.toString(id)});
                        earthArray.remove(position);
                        if(isTablet) {
                            getSupportFragmentManager().beginTransaction().remove(eFragment).commit();
                        }


                    })
                    .setNegativeButton(getString(R.string.earthno), (click,arg) -> {

                    }).create().show();

            return true ;
        });

    }
    /*public void loadDataFromDatabase(){
        earthArray.clear();
        NasaEarthMyOpener earthDB = new NasaEarthMyOpener(this);
        db = earthDB.getWritableDatabase();
        String[] columns = {COL_ID, NasaEarthMyOpener.COL_LONGITUDE, NasaEarthMyOpener.COL_LATITUDE, NasaEarthMyOpener.COL_DATE};
        Cursor results = db.query(false, TABLE_NAME, columns, null,null,null,null,null,null);
        int lonColIndex = results.getColumnIndex(NasaEarthMyOpener.COL_LONGITUDE);
        int latColIndex = results.getColumnIndex(NasaEarthMyOpener.COL_LATITUDE);
        int dateColIndex = results.getColumnIndex(NasaEarthMyOpener.COL_DATE);
        int idColIndex = results.getColumnIndex(COL_ID);

        while(results.moveToNext())
        {
            String lon = results.getString(lonColIndex);
            String lat = results.getString(latColIndex);
            String date = results.getString(dateColIndex);
            long id = results.getLong(idColIndex);

            earthArray.add((new NasaEarth(id, lat, lon, date)));
        }

    }
    public class NasaEarthAdapter extends BaseAdapter {

        public int getCount() {
            return earthArray.size();
        }

        public NasaEarth getItem(int position) {
            return earthArray.get(position);

        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View v = convertView;
            NasaEarth earth = getItem(position);

            v = getLayoutInflater().inflate(R.layout.)
            TextView textDate = (TextView) v.findViEW
            return v;

        }

        public long getItemId(int position) {
            return getItem(position).getId();

        }

    }*/
}
