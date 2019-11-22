package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.github.cmput301f19t19.legendary_fiesta.FirebaseHelper;
import io.github.cmput301f19t19.legendary_fiesta.MainActivity;
import io.github.cmput301f19t19.legendary_fiesta.R;
import io.github.cmput301f19t19.legendary_fiesta.User;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    public static final int DATE_REQUEST_CODE = 66;

    private EditText userEditText;
    private EditText birthDateEditText;
    private EditText bioEditText;

    private Button cancelButton;
    private Button doneButton;

    // Fragment view
    private View mView;
    private Activity mActivity;

    private NavController navController;

    private User user;

    // FireBase Helper
    private static final FirebaseHelper firebaseHelper = new FirebaseHelper(FirebaseApp.getInstance());

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_profile, container, false);

        user = requireActivity().getIntent().getParcelableExtra("USER_PROFILE");

        if(user == null){
            Bundle receiveBundle = this.getArguments();
            assert receiveBundle != null;
            user = receiveBundle.getParcelable("USER_PROFILE");
        }

        // Buttons
        cancelButton = mView.findViewById(R.id.cancel_button);
        doneButton = mView.findViewById(R.id.done_button);

        // EditText
        userEditText = mView.findViewById(R.id.search_friends_edittext);
        birthDateEditText = mView.findViewById(R.id.birthEditText);
        bioEditText = mView.findViewById(R.id.bioEditText);

        // set listener to OnClick defined this class
        userEditText.setOnClickListener(this);
        bioEditText.setOnClickListener(this);
        birthDateEditText.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);

        userEditText.setText(user.getUsername());
        bioEditText.setText(user.getDescription());

        Format f = new SimpleDateFormat("MM/dd/yyyy", Locale.CANADA);
        birthDateEditText.setText(f.format(user.getBirthDate()));

        try {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        }catch (IllegalArgumentException e){
            Log.d("Error", "Illegal Argument for Navigation.findNavController, Message: " + e);
        }

        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // get reference to associated activity
        mActivity = (Activity) context;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Check for the results
        if (requestCode == DATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Get date from string
            String selectedDate = data.getStringExtra("SELECTED_DATE");
            // Set date EditText to the selected date
            birthDateEditText.setText(selectedDate);
        }
    }


    /**
     * Click handler for ProfileFragment and switches based on what is clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        /*
         * Perform different onClick action depending on what was click. Determined by comparing view ID
         */
        switch (view.getId()) {
            case R.id.birthEditText:
                // Create DatePickerFragment
                DialogFragment dateFragment = new DatePickerFragment();
                // Set the target fragment to receive the results and specifying the result code
                dateFragment.setTargetFragment(ProfileFragment.this, DATE_REQUEST_CODE);
                // Show DatePickerFragment
                Bundle args = new Bundle();
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.CANADA);
                Date date = null;
                try {
                    date = format.parse(birthDateEditText.getText().toString());
                } catch (Exception e) {
                    Log.e("FeelsLog", "onFailure: ");
                    return;
                }
                args.putInt(DatePickerFragment.DATE_TAG, date.getDate());
                args.putInt(DatePickerFragment.MONTH_TAG, date.getMonth());
                args.putInt(DatePickerFragment.YEAR_TAG, date.getYear() + 1900);

                dateFragment.setArguments(args);
                dateFragment.show(getFragmentManager(), "DatePicker");
                break;
            case R.id.cancel_button:
                closeFragment();
                break;
            case R.id.done_button:
                onDoneClicked();
                break;
        }
    }

    /**
     * Resets the fragment and navigate to 'Own List Fragment'
     */
    private void closeFragment() {
        userEditText.setText("");
        birthDateEditText.setText("");
        bioEditText.setText("");

        if (navController != null)
            navController.navigate(R.id.navigation_own_list);
    }

    /**
     * Attempts to save mood event on FireBase
     */
    private void onDoneClicked() {
        firebaseHelper.checkUserExists(userEditText.getText().toString(), new FirebaseHelper.FirebaseCallback<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot document) {
                if (!userEditText.getText().toString().equals(user.getUsername()) && !document.isEmpty()) {
                    new AlertDialog.Builder(mActivity)
                        .setTitle("Username taken!")
                        .setMessage("Please try a different username")
                        .setPositiveButton("Ok", (dialogInterface, i) -> {
                            //Go back without changing anything
                            dialogInterface.dismiss();
                        })
                        .create()
                        .show();
                } else {
                    String bio = bioEditText.getText().toString();
                    String username = userEditText.getText().toString();

                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.CANADA);
                    Date date = null;
                    try {
                        date = format.parse(birthDateEditText.getText().toString());
                    } catch (Exception e) {
                        Log.e("FeelsLog", "onFailure: ", e);
                        return;
                    }

                    User updatedUser = new User(username, date, bio);
                    updatedUser.setUid(user.getUid());
                    firebaseHelper.addUser(updatedUser,
                            new FirebaseHelper.FirebaseCallback<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference doc) {
                                    Toast.makeText(getContext(), "Successfully updated user details",
                                            Toast.LENGTH_SHORT).show();
                                    mActivity.setIntent(
                                            new Intent(mActivity, MainActivity.class)
                                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("USER_PROFILE", updatedUser)
                                    );
                                    closeFragment();
                                }

                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Failed to update user details, try again",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                }
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FeelsLog", "onFailure: ", e);
            }
        });
    }


}
