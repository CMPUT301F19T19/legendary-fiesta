package io.github.cmput301f19t19.legendary_fiesta;

import android.view.View;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.cmput301f19t19.legendary_fiesta.ui.FriendsFragment;
import io.github.cmput301f19t19.legendary_fiesta.ui.OwnMoodsFragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.core.StringEndsWith.endsWith;

@RunWith(AndroidJUnit4.class)
public class MyMoodTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
    }

    @Test
    public void LaunchMainActivityTest() {
        onView(withId(R.id.nav_view)).check(matches(isDisplayed()));
    }

    @Test
    public void LaunchFragmentTest() {
        // check if ownMoodsFragment is showned
        onView(withId(R.id.show_on_map_button)).check(matches(isDisplayed()));
    }

    @Test
    public void MyMoodFilterClickTest() {

        //check filter after clicking None
        onView(withId(R.id.filter_spinner)).check(matches(isDisplayed())).perform(click());
        onView(withText("None")).check(matches(isDisplayed())).perform(click());
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

}
