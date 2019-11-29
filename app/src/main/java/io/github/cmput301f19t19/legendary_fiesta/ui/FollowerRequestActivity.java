package io.github.cmput301f19t19.legendary_fiesta.ui;

/*
  FollowerRequestActivity deals with displaying the user's follow requests by other users and also
  allowing the user to accept or reject other user's request to follow the user.
 */

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;

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
    private SwipeRefreshLayout swipeRefreshLayout;

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

        requestAdapter = new RequestAdapter(this, R.layout.request_list_content, requestDataList, users, user);
        requestList.setAdapter(requestAdapter);

        //hide action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        swipeRefreshLayout = findViewById(R.id.follow_request_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestDataList.clear();
                getRequest();
                requestAdapter = new RequestAdapter(getApplicationContext(), R.layout.request_list_content, requestDataList, users, user);
                requestList.setAdapter(requestAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        //hide notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
        }
    }

    private void getRequest() {
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
