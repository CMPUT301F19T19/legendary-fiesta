package io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter;

/*
 * Custom ArrayAdapter to display user's followings' MoodEvents in a beautiful list.
 */


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
import java.util.HashMap;
import java.util.Locale;

import io.github.cmput301f19t19.legendary_fiesta.Mood;
import io.github.cmput301f19t19.legendary_fiesta.MoodEvent;
import io.github.cmput301f19t19.legendary_fiesta.R;

public class MoodEventFriendsAdapter extends ArrayAdapter<MoodEvent> {

    private ArrayList<MoodEvent> moodEventList;
    private HashMap<String, String> friendsUsernames;
    private Context context;

    public MoodEventFriendsAdapter(Context context, ArrayList<MoodEvent> moodEventList, HashMap<String, String> friendsUsernames) {
        super(context, 0, moodEventList);
        this.moodEventList = moodEventList;
        this.friendsUsernames = friendsUsernames;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.mood_list_content_friends, parent, false);
        }

        MoodEvent moodEvent = moodEventList.get(position);
        Mood mood = new Mood(moodEvent.getMoodType());

        ImageView displayed_emoji = view.findViewById(R.id.emoji_friends);
        TextView displayed_name = view.findViewById(R.id.name_friends);
        TextView displayed_date = view.findViewById(R.id.date_friends);
        TextView displayed_time = view.findViewById(R.id.time_friends);

        // Set emoji and its background colour
        displayed_emoji.setImageResource(mood.getIconId());
        displayed_emoji.setBackgroundColor(context.getColor(mood.getColorId()));

        // Set the name
        displayed_name.setText(friendsUsernames.get(moodEvent.getUser()));
        displayed_name.setBackgroundColor(context.getColor(mood.getColorId()));

        Date date = moodEvent.getDate();
        // Set date and its background colour
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        String dateString = dateFormat.format(date);
        displayed_date.setText(dateString);
        displayed_date.setBackgroundColor(context.getColor(mood.getColorId()));

        // Set time and its background colour
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.CANADA);
        String timeString = timeFormat.format(date);
        displayed_time.setText(timeString);
        displayed_time.setBackgroundColor(context.getColor(mood.getColorId()));

        return view;
    }
}
