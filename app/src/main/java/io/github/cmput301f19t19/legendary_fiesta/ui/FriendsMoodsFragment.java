package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private MoodEventFriendsAdapter moodArrayAdapter;

    private Spinner filterSpinner;

    // View Elements
    private Button mapButton;

    //Filter spinner related variables
    private Spinner moodFilter;
    private @Mood.MoodType Integer chosenMoodType;
    private ArrayList<MoodEvent> filteredMoodList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_friends_moods, container, false);

        setUpFilterSpinner();

        moodDataList = new ArrayList<>();

        moodList = mView.findViewById(R.id.mood_list_friends);

        moodFilter = mView.findViewById(R.id.filter_spinner_friends);

        //When an item in the spinner is selected
        moodFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filteredMoodList = new ArrayList<>();
                chosenMoodType = -1;

                //Assign the correct value to chosenMoodType depending on what the user has selected
                switch (i){
                    case 0: //Scared
                        chosenMoodType = Mood.SCARED;
                        break;
                    case 1: //Happy
                        chosenMoodType = Mood.HAPPY;
                        break;
                    case 2: //Surprised
                        chosenMoodType = Mood.SURPRISED;
                        break;
                    case 3: //Sad
                        chosenMoodType = Mood.SAD;
                        break;
                    case 4: //Angry
                        chosenMoodType = Mood.ANGRY;
                        break;
                    case 5: //Disgusted
                        chosenMoodType = Mood.DISGUSTED;
                        break;
                    case 6: //Neutral
                        chosenMoodType = Mood.NEUTRAL;
                        break;
                    default:   //None
                        break;
                }

                // If chosenMoodType is a number between 0-6, filter!
                if(chosenMoodType != -1){
                    for(MoodEvent mood : moodDataList){
                        if(mood.getMoodType() == chosenMoodType){
                            filteredMoodList.add(mood);
                        }
                    }
                    // Update adapter and listview
                    moodArrayAdapter = new MoodEventFriendsAdapter(mActivity, filteredMoodList);
                    moodList.setAdapter(moodArrayAdapter);
                }

                else{
                    moodArrayAdapter = new MoodEventFriendsAdapter(mActivity, moodDataList);
                    moodList.setAdapter(moodArrayAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                moodArrayAdapter = new MoodEventFriendsAdapter(mActivity, moodDataList);
                moodList.setAdapter(moodArrayAdapter);
            }
        });

        // Map Button
        mapButton = mView.findViewById(R.id.show_on_map_button_friends);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                // TODO: Add dataList here.
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