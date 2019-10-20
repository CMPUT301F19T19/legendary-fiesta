package io.github.cmput301f19t19.legendary_fiesta;

import java.util.ArrayList;
import java.util.Date;

/**
 * A Class representing a user of the app
 * Username is unique to each user and not changeable
 * TODO: Decide how the followers are handled (update locally and then to firebase or vice versa)
 */
public class User {
    private String username;
    private Date birthDate;
    private String description;

    private ArrayList<String> following;
    private ArrayList<String> followedBy;
    private ArrayList<String> requestedBy;

    public User(String username) {
        this.username = username;
        this.birthDate = null;
        this.description = null;
    }

    public User(String username, Date birthDate, String description) {
        this.username = username;
        this.birthDate = birthDate;
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void acceptFollowRequest(String username) {
        // TODO
    }

    public void rejectFollowRequest(String username) {
        // TODO
    }

    public void requestToFollow(String username) {
        // TODO
    }

    public void save() {
        // TODO
    }
}
