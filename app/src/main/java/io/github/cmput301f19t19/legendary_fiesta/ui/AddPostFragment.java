package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.github.cmput301f19t19.legendary_fiesta.FirebaseHelper;
import io.github.cmput301f19t19.legendary_fiesta.Mood;
import io.github.cmput301f19t19.legendary_fiesta.MoodEvent;
import io.github.cmput301f19t19.legendary_fiesta.R;
import io.github.cmput301f19t19.legendary_fiesta.User;

// TODO: Change icon (imageview) to radio buttons
public class AddPostFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    //UI variables
    private TextView dateET;
    private TextView timeET;
    private EditText descET;
    private Button cancelButton;
    private Button doneButton;
    private RadioGroup emotionRadioGroup;

    private String selectedDate;
    private String selectedTime;

    // Date result identifier
    public static final int DATE_REQUEST_CODE = 66;
    // Time result identifier
    public static final int TIME_REQUEST_CODE = 99;

    private View mView; //get the fragment view

    private static final FirebaseHelper firebaseHelper = new FirebaseHelper(FirebaseApp.getInstance());
    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_add_post, container, false);

        //Buttons
        cancelButton = mView.findViewById(R.id.cancel_button);
        doneButton = mView.findViewById(R.id.done_button);

        //EditText
        dateET = mView.findViewById(R.id.dateEditText);
        timeET = mView.findViewById(R.id.timeEditText);
        descET = mView.findViewById(R.id.description_edittext);
        emotionRadioGroup = mView.findViewById(R.id.emotionRadioGroup);

        //set listener to OnClick defined this class
        dateET.setOnClickListener(this);
        timeET.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);
        emotionRadioGroup.setOnCheckedChangeListener(this);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        return mView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Check for the results
        if (requestCode == DATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Get date from string
            selectedDate = data.getStringExtra("SELECTED_DATE");
            // Set date EditText to the selected date
            dateET.setText(selectedDate);
        } else if (requestCode == TIME_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Get time from string
            selectedTime = data.getStringExtra("SELECTED_TIME");
            // Set time EditText to the selected time
            timeET.setText(selectedTime);
        }
    }

    @Override
    public void onClick(View view) {

        /*
        Perform different onClick action depending on what was click. Determined by comparing view ID
         */
        switch (view.getId()) {
            case R.id.dateEditText:
                // Create DatePickerFragment
                DialogFragment dateFragment = new DatePickerFragment();
                // Set the target fragment to receive the results and specifying the result code
                dateFragment.setTargetFragment(AddPostFragment.this, DATE_REQUEST_CODE);
                // Show DatePickerFragment
                dateFragment.show(getFragmentManager(), "DatePicker");
                break;
            case R.id.timeEditText:
                // Create TimePickerFragment
                DialogFragment timeFragment = new TimePickerFragment();
                // Set the target fragment to receive the results and specifying the result code
                timeFragment.setTargetFragment(AddPostFragment.this, TIME_REQUEST_CODE);
                // Show TimePickerFragment
                timeFragment.show(getFragmentManager(), "TimePicker");
                break;
            case R.id.cancel_button:
                closeFragment();
                break;
            case R.id.done_button:
                onDoneClicked();
                break;
        }

    }

    private void closeFragment(){
        dateET.setText("");
        timeET.setText("");
        descET.setText("");
        emotionRadioGroup.clearCheck();
        navController.navigate(R.id.navigation_own_list);
    }

    private void onDoneClicked(){
        Mood mood = getSelectedMood();
        if (mood == null) {
            handleError("No mood selected");
            return;
        }
        User user = requireActivity().getIntent().getParcelableExtra("USER_PROFILE");
        String description = descET.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
        Date date = null;
        try{
            date = format.parse(dateET.getText().toString() + " " + timeET.getText().toString().replace(".",""));
        } catch (Exception e) {
            handleError(e.getMessage());
            return;
        }
        //TODO: get social condition from dropdown, photo, map
        MoodEvent moodEvent = new MoodEvent(mood, user.getUsername(), description, date, MoodEvent.SocialCondition.SINGLE, null, null);

        firebaseHelper.addMoodEvent(moodEvent, new FirebaseHelper.FirebaseCallback<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference document) {
                //TODO: show confirmation
                closeFragment();
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                handleError(e.getMessage());
            }
        });
    }

    private void handleError(String message){
        //TODO make toast
        Log.e("Error", message);
    }

    private Mood getSelectedMood(){
        int id = emotionRadioGroup.getCheckedRadioButtonId();
        switch (id) {
            case R.id.icon_neutral:
                return new Mood(Mood.NEUTRAL);
            case R.id.icon_happy:
                return new Mood(Mood.HAPPY);
            case R.id.icon_angry:
                return new Mood(Mood.ANGRY);
            case R.id.icon_disgusted:
                return new Mood(Mood.DISGUSTED);
            case R.id.icon_sad:
                return new Mood(Mood.SAD);
            case R.id.icon_scared:
                return new Mood(Mood.SCARED);
            case R.id.icon_surprised:
                return new Mood(Mood.SURPRISED);
            default:
                return null;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        radioGroupOnClick(group);
    }

    /*
        Set up how the radio click will look like when clicked
        Set up what happens when radio button is clicked
     */
    private void radioGroupOnClick(RadioGroup group) {
        //get selected radio button Id from radio group
        int selectedId = group.getCheckedRadioButtonId();

        /*
        Loop through the radio buttons in radio group, find the one that's selected and make it darker.
        If not, make it white
         */
        for(int i = 0; i < group.getChildCount(); i++){
            RadioButton currentButton = (RadioButton) group.getChildAt(i);

            if(currentButton.getId() == selectedId){
                //make the selectedButton darker, to show that it is Selected
                currentButton.getBackground().setColorFilter(0x40000000, PorterDuff.Mode.MULTIPLY);
            }else{
                //make the unselected buttons white
                currentButton.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            }
        }
    }
}