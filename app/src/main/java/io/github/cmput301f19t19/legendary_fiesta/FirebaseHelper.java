package io.github.cmput301f19t19.legendary_fiesta;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A helper class for Firebase operations
 */
public class FirebaseHelper {
    private FirebaseFirestore db;

    /**
     * @param app the FirebaseApp instance, you can use `FirebaseApp.getInstance()` to get one
     *            when you are inside an Activity
     */
    public FirebaseHelper(FirebaseApp app) {
        db = FirebaseFirestore.getInstance(app);
    }

    public interface FirebaseCallback<T> {
        /**
         * callback handler for handling success cases, returned the value is stored in `document` variable
         *
         * @param document depending on the operation, this variable can hold different type of values
         */
        void onSuccess(T document);

        /**
         * callback handler for handling failure cases
         *
         * @param e Exception thrown by the Firebase library
         */
        void onFailure(@NonNull Exception e);
    }

    /**
     * check if the username has already been taken
     *
     * @param name     the username to check
     * @param callback callback, called when the query finishes, needs to be of type FirebaseCallback<QuerySnapshot>
     */
    public void checkUserExists(String name, final FirebaseCallback<QuerySnapshot> callback) {
        db.collection("users").whereEqualTo("username", name).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                callback.onSuccess(queryDocumentSnapshots);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e);
            }
        });
    }

    /**
     * @param uid      UID generated and tracked by Firebase
     * @param callback callback callback, called when the query finishes, needs to be of type FirebaseCallback<DocumentSnapshot>
     */
    public void getUserByUID(String uid, final FirebaseCallback<DocumentSnapshot> callback) {
        db.collection("users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                callback.onSuccess(documentSnapshot);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e);
            }
        });
    }

    /**
     * add an user to the database
     *
     * @param user     user to be added
     * @param callback callback, called when the query finishes, needs to be of type FirebaseCallback<DocumentReference>
     */
    public void addUser(User user, final FirebaseCallback<DocumentReference> callback) {
        db.collection("users").document(user.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onSuccess(null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e);
            }
        });
    }

    /**
     * add a mood event to the database
     *
     * @param moodEvent MoodEvent to be added
     * @param callback  callback, called when the query finishes, needs to be of type FirebaseCallback<DocumentReference>
     */
    public void addMoodEvent(MoodEvent moodEvent, final FirebaseCallback<DocumentReference> callback) {
        db.collection("moodEvents").add(moodEvent).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                callback.onSuccess(documentReference);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e);
            }
        });
    }

}
