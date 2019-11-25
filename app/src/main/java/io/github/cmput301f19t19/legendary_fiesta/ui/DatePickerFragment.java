package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerFragment extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener {
    public static final String DATE_TAG = "DATE_DATE";
    public static final String MONTH_TAG = "DATE_MONTH";
    public static final String YEAR_TAG = "DATE_YEAR";
    private static final String TAG = "DatePickerFragment";
    final Calendar calendar = Calendar.getInstance();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        int year, month, day;
        Bundle args = getArguments();
        if (args != null) {
            year = args.getInt(YEAR_TAG);
            month = args.getInt(MONTH_TAG);
            day = args.getInt(DATE_TAG);
        } else {
            // Set the current date as the default date
            final Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        // Return a new instance of DatePickerDialog
        return new DatePickerDialog(getActivity(), DatePickerFragment.this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        String selectedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.CANADA).format(calendar.getTime());

        // Send date back to the target fragment (e.g. AddPostFragment)
        getTargetFragment().onActivityResult(
                getTargetRequestCode(),
                Activity.RESULT_OK,
                new Intent().putExtra("SELECTED_DATE", selectedDate)
        );
    }
}
