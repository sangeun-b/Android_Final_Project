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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Nasaearth_result extends AppCompatActivity {
    Button re;
    Button saved;
    ProgressBar earthProgressBar;
    ImageView earthImageView;
    TextView earthLatTextView;
    TextView earthLonTextView;
    TextView earthDateTextView;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nasaearth_result);


        NasaEarthImage nasaEarth = new NasaEarthImage();
        //nasaEarth.execute("https://api.nasa.gov/planetary/earth/imagery/?lon="+ NasaEarthActivity.inputLon + "&lat=" + NasaEarthActivity.inputLat + "&date=2014-02-01&api_key=DEMO_KEY");
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
        private class NasaEarthImage extends AsyncTask<String, Integer, String> {
            String latitude=null, longitude=null, date=null, url=null, id=null;
            Bitmap image;

            public String doInBackground(String... args) {
                try {
                    //create a URL object of what server to contact:
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
                    //convert string to JSON
                    JSONObject json = new JSONObject(result);
                    //JSONArray arr = new JSONArray(result);
                    //JSONObject json = arr.getJSONObject(0);
                    //get the string associated with "id"
                    url = json.getString("imageUrl");
                    publishProgress(25);
                    //get the string associated with "date"
                    date= json.getString("vintageEnd");
                    publishProgress(50);
                    //get the string associated wiht "url"
                    id = json.getString("traceId");
                    publishProgress(75);

                    FileInputStream fis;
                    if(fileExistance(date + ".png")){
                        fis= openFileInput(date +".png");
                        image= BitmapFactory.decodeStream(fis);
                        Log.i("file", "this is the local file.");
                    }else{
                        URL urlImage= new URL(url);
                        HttpURLConnection imageConnection= (HttpURLConnection) urlImage.openConnection();
                        imageConnection.connect();
                        int responseCode= imageConnection.getResponseCode();
                        if(responseCode==200){
                            image= BitmapFactory.decodeStream(imageConnection.getInputStream());
                            Log.i("file", "this file is from online.");
                            FileOutputStream outputStream = openFileOutput( date + ".png", Context.MODE_PRIVATE );
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

            public void onProgressUpdate(Integer... value) {
                super.onProgressUpdate(value);
                earthProgressBar.setVisibility(View.VISIBLE);
                earthProgressBar.setProgress(value[0]);
            }

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
    public boolean fileExistance(String fname){
        File file= getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    }

