package io.github.cmput301f19t19.legendary_fiesta;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MoodTest {

    @Test
    public void moodColorsTest() {
        Mood happyMood = new Mood(Mood.HAPPY);
        assertEquals(R.color.color_happy, happyMood.getColorId());

        Mood sadMood = new Mood(Mood.SAD);
        assertEquals(R.color.color_sad, sadMood.getColorId());

        Mood angryMood = new Mood(Mood.ANGRY);
        assertEquals(R.color.color_angry, angryMood.getColorId());

        Mood scaredMood = new Mood(Mood.SCARED);
        assertEquals(R.color.color_scared, scaredMood.getColorId());

        Mood disgustedMood = new Mood(Mood.DISGUSTED);
        assertEquals(R.color.color_disgusted, disgustedMood.getColorId());

        Mood surprisedMood = new Mood(Mood.SURPRISED);
        assertEquals(R.color.color_surprised, surprisedMood.getColorId());

        Mood neutralMood = new Mood(Mood.NEUTRAL);
        assertEquals(R.color.color_neutral, neutralMood.getColorId());
    }

    @Test
    public void moodNamesTest() {
        Mood happyMood = new Mood(Mood.HAPPY);
        assertEquals(R.string.mood_happy, happyMood.getNameId());

        Mood sadMood = new Mood(Mood.SAD);
        assertEquals(R.string.mood_sad, sadMood.getNameId());

        Mood angryMood = new Mood(Mood.ANGRY);
        assertEquals(R.string.mood_angry, angryMood.getNameId());

        Mood scaredMood = new Mood(Mood.SCARED);
        assertEquals(R.string.mood_scared, scaredMood.getNameId());

        Mood disgustedMood = new Mood(Mood.DISGUSTED);
        assertEquals(R.string.mood_disgusted, disgustedMood.getNameId());

        Mood surprisedMood = new Mood(Mood.SURPRISED);
        assertEquals(R.string.mood_surprised, surprisedMood.getNameId());

        Mood neutralMood = new Mood(Mood.NEUTRAL);
        assertEquals(R.string.mood_neutral, neutralMood.getNameId());
    }

    @Test
    public void moodIconsTest() {
        Mood happyMood = new Mood(Mood.HAPPY);
        assertEquals(R.drawable.icon_happy, happyMood.getIconId());

        Mood sadMood = new Mood(Mood.SAD);
        assertEquals(R.drawable.icon_sad, sadMood.getIconId());

        Mood angryMood = new Mood(Mood.ANGRY);
        assertEquals(R.drawable.icon_angry, angryMood.getIconId());

        Mood scaredMood = new Mood(Mood.SCARED);
        assertEquals(R.drawable.icon_scared, scaredMood.getIconId());

        Mood disgustedMood = new Mood(Mood.DISGUSTED);
        assertEquals(R.drawable.icon_disgusted, disgustedMood.getIconId());

        Mood surprisedMood = new Mood(Mood.SURPRISED);
        assertEquals(R.drawable.icon_surprised, surprisedMood.getIconId());

        Mood neutralMood = new Mood(Mood.NEUTRAL);
        assertEquals(R.drawable.icon_neutral, neutralMood.getIconId());
    }

    @Test
    public void moodTypeTest() {
        Mood happyMood = new Mood(Mood.HAPPY);
        assertEquals(Mood.HAPPY, happyMood.getMoodType());

        Mood sadMood = new Mood(Mood.SAD);
        assertEquals(Mood.SAD, sadMood.getMoodType());

        Mood angryMood = new Mood(Mood.ANGRY);
        assertEquals(Mood.ANGRY, angryMood.getMoodType());

        Mood scaredMood = new Mood(Mood.SCARED);
        assertEquals(Mood.SCARED, scaredMood.getMoodType());

        Mood disgustedMood = new Mood(Mood.DISGUSTED);
        assertEquals(Mood.DISGUSTED, disgustedMood.getMoodType());

        Mood surprisedMood = new Mood(Mood.SURPRISED);
        assertEquals(Mood.SURPRISED, surprisedMood.getMoodType());

        Mood neutralMood = new Mood(Mood.NEUTRAL);
        assertEquals(Mood.NEUTRAL, neutralMood.getMoodType());
    }
}
