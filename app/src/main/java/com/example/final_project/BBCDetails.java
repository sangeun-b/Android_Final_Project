package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * This class extends AppCompatActivity. When user clicks on a BBC news title, it is used to populate a new page with BBC news detailed information,
 * including title, description, publish date and a link to the news website.
 * @author Xin Guo
 * @version 1.0
 */
public class BBCDetails extends AppCompatActivity {
    /**
     * Represents a SharedPreferences viriable and initialize to null.
     */
    //SharedPreferences prefs = null;
    /**
     * Represents BBCItem id in long.
     */
    Long id;
    /**
     * This method gets the intent from main page and extract all the detailed information, including title, description, publish date and a link to the news website.
     * It allows the user to add the news into favourite list or remove from favourite list.
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method of every Android Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbcdetails);

        Intent fromBBC = getIntent();

        TextView theText = (TextView) findViewById(R.id.Title);
        String title = fromBBC.getStringExtra("TITLE");
        theText.setTextSize(20);
        theText.setText("Title: " + title);

        TextView theText1 = (TextView) findViewById(R.id.Description);
        String description = fromBBC.getStringExtra("DESCRIPTION");
        theText1.setTextSize(20);
        theText1.setText("Description: " + description);

        TextView theText2 = (TextView) findViewById(R.id.Link);
        String link = fromBBC.getStringExtra("LINK");
        theText2.setTextSize(20);
        theText2.setText("Link: " + link);

        TextView theText3 = (TextView) findViewById(R.id.Date);
        String date = fromBBC.getStringExtra("DATE");
        theText3.setTextSize(20);
        theText3.setText("Date: " + date);

        Button BBCBack = findViewById(R.id.BBCBack);
        BBCBack.setOnClickListener(v -> {
            finish();
        });

        //prefs = getSharedPreferences("FavouriteNewsName", Context.MODE_PRIVATE);
        //prefs.getString("ReserveName", null);

        Button BBCAddToFavourite = findViewById(R.id.BBCAddToFavourite);
        id = fromBBC.getLongExtra("ID", 0);
        String isFavourite = fromBBC.getStringExtra("ISFAVOURITE");
        int position = fromBBC.getIntExtra("POSITION", 0);

        if(isFavourite.equals("true"))
            BBCAddToFavourite.setText("REMOVE FROM FAVOURITE LIST");

        BBCAddToFavourite.setOnClickListener(v -> {
            BBCItem selectedItem = BBCActivity.itemList.get(position);
            if(BBCAddToFavourite.getText().equals("REMOVE FROM FAVOURITE LIST")) {
                modifyItem(selectedItem, "false");
                selectedItem.setIsFavourite("false");
                BBCAddToFavourite.setText("ADD TO FAVOURITE LIST");
                Snackbar.make(BBCAddToFavourite, "Removed from Favourite List", Snackbar.LENGTH_LONG).show();
            } else {
                modifyItem(selectedItem, "true");
                selectedItem.setIsFavourite("true");
                //saveSharedPrefs(title);
                BBCAddToFavourite.setText("REMOVE FROM FAVOURITE LIST");
                Snackbar.make(BBCAddToFavourite, "Added to Favourite List", Snackbar.LENGTH_LONG).show();
            }
        });
    }
    /**
     * This method is used to change the value of column COL_ISFAVOURITE in the database of BBCItem.
     * When adding a BBC news into favourite list, call this method and pass "true" as the second parameter.
     * When removing a BBC news from favourite list, call this method and pass "false" as the second parameter.
     * @param c the BBC news you want to modify
     * @param s "true" when adding a BBC news into favourite list or "false" when removing a BBC news from favourite list
     */
    protected void modifyItem(BBCItem c, String s)
    {
        ContentValues dataToInsert = new ContentValues();
        dataToInsert.put(BBCMyOpener.COL_ISFAVOURITE, s);
        BBCActivity.db.update(BBCMyOpener.TABLE_NAME, dataToInsert,BBCMyOpener.COL_ID + "= ?", new String[] {Long.toString(id)});
    }
    /**
     * This method is used to save SharedPreference on disk.
     * @param stringToSave the String value to save into SharedPreference
     */
    /*private void saveSharedPrefs(String stringToSave)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ReserveName", stringToSave);
        editor.commit();
    }*/
}
