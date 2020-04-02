package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Sangeun Baekk
 * This Activity for connect to the website and retrieve the data from the website that user want to search,
 * Then the user can save the data to the database.
 */

public class Nasaearth_result extends AppCompatActivity {
    Button re;
    Button saved;
    ProgressBar earthProgressBar;
    ImageView earthImageView;
    TextView earthLatTextView;
    TextView earthLonTextView;
    TextView earthDateTextView;
    SQLiteDatabase db;

    /**
     * setContentView loads objects onto the screen.
     * save the data into the database.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nasaearth_result);


        NasaEarthImage nasaEarth = new NasaEarthImage();
         //not json nasaEarth.execute("https://dev.virtualearth.net/REST/V1/Imagery/Map/Birdseye/" + NasaEarthActivity.inputLat + "," + NasaEarthActivity.inputLon +"/20?dir=180&ms=500,500&key=ApUD42GYzVyU6_EZQ_Vi9qCx9ZmHYkjrQkO93IISLCWUsJbXUHjUIWCIZawaV_LD");
        nasaEarth.execute("https://dev.virtualearth.net/REST/V1/Imagery/Metadata/Aerial/"+ NasaEarthActivity.inputLon +"," + NasaEarthActivity.inputLat +"?zl=15&o=&key=ApUD42GYzVyU6_EZQ_Vi9qCx9ZmHYkjrQkO93IISLCWUsJbXUHjUIWCIZawaV_LD");

        earthImageView = findViewById(R.id.earthImage);
        earthLatTextView = findViewById(R.id.earthlat);
        earthLonTextView = findViewById(R.id.earthlon);
        earthDateTextView = findViewById(R.id.earthdate);
        re = findViewById(R.id.reButton);
        saved = findViewById(R.id.save);


        ContentValues newRowValues= new ContentValues();

        earthProgressBar = findViewById(R.id.earthprogress);
        earthProgressBar.setVisibility(View.VISIBLE);

        Intent goToPrevious = new Intent(Nasaearth_result.this, NasaEarthActivity.class);
        re.setOnClickListener(click -> startActivity(goToPrevious));
        NasaEarthMyOpener earthDB = new NasaEarthMyOpener(this);
        saved.setOnClickListener(click->{
                String inDate = earthDateTextView.getText().toString().substring(6);
                db=earthDB.getWritableDatabase();
                newRowValues.put(NasaEarthMyOpener.COL_LATITUDE, NasaEarthActivity.inputLat);
                newRowValues.put(NasaEarthMyOpener.COL_LONGITUDE, NasaEarthActivity.inputLon );
                newRowValues.put(NasaEarthMyOpener.COL_DATE, inDate);
                long id= db.insert(NasaEarthMyOpener.TABLE_NAME,null, newRowValues);
                Toast.makeText(Nasaearth_result.this, getString(R.string.earthsaved) , Toast.LENGTH_LONG).show();

        });
    }

    /**
     * Connect to the EarthImage website, creates and JSON parse to retrieve desired data from website.
     */
    private class NasaEarthImage extends AsyncTask<String, Integer, String> {
            String latitude=null, longitude=null, date=null, url=null, id=null;
            Bitmap image;

        /**
         *create a URL object of what server to contact.
         * connect to the server
         * convert string to JSON
         * get the string associated with deserve name
         *
         * @param args
         * @return
         */
        public String doInBackground(String... args) {
                try {
                    JSONArray ja = null;
                    URL infoUrl = new URL(args[0]);
                    //open the connection
                    HttpURLConnection urlConnection = (HttpURLConnection) infoUrl.openConnection();
                    //wait for data
                    InputStream response = urlConnection.getInputStream();
                    //JSON reading:
                    //Build the entire string response
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line= null;

                    while ((line = reader.readLine()) != null) {sb.append(line + "\n"); }
                    String result = sb.toString();
                    JSONObject json = new JSONObject(result);
                    ja = json.getJSONArray("resourceSets");
                    JSONObject obj= ja.getJSONObject(0);
                    JSONArray item = obj.getJSONArray("resources");
                    JSONObject json2 = item.getJSONObject(0);
                         url = json2.getString("imageUrl");
                         publishProgress(25);
                         date= json2.getString("vintageEnd");
                         publishProgress(50);

                         FileInputStream fis;
                         if (fileExistance(date + ".png")) {
                             fis = openFileInput(date + ".png");
                             image = BitmapFactory.decodeStream(fis);
                             Log.i("file", "this is the local file.");
                         } else {
                             URL urlImage = new URL(url);
                             HttpURLConnection imageConnection = (HttpURLConnection) urlImage.openConnection();
                             imageConnection.connect();
                             int responseCode = imageConnection.getResponseCode();
                             if (responseCode == 200) {
                                 image = BitmapFactory.decodeStream(imageConnection.getInputStream());
                                 Log.i("file", "this file is from online.");
                                 FileOutputStream outputStream = openFileOutput(date + ".png", Context.MODE_PRIVATE);
                                 image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                                 publishProgress(100);
                                 outputStream.flush();
                                 outputStream.close();
                             }
                         }

                    return "Done";
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
                return "Done";
            }

        /**
         * update the progress bar
         * @param value Receive the bar
         */
        public void onProgressUpdate(Integer... value) {
                super.onProgressUpdate(value);
                earthProgressBar.setVisibility(View.VISIBLE);
                earthProgressBar.setProgress(value[0]);
            }

        /**
         *
         * @param fromDoInBackground
         */
        public void onPostExecute(String fromDoInBackground) {
                super.onPostExecute(fromDoInBackground);
                longitude= NasaEarthActivity.inputLon;
                latitude = NasaEarthActivity.inputLat;
                earthImageView.setImageBitmap(image);
                earthLatTextView.setText(getString(R.string.earthlat) + latitude);
                earthLonTextView.setText(getString(R.string.earthlon) + longitude);
                earthDateTextView.setText(getString(R.string.earthdate) + date);
                earthProgressBar.setVisibility(View.INVISIBLE);
            }


        }

    /**
     * Search image if it is exist in the local device or not
     * @param fname file name to be retrieved
     * @return if the file extists, it is true, if not, it is false
     */
    public boolean fileExistance(String fname){
        File file= getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    }

