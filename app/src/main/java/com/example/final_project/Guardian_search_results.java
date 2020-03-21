package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Guardian_search_results extends AppCompatActivity {
    private ArrayList<GuardianNews> list=new ArrayList<>();
    private MyListAdapter myAdapter;
    public static final String ACTIVITY_NAME="Guardian_search_results";
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guardian_search_results);

        ListView guardianList=findViewById(R.id.guardian_list);
        loadDataFromDatabase();

        myAdapter=new MyListAdapter();
        guardianList.setAdapter(myAdapter);
    }

    private class MyListAdapter extends BaseAdapter {
        public int getCount(){
            return list.size();
        }

        public GuardianNews getItem(int position){
            return list.get(position);
        }

        public long getItemId(int position){
            return getItem(position).getId();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View newView=convertView;
            GuardianNews news=getItem(position);
            LayoutInflater inflater= getLayoutInflater();
            newView=inflater.inflate(R.layout.guardian_searchresult_item,parent,false);
            TextView tView =newView.findViewById(R.id.guardian_search_title);
            tView.setText( news.getTitle());
            return newView;

        }

        }
    public void loadDataFromDatabase(){
        //get a database connection:
        db=GuardianActivity.dbOpener.getWritableDatabase();
        String[]columns={GuardianMyOpener.COL_ID,GuardianMyOpener.COL_TITLE,GuardianMyOpener.COL_URL,GuardianMyOpener.COL_SECTION};
        Cursor results = db.query(false, GuardianMyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int titleColIndex = results.getColumnIndex(GuardianMyOpener.COL_TITLE);
        int urlColIndex = results.getColumnIndex(GuardianMyOpener.COL_URL);
        int sectionColIndex = results.getColumnIndex(GuardianMyOpener.COL_SECTION);
        int idColIndex = results.getColumnIndex(GuardianMyOpener.COL_ID);

        while(results.moveToNext())
        {
            String title = results.getString(titleColIndex);
            String url = results.getString(urlColIndex);
            String section = results.getString(sectionColIndex);
            long id = results.getLong(idColIndex);


            //add the new Message to the array list:
            //Message m=new Message(message,send,id);
            list.add((new GuardianNews(title,url,section,id)));
        }




    }
}
