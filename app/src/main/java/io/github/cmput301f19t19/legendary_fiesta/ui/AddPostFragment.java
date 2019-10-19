package com.example.navbar.ui.addpost;

import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

import androidx.annotation.NonNull;
        import androidx.fragment.app.Fragment;

import com.example.navbar.R;

public class AddPostFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_post, container, false);
        return root;
    }
}