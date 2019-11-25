package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import io.github.cmput301f19t19.legendary_fiesta.MapActivity;
import io.github.cmput301f19t19.legendary_fiesta.Mood;
import io.github.cmput301f19t19.legendary_fiesta.MoodEvent;
import io.github.cmput301f19t19.legendary_fiesta.R;
import io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter.SocialArrayAdapter;
import io.github.cmput301f19t19.legendary_fiesta.ui.UIEventHandlers.FilterEventHandlers;

public class ViewPostFragment extends Fragment implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener {

    // UI variables
    private TextView dateET;
    private TextView timeET;
    private EditText descET;
    private EditText locET;
    private ImageButton addPictureButton;
    private Button cancelButton;
    private Button doneButton;
    private RadioGroup emotionRadioGroup;
    private Spinner socialSpinner;

    private ProxyLatLng location;

    private ArrayList<String> conditionsArray;

    // Fragment view
    private View mView;
    private Activity mActivity;

    private MoodEvent moodEvent;

    // Navigation Controller
    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // to allow loading images on main thread
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        mView = inflater.inflate(R.layout.fragment_add_post, container, false);

        // Buttons
        addPictureButton = mView.findViewById(R.id.addPictureButton);
        addPictureButton.setClickable(false);
        addPictureButton.setFocusable(false);

        cancelButton = mView.findViewById(R.id.cancel_button);
        cancelButton.setVisibility(View.GONE);

        doneButton = mView.findViewById(R.id.done_button);
        doneButton.setVisibility(View.GONE);

        // EditText
        dateET = mView.findViewById(R.id.dateEditText);
        dateET.setFocusable(false);

        timeET = mView.findViewById(R.id.timeEditText);
        timeET.setFocusable(false);

        descET = mView.findViewById(R.id.description_edittext);
        disableEditText(descET);

        locET = mView.findViewById(R.id.locationEditText);
        locET.setOnClickListener(this);

        emotionRadioGroup = mView.findViewById(R.id.emotionRadioGroup);
        emotionRadioGroup.setOnCheckedChangeListener(this);

        emotionRadioGroup.setClickable(false);
        for(int i = 0; i < emotionRadioGroup.getChildCount(); i++){
            RadioButton currentButton = (RadioButton)emotionRadioGroup.getChildAt(i);
            currentButton.setClickable(false);
            currentButton.setFocusable(false);
        }

        try {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        }catch (IllegalArgumentException e){
            Log.d("Error", "Illegal Argument for Navigation.findNavController, Message: " + e);
        }

        setUpSocialSpinner();

        Bundle args = getArguments();
        if (args != null) {
            MoodEvent moodEvent = args.getParcelable(OwnMoodsFragment.MOOD_EVENT_TAG);
            if (moodEvent != null) {
                this.moodEvent = moodEvent;
                setViewMoodEvent(moodEvent);
            }
        }

        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // get reference to associated activity
        mActivity = (Activity) context;
    }

    public void setViewMoodEvent(MoodEvent moodEvent) {
        // set social condition
        String moodSocialCondition = MoodEvent.SocialCondition.SocialConditionStrings.get(moodEvent.getCondition());
        System.out.println(moodEvent.getCondition());
        System.out.println(moodSocialCondition);
        System.out.println(conditionsArray.indexOf(moodSocialCondition));
        socialSpinner.setSelection(conditionsArray.indexOf(moodSocialCondition));

        // set description
        descET.setText(moodEvent.getDescription());

        // set image if moodEvent has one
        if (moodEvent.getPhotoURL() != null) {
            try {
                URL photoURL = new URL(moodEvent.getPhotoURL());
                Bitmap moodEventBmp = BitmapFactory.decodeStream(photoURL.openConnection().getInputStream());
                addPictureButton.setImageResource(0);
                addPictureButton.setBackground(new BitmapDrawable(mView.getResources(), scaleDown(moodEventBmp, addPictureButton.getMaxHeight())));
            } catch (MalformedURLException ex) {
                Toast.makeText(getContext(), "Invalid image URL",
                        Toast.LENGTH_SHORT).show();
            } catch (IOException ex) {
                Toast.makeText(getContext(), "Could not load image",
                        Toast.LENGTH_SHORT).show();
            }
        }

        // set mood type
        emotionRadioGroup.check(getEmotionRadioId(moodEvent.getMoodType()));

        // set date and time
        Format f = new SimpleDateFormat("MM/dd/yyyy", Locale.CANADA);
        dateET.setText(f.format(moodEvent.getDate()));

        f = new SimpleDateFormat("hh:mm aa", Locale.CANADA);
        timeET.setText(f.format(moodEvent.getDate()));

        location = moodEvent.getLocation();
        if (location != null)
            locET.setText(String.format("%f, %f", location.latitude, location.longitude));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.locationEditText:
                if (moodEvent.getLocation() != null) {
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    ArrayList<MoodEvent> moodDataList = new ArrayList<>();
                    moodDataList.add(moodEvent);
                    intent.putParcelableArrayListExtra("EVENTS", moodDataList);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int selectedId = group.getCheckedRadioButtonId();

        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton currentButton = (RadioButton) group.getChildAt(i);

            if (currentButton.getId() == selectedId) {
                currentButton.getBackground().setColorFilter( ContextCompat.getColor(mActivity,R.color.selected_color), PorterDuff.Mode.MULTIPLY);
            } else {
                // Make the unselected buttons white
                currentButton.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            }
        }
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false) ;
        editText.setClickable(false);
        editText.setLongClickable(false);
        editText.setCursorVisible(false) ;
    }


    /**
     * Set up adapter and it's item on creation
     */
    private void setUpSocialSpinner() {
        socialSpinner = mView.findViewById(R.id.social_spinner);

        // Get list of social conditions setup in strings.xml
        conditionsArray = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.social_conditions)));
        conditionsArray.add(getResources().getString(R.string.spinner_empty)); //filter_empty is "None"

        // Convert ArrayList to array, so that it can be passed to SocialArrayAdapter
        String[] spinnerObject = new String[conditionsArray.size()];
        spinnerObject = conditionsArray.toArray(spinnerObject);

        // Create string ArrayAdapter that will be used for filterSpinner
        ArrayAdapter<String> spinnerAdapter = new SocialArrayAdapter(mActivity,
                R.layout.spinner_item, spinnerObject);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        socialSpinner.setAdapter(spinnerAdapter);

        int defaultIndex = conditionsArray.indexOf(getResources().getString(R.string.spinner_empty));
        socialSpinner.setSelection(defaultIndex);

        socialSpinner.setOnItemSelectedListener(new FilterEventHandlers());
    }

    private int getEmotionRadioId(@Mood.MoodType int moodId) {
        switch (moodId) {
            case Mood.NEUTRAL:
                return R.id.icon_neutral;
            case Mood.HAPPY:
                return R.id.icon_happy;
            case Mood.ANGRY:
                return R.id.icon_angry;
            case Mood.DISGUSTED:
                return R.id.icon_disgusted;
            case Mood.SAD:
                return R.id.icon_sad;
            case Mood.SCARED:
                return R.id.icon_scared;
            case Mood.SURPRISED:
                return R.id.icon_surprised;
        }
        return R.id.icon_neutral;
    }

    /**
     * Scales down a Bitmap to a defined max size
     *
     * @param bitmap  Bitmap image
     * @param maxSize Size of the Bitmap to scale to
     * @return Returns a scaled down version of the original Bitmap
     */
    private Bitmap scaleDown(Bitmap bitmap, float maxSize) {
        // Ratio between the maxSize and the bitmap's width and height
        float ratio = Math.min(
                maxSize / bitmap.getWidth(),
                maxSize / bitmap.getHeight()
        );

        // If ratio >=1, the bitmap is smaller than the defined max size
        if (ratio >= 1) {
            return bitmap;
        }

        // Width and height reduction
        int width = Math.round(ratio * bitmap.getWidth());
        int height = Math.round(ratio * bitmap.getHeight());

        if (width <= 0 && height <=0) {
            return bitmap;
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }
}
