package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimePickerFragment extends AppCompatDialogFragment implements TimePickerDialog.OnTimeSetListener {
    public static final String HOUR_TAG = "TIME_HOUR";
    public static final String MINUTE_TAG = "TIME_MIN";
    private static final String TAG = "TimePickerFragment";
    final Calendar calendar = Calendar.getInstance();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        int hour, minute;
        Bundle args = getArguments();
        if (args != null) {
            hour = args.getInt(HOUR_TAG);
            minute = args.getInt(MINUTE_TAG);
        } else {
            // Set the current time as the default time
            final Calendar calendar = Calendar.getInstance();

            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        }


        // Return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(), TimePickerFragment.this, hour, minute, false);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        String selectedTime = new SimpleDateFormat("hh:mm aa", Locale.CANADA).format(calendar.getTime());

        // Send date back to the target fragment (e.g. AddPostFragment)
        getTargetFragment().onActivityResult(
                getTargetRequestCode(),
                Activity.RESULT_OK,
                new Intent().putExtra("SELECTED_TIME", selectedTime)
        );

    }
}
