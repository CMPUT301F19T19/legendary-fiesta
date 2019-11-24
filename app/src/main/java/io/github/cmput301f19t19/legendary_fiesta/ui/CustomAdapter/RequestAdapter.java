package io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.google.firebase.FirebaseApp;
import io.github.cmput301f19t19.legendary_fiesta.FirebaseHelper;
import io.github.cmput301f19t19.legendary_fiesta.FriendRequest;
import io.github.cmput301f19t19.legendary_fiesta.R;
import io.github.cmput301f19t19.legendary_fiesta.User;
import org.jetbrains.annotations.NotNull;

public class RequestAdapter extends ArrayAdapter<FriendRequest> {

    private Context context;
    private int resource;
    private ArrayList<FriendRequest> dataList;
    private ArrayList<User> users;
    private User myUID;
    private FirebaseHelper helper;

    public RequestAdapter(@NonNull Context context, int resource, @NonNull ArrayList<FriendRequest> objects, ArrayList<User> users, User myUID) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
        this.users = users;
        this.helper = new FirebaseHelper(FirebaseApp.getInstance());
        this.myUID = myUID;
        dataList = objects;
    }

    private String UIDToName(String UID) {
        for (User user : users) {
            if (user.getUid().equals(UID)) {
                return user.getUsername();
            }
        }
        return null;
    }

    private void acceptRequest(String UID) {
        helper.flipFriendRequest(UID, myUID, true, new FirebaseHelper.FirebaseCallback<Void>() {
            @Override
            public void onSuccess(Void document) {
                Log.d("FeelsLog", "acceptRequest");
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FeelsLog", "onFailure: ", e);
            }
        });
    }

    private void rejectRequest(String UID) {
        helper.flipFriendRequest(UID, myUID, false, new FirebaseHelper.FirebaseCallback<Void>() {
            @Override
            public void onSuccess(Void document) {
                Log.d("FeelsLog", "rejectRequest");
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FeelsLog", "onFailure: ", e);
            }
        });
    }

    @NotNull
    @Override
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView nameView = view.findViewById(R.id.name);
        String uid = dataList.get(position).getFrom();
        Button accept = view.findViewById(R.id.accept_button);
        Button reject = view.findViewById(R.id.reject_button);
        View finalView = view;
        accept.setOnClickListener(view1 -> {
            view1.setEnabled(false);
            finalView.setVisibility(View.GONE);
            acceptRequest(uid);
        });
        reject.setOnClickListener(view12 -> {
            view12.setEnabled(false);
            finalView.setVisibility(View.GONE);
            rejectRequest(uid);
        });
        nameView.setText(UIDToName(uid));

        return view;
    }
}
