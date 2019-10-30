package io.github.cmput301f19t19.legendary_fiesta;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.cmput301f19t19.legendary_fiesta.ui.AddPostFragment;
import io.github.cmput301f19t19.legendary_fiesta.ui.FragmentEmptyClass;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

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

        //test for something to happen when neutral icon is clicked
        onView(withId(R.id.icon_neutral)).check(matches(isChecked()));
        onView(withId(R.id.icon_angry)).check(matches(not(isChecked())));
    }

    /*
    Test if DateEditText works properly
     */
    @Test
    public void DateEditTest(){
        onView(withId(R.id.dateEditText)).check(matches(isDisplayed())).perform(click()); // click edittext;
        onView(withText("OK")).check(matches(isDisplayed())).perform(click());

        //test for something after clicking ok on today's date
    }

    /*
    Test if TimeEditText works properly
     */
    @Test
    public void TimeEditTest(){
        onView(withId(R.id.timeEditText)).check(matches(isDisplayed())).perform(click()); // click edittext;
        onView(withText("OK")).check(matches(isDisplayed())).perform(click());
    }

    /*
    Test for AddPictureButton
     */
    @Test
    public void AddPictureButtonTest(){
        onView(withId(R.id.addPictureButton)).check(matches(isDisplayed())).perform(click());
    }

    @Test
    public void SocialSpinnerTest(){

    }
}
