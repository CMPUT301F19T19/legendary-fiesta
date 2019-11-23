package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.util.Log;
import androidx.annotation.NonNull;
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
import java.util.List;

import com.google.firebase.FirebaseApp;
import io.github.cmput301f19t19.legendary_fiesta.FirebaseHelper;
import io.github.cmput301f19t19.legendary_fiesta.FriendRequest;
import io.github.cmput301f19t19.legendary_fiesta.R;
import io.github.cmput301f19t19.legendary_fiesta.User;
import io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter.RequestAdapter;

public class FollowerRequestActivity extends AppCompatActivity implements View.OnClickListener {

    //UI Elements
    private ImageButton backButton;
    private ListView requestList;
    private User user;
    private ArrayList<User> users; // all the users

    private ArrayList<FriendRequest> requestDataList;
    private RequestAdapter requestAdapter;
    private FirebaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_request);

        user = getIntent().getParcelableExtra("USER_PROFILE");
        users = getIntent().getParcelableArrayListExtra("USERS");
        requestDataList = new ArrayList<>();
        helper = new FirebaseHelper(FirebaseApp.getInstance());
        getRequest();

        backButton = findViewById(R.id.back_button);
        requestList = findViewById(R.id.request_list);

        backButton.setOnClickListener(this);

        requestAdapter = new RequestAdapter(this, R.layout.request_list_content, requestDataList, users);
        requestList.setAdapter(requestAdapter);

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

    private void getRequest(){
        helper.getPendingRequests(user.getUid(), new FirebaseHelper.FirebaseCallback<List<FriendRequest>>() {
            @Override
            public void onSuccess(List<FriendRequest> document) {
                requestDataList.addAll(document);
                requestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FeelsLog", "onFailure: ", e);
            }
        });
    }
}
