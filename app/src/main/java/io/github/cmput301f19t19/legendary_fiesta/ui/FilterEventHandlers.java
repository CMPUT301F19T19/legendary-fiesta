package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;


// TODO: Filter function (after Reading Week)
public class FilterEventHandlers implements AdapterView.OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("TEST", "PRESSED");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
