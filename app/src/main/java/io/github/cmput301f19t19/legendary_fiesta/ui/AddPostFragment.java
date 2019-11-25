package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
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
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.FirebaseApp;
import com.schibstedspain.leku.LocationPickerActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import io.github.cmput301f19t19.legendary_fiesta.BuildConfig;
import io.github.cmput301f19t19.legendary_fiesta.FirebaseHelper;
import io.github.cmput301f19t19.legendary_fiesta.Mood;
import io.github.cmput301f19t19.legendary_fiesta.MoodEvent;
import io.github.cmput301f19t19.legendary_fiesta.R;
import io.github.cmput301f19t19.legendary_fiesta.User;
import io.github.cmput301f19t19.legendary_fiesta.ui.CustomAdapter.SocialArrayAdapter;

import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;

public class AddPostFragment extends Fragment implements View.OnClickListener,
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

    private String selectedDate;
    private String selectedTime;

    private boolean isEdit;
    private String editMoodId;
    private String originalPhotoURL;
    private byte[] moodEventImage;
    private ProxyLatLng location;
    private String cameraFilePath;


    // Date result identifier
    public static final int DATE_REQUEST_CODE = 66;
    // Time result identifier
    public static final int TIME_REQUEST_CODE = 99;
    // Camera result identifier
    public static final int CAMERA_REQUEST_CODE = 12;
    // Gallery result identifier
    public static final int GALLERY_REQUEST_CODE = 13;
    // Map result identifier
    private static final int LOCATION_REQUEST_CODE = 64;

    // Storage permission code
    private static final int STORAGE_PERMISSION_CODE = 42;

    private ArrayList<String> conditionsArray;

    // Fragment view
    private View mView;
    private Activity mActivity;

    // FireBase Helper
    private static final FirebaseHelper firebaseHelper = new FirebaseHelper(FirebaseApp.getInstance());

    // Navigation Controller
    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // to allow loading images on main thread
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        mView = inflater.inflate(R.layout.fragment_add_post, container, false);

        //Buttons
        addPictureButton = mView.findViewById(R.id.addPictureButton);
        cancelButton = mView.findViewById(R.id.cancel_button);
        doneButton = mView.findViewById(R.id.done_button);

        //EditText
        dateET = mView.findViewById(R.id.dateEditText);
        timeET = mView.findViewById(R.id.timeEditText);
        descET = mView.findViewById(R.id.description_edittext);
        locET = mView.findViewById(R.id.locationEditText);
        emotionRadioGroup = mView.findViewById(R.id.emotionRadioGroup);

        //set listener to OnClick defined this class
        dateET.setOnClickListener(this);
        timeET.setOnClickListener(this);
        locET.setOnClickListener(this);
        addPictureButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);
        emotionRadioGroup.setOnCheckedChangeListener(this);

        try {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        } catch (IllegalArgumentException e) {
            Log.d("Error", "Illegal Argument for Navigation.findNavController, Message: " + e);
        }

        setUpSocialSpinner();

        Bundle args = getArguments();
        if (args != null) {
            MoodEvent moodEvent = args.getParcelable(OwnMoodsFragment.MOOD_EVENT_TAG);
            if (moodEvent != null) {
                setFragmentToEdit(moodEvent);
            }
        }

        return mView;
    }

    /**
     * Launch the location picker
     */
    private void pickLocation() {
        // By default the location picker will enable all the included buttons like
        // Satellite view and position me
        Intent locationPicker = new LocationPickerActivity.Builder().build(this.getContext());
        startActivityForResult(locationPicker, LOCATION_REQUEST_CODE);
    }

    private void setFragmentToEdit(MoodEvent moodEvent) {
        isEdit = true;

        // Set Mood Id for edit mood
        editMoodId = moodEvent.getMoodId();

        // Set social condition
        String moodSocialCondition = MoodEvent.SocialCondition.SocialConditionStrings.get(moodEvent.getCondition());
        socialSpinner.setSelection(conditionsArray.indexOf(moodSocialCondition));

        // Set description
        descET.setText(moodEvent.getDescription());

        // set image if moodEvent has one
        if (moodEvent.getPhotoURL() != null) {
            try {
                originalPhotoURL = moodEvent.getPhotoURL();
                URL photoURL = new URL(originalPhotoURL);
                Bitmap moodEventBmp = BitmapFactory.decodeStream(photoURL.openConnection().getInputStream());
                addPictureButton.setImageResource(0);
                addPictureButton.setBackground(new BitmapDrawable(mView.getResources(), moodEventBmp));
            } catch (MalformedURLException ex) {
                Toast.makeText(getContext(), "Invalid image URL",
                        Toast.LENGTH_SHORT).show();
            } catch (IOException ex) {
                Toast.makeText(getContext(), "Could not load image",
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Set mood type
        emotionRadioGroup.check(getEmotionRadioId(moodEvent.getMoodType()));

        // Set date and time
        Format f = new SimpleDateFormat("MM/dd/yyyy", Locale.CANADA);
        dateET.setText(f.format(moodEvent.getDate()));

        f = new SimpleDateFormat("hh:mm aa", Locale.CANADA);
        timeET.setText(f.format(moodEvent.getDate()));

        location = moodEvent.getLocation();
        if (location != null)
            locET.setText(String.format("%f, %f", location.latitude, location.longitude));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // get reference to associated activity
        mActivity = (Activity) context;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        cameraFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Uri selectedImage = null;
        // Check for the results
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case DATE_REQUEST_CODE:
                // Get date from string
                selectedDate = data.getStringExtra("SELECTED_DATE");
                // Set date EditText to the selected date
                dateET.setText(selectedDate);
                break;
            case TIME_REQUEST_CODE:
                // Get time from string
                selectedTime = data.getStringExtra("SELECTED_TIME");
                // Set time EditText to the selected time
                timeET.setText(selectedTime);
                break;
            case CAMERA_REQUEST_CODE:
                selectedImage = Uri.fromFile(new File(cameraFilePath));
                // fallthrough
            case GALLERY_REQUEST_CODE:
                selectedImage = selectedImage != null ? selectedImage : data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), selectedImage);
                    addPictureButton.setImageResource(0);
                    addPictureButton.setBackground(new BitmapDrawable(mView.getResources(), bitmap));

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    moodEventImage = stream.toByteArray();
                } catch (IOException e) {
                    addPictureButton.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.add_picture_button));
                }
                break;
            case LOCATION_REQUEST_CODE:
                double lat = data.getDoubleExtra(LATITUDE, 0.0);
                double lng = data.getDoubleExtra(LONGITUDE, 0.0);
                location = new ProxyLatLng(lat, lng);
                locET.setText(String.format("%f, %f", lat, lng));
                break;
        }
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
        socialSpinner.setAdapter(spinnerAdapter);

        // Set default selection to None
        int defaultIndex = conditionsArray.indexOf(getResources().getString(R.string.spinner_empty));
        socialSpinner.setSelection(defaultIndex);
    }

    /**
     * Click handler for AddPostFragment and switches based on what is clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        /*
         * Perform different onClick action depending on what was click. Determined by comparing view ID
         */
        switch (view.getId()) {
            case R.id.dateEditText:
                // Create DatePickerFragment
                DialogFragment dateFragment = new DatePickerFragment();
                // Set the target fragment to receive the results and specifying the result code
                dateFragment.setTargetFragment(AddPostFragment.this, DATE_REQUEST_CODE);
                // Show DatePickerFragment
                Bundle args = new Bundle();
                if (isEdit) {
                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm aa", Locale.CANADA);
                    Date date;
                    try {
                        date = format.parse(dateET.getText().toString() + " " +
                                timeET.getText().toString());
                    } catch (Exception e) {
                        handleError("Missing date or time");
                        return;
                    }
                    args.putInt(DatePickerFragment.DATE_TAG, date.getDate());
                    args.putInt(DatePickerFragment.MONTH_TAG, date.getMonth());
                    args.putInt(DatePickerFragment.YEAR_TAG, date.getYear() + 1900);

                    dateFragment.setArguments(args);
                }
                dateFragment.show(getFragmentManager(), "DatePicker");
                break;
            case R.id.timeEditText:
                // Create TimePickerFragment
                DialogFragment timeFragment = new TimePickerFragment();
                // Set the target fragment to receive the results and specifying the result code
                timeFragment.setTargetFragment(AddPostFragment.this, TIME_REQUEST_CODE);
                // Show TimePickerFragment
                args = new Bundle();
                if (isEdit) {
                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm aa", Locale.CANADA);
                    Date date;
                    try {
                        date = format.parse(dateET.getText().toString() + " " +
                                timeET.getText().toString());
                    } catch (Exception e) {
                        handleError("Missing date or time");
                        return;
                    }
                    args.putInt(TimePickerFragment.HOUR_TAG, date.getHours());
                    args.putInt(TimePickerFragment.MINUTE_TAG, date.getMinutes());

                    timeFragment.setArguments(args);
                }
                timeFragment.show(getFragmentManager(), "TimePicker");
                break;
            case R.id.addPictureButton:
                new AlertDialog.Builder(getActivity())
                        .setTitle("Image Picker")
                        .setMessage("Choose image from")
                        .setPositiveButton("Photo Gallery",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(pickPhoto, GALLERY_REQUEST_CODE);
                                    }
                                }
                        )
                        .setNegativeButton("Camera",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                                        }
                                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        try {
                                            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".fileprovider", createImageFile()));
                                            startActivityForResult(takePicture, CAMERA_REQUEST_CODE);
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                }
                        )
                        .setNeutralButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .create()
                        .show();
                break;
            case R.id.cancel_button:
                closeFragment();
                break;
            case R.id.done_button:
                onDoneClicked();
                break;
            case R.id.locationEditText:
                pickLocation();
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(mActivity, "Cannot use camera without granting storage permission",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Resets the fragment and navigate to 'Own List Fragment'
     */
    private void closeFragment() {
        dateET.setText("");
        timeET.setText("");
        descET.setText("");
        locET.setText("");

        emotionRadioGroup.clearCheck();

        if (navController != null)
            navController.navigate(R.id.navigation_own_list);
    }

    /**
     * Attempts to save mood event on FireBase
     */
    private void onDoneClicked() {
        Mood mood = getSelectedMood(emotionRadioGroup.getCheckedRadioButtonId());
        if (mood == null) {
            // Replace handleError with the error popup
            errorPopUp();
            return;
        }

        User user = requireActivity().getIntent().getParcelableExtra("USER_PROFILE");

        if (user == null) {
            Bundle receiveBundle = this.getArguments();
            assert receiveBundle != null;
            user = receiveBundle.getParcelable("USER_PROFILE");
        }
        String description = descET.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm aa", Locale.CANADA);
        Date date;

        try {
            date = format.parse(dateET.getText().toString() + " " +
                    timeET.getText().toString());
        } catch (Exception e) {
            // Replace handleError with the error popup
            errorPopUp();
            return;
        }

        Integer socialCondition = getSelectedSocialCondition(socialSpinner.getSelectedItem().toString());

        MoodEvent moodEvent = new MoodEvent(mood.getMoodType(), user.getUid(), description, date,
                socialCondition, originalPhotoURL, location);
        if (isEdit) {
            moodEvent.setMoodId(editMoodId);
        }

        doneButton.setEnabled(false);
        firebaseHelper.addMoodEvent(moodEvent, moodEventImage,
                new FirebaseHelper.FirebaseCallback<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        Toast.makeText(getContext(), "Successfully saved event",
                                Toast.LENGTH_SHORT).show();
                        closeFragment();
                    }

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        doneButton.setEnabled(true);
                        handleError("Failed to save event");
                    }
                });
    }


    /**
     * When this function is called
     * an error popup will appear on the screen
     */
    private void errorPopUp() {
        new AlertDialog.Builder(mActivity)
                .setTitle("Oops")
                .setMessage("Please choose an emotional state or enter a date and time")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Go back without changing anything
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }


    /**
     * Shows an error toast
     *
     * @param message Error message to show
     */
    private void handleError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param moodId moodId of the mood selected
     * @return Return the id of mood in emotion radio group
     */
    // TODO: TEST
    public int getEmotionRadioId(@Mood.MoodType int moodId) {
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
     * @return Return the mood chosen by the RadioGroup
     */
    // TODO: TEST
    public Mood getSelectedMood(int id) {
        switch (id) {
            case R.id.icon_neutral:
                return new Mood(Mood.NEUTRAL);
            case R.id.icon_happy:
                return new Mood(Mood.HAPPY);
            case R.id.icon_angry:
                return new Mood(Mood.ANGRY);
            case R.id.icon_disgusted:
                return new Mood(Mood.DISGUSTED);
            case R.id.icon_sad:
                return new Mood(Mood.SAD);
            case R.id.icon_scared:
                return new Mood(Mood.SCARED);
            case R.id.icon_surprised:
                return new Mood(Mood.SURPRISED);
            default:
                return null;
        }
    }

    /**
     * Returns the selected social condition
     *
     * @param socialCondition Selected social condition from the dropdown (spinner)
     * @return Returns an integer that corresponds to the selected social condition
     */
    // TODO: TEST
    public Integer getSelectedSocialCondition(String socialCondition) {
        switch (socialCondition) {
            case "Single":
                return MoodEvent.SocialCondition.SINGLE;
            case "Pair":
                return MoodEvent.SocialCondition.PAIR;
            case "Small Group":
                return MoodEvent.SocialCondition.SMALL_GROUP;
            case "Crowd":
                return MoodEvent.SocialCondition.CROWD;
            default:
                return null;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        radioGroupOnClick(group);
    }

    /**
     * RadioGroup Click Handler
     *
     * @param group RadioGroup in the AddPostFragment
     */
    private void radioGroupOnClick(RadioGroup group) {
        // Get selected radio button Id from radio group
        int selectedId = group.getCheckedRadioButtonId();

        /*
         * Loop through the radio buttons in radio group
         * Find the one that's selected and make it darker.
         * If not, make it white
         */
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton currentButton = (RadioButton) group.getChildAt(i);

            if (currentButton.getId() == selectedId) {
                // Make the selectedButton darker, to show that it is Selected
                currentButton.getBackground().setColorFilter(ContextCompat.getColor(mActivity, R.color.selected_color), PorterDuff.Mode.MULTIPLY);
            } else {
                // Make the unselected buttons white
                currentButton.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            }
        }
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

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }
}
