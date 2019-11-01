package io.github.cmput301f19t19.legendary_fiesta;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

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
    public void getUsernameTest() {
        Assert.assertEquals(username, user.getUsername());
    }

    @Test
    public void getBirthDateTest() {
        Assert.assertEquals(birthDate, user.getBirthDate());

    }

    @Test
    public void setBirthDateTest() {
        Assert.assertEquals(birthDate, user.getBirthDate());
        Date newDate = new Date(0);
        user.setBirthDate(newDate);
        Assert.assertEquals(newDate, user.getBirthDate());
    }

    @Test
    public void getDescriptionTest() {
        Assert.assertEquals(description, user.getDescription());
    }

    @Test
    public void setDescriptionTest() {
        Assert.assertEquals(description, user.getDescription());
        String newDescription = "NewTestDescription";
        user.setDescription(newDescription);
        Assert.assertEquals(newDescription, user.getDescription());
    }

    @Test
    public void uidTest() {
        String newUid = "newUserId";
        user.setUid(newUid);
        Assert.assertEquals(newUid, user.getUid());
    }

    @Test
    public void acceptFollowRequestTest() {
        // TODO
    }

    @Test
    public void rejectFollowRequestTest() {
        // TODO
    }

    @Test
    public void requestToFollowTest() {
        // TODO
    }
}