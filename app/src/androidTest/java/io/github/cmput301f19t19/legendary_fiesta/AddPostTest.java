package io.github.cmput301f19t19.legendary_fiesta;

import android.graphics.ColorFilter;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;

import io.github.cmput301f19t19.legendary_fiesta.ui.AddPostFragment;
import io.github.cmput301f19t19.legendary_fiesta.ui.FragmentEmptyClass;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class AddPostTest {

    private AddPostFragment fragment;

    @Rule
    public ActivityTestRule<FragmentEmptyClass> mainActivityRule = new ActivityTestRule<>(FragmentEmptyClass.class);

    //Launch AddPostFragment before every test
    @Before
    public void init(){
        fragment = new AddPostFragment();
        mainActivityRule.getActivity().getSupportFragmentManager().beginTransaction().add(1, fragment, null)
                .commit();
    }

    /*
    Test if the correct fragment is displayed
     */
    @Test
    public void LaunchFragmentTest(){
        onView(withId(R.id.emotionRadioGroup)).check(matches(isDisplayed()));
        onView(withId(R.id.greetingView)).check(matches(isDisplayed()));
    }

    /*
    Test if radio buttons is working correctly
     */
    @Test
    public void RadioGroupTest(){
        onView(withId(R.id.icon_neutral)).perform(click()); //click on neutral icon

        //test if neutral is the only one that is check
        onView(withId(R.id.icon_neutral)).check(matches(isChecked()));
        onView(withId(R.id.icon_angry)).check(matches(not(isChecked())));

        //test if neutral button UI is highlighted
        RadioButton neutralRadio = fragment.getView().findViewById(R.id.icon_neutral);
        RadioButton sadRadio = fragment.getView().findViewById(R.id.icon_sad);

        ColorFilter neutralFilter = neutralRadio.getBackground().getColorFilter();
        ColorFilter otherFilter = sadRadio.getBackground().getColorFilter();

        assertNotEquals(neutralFilter, otherFilter);
    }

    /*
    Test if DateEditText works properly
     */
    @Test
    public void DateEditTest(){
        onView(withId(R.id.dateEditText)).check(matches(isDisplayed())).perform(click()); // click edittext;
        onView(withText("OK")).check(matches(isDisplayed())).perform(click());

        //check format
        EditText dateET = fragment.getView().findViewById(R.id.dateEditText);
        String dateText = dateET.getText().toString();

        //look for three '/' in dateText
        int slashCount = 0;
        for(int i = 0; i < dateText.length(); i++){
            if(dateText.charAt(i) == '/')
                slashCount++;
        }

        assertEquals(slashCount, 2);
    }

    /*
    Test if TimeEditText works properly
     */
    @Test
    public void TimeEditTest(){
        onView(withId(R.id.timeEditText)).check(matches(isDisplayed())).perform(click()); // click edittext;
        onView(withText("OK")).check(matches(isDisplayed())).perform(click());

        //check format
        EditText timeET = fragment.getView().findViewById(R.id.timeEditText);
        String timeText = timeET.getText().toString();

        //look for three ':' in dateText
        int colonCount= 0;
        for(int i = 0; i < timeText.length(); i++){
            if(timeText.charAt(i) == ':')
                colonCount++;
        }

        assertEquals(colonCount, 1);
    }

    /*
    Test for AddPictureButton
     */
    @Test
    public void AddPictureButtonTest(){
        onView(withId(R.id.addPictureButton)).check(matches(isDisplayed())).perform(click());
    }

    @Test
    public void DescriptionTextTest(){
        //Test for basic text
        onView(withId(R.id.description_edittext)).check(matches(isDisplayed())).perform(typeText("TEST"));

        //Test for text more than 20 chars
        onView(withId(R.id.description_edittext)).check(matches(isDisplayed())).perform(clearText());
        onView(withId(R.id.description_edittext)).check(matches(isDisplayed())).perform(typeText("This is more than 20 characters for sure!"));

        //Test if description text only only contain the first 20 char
        onView(withText("This is more than 20")).check(matches(isDisplayed()));
    }
}
