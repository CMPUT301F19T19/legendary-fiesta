package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import io.github.cmput301f19t19.legendary_fiesta.R;

public class OwnMoodsFragment extends Fragment {

    private Activity mActivity; //reference to associated activity class, initialized in onAttach function
    private View mView; //reference to associated view, initialized in onCreateView

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_own_moods, container, false);
        mView = root;

        setUpSpinner();

        return root;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        /**
         * get reference to associated activity
         */
        mActivity = (Activity) context;
    }

    /**
     * Mostly a test function to set up the spinner, populate it with a string array from resource.xml
     */
    private void setUpSpinner(){
        Spinner spinner = mView.findViewById(R.id.filter_spinner);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(mActivity, R.array.filter,
                android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

    }

}