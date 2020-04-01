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

public class BBCDetailsFragment extends Fragment {

    SharedPreferences prefs = null;
    Long id;
    private boolean isTablet;
    private AppCompatActivity parentActivity;

    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }

    public BBCDetailsFragment() {}

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

    protected void modifyItem(BBCItem c, String s)
    {
        ContentValues dataToInsert = new ContentValues();
        dataToInsert.put(BBCMyOpener.COL_ISFAVOURITE, s);
        BBCActivity.db.update(BBCMyOpener.TABLE_NAME, dataToInsert,BBCMyOpener.COL_ID + "= ?", new String[] {Long.toString(id)});
    }

    public void onAttach(Context context){
        super.onAttach(context);
        parentActivity = (AppCompatActivity)context;
    }
}
