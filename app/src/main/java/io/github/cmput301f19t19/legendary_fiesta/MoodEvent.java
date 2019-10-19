package io.github.cmput301f19t19.legendary_fiesta;

import com.google.type.LatLng;

import java.util.Date;

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
    public MoodEvent(Mood mood, String user, @Nullable String description, Date date,
                     @Nullable SocialCondition condition, @Nullable byte[] photo,
                     @ Nullable LatLng location) {
        this.mood = mood;
        this.user = user;
        this.description = description;
        this.date = date;
        this.condition = condition;
        this.photo = photo;
        this.location = location;
    }
}
