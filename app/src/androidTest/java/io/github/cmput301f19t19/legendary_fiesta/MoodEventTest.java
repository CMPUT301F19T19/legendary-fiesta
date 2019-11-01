package io.github.cmput301f19t19.legendary_fiesta;

import android.os.Parcel;
import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.runner.RunWith;

import java.util.Date;

@RunWith(AndroidJUnit4.class)
public class MoodEventTest {
    private MoodEvent moodEvent;

    private Mood mood = new Mood(Mood.NEUTRAL);
    private String user = "TestUser";
    private String description = "TestDescription";
    private Date date = new Date();
    private Integer condition = MoodEvent.SocialCondition.CROWD;
    private String photoURL = "https://example.com/photo.jpg";
    private LatLng location = new LatLng(100, 100);
    private static final FirebaseHelper firebaseHelper = new FirebaseHelper(FirebaseApp.getInstance());


    @Before
    public void setUp() {
        moodEvent = new MoodEvent(mood, user, description, date, condition, photoURL, location);
        firebaseHelper.addMoodEvent(moodEvent, new FirebaseHelper.FirebaseCallback<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentSnapshots) {
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FeelsLog", "onFailure: ");
            }
        });
    }

    @Test
    public void parcelTest() {
        Parcel parcel = Parcel.obtain();
        moodEvent.writeToParcel(parcel, 0);

        MoodEvent deFrosted = MoodEvent.CREATOR.createFromParcel(parcel);

        Assert.assertEquals(deFrosted.getUser(), user);
        Assert.assertEquals(deFrosted.getMood().getMoodType(), mood.getMoodType());
        Assert.assertEquals(deFrosted.getDescription(), description);
        Assert.assertEquals(deFrosted.getDate(), date);
        Assert.assertEquals(deFrosted.getPhotoURL(), photoURL);
        Assert.assertEquals(deFrosted.getCondition(), condition);
        Assert.assertEquals(deFrosted.getLocation(), location);
    }
}
