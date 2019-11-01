package io.github.cmput301f19t19.legendary_fiesta;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.IntDef;

import com.google.android.gms.maps.model.LatLng;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a single mood event
 */
public class MoodEvent implements Parcelable {
    /**
     * Class representing SocialCondition for a mood
     */
    public static class SocialCondition {
        @Retention(RetentionPolicy.SOURCE)
        @IntDef({SINGLE, PAIR, SMALL_GROUP, CROWD})
        public @interface SocialConditionType {}

        public static final int SINGLE = 0;
        public static final int PAIR = 1;
        public static final int SMALL_GROUP = 2;
        public static final int CROWD = 3;
    }

    private Mood mood;
    private String user;
    private String description;
    private Date date;
    private Integer condition;
    private String photoURL;
    private LatLng location;

    /**
     * Constructor for mood event
     *
     * @param mood        Required mood
     * @param user        Required user
     * @param description Optional description
     * @param date        Required date
     * @param condition   Optional social condition
     * @param photoURL    Optional photo URL
     * @param location    Optional location
     */
    public MoodEvent(@Nonnull Mood mood, @Nonnull String user, @Nullable String description,
                     @Nonnull Date date, @Nullable @SocialCondition.SocialConditionType Integer condition,
                     @Nullable String photoURL, @Nullable LatLng location) {
        this.mood = mood;
        this.user = user;
        this.description = description;
        this.date = date;
        this.condition = condition;
        this.photoURL = photoURL;
        this.location = location;
    }

    protected MoodEvent(Parcel in) {
        in.setDataPosition(0);
        mood = new Mood(in.readInt());
        user = in.readString();
        description = in.readString();

        long date = in.readLong();
        if (date == -1) {
            this.date = null;
        } else {
            this.date = new Date(date);
        }

        int socialCondition = in.readInt();
        if (socialCondition == -1) {
            condition = null;
        } else {
            condition = socialCondition;
        }

        String photoURL = in.readString();
        if (photoURL.length() == 0) {
            this.photoURL = null;
        } else {
            this.photoURL = photoURL;
        }

        location = in.readParcelable(LatLng.class.getClassLoader());
    }

    /**
     * Constructor for a MoodEvent (for use with Serializers)
     */
    public MoodEvent() {
    }  // for Firebase database automated serialization

    public static final Creator<MoodEvent> CREATOR = new Creator<MoodEvent>() {
        @Override
        public MoodEvent createFromParcel(Parcel in) {
            return new MoodEvent(in);
        }

        @Override
        public MoodEvent[] newArray(int size) {
            return new MoodEvent[size];
        }
    };

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
     * @return Integer Integer representing socialCondition
     */
    @SocialCondition.SocialConditionType
    public Integer getCondition() {
        return condition;
    }

    /**
     * @param condition Integer Integer representing socialCondition
     */
    public void setCondition(@SocialCondition.SocialConditionType Integer condition) {
        this.condition = condition;
    }

    /**
     * @return String firebase storage photo url for the MoodEvent
     */
    public String getPhotoURL() {
        return photoURL;
    }

    /**
     * @param photoURL firebase storage photo url for the MoodEvent
     */
    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mood.getMoodType());
        dest.writeString(user);
        dest.writeString(description);
        dest.writeLong(date == null ? -1 : date.getTime());
        dest.writeInt(condition == null ? -1 : condition);
        dest.writeString(photoURL == null ? "": photoURL);
        dest.writeParcelable(location, flags);
    }

}
