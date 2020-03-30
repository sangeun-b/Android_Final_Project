package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Guardian_details_fragment extends Fragment {
    private AppCompatActivity parentActivity;
    private Bundle dataFromActivity;
    private long id;
    private boolean isTablet;
    SQLiteDatabase db;

    public void setTablet(boolean tablet) { isTablet = tablet; }

    public Guardian_details_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataFromActivity=getArguments();
        id=dataFromActivity.getLong(Guardian_search_results.ID);

        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.guardian_details_fragment, container, false);


        TextView title=result.findViewById(R.id.guardian_title);
        title.setText("Title:"+dataFromActivity.getString(Guardian_search_results.TITLE));


        TextView url=result.findViewById(R.id.guardian_url);
        url.setText("Url:"+dataFromActivity.getString(Guardian_search_results.URL));


        TextView section=result.findViewById(R.id.guardian_section);
        section.setText("Section Name:"+dataFromActivity.getString(Guardian_search_results.SECTION));


        Button saveButton=result.findViewById(R.id.guardain_saveButton);
        saveButton.setOnClickListener(clk-> {
            GuardianMyOpener guardianDB = new GuardianMyOpener(getContext());
            db = guardianDB.getWritableDatabase();

            Cursor c = db.query(false, GuardianMyOpener.TABLE_NAME, new String[]{"TITLE"},
                    "TITLE like ?", new String[]{dataFromActivity.getString(Guardian_search_results.TITLE)}, null, null, null, null);
            if (c.getCount() > 0) {
                Toast.makeText(getActivity(), "In your favorite list already", Toast.LENGTH_LONG).show();
            } else {


                ContentValues newRowValues = new ContentValues();
                newRowValues.put(GuardianMyOpener.COL_TITLE, dataFromActivity.getString(Guardian_search_results.TITLE));
                newRowValues.put(GuardianMyOpener.COL_URL, dataFromActivity.getString(Guardian_search_results.URL));
                newRowValues.put(GuardianMyOpener.COL_SECTION, dataFromActivity.getString(Guardian_search_results.SECTION));


                db.insert(GuardianMyOpener.TABLE_NAME, null, newRowValues);
                Toast.makeText(getActivity(), "Add to your favourite list", Toast.LENGTH_LONG).show();

            }
        });



        //get the hide button, and add a click listener;
        Button hideButton=result.findViewById(R.id.guardian_hideButton);
        hideButton.setOnClickListener( clk -> {
            if (isTablet) { //both the list and details are on the screen
                Guardian_search_results parent = (Guardian_search_results) getActivity();
                //parent.deleteMessageId((int) id); //this deletes the item and updates the list

                //now remove the fragment since you hided it from the database
                //this is the object to be removed, so remove(this):
                parent .getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            //for phone:
            //You are only looking at the details, you need to go back to the previous list page
            else  {
               GuardianEmptyActivity parent = (GuardianEmptyActivity) getActivity();
                //parent.deleteMessageId((int) id);//this deletes the item and updates the list
                // parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
                Intent backToSearchActivity = new Intent();
                //backToSearchActivity.putExtra(Guardian_search_results.ID, dataFromActivity.getString(Guardian_search_results.ID));
                parent.setResult(Activity.RESULT_OK, backToSearchActivity); //send data back to ChatRoomActivity in onActivityResult()
                parent.finish();
            }
        });
        return result;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //context will either be ChatRoomActivity for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }


}