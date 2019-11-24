package io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import com.google.firebase.FirebaseApp;
import io.github.cmput301f19t19.legendary_fiesta.FirebaseHelper;
import io.github.cmput301f19t19.legendary_fiesta.R;
import io.github.cmput301f19t19.legendary_fiesta.User;

public class FriendsAdapter extends ArrayAdapter<User> {

    ArrayList<User> dataList;
    Context context;
    int resource;
    FirebaseHelper helper;
    User user;
    AdapterCallback onDeleteCallback;

    public interface AdapterCallback{
        void onDelete(int position);
    }

    public FriendsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> objects, User user, AdapterCallback onDeleteCallback) {
        super(context, resource, objects);
        dataList = objects;
        this.context = context;
        this.resource = resource;
        this.user = user;
        this.helper = new FirebaseHelper(FirebaseApp.getInstance());
        this.onDeleteCallback = onDeleteCallback;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        // full logic for sending friend requests
        if (resource == R.layout.friend_request_list_content) {
            Button followButton = view.findViewById(R.id.follow_button);
            followButton.setVisibility(View.INVISIBLE);
            helper.checkRequestExists(user.getUid(), dataList.get(position).getUid(), new FirebaseHelper.FirebaseCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean document) {
                    if (!document) followButton.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(@NonNull Exception e) {
                    // no-op
                }
            });
            followButton.setOnClickListener(view1 -> {
                view1.setVisibility(View.INVISIBLE);
                helper.sendFriendRequest(user.getUid(), dataList.get(position).getUid(), new FirebaseHelper.FirebaseCallback<Void>() {
                    @Override
                    public void onSuccess(Void document) {
                        Log.d("FeelsLog", "onSuccess: follow request sent");
                    }

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed to deliver friend request", Toast.LENGTH_LONG).show();
                        view1.setVisibility(View.VISIBLE);
                    }
                });
            });
        }

        if (resource == R.layout.friend_list_content) {
            ImageView deleteButton = view.findViewById(R.id.delete_event);
            deleteButton.setOnClickListener(view1 -> onDeleteCallback.onDelete(position));
        }

        TextView nameView = view.findViewById(R.id.name);
        nameView.setText(dataList.get(position).getUsername());

        return view;
    }
}
