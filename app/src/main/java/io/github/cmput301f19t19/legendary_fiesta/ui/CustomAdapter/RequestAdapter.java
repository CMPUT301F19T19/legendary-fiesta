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

import io.github.cmput301f19t19.legendary_fiesta.R;

public class RequestAdapter extends ArrayAdapter<String> {

    Context context;
    int resource;
    ArrayList<String> dataList;

    public RequestAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
        dataList = objects;
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
