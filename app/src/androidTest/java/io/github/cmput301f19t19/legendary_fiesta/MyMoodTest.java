package io.github.cmput301f19t19.legendary_fiesta;

import androidx.test.espresso.IdlingPolicies;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import io.github.cmput301f19t19.legendary_fiesta.ui.OwnMoodsFragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.core.StringEndsWith.endsWith;

@RunWith(AndroidJUnit4.class)
public class MyMoodTest {

    private OwnMoodsFragment fragment;

    @Rule
    public ActivityTestRule<LoginActivity> mainActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void init() throws Throwable {
        onView(withText("Sign in with email")).check(matches(isDisplayed())).perform(click());
        onView(withHint("Email")).perform(typeText("test@example.com"));
        onView(withText("NEXT")).check(matches(isDisplayed())).perform(click());
        onView(withHint("Password")).perform(typeText("123456"));
        onView(withText("SIGN IN")).check(matches(isDisplayed())).perform(click());
        IdlingPolicies.setIdlingResourceTimeout(20, TimeUnit.SECONDS);
    }

    @Test
    public void LaunchFragmentTest() {
        // check if ownMoodsFragment is shown
        onView(withId(R.id.show_on_map_button_friends)).check(matches(isDisplayed()));
    }

    @Test
    public void MyMoodFilterClickTest() {
        //check that filter start with None as default
        onView(withText(endsWith("None"))).check(matches(isDisplayed()));

        //check filter after clicking Angry
        onView(withId(R.id.filter_spinner_friends)).check(matches(isDisplayed())).perform(click());
        onView(withText("Angry")).check(matches(isDisplayed())).perform(click());
        onView(withText(endsWith("Angry"))).check(matches(isDisplayed()));

        //check filter after clicking Sad
        onView(withId(R.id.filter_spinner_friends)).check(matches(isDisplayed())).perform(click());
        onView(withText("Sad")).check(matches(isDisplayed())).perform(click());
        onView(withText(endsWith("Sad"))).check(matches(isDisplayed()));
    }
/*
    //Check that a mood event is successfully added into the list and can be seen on the screen
    @Test
    public void MoodListTest() throws ParseException {

        Date newDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01");

        MoodEvent moodEventExample = new MoodEvent(Mood.SAD, "Tiffany", "I'm angry", newDate, MoodEvent.SocialCondition.CROWD, null, null);

        onView(withId(R.id.filter_spinner)).check(matches(isDisplayed()));

        OwnMoodsFragment currentFragment = (OwnMoodsFragment)mainActivityRule.getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        assertNotNull(currentFragment);

        //since changing the data on MoodList changes the UI thread, UI affecting code has to be put in here
        mainActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentFragment.AddMood(moodEventExample);
            }
        });

        //Check for date here
        onView(withText("2020-01-01")).check(matches(isDisplayed()));

    }


    // Check that the delete button does not appear at first
    @Test
    public void testNoDeleteButton(){
        onView(withId(R.id.delete_mood)).check(matches(not(isDisplayed())));
    }


    // Check that the delete button appears when an item in the mood event list is clicked
    @Test
    public void testDeleteButtonAppears() throws ParseException {
        Date newDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01");

        MoodEvent moodEventExample = new MoodEvent(Mood.SAD, "Tiffany", "I'm angry", newDate, MoodEvent.SocialCondition.CROWD, null, null);

        onView(withId(R.id.filter_spinner)).check(matches(isDisplayed()));

        OwnMoodsFragment currentFragment = (OwnMoodsFragment)mainActivityRule.getActivity().getSupportFragmentManager()
                .findFragmentById(1);
        assertNotNull(currentFragment);

        //since changing the data on MoodList changes the UI thread, UI affecting code has to be put in here
        mainActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentFragment.AddMood(moodEventExample);
                //Click the first item of the list
                onData(anything()).inAdapterView(withId(R.id.mood_list)).atPosition(0).perform(click());
            }
        });

        //Check if the delete button appears
        onView(withId(R.id.delete_mood)).check(matches(isDisplayed()));
    }


    // Check that the delete button disappears after an item in the mood event list is deleted
    @Test
    public void testDeleteButtonGone() throws ParseException {
        Date newDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01");

        MoodEvent moodEventExample = new MoodEvent(Mood.SAD, "Tiffany", "I'm angry", newDate, MoodEvent.SocialCondition.CROWD, null, null);

        onView(withId(R.id.filter_spinner)).check(matches(isDisplayed()));

        OwnMoodsFragment currentFragment = (OwnMoodsFragment)mainActivityRule.getActivity().getSupportFragmentManager()
                .findFragmentById(1);
        assertNotNull(currentFragment);

        //since changing the data on MoodList changes the UI thread, UI affecting code has to be put in here
        mainActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentFragment.AddMood(moodEventExample);
                //Click the first item of the list
                onData(anything()).inAdapterView(withId(R.id.mood_list)).atPosition(0).perform(click());
                //Click the delete button to delete the item
                onView(withId(R.id.delete_mood)).check(matches(isDisplayed())).perform(click());
            }
        });

        //Check if the delete button is gone after that
        onView(withId(R.id.delete_mood)).check(matches(not(isDisplayed())));
    }


    // Check that an item is no longer seen on the screen when it is deleted
    @Test
    public void testDeletedItemGone() throws ParseException {
        Date newDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01");

        MoodEvent moodEventExample = new MoodEvent(Mood.SAD, "Tiffany", "I'm angry", newDate, MoodEvent.SocialCondition.CROWD, null, null);

        onView(withId(R.id.filter_spinner)).check(matches(isDisplayed()));

        OwnMoodsFragment currentFragment = (OwnMoodsFragment)mainActivityRule.getActivity().getSupportFragmentManager()
                .findFragmentById(1);
        assertNotNull(currentFragment);

        //since changing the data on MoodList changes the UI thread, UI affecting code has to be put in here
        mainActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentFragment.AddMood(moodEventExample);
                //Click the first item of the list
                onData(anything()).inAdapterView(withId(R.id.mood_list)).atPosition(0).perform(click());
                //Click the delete button to delete the item
                onView(withId(R.id.delete_mood)).check(matches(isDisplayed())).perform(click());
            }
        });

        //Check if the item is gone after being deleted
        onView(withText("2020-01-01")).check(matches(not(isDisplayed())));
    }
*/

}
