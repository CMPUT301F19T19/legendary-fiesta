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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import io.github.cmput301f19t19.legendary_fiesta.R;
import io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter.FriendsAdapter;

public class FriendsFragment extends Fragment implements  View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher {

    private View mView;
    private Activity mActivity;

    //Friend's List variables
    private ListView friendsListView;
    private ArrayList<String> friendsArray;
    private FriendsAdapter friendsArrayAdapter;

    //Variables for Search
    private EditText search;        //Refers to the Search EditText in fragment_friends.xml
    private String searchName;      //searchName is the text that is entered in the Search EditText
    private ArrayList<String> searchFriendsArray;   //A list temporarily used to contain all names that match the search text

    private ImageButton requestButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_friends, container, false);

        friendsListView = mView.findViewById(R.id.friend_list);
        requestButton = mView.findViewById(R.id.follow_request_button);
        search = mView.findViewById(R.id.search_friends_edittext);

        friendsArray = getFriendsList();
        friendsArrayAdapter = new FriendsAdapter(mActivity, R.layout.friend_list_content, friendsArray);
        friendsListView.setAdapter(friendsArrayAdapter);

        requestButton.setOnClickListener(this);
        //Search EditText onchange listener
        search.addTextChangedListener(this);

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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        searchName = search.getText().toString();
        searchFriendsArray = new ArrayList<>();

        //Don't have to search if the string at the search editText is empty
        if(searchName != ""){
            //Loop through all friends to find matching names
            for(String name:friendsArray){
                if(name.toUpperCase().contains(searchName.toUpperCase())){
                    searchFriendsArray.add(name);
                }
            }
            friendsArrayAdapter = new FriendsAdapter(mActivity, R.layout.friend_list_content, searchFriendsArray);
            friendsListView.setAdapter(friendsArrayAdapter);
        }

        //Else, if no name is searched, set adapter back to user friendsArray
        else{
            friendsArrayAdapter = new FriendsAdapter(mActivity, R.layout.friend_list_content, friendsArray);
            friendsListView.setAdapter(friendsArrayAdapter);
        }
    }

}