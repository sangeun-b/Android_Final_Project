package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
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
 * This class extends Fragment class to create layouts for various devices.
 * Phones are typically in Portrait mode, with little screen space. Tablets are typically in Landscape mode with a lot of screen space.
 * Fragments let you reuse components for both phone and tablets.
 */
public class BBCDetailsFragment extends Fragment {
    /**
     * Represents bundle id in long.
     */
    Long id;
    /**
     * A boolean value represents whether the emulator is Table.
     */
    private boolean isTablet;
    /**
     * Represents an AppCompatActivity variable used in onAttach() method.
     */
    private AppCompatActivity parentActivity;
    /**
     * Setter for boolean value isTablet. Set isTablet=true when the emulator is Tablet. Set isTablet=false when the emulator is not Tablet.
     * @param tablet true or false represents whether the emulator is Table
     */
    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }
    /**
     * No-arg BBCDetailsFragment constructor.
     */
    public BBCDetailsFragment() {}
    /**
     * Called to have the fragment instantiate its user interface view. This is where to inflate the UI.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view. This value may be null.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.activity_bbcfragment, container, false);
        Bundle bundle = getArguments();

        TextView theText = (TextView) result.findViewById(R.id.TitleFragment);
        String title = bundle.getString("TITLE");
        theText.setTextSize(20);
        theText.setText("Title: " + title);

        TextView theText1 = (TextView) result.findViewById(R.id.DescriptionFragment);
        String description = bundle.getString("DESCRIPTION");
        theText1.setTextSize(20);
        theText1.setText("Description: " + description);

        TextView theText2 = (TextView) result.findViewById(R.id.LinkFragment);
        String link = bundle.getString("LINK");
        theText2.setTextSize(20);
        theText2.setText("Link: " + link);

        TextView theText3 = (TextView) result.findViewById(R.id.DateFragment);
        String date = bundle.getString("DATE");
        theText3.setTextSize(20);
        theText3.setText("Date: " + date);

        Button BBCBack = result.findViewById(R.id.BBCBackFragment);
        BBCBack.setOnClickListener(v -> {
            if (isTablet) { //both the list and details are on the screen
                BBCSearchList parent = (BBCSearchList) getActivity();
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            } else  {
                BBCEmptyActivity parent = (BBCEmptyActivity) getActivity();
                Intent backToSearchActivity = new Intent();
                parent.setResult(Activity.RESULT_OK, backToSearchActivity);
                parent.finish();
            }
        });

        Button BBCAddToFavourite = result.findViewById(R.id.BBCAddToFavouriteFragment);
        id = bundle.getLong("ID", 0);
        String isFavourite = bundle.getString("ISFAVOURITE");
        int position = bundle.getInt("POSITION", 0);

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
                BBCAddToFavourite.setText("REMOVE FROM FAVOURITE LIST");
                Snackbar.make(BBCAddToFavourite, "Added to Favourite List", Snackbar.LENGTH_LONG).show();
            }
        });

        return result;
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
     * Called when a fragment is first attached to its context.
     * @param context Context
     */
    public void onAttach(Context context){
        super.onAttach(context);
        parentActivity = (AppCompatActivity)context;
    }
}
