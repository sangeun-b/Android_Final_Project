package com.example.final_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class NasaDayImage extends AppCompatActivity {

    ProgressBar mProgressBar;
    private NasaDayImageMyfavoriteList.NasaDayImageMyListAdapter myAdapter= new NasaDayImageMyfavoriteList(). new NasaDayImageMyListAdapter();
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
            /*title and hdurl can have no values*/
            String dateInput = dateText.getText().toString().substring(6);
            String titleInput = titleText.getText().toString().substring(7);
            String urlInput = urlText.getText().toString().substring(5);
            String hdUrlInput = hdUrlText.getText().toString().substring(7);
            db = NasaDayActivity.dbOpener.getWritableDatabase();
            Cursor c = db.query(false, NasaDayImageMyOpener.TABLE_NAME, new String[]{"DATE", "TITLE"},
                    "DATE like ? and TITLE like ?", new String[]{dateInput, titleInput}, null, null, null, null);
            if (c.getCount()>0) {
                Toast.makeText(NasaDayImage.this, "In my favorite list already", Toast.LENGTH_LONG).show();
            } else {
                newRowValues.put(NasaDayImageMyOpener.COL_DATE, dateInput);
                newRowValues.put(NasaDayImageMyOpener.COL_TITLE, titleInput);
                newRowValues.put(NasaDayImageMyOpener.COL_URL, urlInput);
                newRowValues.put(NasaDayImageMyOpener.COL_HDURL, hdUrlInput);
                db.insert(NasaDayImageMyOpener.TABLE_NAME, null, newRowValues);
                //long id= db.insert(NasaDayImageMyOpener.TABLE_NAME,null, newRowValues);
                //NasaDayImageMyfavoriteList.list.add(new Image(dateInput, titleInput, urlInput, hdUrlInput, id));
                //myAdapter.notifyDataSetChanged();
                Toast.makeText(NasaDayImage.this, "Added to my favorite list", Toast.LENGTH_LONG).show();
            }
        });

    }

    class NasaImage extends AsyncTask<String, Integer, String> {

        String date=null, url=null, hdUrl=null, title=null, ret=null;
        Bitmap image= null;

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

                FileInputStream fis;
                if(fileExistance(title + ".png")){
                    fis= openFileInput(title + ".png");
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
                        FileOutputStream outputStream = openFileOutput( title + ".png", Context.MODE_PRIVATE );
                        image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                    }
                }

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
            ImageView NasaDayImageView = findViewById(R.id.NasaDayImageView);
            NasaDayImageView.setImageBitmap(image);
            TextView dateText = findViewById(R.id.dateTextView);
            dateText.setText("DATE: " + date);
            TextView titleText =findViewById(R.id.titleTextView);
            titleText.setText("TITLE: " + ((title != null) ? title : (title= "NA")));
            TextView urlText = findViewById(R.id.urlTextView);
            urlText.setText("URL: " + url);
            TextView hdUrlText =findViewById(R.id.hdurlTextView);
            hdUrlText.setText("HDURL: "+ ((hdUrl == null) ? (hdUrl = "NA"): hdUrl));
            mProgressBar.setVisibility(View.INVISIBLE);

        }
    }

    public boolean fileExistance(String fname){
        File file= getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }


}


