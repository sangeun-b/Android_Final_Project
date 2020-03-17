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

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
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

    ArrayList<BBCItem> itemList = new ArrayList<>();
    MyListAdapter adapter;
    int positionClicked = 0;
    SQLiteDatabase db;
    int versionNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbc);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get the fields from the screen:
        EditText BBCSearchText = findViewById(R.id.BBCSearchText);
        Button BBCSearchButton = findViewById(R.id.BBCSearchButton);
        ListView BBCTitle = findViewById(R.id.BBCTitle);
        Button BBCFavouriteButton = findViewById(R.id.BBCFavouriteButton);

        MyHTTPRequest req = new MyHTTPRequest();
        req.execute("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");  //Type 1

        BBCFavouriteButton.setOnClickListener( new View.OnClickListener()
        {   public void onClick(View v) {
            Intent goToProfile = new Intent(BBCActivity.this, BBCFavouriteList.class);
            startActivity(goToProfile);
        } });


        loadDataFromDatabase(); //get any previously saved Contact objects
        adapter = new MyListAdapter();
        BBCTitle.setAdapter(adapter);


        /*        list.setOnItemLongClickListener((parent,view,position,id)-> {
         *//*View extraViewStuff = getLayoutInflater().inflate(R.layout.row_layout);
            ((TextView) extraViewStuff.findViewById(R.id.textGoesHere)).setText("More stuff");*//*

            Message selectedMessage = msgList.get(position);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to delete this?").setMessage("The selected row is: " + (position+1) + "\nThe database id is: " + (adapter.getItemId(position)))
                    .setPositiveButton("Yes", (click, arg) -> {
                        deleteContact(selectedMessage);
                        msgList.remove(position);
                        list.setAdapter(new MyListAdapter());
                        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
                        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
                        {
                            @Override
                            public void onRefresh()
                            {
                                list.setAdapter(new MyListAdapter());
                            }
                        });
                    })
                    .setNegativeButton("No", (click, arg) -> { Toast.makeText(ChatRoomActivity.this, "Nothing changed", Toast.LENGTH_LONG).show();
                    }).create().show();
            return true;
        });*/
    }

    //Type1     Type2   Type3
    private class MyHTTPRequest extends AsyncTask< String, Integer, String>
    {
        private String title, description, link, date;
        //Type3                Type1
        protected String doInBackground(String ... args)
        {
            try {

                //create a URL object of what server to contact:
                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //From part 3: slide 19
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");

                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                while(eventType != XmlPullParser.END_DOCUMENT)
                {

                    if(eventType == XmlPullParser.START_TAG)
                    {
                        //If you get here, then you are pointing at a start tag
                        if(xpp.getName().equals("item"))
                        {
                            //If you get here, then you are pointing to a <Weather> start tag
                            title = xpp.getAttributeValue(null,    "title");
                            publishProgress(10);
                            description = xpp.getAttributeValue(null,    "description");
                            publishProgress(20);
                            link = xpp.getAttributeValue(null, "link");
                            publishProgress(30);
                            date = xpp.getAttributeValue(null, "pubDate");
                            publishProgress(40);

                            //add to the database and get the new ID
                            ContentValues newRowValues = new ContentValues();

                            //Now provide a value for every database column defined in MyOpener.java:
                            newRowValues.put(BBCMyOpener.COL_TITLE, title);
                            newRowValues.put(BBCMyOpener.COL_DESCRIPTION, description);
                            newRowValues.put(BBCMyOpener.COL_LINK, link);
                            newRowValues.put(BBCMyOpener.COL_DATE, date);

                            //Now insert in the database:
                            long newId = db.insert(BBCMyOpener.TABLE_NAME, null, newRowValues);

                            BBCItem item = new BBCItem(title, description, link, date, newId);
                            itemList.add(item);

                        }
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
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
            setProgress(args[0]);
        }
        //Type3
        public void onPostExecute(String fromDoInBackground)
        {
            Log.i("HTTP", fromDoInBackground);

            ListView BBCTitle = findViewById(R.id.BBCTitle);
            BBCTitle.setAdapter(new MyListAdapter());

        }
    }

    protected void deleteContact(BBCItem c)
    {
        db.delete(BBCMyOpener.TABLE_NAME, BBCMyOpener.COL_ID + "= ?", new String[] {Long.toString(c.getId())});
    }

    private void loadDataFromDatabase()
    {
        //get a database connection:
        BBCMyOpener dbOpener = new BBCMyOpener(this);
        db = dbOpener.getWritableDatabase();


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {BBCMyOpener.COL_ID, BBCMyOpener.COL_TITLE, BBCMyOpener.COL_DESCRIPTION, BBCMyOpener.COL_LINK, BBCMyOpener.COL_DATE};
        //query all the results from the database:
        Cursor results = db.query(false, BBCMyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int titleColumnIndex = results.getColumnIndex(BBCMyOpener.COL_TITLE);
        int descriptionColIndex = results.getColumnIndex(BBCMyOpener.COL_DESCRIPTION);
        int linkColIndex = results.getColumnIndex(BBCMyOpener.COL_LINK);
        int dateColIndex = results.getColumnIndex(BBCMyOpener.COL_DATE);
        int idColIndex = results.getColumnIndex(BBCMyOpener.COL_ID);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            String title = results.getString(titleColumnIndex);
            String description = results.getString(descriptionColIndex);
            String link = results.getString(linkColIndex);
            String date = results.getString(dateColIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            itemList.add(new BBCItem(title, description, link, date, id));
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
            theText.setText(item.getTitle());
            return newView;
        }

    }

}
