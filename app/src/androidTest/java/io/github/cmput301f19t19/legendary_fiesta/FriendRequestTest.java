package io.github.cmput301f19t19.legendary_fiesta;

import android.os.Parcel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;


public class FriendRequestTest {

    private FriendRequest friendRequest;

    private Date date = new Date();
    private String from = "TestA";
    private boolean status = true;
    private String to = "TestB";


    @Before
    public void setUp() {
        friendRequest = new FriendRequest(date, from, status, to);
    }

    @Test
    public void writeToParcel() {
        Parcel parcel = Parcel.obtain();
        friendRequest.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        FriendRequest deFrost = FriendRequest.CREATOR.createFromParcel(parcel);
        Assert.assertEquals(deFrost.getDate(), date);
        Assert.assertEquals(deFrost.getFrom(), from);
        Assert.assertEquals(deFrost.getStatus(), status);
        Assert.assertEquals(deFrost.getTo(), to);
    }
}