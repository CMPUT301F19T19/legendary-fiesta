package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import io.github.cmput301f19t19.legendary_fiesta.MapActivity;
import io.github.cmput301f19t19.legendary_fiesta.R;


public class FriendsMoodsFragment extends Fragment {

    // View Elements
    private Button mapButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_friends_moods, container, false);

        // Map Button Click Listener
        mapButton = mView.findViewById(R.id.show_on_map_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                // TODO: Add moodDataList
                // intent.putParcelableArrayListExtra("MOODEVENTS", moodDataList);
                startActivity(intent);
            }
        });

        return mView;
    }
}