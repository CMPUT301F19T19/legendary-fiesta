package io.github.cmput301f19t19.legendary_fiesta;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.IntDef;

import com.google.android.gms.maps.model.LatLng;
import io.github.cmput301f19t19.legendary_fiesta.ui.ProxyLatLng;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

        public static final int NONE = -1;
        public static final int SINGLE = 0;
        public static final int PAIR = 1;
        public static final int SMALL_GROUP = 2;
        public static final int CROWD = 3;

        public static final Map<Integer, String> SocialConditionStrings;
        static {
            HashMap<Integer, String> socialConditions = new HashMap<Integer, String>();
            socialConditions.put(NONE, "None");
            socialConditions.put(SINGLE, "Single");
            socialConditions.put(PAIR, "Pair");
            socialConditions.put(SMALL_GROUP, "Small Group");
            socialConditions.put(CROWD, "Crowd");
            SocialConditionStrings = Collections.unmodifiableMap(socialConditions);
        }

    }

    private String moodId;

    private @Mood.MoodType Integer moodType;
    private String user;
    private String description;
    private Date date;
    private Integer condition;
    private String photoURL;
    private ProxyLatLng location;

    /**
     * Constructor for mood event
     *
     * @param moodType    Required mood
     * @param user        Required user
     * @param description Optional description
     * @param date        Required date
     * @param condition   Optional social condition
     * @param photoURL    Optional photo URL
     * @param location    Optional location
     */
    public MoodEvent(@Nonnull Integer moodType, @Nonnull String user, @Nullable String description,
                     @Nonnull Date date, @Nullable @SocialCondition.SocialConditionType Integer condition,
                     @Nullable String photoURL, @Nullable ProxyLatLng location) {
        this.moodId = UUID.randomUUID().toString();
        this.moodType = moodType;
        this.user = user;
        this.description = description;
        this.date = date;
        this.condition = condition;
        this.photoURL = photoURL;
        this.location = location;
    }

    protected MoodEvent(Parcel in) {
        in.setDataPosition(0);

        moodId = in.readString();
        moodType = in.readInt();
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
     * for Firebase database automated serialization
     */
    public MoodEvent() { }

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
     * @return String moodId of the MoodEvent
     */
    public String getMoodId() {
        return moodId;
    }

    /**
     * @param moodId String moodId of the MoodEvent, to be used while in edit state
     */
    public void setMoodId(String moodId) {
        this.moodId = moodId;
    }

    /**
     * @return Integer MoodType of the MoodEvent
     */
    public @Mood.MoodType Integer getMoodType() {
        return moodType;
    }

    /**
     * @param moodType MoodType that the MoodEvent should have
     */
    public void setMood(@Mood.MoodType Integer moodType) {
        this.moodType = moodType;
    }

    /**
     * @return String username of the User that had the MoodEvent
     */
    public String getUser() {
        return user;
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
     * @return Integer SocialConditionType representing socialCondition
     */
    public @SocialCondition.SocialConditionType Integer getCondition() {
        return condition;
    }

    /**
     * @param condition Integer SocialConditionType representing socialCondition
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
    public ProxyLatLng getLocation() {
        return location;
    }

    /**
     * @param location maps LatLng of the location of the MoodEvent
     */
    public void setLocation(ProxyLatLng location) {
        this.location = location;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(moodId);
        dest.writeInt(moodType);
        dest.writeString(user);
        dest.writeString(description);
        dest.writeLong(date == null ? -1 : date.getTime());
        dest.writeInt(condition == null ? -1 : condition);
        dest.writeString(photoURL == null ? "": photoURL);
        dest.writeParcelable(location, flags);
    }

}
