package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.app.Activity;
import android.content.Intent;
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

import io.github.cmput301f19t19.legendary_fiesta.R;

// TODO: Change icon (imageview) to radio buttons
public class AddPostFragment extends Fragment implements View.OnClickListener {

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_add_post, container, false);

        //Buttons
        cancelButton = mView.findViewById(R.id.cancel_button);
        doneButton = mView.findViewById(R.id.done_button);

        //EditText
        dateET = mView.findViewById(R.id.date_edittext);
        timeET = mView.findViewById(R.id.time_edittext);
        descET = mView.findViewById(R.id.description_edittext);
        emotionRadioGroup = mView.findViewById(R.id.emotionRadioGroup);

        //set listener to OnClick defined this class
        dateET.setOnClickListener(this);
        timeET.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);
        emotionRadioGroup.setOnClickListener(this);

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
            case R.id.date_edittext:
                // Create DatePickerFragment
                DialogFragment dateFragment = new DatePickerFragment();
                // Set the target fragment to receive the results and specifying the result code
                dateFragment.setTargetFragment(AddPostFragment.this, DATE_REQUEST_CODE);
                // Show DatePickerFragment
                dateFragment.show(getFragmentManager(), "DatePicker");
                Log.d("Test", "Date text pressed");
                break;
            case R.id.time_edittext:
                // Create TimePickerFragment
                DialogFragment timeFragment = new TimePickerFragment();
                // Set the target fragment to receive the results and specifying the result code
                timeFragment.setTargetFragment(AddPostFragment.this, TIME_REQUEST_CODE);
                // Show TimePickerFragment
                timeFragment.show(getFragmentManager(), "TimePicker");
                break;
            case R.id.cancel_button:
                // Clear entered fields
                dateET.setText("");
                timeET.setText("");
                descET.setText("");
                Log.d("Test", "Cancel button pressed");
                break;
            case R.id.done_button:
                break;
            case R.id.emotionRadioGroup:
                radioGroupOnClick();
                break;
        }

    }

    /*
    Set up how the radio click will look like when clicked
    Set up what happens when radio button is clicked
     */
    private void radioGroupOnClick() {

        //get selected radio button Id from radio group
        int selectedId = emotionRadioGroup.getCheckedRadioButtonId();

        //get the view object from the selected id
        RadioButton selectedButton = mView.findViewById(selectedId);

        //make the selectedButton darker, to show that it is Selected


    }

}