package com.example.final_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class Guardian_favourite_details extends Fragment {
    private AppCompatActivity parentActivity;
    private Bundle dataFromActivity;
    private long id;





    public Guardian_favourite_details() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(Guardian_search_results.ID);

        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.guardian_favourite_details, container, false);


        TextView title = result.findViewById(R.id.favourite_title);
        title.setText("Title:" + dataFromActivity.getString(Guardian_search_results.TITLE));


        TextView url = result.findViewById(R.id.favourite_url);
        url.setText("Url:" + dataFromActivity.getString(Guardian_search_results.URL));


        TextView section = result.findViewById(R.id.favourite_section);
        section.setText("Section Name:" + dataFromActivity.getString(Guardian_search_results.SECTION));

        return result;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity)context;
    }
}
