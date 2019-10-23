package io.github.cmput301f19t19.legendary_fiesta;

/**
 * Class that acts as a mood factory based on an enum
 * Contains getters for iconId, colorId, and nameId for a mood based on type
 */
public class Mood {

    /**
     * An enum which represents a possible type of mood
     * Used to construct moods and associate colors, icons, and names with them
     */
    public enum moodType {
        NEUTRAL,
        HAPPY,
        SURPRISED,
        ANGRY,
        SCARED,
        DISGUSTED,
        SAD
    }

    private int iconId;
    private int colorId;
    private int nameId;

    /**
     * Constructor for a mood instance
     * @param type Mood.moodType enum for choosing the mood to build
     */
    public Mood (moodType type) {
        switch (type) {
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
}
