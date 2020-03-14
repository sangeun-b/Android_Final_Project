package com.example.final_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

public class BBCFavouriteList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbcfavourite_list);

        ListView list = findViewById(R.id.BBCFavouriteList);

//        list.setOnItemLongClickListener((parent,view,position,id)-> {
//
//            Message selectedMessage = msgList.get(position);
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//            alertDialogBuilder.setTitle("Do you want to delete this?").setPositiveButton("Yes", (click, arg) -> {
//                deleteContact(selectedMessage);
//                msgList.remove(position);
//                list.setAdapter(new MyListAdapter());
//                final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
//                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
//                {
//                    @Override
//                    public void onRefresh()
//                    {
//                        list.setAdapter(new MyListAdapter());
//                    }
//                });
//            })
//                    .setNegativeButton("No", (click, arg) -> { Toast.makeText(BBCFavouriteList.this, "Nothing changed", Toast.LENGTH_LONG).show();
//                    }).create().show();
//            return true;
//        });


    }
}
