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

    /**
     * Constructor for a user
     * @param username String username. Must be unique
     */
    public User(String username) {
        this.username = username;
        this.birthDate = null;
        this.description = null;
    }

    /**
     * Constructor for a user
     * @param username String username. Must be unique
     * @param birthDate Date of the users birth
     * @param description Short string to act as a user bio
     */
    public User(String username, Date birthDate, String description) {
        this.username = username;
        this.birthDate = birthDate;
        this.description = description;
    }

    /**
     * @return String unique username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return Date birth date of the user
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * @param birthDate Date birth date of the user
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * @return String bio/description of the user
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description String bio/description of the user
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Accept another users request to follow your mood events
     * @param username the username of the requesting user
     */
    public void acceptFollowRequest(String username) {
        // TODO
    }

    /**
     * Reject another users request to follow your mood events
     * @param username the username of the requesting user
     */
    public void rejectFollowRequest(String username) {
        // TODO
    }

    /**
     * Request to follow another users mood events
     * @param username the username of the user to be followed
     */
    public void requestToFollow(String username) {
        // TODO
    }

    /**
     * Update firebase with this users info
     */
    public void save() {
        // TODO
    }
}
