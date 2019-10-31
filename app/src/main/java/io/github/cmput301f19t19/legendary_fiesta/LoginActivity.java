package io.github.cmput301f19t19.legendary_fiesta;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 2;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.EmailBuilder().build()
    );
    private static final FirebaseHelper firebaseHelper = new FirebaseHelper(FirebaseApp.getInstance());
    private String uid;
    private View progressOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        View button = findViewById(R.id.btn_sign_in);
        progressOverlay = findViewById(R.id.progress_overlay);
        showLogin(button);
    }

    private void launchMainActivity(User profile) {
        // re-enable the sign in button
        View button = findViewById(R.id.btn_sign_in);
        showProgressOverlay(false);
        button.setEnabled(true);
        // Parse user information to the MainActivity
        startActivity(
                new Intent(this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("USER_PROFILE", profile)
        );
    }

    /**
     * show/hide the sign up form
     *
     * @param show whether to show the sign up form
     */
    private void showSignup(boolean show) {
        View signup = findViewById(R.id.login_signup_form);
        View signupButton = findViewById(R.id.btn_sign_in);
        showProgressOverlay(false);
        signup.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        signupButton.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
    }

    private void showProgressOverlay(boolean show) {
        progressOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * validate and add new user to the database
     *
     * @param v submit button
     */
    public void addNewUser(View v) {
        final View submit_button = findViewById(R.id.login_signup);
        EditText name = findViewById(R.id.login_username);
        EditText birthDate = findViewById(R.id.login_dob);
        EditText bio = findViewById(R.id.login_bio);
        final User user; // to use the User object in a callback, it needs to be `final`
        final String userName = name.getText().toString();
        final String description = bio.getText().toString();
        submit_button.setEnabled(false);
        showProgressOverlay(true);
        String birthDateString = birthDate.getText().toString();
        Date birth = null;

        if (!birthDateString.isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.CANADA);
            try {
                birth = dateFormat.parse(birthDateString);
            } catch (ParseException ignored) {
                submit_button.setEnabled(true);
                Toast.makeText(this, R.string.login_invalid_date, Toast.LENGTH_LONG).show();
            }
        }

        user = new User(userName, birth, description);
        user.setUid(uid);

        firebaseHelper.checkUserExists(userName, new FirebaseHelper.FirebaseCallback<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot document) {
                submit_button.setEnabled(true);
                showProgressOverlay(false);
                if (!document.isEmpty()) {
                    // An user with the specified username already exists
                    Log.d("FeelsLog", "addNewUser: duplicates");
                } else {
                    firebaseHelper.addUser(user, new FirebaseHelper.FirebaseCallback<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference document) {
                            showSignup(false);
                            launchMainActivity(user);
                        }

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("FeelsLog", "onFailure: ");
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FeelsLog", "onFailure: ", e);
            }
        });
    }

    /**
     * show the login page
     *
     * @param v sign in button
     */
    public void showLogin(View v) {
        v.setEnabled(false);
        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.LoginBackground)
                        .setLogo(R.drawable.logo)
                        .build(),
                RC_SIGN_IN);
    }

    public void selectDate(View v) {
        final EditText editText = (EditText) v;
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                editText.setText(
                        String.format(Locale.ENGLISH, "%s-%02d-%02d", year, month + 1, dayOfMonth)
                );
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            View button = findViewById(R.id.btn_sign_in);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                showProgressOverlay(true);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {  // error when getting the user instance
                    button.setEnabled(true);
                    Toast.makeText(this, getString(R.string.login_error_toast, -1), Toast.LENGTH_LONG);
                    return;
                }
                uid = user.getUid();
                // check if the user already registered
                firebaseHelper.getUserByUID(uid, new FirebaseHelper.FirebaseCallback<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        if (document.exists()) {
                            // if yes, then redirect to the main page
                            launchMainActivity(document.toObject(User.class));
                        } else {
                            // if no, time to take out the sign up form
                            showSignup(true);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // TODO: better error handling
                        Log.e("FeelsLog", "onFailure: ", e);
                    }
                });
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                button.setEnabled(true);
                if (response != null) {
                    Toast.makeText(this, getString(R.string.login_error_toast, response.getError().getErrorCode()), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, R.string.login_error_cancelled, Toast.LENGTH_LONG).show();
                }

            }
        }
    }
}
