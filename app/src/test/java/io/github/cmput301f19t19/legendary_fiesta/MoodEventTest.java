package io.github.cmput301f19t19.legendary_fiesta;


import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.LatLng;

import io.github.cmput301f19t19.legendary_fiesta.ui.ProxyLatLng;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;


public class MoodEventTest {

    private MoodEvent moodEvent;

    private @Mood.MoodType Integer moodType = Mood.NEUTRAL;
    private String user = "TestUser";
    private Date date = new Date();
    private String description = "TestDescription";
    private Integer condition = MoodEvent.SocialCondition.SINGLE;
    private String photoURL = "https://example.com/photo.jpg";
    private ProxyLatLng location = new ProxyLatLng(100, 100);

    @Before
    public void setUp() {
        moodEvent = new MoodEvent(moodType, user, description, date,
                condition, photoURL, location);
    }

    @Test
    public void getMoodTypeTest() {
        Assert.assertEquals(moodType, moodEvent.getMoodType());
    }

    @Test
    public void setMoodTypeTest() {
        Assert.assertEquals(moodType, moodEvent.getMoodType());
        @Mood.MoodType Integer happyMood = Mood.HAPPY;
        moodEvent.setMood(happyMood);
        Assert.assertEquals(happyMood, moodEvent.getMoodType());
    }

    @Test
    public void getUserTest() {
        Assert.assertEquals(user, moodEvent.getUser());
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
        ProxyLatLng newLocation = new ProxyLatLng(-150, -150);
        moodEvent.setLocation(newLocation);
        Assert.assertEquals(newLocation, moodEvent.getLocation());
    }

    @Test
    public void locationConvertTest() {
        LatLng location = new LatLng(123.111, 456.222);
        ProxyLatLng converted = new ProxyLatLng(location);
        Assert.assertEquals(location.latitude, converted.latitude, 0.0d);
        Assert.assertEquals(location.longitude, converted.longitude, 0.0d);
    }
}