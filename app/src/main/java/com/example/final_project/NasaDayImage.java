package com.example.final_project;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


public class NasaDayImage extends AppCompatActivity {

    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_image);

        mProgressBar= findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        NasaImage fetchNasaImage = new NasaImage();
        fetchNasaImage.execute("https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=" + NasaDayActivity.DatePickerFragment.date);

        Button saveButton= findViewById(R.id.saveImageButton);
        saveButton.setOnClickListener(click-> {Toast.makeText(NasaDayImage.this, "The info get added to my favorite list" , Toast.LENGTH_LONG).show(); });
    }

    class NasaImage extends AsyncTask<String, Integer, String> {

        String date=null, url=null, hdUrl=null, title=null, ret=null;

        public String doInBackground(String... args) {
            //String queryurl = "https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=" + NasaDayActivity.DatePickerFragment.date;
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
                //Log.i("NasaImage date", date);
                url= json.getString("url");
                publishProgress(50);
                //Log.i("NasaImage url", url);
                hdUrl= json.getString("hdurl");
                publishProgress(75);
                //Log.i("NasaImage hdurl", hdUrl);
                title= json.getString("title");
                publishProgress(100);
                //Log.i("NasaImage title", title);
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


}


