package com.example.final_project;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class NasaDayImage extends AppCompatActivity {

    ProgressBar mProgressBar;
    ArrayList<Image> list= new ArrayList<>();
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_image);

        mProgressBar= findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        NasaImage fetchNasaImage = new NasaImage();
        if (NasaDayActivity.DatePickerFragment.date== null){NasaDayActivity.DatePickerFragment.date= new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Calendar.getInstance().getTime());}
        fetchNasaImage.execute("https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=" + NasaDayActivity.DatePickerFragment.date);

        TextView dateText = findViewById(R.id.dateTextView);
        TextView titleText =findViewById(R.id.titleTextView);
        TextView urlText = findViewById(R.id.urlTextView);
        TextView hdUrlText =findViewById(R.id.hdurlTextView);
        ContentValues newRowValues= new ContentValues();

        Button saveButton= findViewById(R.id.saveImageButton);
        saveButton.setOnClickListener(click-> {
            /*title and hdurl can have null values*/
            String dateInput= dateText.getText().toString().substring(6);
            String titleInput= titleText.getText().toString().substring(7);
            String urlInput= urlText.getText().toString().substring(5);
            String hdUrlInput= hdUrlText.getText().toString().substring(7);

            //if(!dateInput.equalsIgnoreCase("null") && !titleInput.equalsIgnoreCase("null") && !urlInput.equalsIgnoreCase("null") && !hdUrlInput.equalsIgnoreCase("null")) {
                //newRowValues.put(NasaDayImageMyOpener.COL_DATE, dateInput);
                //newRowValues.put(NasaDayImageMyOpener.COL_TITLE, titleInput);
                //newRowValues.put(NasaDayImageMyOpener.COL_URL, urlInput);
                //newRowValues.put(NasaDayImageMyOpener.COL_HDURL, hdUrlInput);
                //long id= db.insert(NasaDayImageMyOpener.TABLE_NAME,null, newRowValues);
                //list.add(new Image(dateInput, titleInput, urlInput, hdUrlInput, id));
            //}

            /*if(titleInput.equalsIgnoreCase("null") && hdUrlInput.equalsIgnoreCase("null")){
                newRowValues.put(NasaDayImageMyOpener.COL_DATE, dateInput);
                newRowValues.put(NasaDayImageMyOpener.COL_TITLE, "null");
                newRowValues.put(NasaDayImageMyOpener.COL_URL, urlInput);
                newRowValues.put(NasaDayImageMyOpener.COL_HDURL, "null");
                long id= db.insert(NasaDayImageMyOpener.TABLE_NAME, null, newRowValues);
                list.add(new Image(dateInput, null, urlInput, null, id));
            }*/

            Toast.makeText(NasaDayImage.this, "Added to the favorite list" , Toast.LENGTH_LONG).show(); });
    }

    class NasaImage extends AsyncTask<String, Integer, String> {

        String date=null, url=null, hdUrl=null, title=null, ret=null;

        public String doInBackground(String... args) {
            try {
                URL link = new URL(args[0]);
                HttpURLConnection connection = (HttpURLConnection) link.openConnection();
                InputStream response = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line= null;

                while ((line = reader.readLine()) != null) {sb.append(line + "\n"); }
                String result = sb.toString();
                JSONObject json = new JSONObject(result);

                date= json.getString("date");
                publishProgress(25);
                url= json.getString("url");
                publishProgress(50);
                hdUrl= json.getString("hdurl");
                publishProgress(75);
                title= json.getString("title");
                publishProgress(100);
                return "Done";
            }
            catch(MalformedURLException mfe){ ret = "Malformed URL exception"; }
            catch(IOException ioe)          { ret = "IO Exception. Is the Wifi connected?";}
            catch(JSONException je) { ret = "JSON exception."; }
            return ret;
        }

        public void onProgressUpdate(Integer... args) {
            super.onProgressUpdate(args);
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(args[0]);
        }

        public void onPostExecute(String fromDoInBackground) {
            super.onPostExecute(fromDoInBackground);
            TextView dateText = findViewById(R.id.dateTextView);
            dateText.setText("DATE: " + date);
            TextView titleText =findViewById(R.id.titleTextView);
            titleText.setText("TITLE: " + title);
            TextView urlText = findViewById(R.id.urlTextView);
            urlText.setText("URL: " + url);
            TextView hdUrlText =findViewById(R.id.hdurlTextView);
            hdUrlText.setText("HDURL: "+ hdUrl);
            mProgressBar.setVisibility(View.INVISIBLE);

        }
    }

    /*class MyListAdapter extends BaseAdapter {

        public int getCount(){return list.size();}

        public Image getItem(int position){
            return list.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            View newView = convertView;
            Image currentImage= (Image) getItem(position);
        }

        public long getItemId(int position){return getItem(position).getId();}
    }*/



}


