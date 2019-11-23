package io.github.cmput301f19t19.legendary_fiesta;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A helper class for Firebase operations
 */
public class FirebaseHelper {
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    /**
     * @param app the FirebaseApp instance, you can use `FirebaseApp.getInstance()` to get one
     *            when you are inside an Activity
     */
    public FirebaseHelper(FirebaseApp app) {
        db = FirebaseFirestore.getInstance(app);
        storage = FirebaseStorage.getInstance(app);
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
        db.collection("users").whereEqualTo("username", name).get()
                .addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * check if there is already a friend request concerning the two users in either direction
     *
     * @param oneUID User A's UID
     * @param anotherUID User B's UID
     * @param callback
     */
    public void checkRequestExists(String oneUID, String anotherUID, final FirebaseCallback<Boolean> callback) {
        String[] UIDs = {oneUID, anotherUID};
        if (oneUID.equals(anotherUID)) {
            callback.onFailure(new Exception("Cannot send yourself friend requests"));
            return;
        }
        db.collection("requests").whereIn("from", Arrays.asList(UIDs)).whereIn("to", Arrays.asList(UIDs))
            .get()
                .addOnSuccessListener(queryDocumentSnapshots -> callback.onSuccess(!queryDocumentSnapshots.isEmpty()))
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * add new friend request to the database
     *
     * @param friendRequest friend request to be added
     * @param callback
     */
    public void sendFriendRequest(FriendRequest friendRequest, final FirebaseCallback<Void> callback) {
        db.collection("requests").document().set(friendRequest)
                .addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * flip the friend request (either approve or deny)
     *
     * @param fromUID
     * @param toUID
     * @param approve
     */
    public void flipFriendRequest(String fromUID, String toUID, boolean approve, final FirebaseCallback<Void> callback) {
        db.collection("requests").whereEqualTo("from", fromUID).whereEqualTo("to", toUID).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                    DocumentSnapshot request;
                    // there should be only one request for each fromUID <-> toUID
                    if (documentSnapshots.size() != 1) {
                        callback.onFailure(new Exception("Unexpected number of requests: "));
                        return;
                    }
                    request = documentSnapshots.get(0);
                    // request shouldn't be null
                    assert request != null;
                    if (!approve) {
                        // if it's reject, we then remove the request
                        db.collection("requests").document(request.getId()).delete()
                                .addOnFailureListener(callback::onFailure)
                                .addOnSuccessListener(callback::onSuccess);
                        return;
                    }
                    // approve: update the status to true
                    // TODO: add the followed user
                    db.collection("requests").document(request.getId()).update("status", true)
                        .addOnSuccessListener(aVoid -> db.collection("users").document(toUID))
                        .addOnFailureListener(callback::onFailure);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void finishFriendRequest(String myUID, final FirebaseCallback<Void> callback) {
        // TODO:
    }


    public void getAllUsers(final FirebaseCallback<QuerySnapshot> callback) {
        db.collection("users").get()
                .addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }


    /**
     * @param uid      UID generated and tracked by Firebase
     * @param callback callback callback, called when the query finishes, needs to be of type FirebaseCallback<DocumentSnapshot>
     */
    public void getUserByUID(String uid, final FirebaseCallback<DocumentSnapshot> callback) {
        db.collection("users").document(uid).get()
                .addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * @param callback callback callback, called when the query finishes, needs to be of type FirebaseCallback<DocumentSnapshot>
     */
    public void getAllUsers(final FirebaseCallback<QuerySnapshot> callback) {
        db.collection("users")
                .orderBy("username")
                .get()
                .addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * add an user to the database
     *
     * @param user     user to be added
     * @param callback callback, called when the query finishes, needs to be of type FirebaseCallback<DocumentReference>
     */
    public void addUser(User user, final FirebaseCallback<DocumentReference> callback) {
        db.collection("users").document(user.getUid()).set(user)
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * add a mood event to the database
     *
     * @param moodEvent MoodEvent to be added
     * @param photo     photo to be attached
     * @param callback  callback, called when the query finishes, needs to be of type FirebaseCallback<Void>
     */
    public void addMoodEvent(MoodEvent moodEvent, @Nullable byte[] photo, final FirebaseCallback<Void> callback) {
        if (photo != null) {
            uploadImages(moodEvent.getMoodId(), photo, new FirebaseCallback<Uri>() {
                @Override
                public void onSuccess(Uri document) {
                    moodEvent.setPhotoURL(document.toString());
                    addMoodEvent(moodEvent, callback);
                }

                @Override
                public void onFailure(@NonNull Exception e) {
                    callback.onFailure(e);
                }
            });
        } else {
            addMoodEvent(moodEvent, callback);
        }
    }

    private void addMoodEvent(MoodEvent moodEvent, final FirebaseCallback<Void> callback) {
        db.collection("moodEvents").document(moodEvent.getMoodId())
                .set(moodEvent)
                .addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * get mood events by a user
     *
     * @param uid      User's UserID
     * @param callback callback, called when the query finishes, needs to be of type FirebaseCallback<QuerySnapshot>
     */
    public void getMoodEventsById(String uid, final FirebaseCallback<QuerySnapshot> callback) {
        db.collection("moodEvents")
            .whereEqualTo("user", uid)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener(callback::onSuccess)
            .addOnFailureListener(callback::onFailure);
    }

    /**
     * get friend's mood events
     *
     * @param uids User's UserIDs
     * @param callback  callback, called when the query finishes, needs to be of type FirebaseCallback<QuerySnapshot>
     */
    public void getFriendsMoodEvents(ArrayList<String> uids, final FirebaseCallback<QuerySnapshot> callback) {
        db.collection("moodEvents")
                .whereIn("user", uids)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * delete mood event by ID
     *
     * @param moodId   MoodEvent ID
     * @param callback callback, called when the query finishes, needs to be of type FirebaseCallback<Void>
     */
    public void deleteMoodEventById(String moodId, final FirebaseCallback<Void> callback) {
        db.collection("moodEvents").document(moodId)
                .delete()
                .addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * upload files to the firestore storage
     *
     * @param filename filename on the firestore storage
     * @param data     data to be uploaded
     * @param callback callback of type FirebaseCallback<Uri>, called when upload and public sharing link creation
     *                 has finished, returns public URL to the uploaded file
     */
    private void uploadImages(String filename, byte[] data, final FirebaseCallback<Uri> callback) {
        StorageReference storageReference = storage.getReference().child(String.format("moodEvents/%s.jpg", filename));
        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl()
                .addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure)).addOnFailureListener(callback::onFailure);
    }

}
