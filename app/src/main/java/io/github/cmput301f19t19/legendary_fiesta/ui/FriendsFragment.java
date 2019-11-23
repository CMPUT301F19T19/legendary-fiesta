package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import androidx.transition.Visibility;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import io.github.cmput301f19t19.legendary_fiesta.FirebaseHelper;
import io.github.cmput301f19t19.legendary_fiesta.R;
import io.github.cmput301f19t19.legendary_fiesta.User;
import io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter.FriendsAdapter;
import io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter.RequestAdapter;

public class FriendsFragment extends Fragment implements  View.OnClickListener, AdapterView.OnItemClickListener {

    private View mView;
    private Activity mActivity;

    // text views
    private TextView friendView;
    private TextView followView;

    //Friend's List variables
    private ListView friendsListView;
    private ArrayList<String> friendsArray;
    private FriendsAdapter friendsArrayAdapter;

    //Variables for Search
    private EditText search;        //Refers to the Search EditText in fragment_friends.xml
    private String searchName;      //searchName is the text that is entered in the Search EditText
    private ArrayList<User> searchFriendsArray;   //A list temporarily used to contain all names that match the search text
    private ArrayList<User> users; // all the users

    private ImageButton requestButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_friends, container, false);

        friendsListView = mView.findViewById(R.id.friend_list);
        requestButton = mView.findViewById(R.id.follow_request_button);
        friendView = mView.findViewById(R.id.friendView);
        followView = mView.findViewById(R.id.followView);

        friendsArray = getFriendsList();
        friendsArrayAdapter = new FriendsAdapter(mActivity, R.layout.friend_list_content, friendsArray);

        friendsListView.setAdapter(friendsArrayAdapter);

        requestButton.setOnClickListener(this);

        search = mView.findViewById(R.id.search_friends_edittext);

        FirebaseHelper helper = new FirebaseHelper(FirebaseApp.getInstance());

        helper.getAllUsers(new FirebaseHelper.FirebaseCallback<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot document) {
                users = new ArrayList<>();
                for (DocumentSnapshot doc: document.getDocuments()) {
                    users.add(doc.toObject(User.class));
                }
            }

            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        //Search EditText onchange listener
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                searchName = search.getText().toString();
                searchFriendsArray = new ArrayList<>();
                RequestAdapter requestArrayAdapter;

                //Don't have to search if the string at the search editText is empty
                if(!searchName.equals("")){
                    toggleControls(false);
                    //Loop through all friends to find matching names
                    for(User user:users){
                        if(user.getUsername().toUpperCase().contains(searchName.toUpperCase())){
                            searchFriendsArray.add(user);
                        }
                    }
                    requestArrayAdapter = new RequestAdapter(mActivity, R.layout.friend_request_list_content, searchFriendsArray);
                    friendsListView.setAdapter(requestArrayAdapter);
                }

                //Else, if no name is searched, set adapter back to user friendsArray
                else{
                    toggleControls(true);
                    friendsArrayAdapter = new FriendsAdapter(mActivity, R.layout.friend_list_content, friendsArray);
                    friendsListView.setAdapter(friendsArrayAdapter);
                }
            }
        });

        return mView;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mActivity = (Activity)context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.follow_request_button:
                Intent followIntent = new Intent(mActivity, FollowerRequestActivity.class);
                startActivityForResult(followIntent, 1);
        }
    }
    
    /*
    This function needs the dataList that will be past into the adapter
     */
    public ArrayList<String> getFriendsList(){
        ArrayList<String> friendsList = new ArrayList<>();
        return friendsList;
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
}