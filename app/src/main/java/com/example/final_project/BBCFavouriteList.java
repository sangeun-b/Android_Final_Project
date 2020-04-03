package com.example.final_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * This class extends AppCompatActivity. It is used to displays all the favourite news titles in the page.
 * Long click on the news titles will prompt an AlertDialog with the option to remove the news from favourite list.
 * By clicking yes, it will set the COL_ISFAVOURITE to false, remove from ArrayList and refresh the listview.
 * Short click on the news title will go to the details page. It also allows user to go back to previous page.
 * @author Xin Guo
 * @version 1.0
 */
public class BBCFavouriteList extends AppCompatActivity {
    /**
     * Instantiate a new BBCItem ArrayList to store BBC news.
     */
    ArrayList<BBCItem> itemList = new ArrayList<>();
    /**
     * Represents a MyListAdapter object.
     */
    BBCFavouriteList.MyListAdapter adapter;
    /**
     * This method displays all the favourite news titles in the page. It calls loadDataFromDatabase() method to search for favourite news.
     * If something found, call setAdapter() to populate the listview with news titles.
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method of every Android Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbcfavourite_list);

        ListView BBCFavouriteList = findViewById(R.id.BBCFavouriteList);

        loadDataFromDatabase();
        adapter = new MyListAdapter();
        BBCFavouriteList.setAdapter(adapter);

        Button BBCBack = findViewById(R.id.BBCBack1);
        BBCBack.setOnClickListener(v -> {
            finish();
        });

        BBCFavouriteList.setOnItemClickListener((parent,view,position,id)-> {
            Intent goToDetails = new Intent(BBCFavouriteList.this, BBCDetails.class);
            BBCItem selectedItem = itemList.get(position);

            goToDetails.putExtra("POSITION", position);
            goToDetails.putExtra("ID", selectedItem.getId());
            goToDetails.putExtra("TITLE", selectedItem.getTitle());
            goToDetails.putExtra("DESCRIPTION", selectedItem.getDescription());
            goToDetails.putExtra("LINK", selectedItem.getLink());
            goToDetails.putExtra("DATE", selectedItem.getDate());
            goToDetails.putExtra("ISFAVOURITE", selectedItem.getIsFavourite());

            startActivity(goToDetails);

        });

        BBCFavouriteList.setOnItemLongClickListener((parent,view,position,id)-> {

            BBCItem selectedItem = itemList.get(position);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to remove this from Favourite List?").setPositiveButton("Yes", (click, arg) -> {
                modifyItem(selectedItem, "false");
                selectedItem.setIsFavourite("false");
                itemList.remove(position);
                BBCFavouriteList.setAdapter(new MyListAdapter());
                final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
                swipeRefreshLayout.setOnRefreshListener(() -> BBCFavouriteList.setAdapter(new MyListAdapter()));
                Toast.makeText(BBCFavouriteList.this, "News " + selectedItem.getId() + " removed from Favourite List", Toast.LENGTH_LONG).show();
            })
                .setNegativeButton("No", (click, arg) -> { Toast.makeText(BBCFavouriteList.this, "Nothing changed", Toast.LENGTH_LONG).show();
                }).create().show();
            return true;
        });

    }
    /**
     * This class connects to the database and get all the columns where COL_ISFAVOURITE is true, represents the news being added to the favourite list.
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
        Cursor results = BBCActivity.db.query(false, BBCMyOpener.TABLE_NAME, columns, BBCMyOpener.COL_ISFAVOURITE + "= 'true'", null, null, null, null, null);

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
        BBCActivity.db.update(BBCMyOpener.TABLE_NAME, dataToInsert,BBCMyOpener.COL_ID + "= ?", new String[] {Long.toString(c.getId())});
    }
}
