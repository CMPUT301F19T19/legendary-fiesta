package io.github.cmput301f19t19.legendary_fiesta.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.api.Distribution;

import io.github.cmput301f19t19.legendary_fiesta.R;

/*
Acts as an Empty container to contain a fragment. Used in fragment testing
 */
public class FragmentEmptyClass extends AppCompatActivity {

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize a linear layout to contain the empty fragment
        LinearLayout view = new LinearLayout(this);

        //set the id to 1 as example, used to identify
        view.setId(1);

        setContentView(view);
    }
}
