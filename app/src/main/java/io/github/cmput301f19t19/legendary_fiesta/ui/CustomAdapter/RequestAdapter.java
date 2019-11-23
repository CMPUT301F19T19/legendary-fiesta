package io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

import io.github.cmput301f19t19.legendary_fiesta.FriendRequest;
import io.github.cmput301f19t19.legendary_fiesta.R;
import io.github.cmput301f19t19.legendary_fiesta.User;
import org.jetbrains.annotations.NotNull;

public class RequestAdapter extends ArrayAdapter<FriendRequest> {

    Context context;
    int resource;
    ArrayList<FriendRequest> dataList;
    ArrayList<User> users;

    public RequestAdapter(@NonNull Context context, int resource, @NonNull ArrayList<FriendRequest> objects, ArrayList<User> users) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
        this.users = users;
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

    @NotNull
    @Override
    public View getView(int position, View convertView, @NotNull ViewGroup parent){
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView nameView = view.findViewById(R.id.name);
        String uid = dataList.get(position).getFrom();
        nameView.setText(UIDToName(uid));

        return view;
    }
}
