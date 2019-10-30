package io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.github.cmput301f19t19.legendary_fiesta.Mood;
import io.github.cmput301f19t19.legendary_fiesta.R;

public class SocialArrayAdapter extends ArrayAdapter<String> {

    Context context;
    String[] objects;

    public SocialArrayAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getSpinnerView_DropDown(position, convertView, parent);
    }

    /*
    For spinner item (layout showed when filter spinner is not clicked
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        }

        TextView textView = view.findViewById(R.id.filter_text);
        String filterText = "Social Condition: ";
        String capText = objects[position];
        textView.setText(filterText + capText);

        return view;
    }

    /*
    For spinner dropDown_item (layout showed when filter spinner is not clicked
     */
    public View getSpinnerView_DropDown(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.spinner_dropdown, parent, false);
        }

        TextView dropDownText = view.findViewById(R.id.spinner_text);
        dropDownText.setText(objects[position]);

        return view;
    }

}
