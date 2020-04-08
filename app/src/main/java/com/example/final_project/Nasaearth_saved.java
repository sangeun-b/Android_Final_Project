package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static com.example.final_project.NasaEarthMyOpener.COL_ID;
import static com.example.final_project.NasaEarthMyOpener.TABLE_NAME;

/**
 * @author Sangeun Baek
 * This activity for viewing favorite list.
 * User can delete the saved item,
 * and can see the detail information of the item.
 */
public class Nasaearth_saved extends AppCompatActivity {
    private NasaEarthAdapter myAdapter = new NasaEarthAdapter();
    static ArrayList<NasaEarth> earthArray = new ArrayList<>();
    ListView savedList;
    Bitmap image= null;
    SQLiteDatabase db;
    public static final String EARTH_ID="id";
    public static final String EARTH_DATE = "date";
    public static final String EARTH_LATITUDE="Latitude";
    public static final String EARTH_LONGITUDE = "Longitude";
    NasaEarthDetailsFragment earthFragment;

    /**
     * get the data from the database into the list view.
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nasaearth_saved);
        loadDataFromDatabase();
        ListView listview = findViewById(R.id.savedList);
        listview.setAdapter(myAdapter);
        Snackbar snackbar = Snackbar.make(listview, getString(R.string.earthdeleteIn), Snackbar.LENGTH_INDEFINITE);
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
                earthFragment = new NasaEarthDetailsFragment();//add a DetailFragment
                earthFragment.setArguments(dataToPass);//pass it a bundle for information
                earthFragment.setTablet(true);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.earthFragment, earthFragment) //Add the fragment in FrameLayout
                        .commit();
            }else{
                Intent nextActivity = new Intent(Nasaearth_saved.this, NasaEarthEmpty.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }
        });

        savedList.setOnItemLongClickListener((parent, view, position, id) -> {
            FrameLayout frameLayout = findViewById(R.id.earthFragment);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.earthdelete))
                    .setPositiveButton(getString(R.string.earthyes), (click,arg)-> {
                        db.delete(NasaEarthMyOpener.TABLE_NAME, NasaEarthMyOpener.COL_ID + "=?", new String[]{Long.toString(id)});

                       if(frameLayout != null) {
                           for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                               if (fragment.getArguments().getLong(EARTH_ID) == Long.valueOf(myAdapter.getItemId(position))) {
                                   getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                                   break;
                               }
                           }
                       }
                        earthArray.remove(position);
                        myAdapter.notifyDataSetChanged();

                    })


                    .setNegativeButton(getString(R.string.earthno), (click,arg) -> {

                    }).create().show();

            return true ;
        });

    }

    /**
     * create the subclass extended BaseAdapter.
     */

    public class NasaEarthAdapter extends BaseAdapter {
        /**
         * number of element in the array
         * @return current array size
         */
        public int getCount() {
            return earthArray.size();
        }

        /**
         * get Item of the position
         * @param position row position
         * @return the item of the row position
         */
        public NasaEarth getItem(int position) {
            return earthArray.get(position);

        }

        /**
         * get view
         * @param position the position of the item
         * @param convertView
         * @param parent
         * @return view of the item
         */

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View v = convertView;
            NasaEarth earth = getItem(position);

            v = inflater.inflate(R.layout.nasaearth_saved_item, null);
            TextView earthDate = v.findViewById(R.id.earthsaveddate);
            earthDate.setText(getString(R.string.earthdate) + earth.getDate());
            TextView earthLat = v.findViewById(R.id.earthsavedlat);
            earthLat.setText(getString(R.string.earthlat) + earth.getLatitude());
            TextView earthLot = v.findViewById(R.id.earthsavedlon);
            earthLot.setText(getString(R.string.earthlon)+earth.getLongitude());

            FileInputStream fis;
            File file = getBaseContext().getFileStreamPath(earth.getDate()+".png");
            if(file.exists()){
                try{
                    fis = openFileInput(earth.getDate()+".png");
                    image = BitmapFactory.decodeStream(fis);
                    ImageView savedImage = v.findViewById(R.id.earthsavedimage);
                    savedImage.setImageBitmap(image);
                }catch(FileNotFoundException e){
                    e.printStackTrace();
                }
            }
            return v;

        }

        /**
         * get id of the position.
         * @param position row postion
         * @return the id of the position.
         */

        public long getItemId(int position) {
            return getItem(position).getId();

        }

    }

    /**
     * retrieve the data form database.
     */
    public void loadDataFromDatabase(){
        earthArray.clear();
        NasaEarthMyOpener earthDB = new NasaEarthMyOpener(this);
        db = earthDB.getWritableDatabase();
        String[] columns = {NasaEarthMyOpener.COL_ID, NasaEarthMyOpener.COL_LONGITUDE, NasaEarthMyOpener.COL_LATITUDE, NasaEarthMyOpener.COL_DATE};
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
}
