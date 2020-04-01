package com.example.final_project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class BBCSearchList extends AppCompatActivity {

    ArrayList<BBCItem> itemList = new ArrayList<>();
    BBCSearchList.MyListAdapter adapter;
    String keyword;
    BBCDetailsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbcsearch_list);

        ListView BBCSearchList = findViewById(R.id.BBCSearchList);
        Intent fromBBC = getIntent();
        keyword = fromBBC.getStringExtra("KEYWORD");

        loadDataFromDatabase();

        //BBCSearchList.setAdapter(new BBCSearchList.MyListAdapter());
        //final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh1);
        //swipeRefreshLayout.setOnRefreshListener(() -> BBCSearchList.setAdapter(new BBCSearchList.MyListAdapter()));

        if(itemList.size() == 0) {
            Toast.makeText(BBCSearchList.this, "No news was found related to the keywords entered", Toast.LENGTH_LONG).show();
            //Snackbar.make(BBCSearchList, "No news was found related to the keywords entered", Snackbar.LENGTH_LONG).show();
        } else {
            adapter = new MyListAdapter();
            BBCSearchList.setAdapter(adapter);
        }

        Button BBCBack = findViewById(R.id.BBCBack2);
        BBCBack.setOnClickListener(v -> {
            finish();
        });

        boolean isTablet = findViewById(R.id.fragmentLocation) != null;

        BBCSearchList.setOnItemClickListener((parent,view,position,id)-> {
            Bundle goToDetails = new Bundle();
            goToDetails.putLong("ID", itemList.get(position).getId());
            goToDetails.putString("TITLE", itemList.get(position).getTitle());
            goToDetails.putString("DESCRIPTION", itemList.get(position).getDescription());
            goToDetails.putString("LINK", itemList.get(position).getLink());
            goToDetails.putString("DATE", itemList.get(position).getDate());
            goToDetails.putString("ISFAVOURITE", itemList.get(position).getIsFavourite());

            if(isTablet){
                fragment = new BBCDetailsFragment();
                fragment.setArguments(goToDetails);
                fragment.setTablet(true);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, fragment)
                        .commit();
            }else{
                Intent nextActivity = new Intent(BBCSearchList.this, BBCDetails.class);
                nextActivity.putExtras(goToDetails); //send data to next activity
                startActivity(nextActivity); //make the transition
            }

        });

    }

    private void loadDataFromDatabase()
    {
        itemList.clear();

        //get a database connection:
        BBCMyOpener dbOpener = new BBCMyOpener(this);
        BBCActivity.db = dbOpener.getWritableDatabase();

        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {BBCMyOpener.COL_ID, BBCMyOpener.COL_TITLE, BBCMyOpener.COL_DESCRIPTION, BBCMyOpener.COL_LINK, BBCMyOpener.COL_DATE, BBCMyOpener.COL_ISFAVOURITE};
        //query all the results from the database:
        Cursor results = BBCActivity.db.query(false, BBCMyOpener.TABLE_NAME, columns, BBCMyOpener.COL_TITLE + " like '%" + keyword + "%'", null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int idColIndex = results.getColumnIndex(BBCMyOpener.COL_ID);
        int titleColumnIndex = results.getColumnIndex(BBCMyOpener.COL_TITLE);
        int descriptionColIndex = results.getColumnIndex(BBCMyOpener.COL_DESCRIPTION);
        int linkColIndex = results.getColumnIndex(BBCMyOpener.COL_LINK);
        int dateColIndex = results.getColumnIndex(BBCMyOpener.COL_DATE);
        int isFavouriteColIndex = results.getColumnIndex(BBCMyOpener.COL_ISFAVOURITE);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            long id = results.getLong(idColIndex);
            String title = results.getString(titleColumnIndex);
            String description = results.getString(descriptionColIndex);
            String link = results.getString(linkColIndex);
            String date = results.getString(dateColIndex);
            String isFavourite = results.getString(isFavouriteColIndex);

            //add the new Contact to the array list:
            itemList.add(new BBCItem(id, title, description, link, date, isFavourite));
        }

        //At this point, the contactsList array has loaded every row from the cursor.
    }

    private class MyListAdapter extends BaseAdapter {
        public int getCount() {return itemList.size();}

        @Override
        public Object getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return ((BBCItem)getItem(position)).getId();
        }

        @Override
        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            BBCItem item = (BBCItem) getItem(position);
            View newView = inflater.inflate(R.layout.activity_bbclistview, null);
            TextView theText = newView.findViewById(R.id.bbclistview);
            theText.setTextSize(20);
            theText.setText(item.getId() + ": " + item.getTitle());
            return newView;
        }
    }

}
