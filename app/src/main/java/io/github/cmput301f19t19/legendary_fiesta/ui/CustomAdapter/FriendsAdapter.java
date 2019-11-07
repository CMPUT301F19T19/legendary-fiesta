package io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import io.github.cmput301f19t19.legendary_fiesta.R;

public class FriendsAdapter extends ArrayAdapter<String> {

    ArrayList<String> dataList;
    Context context;
    int resource;

    public FriendsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        dataList = objects;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView nameView = view.findViewById(R.id.name);
        nameView.setText(dataList.get(position));

        return view;
    }
}
