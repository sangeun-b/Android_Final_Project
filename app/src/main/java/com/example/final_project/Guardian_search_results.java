package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Guardian_search_results extends AppCompatActivity {
    ListView titleListView;
    ArrayList<GuardianNews> list=new ArrayList<>();
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guardian_search_results);
        titleListView = findViewById(R.id.guardian_list);
        MyHttpRequest req = new MyHttpRequest();
        req.execute("https://content.guardianapis.com/search?api-key=1fb36b70-1588-4259-b703-2570ea1fac6a&q=" + GuardianActivity.searchInput);

    }

    private class MyHttpRequest extends AsyncTask<String, Integer, String> {

        //Type3                Type1
        public String doInBackground(String... args) {
            try {
                //create a URL object of what server to contact:
                URL url = new URL(args[0]);
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //wait for data:
                InputStream response = urlConnection.getInputStream();
                //JSON reading:
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string*/

                JSONObject jo = new JSONObject(result);
                JSONArray jsa= jo.getJSONArray("results");
                Log.i("TAG", Integer.toString(jsa.length()));
                for(int i=0; i<jsa.length(); i++){
                    JSONObject item = jsa.getJSONObject(i);
                    GuardianNews gd= new GuardianNews();
                    gd.setTitle(item.getString("webTitle"));
                    gd.setUrl(item.getString("webUrl"));
                    gd.setSection(item.getString("sectionName"));
                    list.add(gd);
                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }

            return "Done";
        }

        protected void onProgressUpdate(Integer... value) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
        }

        protected void onPostExecute(String fromDoInBackground) {
            titleListView.setAdapter(new GuardianListAdapter());
        }

    }

    private class GuardianListAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public GuardianNews getItem(int position)
        {
            return list.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View newView=convertView;
            LayoutInflater inflater = getLayoutInflater();
            GuardianNews gnews= getItem(position);
            newView=inflater.inflate(R.layout.guardian_searchresult_item, parent,false);
            TextView tView= newView.findViewById(R.id.guardian_news_item);
            tView.setText( gnews.getTitle());
            return newView;
        }

        //we return the object's database id
        @Override
        public long getItemId(int position)
        {
            return getItem(position).getId();
        }
    }

}