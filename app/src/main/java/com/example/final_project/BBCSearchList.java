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

/**
 * This class extends AppCompatActivity. It is used to display news title which contains the keyword user entered in the main page.
 * It gets intent from main page, call loadDataFromDatabase() method and search for the keyword in the database.
 * If nothing found, make a toast says "No news was found related to the keywords entered".
 * If something found, call setAdapter() to populate the listview with news titles.
 * Short click on the news title will go to the details page. It also allows user to go back to previous page.
 * @author Xin Guo
 * @version 1.0
 */
public class BBCSearchList extends AppCompatActivity {
    /**
     * Instantiate a new BBCItem ArrayList to store BBC news.
     */
    ArrayList<BBCItem> itemList = new ArrayList<>();
    /**
     * Represents a MyListAdapter object.
     */
    BBCSearchList.MyListAdapter adapter;
    /**
     * Represents the keyword user entered in the main page.
     */
    String keyword;
    /**
     * Represents a BBCDetailsFragment object.
     */
    BBCDetailsFragment fragment;
    /**
     * This class is used to display news title which contains the keyword user entered in the main page.
     * It gets intent from main page, call loadDataFromDatabase() method and search for the keyword in the database.
     * If nothing found, make a toast says "No news was found related to the keywords entered".
     * If something found, call setAdapter() to populate the listview with news titles.
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method of every Android Activity
     */
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
    /**
     * This class connects to the database and get all the columns where COL_TITLE contains the keyword user entered in the main page.
     * With all the column information, it instantiate new BBCItem and add to the ArrayList.
     */
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

    /**
     * To populate the ListView with data, call setAdapter() on the listview. ListAdapter is an Interface that you must implement by writing these 4 public functions:
     * int getCount() – returns the number of items
     * Object getItem(int position) – returns what to show at row position
     * View getView( ) – creates a View object to go in a row of the ListView
     * long getItemId(int i) – returns the database id of the item at position i
     *
     * @author Xin Guo
     * @version 1.0
     */
    private class MyListAdapter extends BaseAdapter {
        /**
         * How many items are in the data set represented by this Adapter.
         * @return Count of items.
         */
        public int getCount() {return itemList.size();}
        /**
         * Get the data item associated with the specified position in the data set.
         * @param position Position of the item whose data we want within the adapter's data set.
         * @return The data at the specified position.
         */
        @Override
        public Object getItem(int position) {
            return itemList.get(position);
        }
        /**
         * Get the row id associated with the specified position in the list.
         * @param position The position of the item within the adapter's data set whose row id we want.
         * @return The id of the item at the specified position.
         */
        @Override
        public long getItemId(int position) {
            return ((BBCItem)getItem(position)).getId();
        }
        /**
         * Get a View that displays the data at the specified position in the data set.
         * @param position The position of the item within the adapter's data set of the item whose view we want.
         * @param old The old view to reuse, if possible.
         * @param parent The parent that this view will eventually be attached to.
         * @return A View corresponding to the data at the specified position.
         */
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
