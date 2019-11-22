package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import io.github.cmput301f19t19.legendary_fiesta.FirebaseHelper;
import io.github.cmput301f19t19.legendary_fiesta.MapActivity;
import io.github.cmput301f19t19.legendary_fiesta.Mood;
import io.github.cmput301f19t19.legendary_fiesta.MoodEvent;
import io.github.cmput301f19t19.legendary_fiesta.R;
import io.github.cmput301f19t19.legendary_fiesta.User;
import io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter.MoodEventAdapter;
import io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter.SpinnerArrayAdapter;
import io.github.cmput301f19t19.legendary_fiesta.ui.UIEventHandlers.FilterEventHandlers;

public class OwnMoodsFragment extends Fragment {

    // Reference to associated activity class, initialized in onAttach function
    private Activity mActivity;
    // Reference to associated view, initialized in onCreateView
    private View mView;

    private User user;

    // Reference to the ListView on own moods page
    private ListView moodList;
    private ArrayList<MoodEvent> moodDataList;
    private MoodEventAdapter moodArrayAdapter;
    private Button mapButton;

    private Spinner filterSpinner;

    public static final String MOOD_EVENT_TAG = "MOOD_EVENT";
    public static final String MY_MOOD_UI_TEST_TAG = "FROM_UI_TESTS";

    //Filter spinner related variables
    private Spinner moodFilter;
    private @Mood.MoodType Integer chosenMoodType;
    private ArrayList<MoodEvent> filteredMoodList;
    private Integer oriListLength;
    private MoodEvent moodToDelete;  //to avoid ConcurrentModificationException
    private MoodEventAdapter tempMoodArrayAdapter;


    private static final FirebaseHelper firebaseHelper = new FirebaseHelper(FirebaseApp.getInstance());


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_own_moods, container, false);

        setUpFilterSpinner();

        user = requireActivity().getIntent().getParcelableExtra("USER_PROFILE");

        if(user == null){
            Bundle receiveBundle = this.getArguments();
            assert receiveBundle != null;
            user = receiveBundle.getParcelable("USER_PROFILE");
        }

        moodDataList = new ArrayList<>();

        if(getTag() != MY_MOOD_UI_TEST_TAG){
            loadMoodData();
        }

        moodList = mView.findViewById(R.id.mood_list);

        moodFilter = mView.findViewById(R.id.filter_spinner);

        //When an item is selected from the spinner
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
                    default:    //None
                        break;
                }

                //If chosenMoodType is a number between 0-6, filter!
                if(chosenMoodType != -1){
                    for(MoodEvent mood : moodDataList){
                        if(mood.getMoodType() == chosenMoodType){
                            filteredMoodList.add(mood);
                        }
                    }
                    //Update adapter and listview
                    moodArrayAdapter = deleteCallback(filteredMoodList);
                    moodList.setAdapter(moodArrayAdapter);
                }

                else{
                    moodArrayAdapter = deleteCallback(moodDataList);
                    moodList.setAdapter(moodArrayAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                moodArrayAdapter = deleteCallback(moodDataList);
                moodList.setAdapter(moodArrayAdapter);
            }
        });


        moodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fragment replacement = new AddPostFragment();
                Bundle args = new Bundle();
                args.putParcelable(MOOD_EVENT_TAG, moodDataList.get(i));
                replacement.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(R.id.nav_host_fragment, replacement );
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        // Map Button
        mapButton = mView.findViewById(R.id.show_on_map_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                intent.putParcelableArrayListExtra("EVENTS", moodDataList);
                startActivity(intent);
            }
        });

        return mView;
    }

    public void loadMoodData() {
        firebaseHelper.getMoodEventsById(user.getUid(), new FirebaseHelper.FirebaseCallback<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                moodDataList.clear();
                for (QueryDocumentSnapshot document : documentSnapshots) {
                    moodDataList.add(document.toObject(MoodEvent.class));
                }
                moodArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FeelsLog", "onFailure: ");
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // get reference to associated activity
        mActivity = (Activity) context;
    }

    /*
     * Mostly a test function to set up the spinner, populate it with a string array from resource.xml
     */
    private void setUpFilterSpinner() {
        filterSpinner = mView.findViewById(R.id.filter_spinner);
        /*
         * get list of mood from the Mood.moodType enum. Also turn the first letter of each enum to Uppercase
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

    /*
     * This function is used in tests to add a mood event into the data list
     */
    public void AddMoodEvent(MoodEvent newMoodEvent){
        moodDataList.add(newMoodEvent);
        moodArrayAdapter.notifyDataSetChanged();
    }


    /*
     * This function returns a MoodEventAdapter with the appropriate data list depending on the filter/spinner
     */
    private MoodEventAdapter deleteCallback(ArrayList<MoodEvent> moodData){
        oriListLength = moodDataList.size();

        tempMoodArrayAdapter = new MoodEventAdapter(mActivity, moodData, new MoodEventAdapter.AdapterCallback() {
            @Override
            public void onDelete(int position) {
                new AlertDialog.Builder(mActivity).setTitle("Confirm Delete?")
                        .setMessage("Are you sure you want to delete this event?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        firebaseHelper.deleteMoodEventById(moodData.get(position).getMoodId(), new FirebaseHelper.FirebaseCallback<Void>() {
                                            @Override
                                            public void onSuccess(Void v) {
                                                Toast.makeText(mActivity, R.string.event_delete_success, Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(mActivity, R.string.event_delete_fail,
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        //Also delete this moodEvent from moodDataList
                                        for(MoodEvent mood : moodDataList){
                                            if(mood.getMoodId() == moodData.get(position).getMoodId()){
                                                moodToDelete = mood;
                                            }
                                        }
                                        moodData.remove(position);

                                        //To check if moodData is actually moodDataList
                                        //Avoid deleting 2 things from moodDataList
                                        if(moodDataList.size() == oriListLength){
                                            moodDataList.remove(moodToDelete);
                                        }

                                        tempMoodArrayAdapter.notifyDataSetChanged();
                                        dialog.dismiss();
                                    }
                                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });

        return tempMoodArrayAdapter;
    }


}