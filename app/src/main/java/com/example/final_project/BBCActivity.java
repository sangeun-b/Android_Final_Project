package com.example.final_project;

import android.content.Intent;
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

public class BBCActivity extends AppCompatActivity {

    static ArrayList<BBCItem> itemList = new ArrayList<>();
    MyListAdapter adapter;
    int positionClicked = 0;
    static SQLiteDatabase db;
    int versionNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbc);

        //Get the fields from the screen:
        EditText BBCSearchText = findViewById(R.id.BBCSearchText);
        Button BBCSearchButton = findViewById(R.id.BBCSearchButton);
        ListView BBCTitle = findViewById(R.id.BBCTitle);
        ImageView BBCFavouriteStar = findViewById(R.id.BBCFavouriteStar);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
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

    //Type1     Type2   Type3
    private class MyHTTPRequest extends AsyncTask< String, Integer, String>
    {
        //Type3                Type1
        protected String doInBackground(String ... args)
        {
            try {

                //create a URL object of what server to contact:
                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                publishProgress(10);
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
                    publishProgress(25);
                    while (xpp.next() != XmlPullParser.END_TAG) {
                        publishProgress(50);
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

        //Type 2
        public void onProgressUpdate(Integer ... args)
        {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            setProgress(args[0]);
        }
        //Type3
        public void onPostExecute(String fromDoInBackground)
        {
            Log.i("HTTP", fromDoInBackground);

            ListView BBCTitle = findViewById(R.id.BBCTitle);
            BBCTitle.setAdapter(new MyListAdapter());

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);

        }
    }

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

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "title");
        String title = readText(parser);
        Log.d("title", title);
        parser.require(XmlPullParser.END_TAG, null, "title");
        return title;
    }

    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "description");
        String description = readText(parser);
        Log.d("description", description);
        parser.require(XmlPullParser.END_TAG, null, "description");
        return description;
    }

    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "link");
        String link = readText(parser);
        Log.d("link", link);
        parser.require(XmlPullParser.END_TAG, null, "link");
        return link;
    }

    private String readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "pubDate");
        String date = readText(parser);
        Log.d("date", date);
        parser.require(XmlPullParser.END_TAG, null, "pubDate");
        return date;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

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

}
