package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import io.github.cmput301f19t19.legendary_fiesta.FirebaseHelper;
import io.github.cmput301f19t19.legendary_fiesta.R;
import io.github.cmput301f19t19.legendary_fiesta.User;
import io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter.FriendsAdapter;

public class FriendsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher {

    private static final FirebaseHelper firebaseHelper = new FirebaseHelper(FirebaseApp.getInstance());
    private View mView;
    private Activity mActivity;
    private User user;
    // text views
    private TextView friendView;
    private TextView followView;
    //Friend's List variables
    private ListView friendsListView;
    private FriendsAdapter friendsArrayAdapter;
    //Variables for Search
    private EditText search;        //Refers to the Search EditText in fragment_friends.xml
    private String searchName;      //searchName is the text that is entered in the Search EditText
    private ArrayList<User> searchFriendsArray;   //A list temporarily used to contain all names that match the search text
    private ArrayList<User> users; // all the users
    private ArrayList<User> friends;
    private ImageButton requestButton;

    private boolean isDeleting = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_friends, container, false);

        friendsListView = mView.findViewById(R.id.friend_list);
        requestButton = mView.findViewById(R.id.follow_request_button);
        friendView = mView.findViewById(R.id.friendView);
        followView = mView.findViewById(R.id.followView);
        friends = new ArrayList<>();
        user = requireActivity().getIntent().getParcelableExtra("USER_PROFILE");

        if (user == null) {
            Bundle receiveBundle = this.getArguments();
            assert receiveBundle != null;
            user = receiveBundle.getParcelable("USER_PROFILE");
        }

        assert user != null;
        firebaseHelper.finishFriendRequest(user, new FirebaseHelper.FirebaseCallback<Void>() {
            @Override
            public void onSuccess(Void document) {}

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FeelsLog", "onFailure: ", e);
                Toast.makeText(getContext(), "Database transaction problem", Toast.LENGTH_LONG).show();
            }
        });

        friendsArrayAdapter = new FriendsAdapter(mActivity, R.layout.friend_list_content, friends, user, this::onDeleteCallback);

        friendsListView.setAdapter(friendsArrayAdapter);

        requestButton.setOnClickListener(this);

        search = mView.findViewById(R.id.search_friends_edittext);

        firebaseHelper.getAllUsers(new FirebaseHelper.FirebaseCallback<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot document) {
                users = new ArrayList<>();
                for (DocumentSnapshot doc : document.getDocuments()) {
                    users.add(doc.toObject(User.class));
                }
                if (user != null) {
                    for (String uid : user.getFollowing()) {
                        User friend = UIDToUser(uid);
                        if (friend != null) friends.add(friend);
                    }
                    friendsArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        //Search EditText onchange listener
        search.addTextChangedListener(this);

        return mView;
    }

    private void onDeleteCallback(int position) {
        if (!isDeleting) {
            isDeleting = true;
            String toUID = friends.get(position).getUid();
            firebaseHelper.unfollowUser(user.getUid(), toUID, new FirebaseHelper.FirebaseCallback<Void>() {
                @Override
                public void onSuccess(Void document) {
                    friends.remove(position);
                    user.removeFollowing(toUID);
                    friendsArrayAdapter.notifyDataSetChanged();
                    isDeleting = false;
                }

                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mActivity, "Could not unfollow user", Toast.LENGTH_LONG).show();
                    isDeleting = false;
                }
            });
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.follow_request_button:
                Intent followIntent = new Intent(mActivity, FollowerRequestActivity.class)
                        .putExtra("USER_PROFILE", user)
                        .putParcelableArrayListExtra("USERS", users);
                startActivityForResult(followIntent, 1);
        }
    }

    private User UIDToUser(String UID) {
        for (User user : users) {
            if (user.getUid().equals(UID)) {
                return user;
            }
        }
        return null;
    }

    public void toggleControls(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        requestButton.setVisibility(visibility);
        friendView.setVisibility(visibility);
        followView.setVisibility(visibility);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        searchName = search.getText().toString();
        searchFriendsArray = new ArrayList<>();
        FriendsAdapter requestArrayAdapter;

        //Don't have to search if the string at the search editText is empty
        if (!searchName.equals("")) {
            toggleControls(false);
            //Loop through all friends to find matching names
            for (User user : users) {
                if (user.getUsername().toUpperCase().contains(searchName.toUpperCase())) {
                    searchFriendsArray.add(user);
                }
            }
            requestArrayAdapter = new FriendsAdapter(mActivity, R.layout.friend_request_list_content, searchFriendsArray, user, new FriendsAdapter.AdapterCallback() {
                @Override
                public void onDelete(int position) {
                    // no-op because no deleting searches
                }
            });
            friendsListView.setAdapter(requestArrayAdapter);
        }

        //Else, if no name is searched, set adapter back to user friendsArray
        else {
            toggleControls(true);
            friendsArrayAdapter = new FriendsAdapter(mActivity, R.layout.friend_list_content, friends, user, new FriendsAdapter.AdapterCallback() {
                @Override
                public void onDelete(int position) {
                    this.onDelete(position);
                }
            });
            friendsListView.setAdapter(friendsArrayAdapter);
        }
    }

}