package io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.github.cmput301f19t19.legendary_fiesta.Mood;
import io.github.cmput301f19t19.legendary_fiesta.MoodEvent;
import io.github.cmput301f19t19.legendary_fiesta.R;

public class MoodEventCustomList extends ArrayAdapter<MoodEvent> {

    private ArrayList<MoodEvent> moodEventList;
    private Context context;

    public MoodEventCustomList(Context context, ArrayList<MoodEvent> moodEventList) {
        super(context, 0, moodEventList);
        this.moodEventList = moodEventList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.mood_list_content, parent, false);
        }

        MoodEvent moodEvent = moodEventList.get(position);

        ImageView displayed_emoji = view.findViewById(R.id.emoji);
        TextView displayed_date = view.findViewById(R.id.date);
        TextView displayed_time = view.findViewById(R.id.time);

        // Set emoji
        displayed_emoji.setImageResource(moodEvent.getMood().getIconId());

        Date date = moodEvent.getDate();
        // Set date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        String dateString = dateFormat.format(date);
        displayed_date.setText(dateString);

        // Set time
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.CANADA);
        String timeString = timeFormat.format(date);
        displayed_time.setText(timeString);

        return view;
    }
}
