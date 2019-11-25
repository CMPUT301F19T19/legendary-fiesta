package io.github.cmput301f19t19.legendary_fiesta.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import io.github.cmput301f19t19.legendary_fiesta.User;

/*
Acts as an Empty container to contain a fragment. Used in fragment testing
 */
public class FragmentEmptyClass extends AppCompatActivity {

    String uid = "TPgV90AKmsZM33xpgblYQYH6Abh1";
    User user;
    FirebaseFirestore db;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();

        //Initialize a linear layout to contain the empty fragment
        LinearLayout view = new LinearLayout(this);

        //set the id to 1 as example, used to identify
        view.setId(1);
        user = injectUser(uid);

        setContentView(view);
    }

    public User injectUser(String uid) {
        db.collection("users").document(uid).get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot document = task.getResult();
                    user = document.toObject(User.class);
                });
        return user;
    }

    public User getTestUser() {
        return user;
    }

}
