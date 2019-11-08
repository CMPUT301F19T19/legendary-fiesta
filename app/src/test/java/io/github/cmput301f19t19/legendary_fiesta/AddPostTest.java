package io.github.cmput301f19t19.legendary_fiesta;

import android.content.Context;
import android.widget.RadioGroup;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import io.github.cmput301f19t19.legendary_fiesta.ui.AddPostFragment;
import io.github.cmput301f19t19.legendary_fiesta.ui.FragmentEmptyClass;

@RunWith(MockitoJUnitRunner.class)
public class AddPostTest {

    private AddPostFragment fragment;

    @Test
    public void getEmotionRadioIdTest() {

    }

    @Test
    public void getSelectedMoodTest() {

    }

    @Test
    public void getSelectedSocialConditionTest() {
        Mockito.when(fragment.getSelectedSocialCondition("Single"),)
        Integer socialCondition = fragment.getSelectedSocialCondition("Single");
        assertEquals(MoodEvent.SocialCondition.SINGLE, socialCondition.intValue());
    }



    // getSelectedMood(int id)

    // getEmotionRadioId(@Mood.MoodType int moodId)


}
