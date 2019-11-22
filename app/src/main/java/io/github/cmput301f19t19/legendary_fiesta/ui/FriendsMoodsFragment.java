package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import io.github.cmput301f19t19.legendary_fiesta.Mood;
import io.github.cmput301f19t19.legendary_fiesta.MoodEvent;
import io.github.cmput301f19t19.legendary_fiesta.MapActivity;
import io.github.cmput301f19t19.legendary_fiesta.R;
import io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter.MoodEventFriendsAdapter;
import io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter.SpinnerArrayAdapter;
import io.github.cmput301f19t19.legendary_fiesta.ui.UIEventHandlers.FilterEventHandlers;


public class FriendsMoodsFragment extends Fragment {

    private Activity mActivity;
    private View mView;

    private ListView moodList;
    private ArrayList<MoodEvent> moodDataList;
    private MoodEventFriendsAdapter moodEventAdapter;

    private Spinner filterSpinner;

    // View Elements
    private Button mapButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_friends_moods, container, false);

        setUpFilterSpinner();

        moodDataList = new ArrayList<>();

        moodList = mView.findViewById(R.id.mood_list_friends);

        moodList.setAdapter(moodEventAdapter);

        // Map Button
        mapButton = mView.findViewById(R.id.show_on_map_button_friends);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                // intent.putExtra("EVENTARY", moodDataList.toArray());
                // intent.putParcelableArrayListExtra("EVENTS", moodDataList);
                startActivity(intent);
            }
        });

        return mView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // get reference to associated activity
        mActivity = (Activity) context;
    }

    /*
     * Used to set up the spinner, populate it with a string array from resource.xml
     */
    private void setUpFilterSpinner() {
        filterSpinner = mView.findViewById(R.id.filter_spinner_friends);

        /*
         * filterArray will contain a list of mood from Mood.moodtype enum
         */
        ArrayList<String> filterArray = new ArrayList<>();
        Mood.MoodTypes.forEach((k, v) -> filterArray.add(k));
        filterArray.add(getResources().getString(R.string.spinner_empty)); //filter_empty is "None"

        /*
         * convert ArrayList to array, so that it can be passed to SpinnerArrayAdapter
         */
        String[] filterObject = new String[filterArray.size()];
        filterObject = filterArray.toArray(filterObject);

        /*
        Create string ArrayAdapter that will be used for filterSpinner
         */
        ArrayAdapter<String> spinnerAdapter = new SpinnerArrayAdapter(mActivity, R.layout.spinner_item, filterObject);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        filterSpinner.setAdapter(spinnerAdapter);

        //set default selection to None
        int defaultIndex = filterArray.indexOf(getResources().getString(R.string.spinner_empty));
        filterSpinner.setSelection(defaultIndex);

        //assign filter selected listener
        filterSpinner.setOnItemSelectedListener(new FilterEventHandlers());
    }
}