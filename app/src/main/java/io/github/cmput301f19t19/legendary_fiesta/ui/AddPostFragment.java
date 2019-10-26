package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import io.github.cmput301f19t19.legendary_fiesta.R;

public class AddPostFragment extends Fragment {

    EditText dateET;
    String selectedDate;

    // Date result identifier
    public static final int DATE_REQUEST_CODE = 66;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_post, container, false);
        return root;
    }
}