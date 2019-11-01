package io.github.cmput301f19t19.legendary_fiesta;


import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;


public class MoodEventTest {

    private MoodEvent moodEvent;
    private Date date = new Date();
    private Mood mood = new Mood(Mood.NEUTRAL);
    private String user = "TestUser";
    private String description = "TestDescription";
    private Integer condition = MoodEvent.SocialCondition.SINGLE;
    private String photoURL = "https://example.com/photo.jpg";
    private LatLng location = new LatLng(100, 100);

    @Before
    public void setUp() {
        moodEvent = new MoodEvent(mood, user, description, date,
                condition, photoURL, location);
    }

    @Test
    public void getMoodTest() {
        Assert.assertEquals(mood, moodEvent.getMood());
    }

    @Test
    public void setMoodTest() {
        Assert.assertEquals(mood, moodEvent.getMood());
        Mood happyMood = new Mood(Mood.HAPPY);
        moodEvent.setMood(happyMood);
        Assert.assertEquals(happyMood, moodEvent.getMood());
    }

    @Test
    public void getUserTest() {
        Assert.assertEquals(user, moodEvent.getUser());
    }

    @Test
    public void getDescriptionTest() {
        Assert.assertEquals(description, moodEvent.getDescription());
    }

    @Test
    public void setDescriptionTest() {
        Assert.assertEquals(description, moodEvent.getDescription());
        String newDescription = "NewTestDescription";
        moodEvent.setDescription(newDescription);
        Assert.assertEquals(newDescription, moodEvent.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setDescriptionTooLongTest() {
        moodEvent.setDescription("123456789012345678901"); // 21 Chars long
    }

    @Test
    public void getDateTest() {
        Assert.assertEquals(date, moodEvent.getDate());
    }

    @Test
    public void setDateTest() {
        Assert.assertEquals(date, moodEvent.getDate());
        Date newDate = new Date(0);
        moodEvent.setDate(newDate);
        Assert.assertEquals(newDate, moodEvent.getDate());
    }

    @Test
    public void getConditionTest() {
        Assert.assertEquals(condition, moodEvent.getCondition());
    }

    @Test
    public void setConditionTest() {
        Assert.assertEquals(condition, moodEvent.getCondition());
        Integer newCondition = MoodEvent.SocialCondition.CROWD;
        moodEvent.setCondition(newCondition);
        Assert.assertEquals(newCondition, moodEvent.getCondition());
    }

    @Test
    public void getPhotoURLTest() {
        Assert.assertEquals(photoURL, moodEvent.getPhotoURL());
    }

    @Test
    public void setPhotoURLTest() {
        Assert.assertEquals(photoURL, moodEvent.getPhotoURL());
        String newPhoto = "https://example.com/moodEvents/photo.jpg";
        moodEvent.setPhotoURL(newPhoto);
        Assert.assertEquals(newPhoto, moodEvent.getPhotoURL());
    }

    @Test
    public void getLocationTest() {
        Assert.assertEquals(location, moodEvent.getLocation());
    }

    @Test
    public void setLocationTest() {
        Assert.assertEquals(location, moodEvent.getLocation());
        LatLng newLocation = new LatLng(-150, -150);
        moodEvent.setLocation(newLocation);
        Assert.assertEquals(newLocation, moodEvent.getLocation());
    }

    @Test
    public void saveTest() {
        // TODO: write the test that checks this
    }
}