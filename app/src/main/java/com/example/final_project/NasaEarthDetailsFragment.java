package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class NasaEarthDetailsFragment extends Fragment {

    private Bundle dataFromActivity;
    private long id;
    private boolean isTablet;

    public void setTablet(boolean tablet) { isTablet = tablet; }

    public NasaEarthDetailsFragment() {}

   // @Override
    //public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       // dataFromActivity = getArguments();
        //id = dataFromActivity.getLong(Nasaearth_saved.EARTH_ID);

        //View result =  inflater.inflate(R.layout.activity_nasaearth_details_fragment, container, false);

        //ImageView detailEarthImageView = result.findViewById(R.id.detailEarthImage);
       /* detailEarthImageView.setText(dataFromActivity.getString(ChatRoomActivity.MESSAGE_SELECTED));

        TextView messageId = (TextView)result.findViewById(R.id.lab7t2);
        messageId.setText("ID=" + id);

        Button hideButton = (Button)result.findViewById(R.id.lab7b1);

        hideButton.setOnClickListener( clk -> {

            //Tell the parent activity to remove
            if(isTablet){
                ChatRoomActivity parent = (ChatRoomActivity) getActivity();
                //parent.deleteMessageId((int)id);
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }else {

                EmptyActivity parent = (EmptyActivity) getActivity();
                Intent backToFragmentExample = new Intent();
                backToFragmentExample.putExtra(ChatRoomActivity.MESSAGE_ID, dataFromActivity.getLong(ChatRoomActivity.MESSAGE_ID));
                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back

            }
        });*/


        //return result;


   // }
}


