package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.util.Assert;

import java.util.ArrayList;

import io.github.cmput301f19t19.legendary_fiesta.R;
import io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter.FriendsAdapter;

public class FriendsFragment extends Fragment {

    private View mView;
    private Activity mActivity;

    //Friend's List variables
    private ListView friendsListView;
    private ArrayList<String> friendsArray;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_friends, container, false);

        friendsListView = mView.findViewById(R.id.friend_list);
        friendsArray = getFriendsList();

        friendsListView.setAdapter(new FriendsAdapter(mActivity, R.layout.friend_list_content, friendsArray));

        return mView;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mActivity = (Activity)context;
    }

    /*
    This function needs the dataList that will be past into the adapter
     */
    public ArrayList<String> getFriendsList(){
        ArrayList<String> friendsList = new ArrayList<>();
        friendsList.add("Juliana");
        friendsList.add("Tom");
        friendsList.add("Hery Martial Rakotoarimanana Rajaonarimampianina");

        return friendsList;
    }
}