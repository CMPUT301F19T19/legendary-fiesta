package io.github.cmput301f19t19.legendary_fiesta;

import android.media.Image;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.test.espresso.IdlingPolicies;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.github.cmput301f19t19.legendary_fiesta.ui.FragmentEmptyClass;
import io.github.cmput301f19t19.legendary_fiesta.ui.OwnMoodsFragment;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringEndsWith.endsWith;

@RunWith(AndroidJUnit4.class)
public class MyMoodTest {

    private OwnMoodsFragment fragment;

    @Rule
    public ActivityTestRule<FragmentEmptyClass> mainActivityRule = new ActivityTestRule<>(FragmentEmptyClass.class);

    @Before
    public void init() throws Throwable {
        fragment = new OwnMoodsFragment();

        mainActivityRule.getActivity().getSupportFragmentManager().beginTransaction().add(1, fragment,"From Tests :)").commit();

        // create a date
        String dateString = "2010-02-02";
        Date dateExample = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);

        // create a mood event
        MoodEvent moodEventExample = new MoodEvent(Mood.SAD,"Tiffany","I'm crying",dateExample, MoodEvent.SocialCondition.CROWD,null,null);

        // add the mood event into the arraylist
        mainActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fragment.AddMoodEvent(moodEventExample);
            }
        });
    }

    @Test
    public void LaunchFragmentTest() {
        // check if ownMoodsFragment is shown
        onView(withId(R.id.show_on_map_button)).check(matches(isDisplayed()));
    }

    @Test
    public void MyMoodFilterClickTest() {
        //check that filter start with None as default
        onView(withText(endsWith("None"))).check(matches(isDisplayed()));

        //check filter after clicking Angry
        onView(withId(R.id.filter_spinner)).check(matches(isDisplayed())).perform(click());
        onView(withText("Angry")).check(matches(isDisplayed())).perform(click());
        onView(withText(endsWith("Angry"))).check(matches(isDisplayed()));

        //check filter after clicking Sad
        onView(withId(R.id.filter_spinner)).check(matches(isDisplayed())).perform(click());
        onView(withText("Sad")).check(matches(isDisplayed())).perform(click());
        onView(withText(endsWith("Sad"))).check(matches(isDisplayed()));
    }


    // Check that a mood event is successfully added into the list
    @Test
    public void AddMoodEventTest() throws Throwable {
        // check if the date of the mood is on the screen
        onView(withText("2010-02-02")).check(matches(isDisplayed()));
    }


    // Check that when nothing is done, the trash can should not appear on the screen
    @Test
    public void DeleteIconNotSeenTest() throws Throwable {
        onData(anything()).inAdapterView(withId(R.id.mood_list)).atPosition(0).onChildView(withId(R.id.delete_event)).check(matches(not(isDisplayed())));
    }


    // Check that when a mood event is swiped to the left, a trash can appears on the right
    @Test
    public void DeleteIconAppearsTest() throws Throwable {
        // swipe a mood event to the left
        onData(anything()).inAdapterView(withId(R.id.mood_list)).atPosition(0).perform(swipeLeft());

        // check if the trash can is displayed
        onData(anything()).inAdapterView(withId(R.id.mood_list)).atPosition(0).onChildView(withId(R.id.delete_event)).check(matches(isDisplayed()));
    }


    // Check that when the trash can is clicked, a pop up appears
    @Test
    public void PopUpAppears() throws Throwable {
        // swipe a mood event to the left
        onData(anything()).inAdapterView(withId(R.id.mood_list)).atPosition(0).perform(swipeLeft());

        //Click the trash can
        onData(anything()).inAdapterView(withId(R.id.mood_list)).atPosition(0).onChildView(withId(R.id.delete_event)).perform(click());

        //Check if popup appears
        onView(withText("Confirm Delete?")).check(matches(isDisplayed()));
    }


    // Check that when a mood event is deleted, it no longer appears on the screen
    @Test
    public void MoodDeleted() throws Throwable {
        //swipe a mood event to the left
        onData(anything()).inAdapterView(withId(R.id.mood_list)).atPosition(0).perform(swipeLeft());

        // Click the trash can
        onData(anything()).inAdapterView(withId(R.id.mood_list)).atPosition(0).onChildView(withId(R.id.delete_event)).perform(click());

        // Click yes on the pop up
        onView(withText("Yes")).perform(click());

        // Check that the mood event is no longer on the screen
        onView(withText("2010-02-02")).check(doesNotExist());
    }
}
