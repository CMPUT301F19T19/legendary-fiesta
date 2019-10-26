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
    private static final String TAG = "TimePickerFragment";
    final Calendar calendar = Calendar.getInstance();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Set the current time as the default time
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

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
