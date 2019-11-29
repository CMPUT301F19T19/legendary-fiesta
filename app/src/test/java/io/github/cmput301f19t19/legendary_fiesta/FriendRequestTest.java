package io.github.cmput301f19t19.legendary_fiesta;

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
    public void getDateTest() {
        Assert.assertEquals(date, friendRequest.getDate());
    }

    @Test
    public void setDateTest() {
        Assert.assertEquals(date, friendRequest.getDate());
        Date newDate = new Date(0);
        friendRequest.setDate(newDate);
        Assert.assertEquals(newDate, friendRequest.getDate());
    }

    @Test
    public void getStatus() {
        Assert.assertEquals(status, friendRequest.getStatus());
    }

    @Test
    public void setStatus() {
        Assert.assertEquals(status, friendRequest.getStatus());
        friendRequest.setStatus(!status);
        Assert.assertEquals(!status, friendRequest.getStatus());
    }

    @Test
    public void getFrom() {
        Assert.assertEquals(from, friendRequest.getFrom());
    }

    @Test
    public void setFrom() {
        Assert.assertEquals(from, friendRequest.getFrom());
        String newFrom = "TestUserA";
        friendRequest.setFrom(newFrom);
        Assert.assertEquals(newFrom, friendRequest.getFrom());
    }

    @Test
    public void getTo() {
        Assert.assertEquals(to, friendRequest.getTo());
    }

    @Test
    public void setTo() {
        Assert.assertEquals(to, friendRequest.getTo());
        String newTo = "TestUserA";
        friendRequest.setTo(newTo);
        Assert.assertEquals(newTo, friendRequest.getTo());
    }
}