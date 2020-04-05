package com.example.final_project;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * get detailed information of the favourite news
 */
public class Guardian_favourite_details extends Fragment {
    private AppCompatActivity parentActivity;
    private Bundle dataFromActivity;
    private long id;


    /**
     * an empty constructor
     */
    public Guardian_favourite_details() {
        // Required empty public constructor
    }

    /**
     *Called to have the fragment instantiate its user interface view.
     * user can see the details of Title, Url and Section name in the fragment layout
     * @param inflater-The LayoutInflater object that can be used to inflate the details views in the fragment
     * @param container-If not null the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState-this fragment is being re-constructed from a previous saved state as given here.
     * @return the result of "Tile","Url","Section name" in the fragment layout.
     */
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


    /**
     * When the fragment has been added to the Activity which has the FrameLayout.
     * @param context- The Context that is inflating this fragment.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity)context;
    }
}
