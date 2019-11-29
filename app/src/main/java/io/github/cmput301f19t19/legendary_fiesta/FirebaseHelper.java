package io.github.cmput301f19t19.legendary_fiesta;

/*
 * FireBaseHelper is a helper class to for FireBase Operations.
 */

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A helper class for FireBase operations
 */
public class FirebaseHelper {
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    /**
     * @param app the FireBaseApp instance, you can use `FireBaseApp.getInstance()` to get one
     *            when you are inside an Activity
     */
    public FirebaseHelper(FirebaseApp app) {
        db = FirebaseFirestore.getInstance(app);
        storage = FirebaseStorage.getInstance(app);
    }

    /**
     * check if the username has already been taken
     *
     * @param name     the username to check
     * @param callback Called when the query finishes, needs to be of type FirebaseCallback<QuerySnapshot>
     */
    public void checkUserExists(String name, final FirebaseCallback<QuerySnapshot> callback) {
        db.collection("users").whereEqualTo("username", name).get()
                .addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * Check if there is already a friend request concerning the two users
     *
     * @param fromUID  Requester
     * @param toUID    Requestee
     * @param callback callback, called when the query finishes, needs to be of type FirebaseCallback<Boolean>
     */
    public void checkRequestExists(String fromUID, String toUID, final FirebaseCallback<Boolean> callback) {
        if (fromUID.equals(toUID)) {
            callback.onFailure(new Exception("Cannot send yourself friend requests"));
            return;
        }
        db.collection("requests").whereEqualTo("from", fromUID).whereEqualTo("to", toUID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> callback.onSuccess(!queryDocumentSnapshots.isEmpty()))
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * get all the pending requests for the specified user
     *
     * @param UID      Current user
     * @param callback Callback, called when the query finishes, needs to be of type FirebaseCallback<List<FriendRequest>>
     */
    public void getPendingRequests(String UID, final FirebaseCallback<List<FriendRequest>> callback) {
        db.collection("requests").whereEqualTo("to", UID).whereEqualTo("status", false)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<FriendRequest> requests = new ArrayList<>();
            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                requests.add(doc.toObject(FriendRequest.class));
            }
            callback.onSuccess(requests);
        }).addOnFailureListener(callback::onFailure);
    }

    /**
     * Add new friend request to the database
     *
     * @param fromUID  Requester
     * @param toUID    Requestee
     * @param callback Called when the query finishes, needs to be of type FirebaseCallback<Void>
     */
    public void sendFriendRequest(String fromUID, String toUID, final FirebaseCallback<Void> callback) {
        FriendRequest friendRequest = new FriendRequest(new Date(), fromUID, false, toUID);
        db.collection("requests").document().set(friendRequest)
                .addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * Flip the friend request (either approve or deny)
     *
     * @param fromUID  Requester
     * @param toUser   Requestee
     * @param approve  Approve or deny request
     * @param callback Called when the query finishes, needs to be of type FirebaseCallback<Void>
     */
    public void flipFriendRequest(String fromUID, User toUser, boolean approve, final FirebaseCallback<Void> callback) {
        db.collection("requests").whereEqualTo("from", fromUID).whereEqualTo("to", toUser.getUid())
                .get()
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
                    db.collection("requests").document(request.getId()).update("status", true)
                            .addOnSuccessListener(aVoid -> {
                                toUser.acceptFollowRequest(fromUID);
                                db.collection("users").document(toUser.getUid())
                                        .update("followedBy", FieldValue.arrayUnion(fromUID))
                                        .addOnSuccessListener(callback::onSuccess)
                                        .addOnFailureListener(callback::onFailure);
                            })
                            .addOnFailureListener(callback::onFailure);
                })
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * Finish the friend request "hand-shaking" process
     *
     * @param myUser   Current user
     * @param callback Callback, called when the query finishes, needs to be of type FirebaseCallback<Void>
     */
    public void finishFriendRequest(User myUser, final FirebaseCallback<Void> callback) {
        db.collection("requests").whereEqualTo("from", myUser.getUid()).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<DocumentReference> pendingDeletion = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        FriendRequest request = documentSnapshot.toObject(FriendRequest.class);
                        if (request != null && request.getStatus()) {
                            myUser.finishFollowing(request.getTo());
                            pendingDeletion.add(documentSnapshot.getReference());
                        }
                    }
                    db.runBatch(writeBatch -> {
                        // Delete all the completed requests
                        for (DocumentReference reference : pendingDeletion) {
                            writeBatch.delete(reference);
                        }
                    }).addOnSuccessListener(Void -> db.collection("users").document(myUser.getUid())
                            .update("following", FieldValue.arrayUnion(myUser.getFollowing().toArray()))
                            .addOnSuccessListener(callback::onSuccess) // return with success
                            .addOnFailureListener(callback::onFailure));
                })
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * Un-follows the toUser by the myUser
     *
     * @param fromUID  Follower
     * @param toUID    Followee
     * @param callback Callback, called when the query finishes, needs to be of type FirebaseCallback<Void>
     */
    public void unfollowUser(String fromUID, String toUID, final FirebaseCallback<Void> callback) {
        db.collection("users").document(fromUID)
                .update("following", FieldValue.arrayRemove(toUID))
                .addOnSuccessListener(Void -> {
                    db.collection("users").document(toUID)
                            .update("followedBy", FieldValue.arrayRemove(fromUID))
                            .addOnSuccessListener(callback::onSuccess)
                            .addOnFailureListener(callback::onFailure);
                })
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * Get the user with specific UID
     *
     * @param uid      UID generated and tracked by FireBase
     * @param callback Callback, called when the query finishes, needs to be of type FirebaseCallback<DocumentSnapshot>
     */
    public void getUserByUID(String uid, final FirebaseCallback<DocumentSnapshot> callback) {
        db.collection("users").document(uid).get()
                .addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * Get all the users from the database
     *
     * @param callback Callback, Called when the query finishes, needs to be of type FirebaseCallback<DocumentSnapshot>
     */
    public void getAllUsers(final FirebaseCallback<QuerySnapshot> callback) {
        db.collection("users")
                .orderBy("username")
                .get()
                .addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * Add an user to the database, can also be an update action
     *
     * @param user     user to be added or updated
     * @param callback Callback, called when the query finishes, needs to be of type FirebaseCallback<DocumentReference>
     */
    public void addUser(User user, final FirebaseCallback<DocumentReference> callback) {
        db.collection("users").document(user.getUid()).set(user)
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * update the user information in the database on demand
     *
     * @param user     user to be updated
     * @param callback callback, called when the query finishes, needs to be of type FirebaseCallback<DocumentReference>
     */
    public void updateUser(User user, final FirebaseCallback<DocumentSnapshot> callback) {
        db.collection("users").document(user.getUid())
                .update("username", user.getUsername(),
                        "birthDate", user.getBirthDate(),
                        "description", user.getDescription())
                .addOnSuccessListener(Void -> {
                    db.collection("users").document(user.getUid()).get()
                            .addOnSuccessListener(callback::onSuccess)
                            .addOnFailureListener(callback::onFailure);
                })
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * add a mood event to the database
     *
     * @param moodEvent MoodEvent to be added
     * @param photo     Photo to be attached
     * @param callback  Callback, called when the query finishes, needs to be of type FirebaseCallback<Void>
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

    /**
     * Add MoodEvent to FireBase
     *
     * @param moodEvent MoodEvent to be added
     * @param callback  Callback, called called when the query finishes, needs to be of type FirebaseCallback<Void>
     */
    private void addMoodEvent(MoodEvent moodEvent, final FirebaseCallback<Void> callback) {
        db.collection("moodEvents").document(moodEvent.getMoodId())
                .set(moodEvent)
                .addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * Get mood events by a user
     *
     * @param uid      User's UserID
     * @param callback Callback, called when the query finishes, needs to be of type FirebaseCallback<QuerySnapshot>
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
     * Get friend's mood events
     *
     * @param uids     User's UserIDs
     * @param callback Callback, called when the query finishes, needs to be of type FirebaseCallback<QuerySnapshot>
     */
    public void getFriendsMoodEvents(ArrayList<String> uids, final FirebaseCallback<QuerySnapshot> callback) {
        for (String uid : uids) {
            db.collection("moodEvents")
                    .whereEqualTo("user", uid)
                    .orderBy("date", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .addOnSuccessListener(callback::onSuccess)
                    .addOnFailureListener(callback::onFailure);
        }
    }

    /**
     * Delete mood event by ID
     *
     * @param moodId   MoodEvent ID
     * @param callback Callback, called when the query finishes, needs to be of type FirebaseCallback<Void>
     */
    public void deleteMoodEventById(String moodId, final FirebaseCallback<Void> callback) {
        db.collection("moodEvents").document(moodId)
                .delete()
                .addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * Upload files to the FireStore storage
     *
     * @param filename filename on the FireStore storage
     * @param data     Data to be uploaded
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

    public interface FirebaseCallback<T> {
        /**
         * Callback handler for handling success cases, returned the value is stored in `document` variable
         *
         * @param document depending on the operation, this variable can hold different type of values
         */
        void onSuccess(T document);

        /**
         * Callback handler for handling failure cases
         *
         * @param e Exception thrown by the FireBase library
         */
        void onFailure(@NonNull Exception e);
    }

}
