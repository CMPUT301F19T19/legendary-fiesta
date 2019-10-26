package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import io.github.cmput301f19t19.legendary_fiesta.R;

public class AddPostFragment extends Fragment {

    EditText dateET;
    String selectedDate;

    // Date result identifier
    public static final int DATE_REQUEST_CODE = 66;
    // Time result identifier
    public static final int TIME_REQUEST_CODE = 99;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_post, container, false);
        Button dateButton = root.findViewById(R.id.date_picker_button);

        // Launch DatePicker on Date button press
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create DatePickerFragment
                DialogFragment dateFragment = new DatePickerFragment();
                // Set the target fragment to receive the results and specifying the result code
                dateFragment.setTargetFragment(AddPostFragment.this, DATE_REQUEST_CODE);
                // Show DatePickerFragment
                dateFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Check for the results
        if (requestCode == DATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Get date from string
            selectedDate = data.getStringExtra("SELECTED_DATE");
            // Set date EditText to the selected date
            dateET.setText(selectedDate);
        }
    }
}