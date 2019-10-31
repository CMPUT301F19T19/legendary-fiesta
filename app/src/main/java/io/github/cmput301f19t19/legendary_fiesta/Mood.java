package io.github.cmput301f19t19.legendary_fiesta;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that acts as a mood factory based on an enum
 * Contains getters for iconId, colorId, and nameId for a mood based on type
 */
public class Mood {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NEUTRAL, HAPPY, SURPRISED, ANGRY, SCARED, DISGUSTED, SAD})
    public @interface MoodType {
    }

    public static final int NEUTRAL = 0;
    public static final int HAPPY = 1;
    public static final int SURPRISED = 2;
    public static final int ANGRY = 3;
    public static final int SCARED = 4;
    public static final int DISGUSTED = 5;
    public static final int SAD = 6;

    public static final Map<String, Integer> MoodTypes;

    static {
        HashMap<String, Integer> moodTypes = new HashMap<String, Integer>();
        moodTypes.put("NEUTRAL", NEUTRAL);
        moodTypes.put("HAPPY", HAPPY);
        moodTypes.put("SURPRISED", SURPRISED);
        moodTypes.put("ANGRY", ANGRY);
        moodTypes.put("SCARED", SCARED);
        moodTypes.put("DISGUSTED", DISGUSTED);
        moodTypes.put("SAD", SAD);
        MoodTypes = Collections.unmodifiableMap(moodTypes);
    }

    private int iconId;
    private int colorId;
    private int nameId;
    private int moodType;

    /**
     * Constructor for a mood instance
     *
     * @param moodType int Integer representing a moodType
     *                 NEUTRAL = 0;
     *                 HAPPY = 1;
     *                 SURPRISED = 2;
     *                 ANGRY = 3;
     *                 SCARED = 4;
     *                 DISGUSTED = 5;
     *                 SAD = 6;
     */
    public Mood(@MoodType int moodType) {
        this.moodType = moodType;

        switch (moodType) {
            case NEUTRAL:
                this.colorId = R.color.color_neutral;
                this.iconId = R.drawable.icon_neutral;
                this.nameId = R.string.mood_neutral;
                break;
            case HAPPY:
                this.colorId = R.color.color_happy;
                this.iconId = R.drawable.icon_happy;
                this.nameId = R.string.mood_happy;
                break;
            case SURPRISED:
                this.colorId = R.color.color_surprised;
                this.iconId = R.drawable.icon_surprised;
                this.nameId = R.string.mood_surprised;
                break;
            case ANGRY:
                this.colorId = R.color.color_angry;
                this.iconId = R.drawable.icon_angry;
                this.nameId = R.string.mood_angry;
                break;
            case SCARED:
                this.colorId = R.color.color_scared;
                this.iconId = R.drawable.icon_scared;
                this.nameId = R.string.mood_scared;
                break;
            case DISGUSTED:
                this.colorId = R.color.color_disgusted;
                this.iconId = R.drawable.icon_disgusted;
                this.nameId = R.string.mood_disgusted;
                break;
            case SAD:
                this.colorId = R.color.color_sad;
                this.iconId = R.drawable.icon_sad;
                this.nameId = R.string.mood_sad;
                break;
        }
    }

    /**
     * @return Int iconId of the selected mood type
     */
    public int getIconId() {
        return this.iconId;
    }

    /**
     * @return Int colorId of the selected mood type
     */
    public int getColorId() {
        return this.colorId;
    }

    /**
     * @return Int nameId of the selected mood type
     */
    public int getNameId() {
        return this.nameId;
    }

    /**
     * @return int moodType of the selected mood types
     */
    @MoodType
    public final int getMoodType() {
        return moodType;
    }
}
