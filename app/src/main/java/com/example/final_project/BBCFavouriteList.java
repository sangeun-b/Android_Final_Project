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

public class BBCFavouriteList extends AppCompatActivity {

    ArrayList<BBCItem> itemList = new ArrayList<>();
    BBCFavouriteList.MyListAdapter adapter;

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

    protected void modifyItem(BBCItem c, String s)
    {
        ContentValues dataToInsert = new ContentValues();
        dataToInsert.put(BBCMyOpener.COL_ISFAVOURITE, s);
        BBCActivity.db.update(BBCMyOpener.TABLE_NAME, dataToInsert,BBCMyOpener.COL_ID + "= ?", new String[] {Long.toString(c.getId())});
    }

}
