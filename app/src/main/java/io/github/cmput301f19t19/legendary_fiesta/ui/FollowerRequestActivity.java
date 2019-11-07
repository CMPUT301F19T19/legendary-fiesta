package io.github.cmput301f19t19.legendary_fiesta.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import io.github.cmput301f19t19.legendary_fiesta.R;

public class FollowerRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_request);

        //hide action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        //hide notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
