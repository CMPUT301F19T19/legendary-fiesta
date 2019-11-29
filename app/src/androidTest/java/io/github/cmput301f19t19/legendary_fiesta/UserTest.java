package io.github.cmput301f19t19.legendary_fiesta;

import android.os.Parcel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.runner.RunWith;

import java.util.Date;

@RunWith(AndroidJUnit4.class)
public class UserTest {
    private User user;
    private String username = "TestUsername";
    private Date birthDate = new Date();
    private String description = "TestDescription";

    @Before
    public void setUp() {
        user = new User(username, birthDate, description);
    }

    @Test
    public void parcelTest() {
        Parcel parcel = Parcel.obtain();
        user.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        User deFrost = User.CREATOR.createFromParcel(parcel);
        Assert.assertEquals(deFrost.getUsername(), username);
        Assert.assertEquals(deFrost.getBirthDate(), birthDate);
        Assert.assertEquals(deFrost.getDescription(), description);
    }
}
