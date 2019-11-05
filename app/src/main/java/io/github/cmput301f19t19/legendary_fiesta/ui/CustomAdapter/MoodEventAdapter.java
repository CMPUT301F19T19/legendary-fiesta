package io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.daimajia.swipe.adapters.ArraySwipeAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.github.cmput301f19t19.legendary_fiesta.FirebaseHelper;
import io.github.cmput301f19t19.legendary_fiesta.Mood;
import io.github.cmput301f19t19.legendary_fiesta.MoodEvent;
import io.github.cmput301f19t19.legendary_fiesta.R;

public class MoodEventAdapter extends ArraySwipeAdapter<MoodEvent> {

    private ArrayList<MoodEvent> moodEventList;
    private Context context;
    AdapterCallback callback;

    public interface AdapterCallback{
        void onDelete(int position);
    }

    public MoodEventAdapter(Context context, ArrayList<MoodEvent> moodEventList, AdapterCallback callback) {
        super(context, 0, moodEventList);
        this.moodEventList = moodEventList;
        this.context = context;
        this.callback = callback;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_event;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.mood_list_content, parent, false);
        }

        MoodEvent moodEvent = moodEventList.get(position);
        Mood mood = new Mood(moodEvent.getMoodType());

        ImageView displayed_emoji = view.findViewById(R.id.emoji);
        TextView displayed_date = view.findViewById(R.id.date);
        TextView displayed_time = view.findViewById(R.id.time);

        // Set emoji and its background colour
        displayed_emoji.setImageResource(mood.getIconId());
        displayed_emoji.setBackgroundColor(context.getColor(mood.getColorId()));

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

        view.findViewById(R.id.delete_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onDelete(position);
            }
        });

        return view;
    }
}
