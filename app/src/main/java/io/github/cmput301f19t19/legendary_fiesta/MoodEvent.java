package io.github.cmput301f19t19.legendary_fiesta;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a single mood event
 */
public class MoodEvent {

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

    // TODO: Photo
    // TODO: Location

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

    public Mood getMood() {
        return mood;
    }

    public String getUser() {
        return user;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description.length() > 20) {
            throw new IllegalArgumentException("Description too long");
        }
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public SocialCondition getCondition() {
        return condition;
    }

    public void setCondition(SocialCondition condition) {
        this.condition = condition;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void save() {
        // TODO: send all info to firebase to update a mood event
    }
}
