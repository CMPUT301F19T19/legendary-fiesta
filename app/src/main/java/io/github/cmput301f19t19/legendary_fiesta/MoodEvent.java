package io.github.cmput301f19t19.legendary_fiesta;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a single mood event
 */
public class MoodEvent {

    /**
     * An enum representing the possible social conditions that a MoodEvent occurred in
     */
    public enum SocialCondition {
        SINGLE,
        PAIR,
        SMALL_GROUP,
        CROWD
    }

    private Mood mood;
    private String user;
    private String description;
    private Date date;
    private SocialCondition condition;
    private byte[] photo;
    private LatLng location;

    /**
     * Constructor for mood event
     * @param mood Required mood
     * @param user Required user
     * @param description Optional description
     * @param date Required date
     * @param condition Optional social condition
     * @param photo Optional photo byte array
     * @param location Optional location
     */
    public MoodEvent(@Nonnull Mood mood, @Nonnull String user, @Nullable String description,
                     @Nonnull Date date, @Nullable SocialCondition condition,
                     @Nullable byte[] photo, @ Nullable LatLng location) {
        this.mood = mood;
        this.user = user;
        this.description = description;
        this.date = date;
        this.condition = condition;
        this.photo = photo;
        this.location = location;
    }

    /**
     * @return String username of the User that had the MoodEvent
     */
    public String getUser() {
        return user;
    }

    /**
     * @return Mood of the MoodEvent
     */
    public Mood getMood() {
        return mood;
    }

    /**
     * @param mood Mood that the MoodEvent should have
     */
    public void setMood(Mood mood) {
        this.mood = mood;
    }

    /**
     * @return The 20 char or less String description of the MoodEvent
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description String description of the MoodEvent (length <= 20)
     * @throws IllegalArgumentException if String too long
     */
    public void setDescription(String description) throws IllegalArgumentException {
        if (description.length() > 20) {
            throw new IllegalArgumentException("Description too long");
        }
        this.description = description;
    }

    /**
     * @return Date that the MoodEvent occurred
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date Date that the MoodEvent occurred
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return MoodEvent.SocialCondition enum of the MoodEvent
     */
    public SocialCondition getCondition() {
        return condition;
    }

    /**
     * @param condition MoodEvent.SocialCondition enum of the MoodEvent
     */
    public void setCondition(SocialCondition condition) {
        this.condition = condition;
    }

    /**
     * @return byte[] photo of the MoodEvent
     */
    public byte[] getPhoto() {
        return photo;
    }

    /**
     * @param photo byte[] photo of the MoodEvent
     */
    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    /**
     * @return maps LatLng of the location of the MoodEvent
     */
    public LatLng getLocation() {
        return location;
    }

    /**
     * @param location maps LatLng of the location of the MoodEvent
     */
    public void setLocation(LatLng location) {
        this.location = location;
    }

    /**
     * Called to save any changes to the MoodEvent to firebase
     */
    public void save() {
        // TODO: send all info to firebase to update a mood event
    }
}
