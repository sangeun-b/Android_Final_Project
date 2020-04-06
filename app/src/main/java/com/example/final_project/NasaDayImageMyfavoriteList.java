package com.example.final_project;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * The NasaDayImageMyFravoritelist is a class that display all the saved images and their related information on the listview from the database.
 * It has the delete function.
 *
 * @author Hsing-I Wang
 * @version 1.0
 */

public class NasaDayImageMyfavoriteList extends AppCompatActivity {

    private NasaDayImageMyListAdapter nasaDayMyAdapter= new NasaDayImageMyListAdapter();
    static ArrayList<Image> list= new ArrayList<>();
    public static final String ITEM_DATE= "date";
    public static final String ITEM_TITLE= "title";
    public static final String ITEM_URL= "url";
    public static final String ITEM_HDURL= "hdurl";
    public static final String ITEM_ID= "id";
    Bitmap image= null;
    SQLiteDatabase db;
    NasaDayImageDetailsFragment dFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_day_image_myfavorite_list);

        loadDataFromDatabase();
        ListView listView= findViewById(R.id.NasaDayImagelistView);
        listView.setAdapter(nasaDayMyAdapter);


        Snackbar.make(listView, "short click: view more info and long click: delete", Snackbar.LENGTH_LONG).show();
        boolean isTablet = findViewById(R.id.fragmentLocation) != null;

        listView.setOnItemClickListener((p, v, pos, id)->{
                    Bundle dataToPass= new Bundle();
                    dataToPass.putString(ITEM_DATE, list.get(pos).getDate());
                    dataToPass.putString(ITEM_TITLE, list.get(pos).getTitle());
                    dataToPass.putString(ITEM_URL, list.get(pos).getUrl());
                    dataToPass.putString(ITEM_HDURL, list.get(pos).getHdurl());
                    dataToPass.putLong(ITEM_ID, id);
                    /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.PNG, 80, stream);
                    byte[] bytes = stream.toByteArray();
                    //intent.putExtra("bitmapbytes",bytes);
                    dataToPass.putByteArray(ITEM_IMAGE, bytes);*/

                    if(isTablet){
                        dFragment = new NasaDayImageDetailsFragment(); //add a NasaDayImageDetailsFragment
                        dFragment.setArguments(dataToPass); //pass it a bundle for information
                        dFragment.setTablet(true);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragmentLocation, dFragment)
                                .commit();
                    }else{
                        Intent nextActivity = new Intent(NasaDayImageMyfavoriteList.this, NasaDayImageEmptyActivity.class);
                        nextActivity.putExtras(dataToPass); //send data to next activity
                        startActivity(nextActivity); //make the transition
                    }


        });


        listView.setOnItemLongClickListener((p, v, pos, id)->{
            FrameLayout frameLayout = findViewById(R.id.fragmentLocation);
            AlertDialog.Builder alert= new AlertDialog.Builder(this);
            alert.setTitle(getString(R.string.NasaImageAlertDelete))
                    .setMessage(getString(R.string.NasaImageSelectRow)+ " " + pos + "\n"+ getString(R.string.NasaImageDatabaseRow) + " " + id)
                    .setPositiveButton("Yes", (click, arg) -> {
                        list.remove(pos);
                        db.delete(NasaDayImageMyOpener.TABLE_NAME, NasaDayImageMyOpener.COL_ID + "= ?", new String[] {Long.toString(id)});
                        nasaDayMyAdapter.notifyDataSetChanged();
                        if(frameLayout != null){
                            for(Fragment fragment : getSupportFragmentManager().getFragments()){
                                if(fragment.getArguments().getLong(ITEM_ID) == Long.valueOf(nasaDayMyAdapter.getItemId(pos))) {
                                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                                    break;
                                }
                            }
                        }
                    })
                    .setNegativeButton("No", (click, arg) -> { })
                    .show();
            return true;
        });
        SwipeRefreshLayout refresher = findViewById(R.id.refresher);
        refresher.setOnRefreshListener(() -> {
            nasaDayMyAdapter.notifyDataSetChanged(); //update yourself
            refresher.setRefreshing(false);  //get rid of spinning wheel;
        });
    }


    class NasaDayImageMyListAdapter extends BaseAdapter {

        public int getCount(){return list.size();}

        public Image getItem(int position){
            return list.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View newView = convertView;
            Image currentImage = (Image) getItem(position);
            //Bitmap image= null;

            //if (newView == null) {
            //    newView = getLayoutInflater().inflate(R.layout.activity_nasa_day_image_myfavorite_list, parent, false);
            //} else {
                newView = getLayoutInflater().inflate(R.layout.nasadayimage_myfavoritelist_eachitem, null);
                TextView textDate = (TextView) newView.findViewById(R.id.myfavoriteDate);
                textDate.setText("DATE: "+ currentImage.getDate());
                TextView textTitle = (TextView) newView.findViewById(R.id.myfavoriteTitle);
                textTitle.setText("TITLE: "+ currentImage.getTitle());

                FileInputStream fis;
                File file = getBaseContext().getFileStreamPath(currentImage.getTitle() + ".png");
                if (file.exists()) {
                    try {
                        fis = openFileInput(currentImage.getTitle() + ".png");
                        image = BitmapFactory.decodeStream(fis);
                        ImageView myFavoriteImage = (ImageView) newView.findViewById(R.id.myfavoriteImg);
                        myFavoriteImage.setImageBitmap(image);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            return newView;
        }
        public long getItemId(int position){return getItem(position).getId();}

    }


    public void loadDataFromDatabase(){
        list.clear();
        db= NasaDayActivity.dbOpener.getWritableDatabase();
        String[] columns= {NasaDayImageMyOpener.COL_ID, NasaDayImageMyOpener.COL_DATE, NasaDayImageMyOpener.COL_TITLE, NasaDayImageMyOpener.COL_URL, NasaDayImageMyOpener.COL_HDURL};
        Cursor results= db.query(true, NasaDayImageMyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        while(results.moveToNext()){
            String date= results.getString(results.getColumnIndex(NasaDayImageMyOpener.COL_DATE));
            String title= results.getString(results.getColumnIndex(NasaDayImageMyOpener.COL_TITLE));
            String url= results.getString(results.getColumnIndex(NasaDayImageMyOpener.COL_URL));
            String hdurl= results.getString(results.getColumnIndex(NasaDayImageMyOpener.COL_HDURL));
            long id= results.getLong(results.getColumnIndex(NasaDayImageMyOpener.COL_ID));
            list.add(new Image(date, title, url, hdurl, id));
        }
    }

}
