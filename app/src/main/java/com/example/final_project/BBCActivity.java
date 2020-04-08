package com.example.final_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class extends AppCompatActivity. It is the main page of BBC News application. It is used to displays all the BBC news titles in the page.
 * Click on the star image button on the upper right corner will go to the favourite list page.
 * Short click on the news title will go to the details page.
 * Enter the keyword in the edit text and click on the search button will go to the search list page to find specific news contains the keyword.
 * @author Xin Guo
 * @version 1.0
 */
public class BBCActivity extends AppCompatActivity {
    /**
     * Instantiate a new BBCItem ArrayList to store BBC news.
     */
    static ArrayList<BBCItem> itemList = new ArrayList<>();
    /**
     * Represents a MyListAdapter object.
     */
    MyListAdapter adapter;
    /**
     * Represents a SQLiteDatabase object.
     */
    static SQLiteDatabase db;
    /**
     * Represents a SharedPreferences viriable and initialize to null.
     */
    SharedPreferences prefs = null;
    /**
     * This method instantiate a new MyHTTPRequest object and connect to the website to download BBC news and save to the database.
     * It calls setAdapter() to populate the listview with news titles.
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method of every Android Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbc);

        //Get the fields from the screen:
        EditText BBCSearchText = findViewById(R.id.BBCSearchText);
        Button BBCSearchButton = findViewById(R.id.BBCSearchButton);
        ListView BBCTitle = findViewById(R.id.BBCTitle);
        ImageView BBCFavouriteStar = findViewById(R.id.BBCFavouriteStar);

        prefs = getSharedPreferences("FavouriteNewsName", Context.MODE_PRIVATE);
        String search = prefs.getString("ReserveName", null);
        BBCSearchText.setText(search);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarBBC);
        progressBar.setVisibility(View.VISIBLE);

        MyHTTPRequest req = new MyHTTPRequest();
        req.execute("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");  //Type 1

        loadDataFromDatabase(); //get any previously saved Contact objects
        adapter = new MyListAdapter();
        BBCTitle.setAdapter(adapter);

        BBCFavouriteStar.setOnClickListener(v -> {
            Intent goToProfile = new Intent(BBCActivity.this, BBCFavouriteList.class);
            startActivity(goToProfile);
        });

        BBCSearchButton.setOnClickListener(v -> {
//            saveSharedPrefs(editText.getText().toString());
            Intent goToSearch = new Intent(BBCActivity.this, BBCSearchList.class);
            goToSearch.putExtra("KEYWORD", BBCSearchText.getText().toString());
            startActivity(goToSearch);
        });

        /*ListView BBCTitle = findViewById(R.id.BBCTitle);
            BBCTitle.setAdapter(new MyListAdapter());
            final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
            swipeRefreshLayout.setOnRefreshListener(() -> BBCTitle.setAdapter(new MyListAdapter()));*/

        BBCTitle.setOnItemClickListener((parent,view,position,id)-> {
            Intent goToDetails = new Intent(BBCActivity.this, BBCDetails.class);
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
    }

    /**
     * In order for the interface to be responsive to user input, any long-running tasks must be run on a different thread.
     * Synchronization between the GUI and background threads can cause many problems.
     * To avoid this, Android provides the AsyncTask object which takes care of the thread synchronization issues.
     * The class has 3 important functions: doInBackground, onProgressUpdate, onPostExecute.
     * This class is used to connect to the website, download news and update GUI.
     */
    //Type1     Type2   Type3
    private class MyHTTPRequest extends AsyncTask< String, Integer, String>
    {
        /**
         * The first generic parameter is for the doInBackground array type. The “...” means variable arguments (array).
         * The doInBackground function is where you should be doing any long-lasting computations, network access, file writing, etc.
         * @param args The parameters of the task.
         * @return A result, defined by the subclass of this task.
         */
        //Type3                Type1
        protected String doInBackground(String ... args)
        {
            try {

                //create a URL object of what server to contact:
                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                publishProgress(25);
                //wait for data:
                InputStream response = urlConnection.getInputStream();

                /*
                //JSON reading:
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string

                JSONObject obj1 = new JSONObject(result);
                JSONArray array= obj1.getJSONObject("rss").getJSONArray("item");
                for(int i=0; i<array.length(); i++){
                    JSONObject obj = array.getJSONObject(i);
                    BBCItem item = new BBCItem();
                    ContentValues newRowValues = new ContentValues();
                    item.setTitle(obj.getString("title"));
                    newRowValues.put(BBCMyOpener.COL_TITLE, obj.getString("title"));
                    item.setDescription(obj.getString("description"));
                    newRowValues.put(BBCMyOpener.COL_DESCRIPTION, obj.getString("description"));
                    item.setLink(obj.getString("link"));
                    newRowValues.put(BBCMyOpener.COL_LINK, obj.getString("link"));
                    item.setDate(obj.getString("pubDate"));
                    newRowValues.put(BBCMyOpener.COL_DATE, obj.getString("pubDate"));
                    item.setIsFavourite("false");
                    newRowValues.put(BBCMyOpener.COL_ISFAVOURITE, "false");
                    //Now insert in the database:
                    long newId = db.insert(BBCMyOpener.TABLE_NAME, null, newRowValues);
                    item.setId(newId);
                    itemList.add(item);
                }
                */

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");
                xpp.nextTag();

                    xpp.require(XmlPullParser.START_TAG, null, "rss");
                    publishProgress(50);
                    while (xpp.next() != XmlPullParser.END_TAG) {
                        //publishProgress(50);
                        if (xpp.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }
                        if (xpp.getName().equals("channel")) {
                            readChannel(xpp);
                        } else {
                            skip(xpp);
                        }
                    }
            }
            catch (Exception e)
            {
                Log.e("Error", e.getMessage());
            }

            return "Done";
        }
        /**
         * To update any progress indicators, or information on the GUI.
         * @param args The values indicating progress.
         */
        //Type 2
        public void onProgressUpdate(Integer ... args)
        {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarBBC);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(args[0]);
        }
        /**
         * When doInBackground has finished, this method can do the final GUI update.
         * @param fromDoInBackground The result of the operation computed by doInBackground(Params...).
         */
        //Type3
        public void onPostExecute(String fromDoInBackground)
        {
            Log.i("HTTP", fromDoInBackground);

            ListView BBCTitle = findViewById(R.id.BBCTitle);
            BBCTitle.setAdapter(new MyListAdapter());

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarBBC);
            progressBar.setVisibility(View.INVISIBLE);

        }
    }
    /**
     * This class connects to the database and get all the columns stored in the database.
     * With all the column information, it instantiate new BBCItem and add to the ArrayList.
     */
    private void loadDataFromDatabase()
    {
        itemList.clear();

        //get a database connection:
        BBCMyOpener dbOpener = new BBCMyOpener(this);
        db = dbOpener.getWritableDatabase();

        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {BBCMyOpener.COL_ID, BBCMyOpener.COL_TITLE, BBCMyOpener.COL_DESCRIPTION, BBCMyOpener.COL_LINK, BBCMyOpener.COL_DATE, BBCMyOpener.COL_ISFAVOURITE};
        //query all the results from the database:
        Cursor results = db.query(false, BBCMyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

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
     * This method sets the XML START_TAG as "channel", loop through and search for "item" tag on the website. Call readItem() method to get the information between "item" tag.
     * @param parser to process quickly and efficiently all input elements
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void readChannel(XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, null, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("item")) {
                readItem(parser);
            } else {
                skip(parser);
            }
        }
    }
    /**
     * This method sets the XML START_TAG as "item", loop through and search for "item" tag on the website. Call readItem() method to get the information between "item" tag.
     * @param parser to process quickly and efficiently all input elements
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void readItem(XmlPullParser parser) throws XmlPullParserException, IOException {

        String title=null, description=null, link=null, date=null, isFavourite="false";

        parser.require(XmlPullParser.START_TAG, null, "item");
        ContentValues newRowValues = new ContentValues();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("title")) {
                title = readTitle(parser);
                newRowValues.put(BBCMyOpener.COL_TITLE, title);
            } else if (parser.getName().equals("description")) {
                    description = readDescription(parser);
                    newRowValues.put(BBCMyOpener.COL_DESCRIPTION, description);
                } else if (parser.getName().equals("link")) {
                        link = readLink(parser);
                        newRowValues.put(BBCMyOpener.COL_LINK, link);
                    } else if (parser.getName().equals("pubDate")) {
                            date = readDate(parser);
                            newRowValues.put(BBCMyOpener.COL_DATE, date);
                        } else {
                            skip(parser);
                        }
        }

        newRowValues.put(BBCMyOpener.COL_ISFAVOURITE, isFavourite);

        //Now insert in the database:
        long newId = db.insert(BBCMyOpener.TABLE_NAME, null, newRowValues);

        BBCItem item = new BBCItem(newId, title, description, link, date, isFavourite);
        itemList.add(item);
    }
    /**
     * This method sets the XML START_TAG as "title" on the website. Call readText() method to get the news title between "title" tag.
     * @param parser to process quickly and efficiently all input elements
     * @return news title in String
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "title");
        String title = readText(parser);
        Log.d("title", title);
        parser.require(XmlPullParser.END_TAG, null, "title");
        return title;
    }
    /**
     * This method sets the XML START_TAG as "description" on the website. Call readText() method to get the news description between "description" tag.
     * @param parser to process quickly and efficiently all input elements
     * @return news description in String
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "description");
        String description = readText(parser);
        Log.d("description", description);
        parser.require(XmlPullParser.END_TAG, null, "description");
        return description;
    }
    /**
     * This method sets the XML START_TAG as "link" on the website. Call readText() method to get the news link between "link" tag.
     * @param parser to process quickly and efficiently all input elements
     * @return news link in String
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "link");
        String link = readText(parser);
        Log.d("link", link);
        parser.require(XmlPullParser.END_TAG, null, "link");
        return link;
    }
    /**
     * This method sets the XML START_TAG as "pubDate" on the website. Call readText() method to get the news publish date between "pubDate" tag.
     * @param parser to process quickly and efficiently all input elements
     * @return news publish date in String
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "pubDate");
        String date = readText(parser);
        Log.d("date", date);
        parser.require(XmlPullParser.END_TAG, null, "pubDate");
        return date;
    }
    /**
     * This method is used to parser String.
     * @param parser to process quickly and efficiently all input elements
     * @return information in String
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    /**
     * This method is for the parser to skip tags it's not interested in.
     * @param parser to process quickly and efficiently all input elements
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
    /**
     * Called as part of the activity lifecycle when the user no longer actively interacts with the activity, but it is still visible on screen.
     */
    protected void onPause() {
        super.onPause();

        EditText BBCSearchText = findViewById(R.id.BBCSearchText);
        String search = BBCSearchText.getText().toString();
        saveSharedPrefs(search);
    }
    /**
     * This method is used to save search keyword in SharedPreference on disk. Whenever the user returns to the main page, the search keyword from last time is still in the edit text.
     * @param stringToSave the String value to save into SharedPreference
     */
    private void saveSharedPrefs(String stringToSave)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ReserveName", stringToSave);
        editor.commit();
    }
}
