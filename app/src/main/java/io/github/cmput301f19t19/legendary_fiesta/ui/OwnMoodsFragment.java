package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import io.github.cmput301f19t19.legendary_fiesta.R;

public class OwnMoodsFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_own_moods, container, false);
        return root;
    }
}