package io.github.cmput301f19t19.legendary_fiesta.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.drm.DrmStore;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import io.github.cmput301f19t19.legendary_fiesta.R;
import io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter.RequestAdapter;

public class FollowerRequestActivity extends AppCompatActivity implements View.OnClickListener {

    //UI Elements
    private ImageButton backButton;
    private ListView requestList;

    private ArrayList<String> requestDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_request);

        backButton = findViewById(R.id.back_button);
        requestList = findViewById(R.id.request_list);

        backButton.setOnClickListener(this);

        requestDataList = getRequest();
        requestList.setAdapter(new RequestAdapter(this, R.layout.request_list_content, requestDataList));

        //hide action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        //hide notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back_button:
                finish();
        }
    }

    private ArrayList<String> getRequest(){
        ArrayList<String> list = new ArrayList<>();

        return list;
    }
}
