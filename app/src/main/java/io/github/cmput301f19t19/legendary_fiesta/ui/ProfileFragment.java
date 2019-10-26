package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import io.github.cmput301f19t19.legendary_fiesta.R;
import io.github.cmput301f19t19.legendary_fiesta.User;

public class ProfileFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        // Information from FireBase
        TextView textView = root.findViewById(R.id.text_friends_moods);
        User user = requireActivity().getIntent().getParcelableExtra("USER_PROFILE");
        assert user != null;  // TODO: clean this up later
        textView.setText(user.getUsername());

        return root;
    }
}