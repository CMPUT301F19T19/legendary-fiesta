package io.github.cmput301f19t19.legendary_fiesta;

import androidx.test.espresso.IdlingPolicies;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import io.github.cmput301f19t19.legendary_fiesta.ui.FragmentEmptyClass;
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
    public ActivityTestRule<FragmentEmptyClass> mainActivityRule = new ActivityTestRule<>(FragmentEmptyClass.class);

    @Before
    public void init(){
        fragment = new OwnMoodsFragment();

        mainActivityRule.getActivity().getSupportFragmentManager().beginTransaction().add(1, fragment,"from my mood test").commit();
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


}
